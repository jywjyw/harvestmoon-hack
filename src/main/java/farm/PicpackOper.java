package farm;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import common.Util;

public class PicpackOper {
	
	public static void write0(File f0, Picpack pac)throws IOException{
		write(f0, 0, pac);
	}
	
	public static Picpack loadD(File f0, int entrance, int exit) throws IOException{
		RandomAccessFile in = new RandomAccessFile(f0, "r");
		in.seek(entrance);
		Picpack pac=new Picpack();
		pac.capacity=exit-entrance;	//capacity means multiple of 0x800
		pac.unk=in.readInt();
		while(in.getFilePointer()<exit){
			Pic p=new Pic();
			int size_type=Util.hilo(in.readInt());
			if(size_type==0) {
//				System.out.println("encount size_type=0, break load picpack");
				break;
			}
			int size=size_type&0xffffff;
			p.flag = (byte) (size_type>>>24);
			
			p.x = Util.hiloShort(in.readShort());
			p.y = Util.hiloShort(in.readShort());
			p.w = Util.hiloShort(in.readShort());
			p.h = Util.hiloShort(in.readShort());
			
			byte[] data=new byte[size];
			in.read(data);
			p.rawImg=data;
			in.seek(Util.get4Multiple((int)in.getFilePointer()));
			pac.pics.add(p);
		}
		in.close();
		return pac;
	}
	
	public static Picpack loadE(File f0, int entrance, int dStart, int exit) throws IOException{
		RandomAccessFile in = new RandomAccessFile(f0, "r");
		in.seek(entrance);
		Picpack pac=new Picpack();
		pac.headE=new byte[dStart-entrance];
		in.read(pac.headE);
		
		pac.capacity=exit-entrance;	//capacity means multiple of 0x800
		pac.unk=in.readInt();
		while(in.getFilePointer()<exit){
			Pic p=new Pic();
			int size_type=Util.hilo(in.readInt());
			if(size_type==0) {
//				System.out.println("encount size_type=0, break load picpack");
				break;
			}
			int size=size_type&0xffffff;
			p.flag = (byte) (size_type>>>24);
			
			p.x = Util.hiloShort(in.readShort());
			p.y = Util.hiloShort(in.readShort());
			p.w = Util.hiloShort(in.readShort());
			p.h = Util.hiloShort(in.readShort());
			
			byte[] data=new byte[size];
			in.read(data);
			p.rawImg=data;
			in.seek(Util.get4Multiple((int)in.getFilePointer()));
			pac.pics.add(p);
		}
		in.close();
		return pac;
	}


	public static void write(File f0, int entrance, Picpack pac)throws IOException{
		RandomAccessFile f = new RandomAccessFile(f0, "rw");
		f.seek(entrance);
		f.write(pac.rebuild());
		f.close();
	}
	
	public static byte[] buildXYWH(short x,short y,short w,short h){
		ByteBuffer newXYWH=ByteBuffer.allocate(8);
		newXYWH.order(ByteOrder.LITTLE_ENDIAN);
		newXYWH.putShort(x);
		newXYWH.putShort(y);
		newXYWH.putShort(w);
		newXYWH.putShort(h);
		return newXYWH.array();
	}
}
