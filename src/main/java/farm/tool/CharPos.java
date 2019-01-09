package farm.tool;

public class CharPos {
	//get char pos by code
	public static void main(String[] args) {
		getCharPos1(0x01a5);
		getCharPos1(0x0244);
		getCharPos1(0x0844);
		System.out.println(0xb%3);
	}
	
	/**
	 * layer 0:00xx,01xx,02xx
	 * layer 1:03xx,04xx,05xx
	 * layer 2:06xx,07xx,08xx
	 * layer 3:09xx,0Axx,0Bxx
	 * left :0000~01B8=735
	 * right:01B9~02DE=441
	 */
	public static void getCharPos1(int code){
		int n2=code>>8;
		int layer=n2/3;
		int mod=n2%3;
		int code1=(mod<<8) | (code&0xff);
		int tp=0x380;
		if(code1>=0x01b9){
			tp+=0x40;
			code1-=0x01b9;
		}
		int u=code1%21;
		int v=code1/21;
		u=(u<<3)+(u<<2);	//u*12
		v=(v<<3)+(v<<2);	//v*12
		System.out.printf("code=%04X,layer=%d,tp=%d,u=%d,v=%d\n",code,layer,tp,u,v);
	}

}
