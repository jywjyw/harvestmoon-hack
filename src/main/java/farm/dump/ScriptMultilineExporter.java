package farm.dump;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import common.Conf;
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
					if(fileIndex==4 && i==256)
						System.out.println();
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

	@Override
	public void every2Bytes(int index, String char_, int unsignedShort, boolean isCtrl) {
		if(isCtrl){
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
