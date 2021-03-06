package farm.dump;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import common.Conf;
import common.Util;
import farm.BinSplitPacker;


public class ScriptMultilineExporter implements ScriptReader.Callback{
	
	XSSFSheet sheet;
	static final boolean LEFT=true,RIGHT=false;
	boolean lastDirection=LEFT;
	Row row;
	int scriptIndex;
	
	XSSFCellStyle colorBg=null;
	
	public void exportExcel(BinSplitPacker bin,Charset cs, XSSFWorkbook excel) throws IOException {
		colorBg=excel.createCellStyle();
		colorBg.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		colorBg.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		for(int fileIndex : new int[]{2,3,4,5,6}){
			resetOnNewSheet();
			this.sheet = excel.createSheet(fileIndex+"");
			sheet.setColumnWidth(1, 10000);
			sheet.setColumnWidth(2, 10000);
			
			RandomAccessFile file = new RandomAccessFile(bin.getFile(fileIndex), "r");
			byte[] buf = new byte[Conf.SECTOR];
			for(int i=0;i*Conf.SCRIPT_GROUP_LEN+Conf.SCRIPT_PREFIX_LEN<=file.length();i++) {
				try {
					file.seek(i*Conf.SCRIPT_GROUP_LEN+Conf.SCRIPT_PREFIX_LEN);
					file.read(buf);
					if(!is0(buf)){
						scriptIndex=i;
						newRow();
						ScriptReader.readUntilFFFF(buf, cs, this);
					}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			file.close();
		}
	}
	
	public void exportGirlExcel(BinSplitPacker bin,Charset cs, XSSFWorkbook excel) throws IOException {
		exportExcel(bin, cs, excel);
		resetOnNewSheet();
		this.sheet = excel.createSheet("12");
		sheet.setColumnWidth(1, 10000);
		sheet.setColumnWidth(2, 10000);
		
		RandomAccessFile file = new RandomAccessFile(bin.getFile(12), "r");
		ByteArrayOutputStream sector=new ByteArrayOutputStream();
		byte[] charBuf=new byte[2];
		scriptIndex=0;
		while(file.getFilePointer()<0x1900){
			file.read(charBuf);
			sector.write(charBuf);
			if(charBuf[0]==(byte)0xff && charBuf[1]==(byte)0xff){
				newRow();
				ScriptReader.readUntilFFFF(sector.toByteArray(), cs, this);
				sector=new ByteArrayOutputStream();
				scriptIndex=Util.getMultiple((int)file.getFilePointer(), 0x100);
				file.seek(scriptIndex);
			}
		}
		file.close();
	}
	
	public void exportExcelGirlEn(BinSplitPacker bin,Charset cs, XSSFWorkbook excel) throws IOException {
		colorBg=excel.createCellStyle();
		colorBg.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		colorBg.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		for(int fileIndex : new int[]{2,3,4,5,6}){
			resetOnNewSheet();
			this.sheet = excel.createSheet(fileIndex+"");
			sheet.setColumnWidth(1, 10000);
			sheet.setColumnWidth(2, 10000);
			
			RandomAccessFile file = new RandomAccessFile(bin.getFile(fileIndex), "r");
			byte[] buf = new byte[Conf.SECTOR];
			for(int i=0;i*0x2800+Conf.SCRIPT_PREFIX_LEN<=file.length();i++) {
				try {
					file.seek(i*0x2800+Conf.SCRIPT_PREFIX_LEN);
					file.read(buf);
					if(!is0(buf)){
						scriptIndex=i;
						newRow();
						ScriptReader.readUntilFFFF(buf, cs, this);
					}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			file.close();
		}
	}
	
	private static byte[] zero = new byte[Conf.SECTOR];
	private boolean is0(byte[] buf){
		return Arrays.equals(buf, zero);
	}
	
	private List<String> placeholders=Arrays.asList(
			"{boy}","{girl}","{horse}","{farm}","{dog}","{baby}","{husband}");

	@Override
	public void every2Bytes(int index, String char_, int unsignedShort, boolean isCtrl) {
		if(isCtrl && !placeholders.contains(char_)){
			if(lastDirection==RIGHT){
				newRow();
			}
			append(row, 1, char_);
			lastDirection = LEFT;
		} else {
			append(row, 2, char_);
			lastDirection = RIGHT;
		}
		
	}
	
	private void append(Row row, int cellIndex, String val) {
		Cell cell = row.getCell(cellIndex);
		if(cell==null) cell = row.createCell(cellIndex);
		cell.setCellValue(cell.getStringCellValue()+val);
		if(scriptIndex%2==0)	cell.setCellStyle(colorBg);
	}
	
	private void resetOnNewSheet(){
		row=null;
	}
	
	private void newRow(){
		if(row==null) {
			row = sheet.createRow(0);
		} else {
			row = sheet.createRow(row.getRowNum()+1);
		}
		if(scriptIndex%2==0)	row.setRowStyle(colorBg);
		Cell c=row.createCell(0);
		c.setCellValue(scriptIndex);
		if(scriptIndex%2==0)	c.setCellStyle(colorBg);
	}
	
}
