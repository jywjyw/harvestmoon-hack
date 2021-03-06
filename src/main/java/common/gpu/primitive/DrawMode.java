package common.gpu.primitive;

import common.Util;

public class DrawMode implements GpuCmd {
	public static void main(String[] args) {
		DrawMode s = new DrawMode(Util.decodeHex("1f0600e1"));
		System.out.println(s);
		System.out.println(Util.hexEncode(s.toBytes()));
	}
	
	public static final int SIZE = 4;
	public static final byte CMD = (byte)0xe1;
	
	private int tx,ty,abr,tp,dtd,dfe;
	
	public DrawMode(byte[] bs){
		if(bs.length!=SIZE||bs[3]!=CMD) 
			throw new UnsupportedOperationException();
		int i = bs[1]<<8 | bs[0];
		this.tx = i&0xf;
		this.ty = (i>>>4)&1;
		this.abr = (i>>>5)&3;
		this.tp = (i>>>7)&3;
		this.dtd = (i>>>9)&1;
		this.dfe = (i>>>10)&1;
	}
	
	@Override
	public byte[] toBytes(){
		byte[] bs = new byte[SIZE];
		bs[0] = (byte)(this.tx&7 | (this.ty&1)<<3 | (this.abr&3)<<4 | (this.tp&3)<<6); 
		bs[1] = (byte)(this.dtd&1 | (this.dfe&1)<<1);
		bs[2] = 0;
		bs[3] = CMD;
		return bs;
	}
	
	@Override
	public String toString() {
		return String.format("draw mode TP(%d, %d)(bit:%d)",
				tx*64, ty==0?0:256, tp==0?4:tp==1?8:tp==2?15:99999);
	}

}
