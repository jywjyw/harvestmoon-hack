package farm.tool;

import common.gpu.primitive.ClutId;

//根据字符编码计算色板/UV/TP,写汇编指令的依据
public class CharPos {
	//get char pos by code
	public static void main(String[] args) {
//		getCharPos(0x1);
//		getCharPos(0x01a5);
		getCharPos(0x0301);
//		getCharPos(0x0b44);
		System.out.println(0xb%3);
	}
	
	/**
	 * layer 0:0000,01B8,01B9,02DE
	 * layer 1:0300,04B8,04B9,05DE
	 * layer 2:0600,07B8,07B9,08DE
	 * layer 3:0900,0AB8,0aB9,0BDE
	 * left :0000~01B8=441
	 * right:01B9~02DE=294
	 */
	public static void getCharPos(int code){
		int codeL=code>>8;
		int mod=codeL%3, layer=codeL/3;
		System.out.println("layer="+layer);
		int clutX=896,clutY=252;
		layer=layer<<4;
		clutX+=layer;
		
		code=(mod<<8) | (code&0xff);
		int boundary=0x1b8;
		int boundaryOffset=code-boundary;
		int codeX=code;
		if(boundaryOffset > 0){
			codeX -= boundary;
			codeX -= 1;
		}
		
		int u=codeX%0x15, v=codeX/0x15;
		u=((u<<1)+u)<<2;	//u*12
		v=((v<<1)+v)<<2;	//v*12
		
		int tp=0xe;
		if(code-0x01b8 > 0){
			tp+=1;
		}
		
		System.out.printf("code=%04X,clut=%s,tp=%x,u=%d,v=%d\n",code,new ClutId(clutX,clutY),tp,u,v);
		//输出值:tp/clutX/clutY/u/v
	}

}
