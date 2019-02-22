package farm.dump;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import common.Util;
public class NoPointerTextReader {
	
	private long thisAddr,endAddr;
	private StringBuilder sentence = new StringBuilder();
	private int sentenceSize=0;	//这段文本占多少字节
	
	public NoPointerTextReader(long startAddr, long endAddr){
		this.thisAddr=startAddr;
		this.endAddr=endAddr;
	}
	
	public List<NoPointerText> read(Charset cs, String file) throws IOException{
		List<NoPointerText> ret = new ArrayList<>();
		RandomAccessFile exe = new RandomAccessFile(file, "r");
		exe.seek(thisAddr);
		byte[] buf=new byte[2];
		while(exe.getFilePointer()<endAddr){
			exe.read(buf);
			int code=Util.toInt(buf[1], buf[0]);
			if(code!=0xffff){
				String char_ = cs.getChar(code);
				if(char_==null) char_ = String.format("{%04X}", code);
				sentence.append(char_);
			}
			sentenceSize+=2;
			if(code==0xffff){
				ret.add(new NoPointerText(thisAddr, sentenceSize/2, sentence.toString()));	//用字符数代替字节数
				thisAddr = exe.getFilePointer();
				sentence = new StringBuilder();
				sentenceSize=0;
			}
		}
		exe.close();
		return ret;
	}
}
