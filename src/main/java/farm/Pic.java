package farm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import farm.dump.Uncompressor;

public class Pic{
	public byte flag;
	public short x,y,w,h;	//2 bytes
	public byte[] rawImg;	//maybe compressed
	public boolean isCompress(){
		return (flag&0xc)==0;
	}
	public void disableCompress(){
		flag = (byte)(4|(flag&3));
	}
	public byte[] extractImg(){
		if(isCompress()){
			try {
				return Uncompressor.uncomp(new ByteArrayInputStream(rawImg));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return rawImg;
	}
	
	public byte[] rebuild(){
		ByteBuffer buf=ByteBuffer.allocate(rawImg.length+12);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.putInt((flag<<24)|rawImg.length);
		buf.putShort(x);
		buf.putShort(y);
		buf.putShort(w);
		buf.putShort(h);
		buf.put(rawImg);
		return buf.array();
	}
}