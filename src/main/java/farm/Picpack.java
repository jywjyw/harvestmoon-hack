package farm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import common.Util;
import farm.hack.Compressor;

public class Picpack {
	
	public byte[] headE;	//如果第一个数据为0x0e,则有头数据
	public int capacity;	//headE + data. 补齐至0x800后的大小
	public int unk;
	public List<Pic> pics=new ArrayList<>();
	
	public void modify(int index, short x, short y) throws IOException{
		Pic old=pics.get(index);
		old.x=x;
		old.y=y;
	}
	
	public void modify(int index, short w, short h, byte[] data) throws IOException{
		modify(index, null,null,w, h, data);;
	}
	
	public void modify(int index, Short x, Short y, short w, short h, byte[] data) throws IOException{
		Pic old=pics.get(index);
		if(x!=null) old.x=x;
		if(y!=null) old.y=y;
		old.w=w;
		old.h=h;
		if(old.isCompress()){
			data=new Compressor().compress(new ByteArrayInputStream(data));
		}
		old.rawImg=data;
	}
	
	public void modifyHeadE(int pos, byte[] data) {
		System.arraycopy(data, 0, headE, pos, data.length);
	}
	
	public Picpack adjustCapacity(int capacity){
		this.capacity=capacity;
		return this;
	}
	
	public byte[] rebuild(){
		ByteBuffer ret=ByteBuffer.allocate(capacity);
		if(headE!=null) {
			ret.put(headE);
		}
		try {
			ret.putInt(unk);
			for(Pic p:pics){
				ret.put(p.rebuild());
				int align=Util.get4Multiple(p.rawImg.length)-p.rawImg.length;
				if(align>0){
					ret.put(new byte[align]);
				}
			}
		} catch (BufferOverflowException e) {
			System.err.println("是否压缩后的数据超出了范围");
			throw new RuntimeException(e);
		}
		System.out.println("picpack real len="+Integer.toHexString(ret.position()));
		return ret.array();
	}

	
}
