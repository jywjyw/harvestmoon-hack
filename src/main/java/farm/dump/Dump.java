package farm.dump;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import common.Conf;
import common.Util;
import farm.BinSplitPacker;

public class Dump {
	
	public static void main(String[] args) throws IOException {
//		dumpBoyJp();
//		dumpBoyEn();
//		dumpGirlJp();
		dumpGirlEn();
	}
	
	public static void dumpBoyJp() throws IOException {
		BinSplitPacker bin = new BinSplitPacker();
		bin.split(Conf.boyjpdir, Conf.desktop+"boyjp\\");
		
		Charset cs = new Charset("charset_jp.gbk");
		XSSFWorkbook excel=new XSSFWorkbook();
		List<NoPointerText> exeTexts = new NoPointerTextReader(0x443f4, 0x46a0c).read(cs, Conf.boyjpdir+Conf.BOY_JP_EXE);
		new NoPointerTextExporter().export(exeTexts,excel,"EXE");
		new ScriptMultilineExporter().exportExcel(bin, cs, excel);
		saveExcel(excel, Conf.desktop+"boy-jp.xlsx");
		System.out.println("dump complete");
	}
	
	public static void dumpGirlJp() throws IOException {
		BinSplitPacker bin = new BinSplitPacker();
		bin.split(Conf.girljpdir, Conf.desktop+"boyjp\\");
		
		Charset cs = new Charset("charset_jp.gbk");
		XSSFWorkbook excel=new XSSFWorkbook();
		List<NoPointerText> exeTexts = new NoPointerTextReader(0x443f4, 0x46a0c).read(cs, Conf.girljpdir+Conf.GIRL_JP_EXE);
		new NoPointerTextExporter().export(exeTexts,excel,"EXE");
		new ScriptMultilineExporter().exportExcel(bin, cs, excel);
		saveExcel(excel, Conf.desktop+"girl-jp.xlsx");
		System.out.println("dump complete");
	}
	
	public static void dumpBoyEn() throws IOException {
		BinSplitPacker bin = new BinSplitPacker();
		bin.split(Conf.boyendir, Conf.desktop+"boyjp\\");
		
		Charset cs = new Charset("charset_en.gbk");
		XSSFWorkbook excel=new XSSFWorkbook();
		List<NoPointerText> exeTexts = new NoPointerTextReader(0x41f5c, 0x453ba).read(cs, Conf.boyendir+Conf.BOY_EN_EXE);
		new NoPointerTextExporter().export(exeTexts,excel,"EXE");
		new ScriptMultilineExporter().exportExcel(bin, cs, excel);
		saveExcel(excel, Conf.desktop+"boy-en.xlsx");
		System.out.println("dump complete");
	}
	
	public static void dumpGirlEn() throws IOException {
		BinSplitPacker bin = new BinSplitPacker();
		bin.split(Conf.girlendir, Conf.desktop+"boyjp\\");
		
		Charset cs = new Charset("charset_en.gbk");
		XSSFWorkbook excel=new XSSFWorkbook();
		new ScriptMultilineExporter().exportExcelGirlEn(bin, cs, excel);
		saveExcel(excel, Conf.desktop+"girl-en.xlsx");
		System.out.println("dump complete");
	}
	
	private static void saveExcel(XSSFWorkbook excel, String target) throws FileNotFoundException{
		FileOutputStream fos=new FileOutputStream(target);
		try {
			excel.write(fos);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			Util.close(excel);
		}
	}
}
