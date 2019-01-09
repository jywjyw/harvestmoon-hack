package farm.dump;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

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
	
	public byte[] exportExcel(BinSplitPacker bin,Charset charTable) throws IOException {
		XSSFWorkbook book = new XSSFWorkbook();
		colorBg=book.createCellStyle();
		colorBg.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		colorBg.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		for(int fileIndex : new int[]{2,3,4,5,6}){
			resetOnNewSheet();
			this.sheet = book.createSheet(fileIndex+"");
			sheet.setColumnWidth(1, 10000);
			sheet.setColumnWidth(2, 10000);
			
			RandomAccessFile file = new RandomAccessFile(bin.getFile(fileIndex), "r");
			byte[] buf = new byte[Conf.SECTOR];
			for(int i=0;i*Conf.SCRIPT_GROUP_LEN+Conf.SCRIPT_PREFIX_LEN<=file.length();i++) {
				try {
					file.seek(i*Conf.SCRIPT_GROUP_LEN+Conf.SCRIPT_PREFIX_LEN);
					file.read(buf);
					scriptIndex=i;
					newRow();
					ScriptReader.readUntilFFFF(buf, charTable, this);
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			file.close();
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			book.write(bos);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			Util.close(book);
		}
		return bos.toByteArray();
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
