package farm.hack;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import common.Conf;
import common.ExcelParser;
import common.Util;
import common.ExcelParser.RowCallback;
import farm.BinSplitPacker;

public class ScriptImporter {
	
	Encoding enc;
	public ScriptImporter(Encoding enc) {
		this.enc = enc;
	}

	String lastSentenceId;
	StringBuilder sentence = new StringBuilder();
	
	public void import_(BinSplitPacker bin, File excel) throws IOException{
		for(int fileIndex : new int[]{2,3,4,5,6}){
			RandomAccessFile file = new RandomAccessFile(bin.getFile(fileIndex), "rw");
			new ExcelParser(excel).parse(fileIndex+"", 1, new RowCallback() {
				@Override
				public void doInRow(List<String> strs, int rowNum) {
					String thisSentenceId = strs.get(0);
					if(lastSentenceId!=null && !thisSentenceId.equals(lastSentenceId)){
						try {
							flushSentence(file, fileIndex);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					} 
					String chinese="";
					if(strs.size()>3 && strs.get(3)!=null) {
						chinese = strs.get(3);
					}
					sentence.append(strs.get(1)).append(chinese);
					lastSentenceId=thisSentenceId;
				}
			});
			
			flushSentence(file, fileIndex);
			
			file.close();
			lastSentenceId=null;
		}
	}

	private void flushSentence(RandomAccessFile file, int fileIndex) throws IOException {
//		if(fileIndex==5 && lastSentenceId.equals("930")){
//			System.out.println(Util.hexEncode(SentenceSerializer.toBytes(enc, sentence.toString())));
//		}
//		System.out.println(fileIndex + " "+lastSentenceId + "  "+sentence.toString());
		int dataPos = Integer.parseInt(lastSentenceId)*Conf.SCRIPT_GROUP_LEN+Conf.SCRIPT_PREFIX_LEN;
		file.seek(dataPos);
		file.write(SentenceSerializer.toBytes(enc, sentence.toString()));
		this.sentence = new StringBuilder();
	}
}
