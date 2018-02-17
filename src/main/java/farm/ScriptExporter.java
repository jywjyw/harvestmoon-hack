package farm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import common.Conf;

public class ScriptExporter {
	
	public static void main(String[] args) throws IOException {
		export("japan.tbl", "C:\\Users\\me\\Desktop\\farm-jp\\");
		export("english.tbl", "C:\\Users\\me\\Desktop\\farm-en\\");
	}
	
	public static void export(String tbl, String dir) throws IOException {
		CharTable table = new CharTable(tbl);
		int unknownData = 0x1800;
		int group = unknownData+Conf.SECTOR;
		XSSFWorkbook book = new XSSFWorkbook();
		for(int fileIndex : new int[]{2,3,4,5,6}){
			XSSFSheet sheet = book.createSheet(fileIndex+"");
			RandomAccessFile file = new RandomAccessFile(dir+fileIndex, "r");
			byte[] buf = new byte[Conf.SECTOR];
			for(int i=0;i*group+unknownData<=file.length();i++) {
				try {
					file.seek(i*group+unknownData);
					file.read(buf);
					Cell cell = sheet.createRow(i).createCell(0);
					cell.setCellValue(ScriptReader.readUntilFF(buf, table));
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			
			file.close();
		}
		
		FileOutputStream fos = new FileOutputStream(dir+tbl+".xlsx");
		book.write(fos);
		book.close();
		fos.close();
	}

}
