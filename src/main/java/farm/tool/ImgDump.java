package farm.tool;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import common.Conf;
import common.Util;
import farm.dump.TextureMeta;

public class ImgDump {
	
	public static void main(String[] args) throws IOException {
		RandomAccessFile file = new RandomAccessFile(Conf.desktop+"boyjp\\0", "r");
		PrintWriter out = new PrintWriter(new FileOutputStream(Conf.desktop+"0.txt"));
		file.skipBytes(4);//前4个字节含义未知
		int i=0;
		long addr=0;
		while(true){
			try {
				TextureMeta t = new TextureMeta();
				addr=file.getFilePointer();
				int f4=Util.hilo(file.readInt());
				t.imgSize = f4&0xffffff;
				t.type = f4>>>24;
				t.x = Util.hiloShort(file.readUnsignedShort());
				t.y = Util.hiloShort(file.readUnsignedShort());
				t.w = Util.hiloShort(file.readUnsignedShort());
				t.h = Util.hiloShort(file.readUnsignedShort());
				file.skipBytes(((t.imgSize+3)>>>2)<<2);
//				if((t.type&3)==2){
//					//800181ec
//					file.skipBytes(t.imgSize);
//				} else if((t.type&12)!=0){
//					//8001837c 
//					file.skipBytes(t.imgSize);
//				} else if((t.type&8)==0){
//					//800184e8 maybe compress data
//					byte[] body=new byte[t.imgSize+1];
//					file.read(body);
//					if(t.imgSize%2==0) file.skipBytes(1);
//				} else {
//					file.skipBytes(t.imgSize);
//				}
				out.printf("%d - addr=0x%08X,size=%d,type=%d,xy=(%d,%d),wh=(%d,%d)\n",
						i++,addr,t.imgSize,t.type,t.x,t.y, t.w,t.h);
				if(file.getFilePointer()==0xcae0) file.seek(0xd000);
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		file.close();
		out.close();
	}

}
