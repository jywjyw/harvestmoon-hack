package farm.dump;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import common.Conf;
import common.Util;
import farm.BinSplitPacker;
import farm.FloatText;
import farm.pic.AllPicture;

public class Dump {
	
	public static void main(String[] args) throws IOException {
//		dumpBoyJp();
//		dumpBoyEn();
		dumpGirlJp();
//		dumpGirlEn();
	}
	
	public static void dumpBoyJp() throws IOException {
		BinSplitPacker bin = new BinSplitPacker();
		bin.split(Conf.boyjpdir, Conf.desktop+"harvest\\");
		
		Charset cs = new Charset("charset_jp.gbk");
		XSSFWorkbook excel=new XSSFWorkbook();
		List<FloatText> tExe=new ArrayList<>();
		tExe.addAll(new FloatTextReader(0x443f4, 0x46a0c).read(cs, new File(Conf.boyjpdir+Conf.BOY_JP_EXE)));
		tExe.addAll(new FloatTextReader(0x474fc, 0x47716).read(cs, new File(Conf.boyjpdir+Conf.BOY_JP_EXE)));
		FloatTextExporter.export(tExe,excel,"EXE");
		List<FloatText> t11=new ArrayList<>();
		t11.addAll(new FloatTextReader(0x77908, 0x7792e).read(cs, bin.getFile(11)));
		t11.addAll(new FloatTextReader(0x77aa4, 0x77b1a).read(cs, bin.getFile(11)));
		FloatTextExporter.export(t11,excel,"11");
		FloatTextExporter.export(new FloatTextReader(0x3f50, 0x3fee).read(cs, bin.getFile(21)),excel,"21");
		FloatTextExporter.export(new FloatTextReader(0x571c, 0x57d6).read(cs, bin.getFile(23)),excel,"23");
		FloatTextExporter.export(new FloatTextReader(0x6140, 0x61b0).read(cs, bin.getFile(25)),excel,"25");
		FloatTextExporter.export(new FloatTextReader(0x3ecc, 0x3fa8).read(cs, bin.getFile(27)),excel,"27");
		FloatTextExporter.export(new FloatTextReader(0x5bbc, 0x5bfc).read(cs, bin.getFile(29)),excel,"29");
		new ScriptMultilineExporter().exportExcel(bin, cs, excel);
		saveExcel(excel, Conf.desktop+"boy-jp.xlsx");
		
		new AllPicture().exportBoy(bin.getFile(0), Conf.desktop+"pic\\");
		System.out.println("dump complete");
	}
	
	public static void dumpBoyEn() throws IOException {
		BinSplitPacker bin = new BinSplitPacker();
		bin.split(Conf.boyendir, Conf.desktop+"harvest\\");
		
		Charset cs = new Charset("charset_en.gbk");
		XSSFWorkbook excel=new XSSFWorkbook();
		List<FloatText> tExe=new ArrayList<>();
		tExe.addAll(new FloatTextReader(0x41f5c, 0x453ba).read(cs, new File(Conf.boyendir+Conf.BOY_EN_EXE)));
		tExe.addAll(new FloatTextReader(0x45ea8, 0x460a2).read(cs, new File(Conf.boyendir+Conf.BOY_EN_EXE)));
		FloatTextExporter.export(tExe,excel,"EXE");
		new ScriptMultilineExporter().exportExcel(bin, cs, excel);
		saveExcel(excel, Conf.desktop+"boy-en.xlsx");
		System.out.println("dump complete");
	}
	
	public static void dumpGirlJp() throws IOException {
		BinSplitPacker bin = new BinSplitPacker();
		bin.split(Conf.girljpdir, Conf.desktop+"harvest\\");
		
		Charset cs = new Charset("charset_jp.gbk");
		XSSFWorkbook excel=new XSSFWorkbook();
		List<FloatText> tExe=new ArrayList<>();
		tExe.addAll(new FloatTextReader(0x43D18, 0x46a0c).read(cs, new File(Conf.girljpdir+Conf.GIRL_JP_EXE)));
		tExe.addAll(new FloatTextReader(0x46e54, 0x47078).read(cs, new File(Conf.girljpdir+Conf.GIRL_JP_EXE)));
		FloatTextExporter.export(tExe,excel,"EXE");
		FloatTextExporter.export(new FloatTextReader(0x71cf0, 0x71d66).read(cs, bin.getFile(11)),excel,"11");
		FloatTextExporter.export(new FloatTextReader(0x3798, 0x382a).read(cs, bin.getFile(17)),excel,"17");
		FloatTextExporter.export(new FloatTextReader(0x4f38, 0x500a).read(cs, bin.getFile(20)),excel,"20");
		FloatTextExporter.export(new FloatTextReader(0x4d08, 0x4d78).read(cs, bin.getFile(22)),excel,"22");
		FloatTextExporter.export(new FloatTextReader(0x38b4, 0x39c2).read(cs, bin.getFile(24)),excel,"24");
		FloatTextExporter.export(new FloatTextReader(0x4b08, 0x4b48).read(cs, bin.getFile(26)),excel,"26");
		new ScriptMultilineExporter().exportGirlExcel(bin, cs, excel);
		saveExcel(excel, Conf.desktop+"girl-jp.xlsx");
		new AllPicture().exportGirl(bin.getFile(0), Conf.desktop+"pic\\");
		System.out.println("dump complete");
	}
	
	public static void dumpGirlEn() throws IOException {
		BinSplitPacker bin = new BinSplitPacker();
		bin.split(Conf.girlendir, Conf.desktop+"harvest\\");
		
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
