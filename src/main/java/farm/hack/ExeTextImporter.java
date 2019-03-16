package farm.hack;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import common.ExcelParser;
import common.ExcelParser.RowCallback;

public class ExeTextImporter {
	
	Encoding enc;
	public ExeTextImporter(Encoding enc) {
		this.enc = enc;
	}

	public void import_(File excel, String exe) throws IOException{
		final RandomAccessFile file = new RandomAccessFile(exe, "rw");
		final List<String> error=new ArrayList<>();
		new ExcelParser(excel).parse("EXE", 2, new RowCallback() {
			@Override
			public void doInRow(List<String> strs, int rowNum) {
				int addr=Integer.parseInt(strs.get(0),16);
				int len=Integer.parseInt(strs.get(1),16);
				String chinese = null;
				if(strs.size()>3 && strs.get(3)!=null) {
					chinese = strs.get(3);
					byte[] bs=SentenceSerializer.toBytes(enc, chinese);
					if(bs.length<=len*2) {
						try {
							file.seek(addr);
							file.write(bs);
							file.writeShort(0xffff);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					} else {
						error.add(rowNum+"行超长: "+chinese);
					}
				}
			}
		});
		file.close();
		if(!error.isEmpty()) {
			for(String s:error)
				System.err.println(s);
		}
	}
	 
}
