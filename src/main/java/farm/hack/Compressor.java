package farm.hack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import common.Util;

public class Compressor {
	public static void main(String[] args) throws IOException {
		byte[] raw=Util.decodeHex("00010222222222223333");
		compress(new ByteArrayInputStream(raw));
	}
	
	public static byte[] compress(InputStream is) throws IOException{
		ByteArrayInputStream pairs=new ByteArrayInputStream(encodeToPairs(is));
		ByteArrayOutputStream comp=new ByteArrayOutputStream();
		byte[] pair=new byte[2];
		int len=0;
		ByteBuffer noRepeat=ByteBuffer.allocate(128);
		while((len=pairs.read(pair))!=-1){
//			System.out.printf("(%d,%02X)",pair[0],pair[1]);
			if(pair[0]==1){
				noRepeat.put(pair[1]);
				if(!noRepeat.hasRemaining()){
					flushNoRepeat(comp, noRepeat);
				}
			} else {
				if(noRepeat.position()>0){
					flushNoRepeat(comp, noRepeat);
				}
				comp.write(pair);
			}
		}
		if(noRepeat.position()>0){
			flushNoRepeat(comp, noRepeat);
		}
		return comp.toByteArray();
	}
	
	private static void flushNoRepeat(ByteArrayOutputStream comp, ByteBuffer noRepeat) throws IOException{
		comp.write(0xff-noRepeat.position()+1);
		comp.write(Arrays.copyOf(noRepeat.array(), noRepeat.position()));
		noRepeat.position(0);
	}
	
	
	
	private static byte[] encodeToPairs(InputStream is) throws IOException{
		ByteArrayOutputStream pairs=new ByteArrayOutputStream();
		int prev=is.read(),buf,count=1;
		while((buf=is.read())!=-1){
			if(buf==prev) {
				count++;
				if(count==127){
					pairs.write(count);
					pairs.write(prev);
					buf=is.read();
					if(buf==-1) break;
					count=1; 
				}
			} else if(count>0){
				pairs.write(count);
				pairs.write(prev);
				count=1;
			} else {
				throw new RuntimeException();
			}
			prev=buf;
		}
		is.close();
		
		pairs.write(1);		//assert count==1
		pairs.write(prev);
		return pairs.toByteArray();
	}


}
