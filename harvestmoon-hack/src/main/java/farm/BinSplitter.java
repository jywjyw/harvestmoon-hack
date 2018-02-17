package farm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import common.Util;

public class BinSplitter {
	
	public static void main(String[] args) throws IOException {
		String dir = "C:\\Users\\me\\Desktop\\farm-en\\";
		List<Integer> addrs = new ArrayList<>();
		RandomAccessFile hdt = new RandomAccessFile(new File(dir+"A_FILE.HDT"), "r");
		
		try {
			while(true){
				int addr = Util.hilo(hdt.readInt());
				addrs.add(addr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		hdt.close();
		
		RandomAccessFile bin = new RandomAccessFile(new File(dir+"A_FILE.BIN"), "r");
		for(int i=0;i<addrs.size()-1;i++){	//最后一个是文件边界
			bin.seek(addrs.get(i));
			int len=addrs.get(i+1)-addrs.get(i);
			byte[] buf = new byte[len];
			bin.read(buf);
			FileOutputStream fos = new FileOutputStream(dir+File.separator+i);
			fos.write(buf);
			fos.flush();
			fos.close();
		}
		bin.close();
		
	}

}
