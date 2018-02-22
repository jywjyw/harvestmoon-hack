package farm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import common.Conf;


public class Patcher {
	
	public static void patch(File file, int fileRealLen, byte[] patch, int startAddr, int overwriteLen) throws IOException{
		File tmp = File.createTempFile("newfilelink", "");
		RandomAccessFile from = new RandomAccessFile(file, "r");
		FileOutputStream to = new FileOutputStream(tmp);
		int writeLen = 0;
		
		byte[] buf = new byte[startAddr];
		from.read(buf);
		to.write(buf);
		writeLen+=buf.length;
		
		to.write(patch);
		writeLen+=patch.length;
		
		from.seek(startAddr+overwriteLen);
		buf = new byte[(int) (fileRealLen-from.getFilePointer())];
		from.read(buf);
		to.write(buf);
		writeLen+=buf.length;
		
		int outlimit = (int) (writeLen%Conf.SECTOR);
		if(outlimit>0){
			to.write(new byte[Conf.SECTOR-outlimit]);
		}
		
		from.close();
		to.close();
		file.delete();
		if(!tmp.renameTo(file)){
			throw new RuntimeException("无法删除"+file);
		}
	}
}
