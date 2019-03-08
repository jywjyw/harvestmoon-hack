package farm.tool;

public class Uncomp {
	
	public static void main(String[] args) {
		byte[] pair=new byte[]{0x3,(byte)0xd8};
		
		int mask1=7;
		int offset=(((pair[1]&7)<<8)|pair[0])+1;
		
		int mask2=3;
		int len=(pair[1]>>mask2)+3;
		
	}

}
