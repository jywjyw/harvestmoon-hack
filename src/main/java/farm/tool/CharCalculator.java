package farm.tool;

public class CharCalculator {
	
	public static void main(String[] args) {
		CharCalculator c=new CharCalculator();
		int code=0x2a;
		System.out.printf("tpOffset=%d,uv=(%d,%d)",c.getTPPointerOffset(code),c.getU(code), c.getV(code));
	}

	private int getTPPointerOffset(int code) {
		long mul=code*(long)0x4a4dc96f;
		int x=(int) ((mul>>>32)&0xffffffff);
		int y=x>>7;
		int tpPointerOffset=((y<<1)+y)*4;		//tpPointerAddr=8004b538+offset
		return tpPointerOffset;
	}
	
	private int getUVseed(int code) {
		long mul=code*0x86186187L;
		int hi=(int) ((mul>>>32)&0xffffffff);
		int r1=hi + (code-hi)/2;
		return r1>>>4;
	}
	
	private int getU(int code) {
		int seed=getUVseed(code);
		int useed=code-seed*21;
		return useed*12+0;		//TODO 0=&8004b538+8
	}
	
	private int getV(int code) {
		int seed=getUVseed(code);
		int r1=seed*12;
		return r1+0;	//TODO 0=&8004b538+a
	}
	

}
