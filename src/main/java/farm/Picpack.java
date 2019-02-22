package farm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import common.Util;
import farm.dump.Uncompressor;
import farm.hack.Compressor;

public class Picpack {
	
	public static Picpack load(byte[] data, int capacity) throws IOException{
		DataInputStream in=new DataInputStream(new ByteArrayInputStream(data));
		Picpack pac=new Picpack();
		pac.capacity=capacity;	//capacity means multiple of 0x800
		pac.unk=Util.hilo(in.readInt());
		while(true){
			try {
				Pic p=new Pic();
				int size_type=Util.hilo(in.readInt());
				p.type = (byte) (size_type>>>24);
				p.x = Util.hiloShort(in.readShort());
				p.y = Util.hiloShort(in.readShort());
				p.w = Util.hiloShort(in.readShort());
				p.h = Util.hiloShort(in.readShort());
				if(p.type==0 || p.type==4){
					byte[] realData=new byte[(((size_type&0xffffff)+3)>>>2)<<2];	//be multiple of 4
					in.read(realData);
					p.data=Uncompressor.uncomp(new ByteArrayInputStream(realData));
				} else {
					p.data=new byte[size_type&0xffffff];
					in.read(p.data);
				}
				pac.pics.add(p);
			} catch (EOFException e) {
				break;
			}
		}
		return pac;
	}
	
	public int capacity;
	public int unk;
	public List<Pic> pics=new ArrayList<>();
	
	public void modify(int index, short w, short h, byte[] data){
		Pic old=pics.get(index);
		old.data=data;
		old.w=w;
		old.h=h;
	}
	
	public byte[] rebuild(){
		ByteBuffer ret=ByteBuffer.allocate(capacity);
		ret.order(ByteOrder.LITTLE_ENDIAN);
		try {
			ret.putInt(unk);
			for(Pic p:pics){
				if(p.type==Pic.TYPE_COMPRESS){
					byte[] comp=Compressor.compress(new ByteArrayInputStream(p.data));
					ret.putInt(p.type<<24|comp.length);
					ret.putShort(p.x);
					ret.putShort(p.y);
					ret.putShort(p.w);
					ret.putShort(p.h);
					ret.put(comp);
					int multiple4=((comp.length+3)>>>2)<<2;
					byte[] align=new byte[multiple4-comp.length];
					ret.put(align);
					
				} else {
					ret.putInt(p.type<<24|p.data.length);
					ret.putShort(p.x);
					ret.putShort(p.y);
					ret.putShort(p.w);
					ret.putShort(p.h);
					ret.put(p.data);
				}
			}
		} catch (BufferOverflowException e) {
			System.err.println("是否压缩后的数据超出了范围");
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.println("rebuilded picpack's real length="+ret.position());
		return ret.array();
	}
	
}
