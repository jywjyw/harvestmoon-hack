package farm.tool;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

import common.Conf;
import common.Util;
import farm.Pic;
import farm.dump.Uncompressor;

//文件0不是多个picpack组合而成,无法拆解
public class File0Analyzer {
	//d000/
	public static void main(String[] args) throws IOException {
		RandomAccessFile in = new RandomAccessFile(Conf.desktop+"boyjp\\0", "r");
		in.seek(0);
		in.skipBytes(4);
		while(true){
			try {
				Pic p=new Pic();
				int size_type=Util.hilo(in.readInt());
				if(size_type==0) {
					break;
				}
				p.type = (byte) (size_type>>>24);
				p.x = Util.hiloShort(in.readShort());
				p.y = Util.hiloShort(in.readShort());
				p.w = Util.hiloShort(in.readShort());
				p.h = Util.hiloShort(in.readShort());
				System.out.printf("addr=%X,x=%d,y=%d,w=%d,h=%d\n",in.getFilePointer()-12,p.x,p.y,p.w,p.h);
				if(p.type==0 || p.type==4){
					byte[] realData=new byte[(((size_type&0xffffff)+3)>>>2)<<2];	//be multiple of 4
					in.read(realData);
					p.data=Uncompressor.uncomp(new ByteArrayInputStream(realData));
				} else {
					p.data=new byte[size_type&0xffffff];
					in.read(p.data);
				}
			} catch (EOFException e) {
				break;
			}
		}
		in.close();
	}

}
