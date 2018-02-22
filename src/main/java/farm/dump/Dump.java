package farm.dump;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import common.Util;

public class Dump {
	
	public static void main(String[] args) throws IOException {
		RandomAccessFile file = new RandomAccessFile("C:\\Users\\me\\Desktop\\farm-jp\\0", "r");
		PrintWriter out = new PrintWriter(new FileOutputStream("C:\\Users\\me\\Desktop\\0.txt"));
		file.skipBytes(4);
		int i=0;
		long addr=0;
		while(true){
			try {
				TextureMeta t = new TextureMeta();
				addr=file.getFilePointer();
				t.imgSize = Util.hiloShort(file.readUnsignedShort());
				t.bit = file.readByte();
				t.type = file.readByte();
				t.x = Util.hiloShort(file.readUnsignedShort());
				t.y = Util.hiloShort(file.readUnsignedShort());
				t.w = Util.hiloShort(file.readUnsignedShort());
				t.h = Util.hiloShort(file.readUnsignedShort());
				file.skipBytes(t.imgSize);
				if(i==4)file.skipBytes(1);
				else if(i==6||i==10)file.skipBytes(2);
				else if(i==12)file.skipBytes(0x520);
				out.printf("%d - addr=0x%08X,size=%d,bit=%d,type=%d,xy=(%d,%d),wh=(%d,%d)\n",
						i++,addr,t.imgSize,t.bit,t.type,t.x,t.y, t.w,t.h);
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		file.close();
		out.close();
	}

}
