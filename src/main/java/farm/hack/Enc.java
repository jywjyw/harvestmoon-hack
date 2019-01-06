package farm.hack;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import common.Conf;
import common.Img4bitUtil;
import common.Palette;
import common.Util;
import common.VramImg;

public class Enc {
	public static void main(String[] args) throws IOException {
		Enc enc=new Enc();
		for(int a=0xb0;a<=0xf7;a++){
			for(int b=0xa1;b<=0xfe;b++){
				if(enc.size()>=CAPACITY) break;
				String zh=new String(new byte[]{(byte)a,(byte)b},"gbk");
//				System.out.println(zh);
				enc.getCode(zh);
			}
		}
		
		VramImg vram=FourLayerFontGen.build4LayerFont(enc.getLeftChars(), 63*4, Conf.desktop+"Zfull-GB.ttf", 12);
		List<Palette> pals=FourLayerFontGen.build4Palette((short)0xff7f);
		for(int i=0;i<pals.size();i++){
			Palette p = pals.get(i);
			BufferedImage img = Img4bitUtil.readRomToBmp(new ByteArrayInputStream(vram.data), vram.w, vram.h, p);
			ImageIO.write(img, "bmp", new File(Conf.desktop+i+"L.bmp"));
		}
		
		System.out.println(Util.join(enc.getRightChars().getCharLayer(3), "",""));
		vram=FourLayerFontGen.build4LayerFont(enc.getRightChars(), 63*4, Conf.desktop+"Zfull-GB.ttf", 12);
		pals=FourLayerFontGen.build4Palette((short)0xff7f);
		for(int i=0;i<pals.size();i++){
			Palette p = pals.get(i);
			BufferedImage img = Img4bitUtil.readRomToBmp(new ByteArrayInputStream(vram.data), vram.w, vram.h, p);
			ImageIO.write(img, "bmp", new File(Conf.desktop+i+"R.bmp"));
		}
	}
	
	private static final int CAPACITY=2940, L_LIMIT=0x1B8, R_LIMIT=0x02DE;
	
	private Map<String,Integer> 
		char_code=new HashMap<>(),
		char_count=new HashMap<>();
	
	private LinkedList<Integer> codePool=new LinkedList<>();
	
	public Enc(){
		int c;
		for(int i=0;i<=R_LIMIT;i++){
			c=i;
			codePool.add(c);
			c+=0x300;
			codePool.add(c);
			c+=0x300;
			codePool.add(c);
			c+=0x300;
			codePool.add(c);
		}
//		RscLoader.load("init_enc.gbk", "gbk", new Callback() {
//			@Override
//			public void doInline(String line) {
//				String[] arr=line.split("=");
//				String ch=arr[1];
//				Integer code=Integer.parseInt(arr[0],16);
//				char_code.put(ch, code);
//				char_count.put(ch,99999);
//				codePool.remove(code);
//			}
//		});
	}
	
	
	public int getCode(String char_){
		Integer code=char_code.get(char_);
		if(code!=null){
			return code;
		} else {
			code=codePool.poll();
			if(code==null) throw new UnsupportedOperationException("too many char, there's not enough char code");
			char_code.put(char_,code);
			return code;
		}
	}
	
	public int size(){
		return char_code.size();
	}
	
	 
	public void check(){
		
	}
	
	
	public FourLayerChars getLeftChars(){
		List<Kv> kvs=buildKvs();
		FourLayerChars f=new FourLayerChars();
		for(int i=0;i<=L_LIMIT;i++){
			for(int j=0,index=i;j<4;j++,index+=0x300){
				if(index<kvs.size()){
					Kv kv=kvs.get(index);
					f.getCharLayer(j).add(kv.char_);
				}
			}
		}
		return f;
	}
	
	
	public FourLayerChars getRightChars(){
		List<Kv> kvs=buildKvs();
		FourLayerChars f=new FourLayerChars();
		for(int i=L_LIMIT+1;i<=R_LIMIT;i++){
			for(int j=0,index=i;j<4;j++,index+=0x300){
				if(index<kvs.size()){
					Kv kv=kvs.get(index);
					f.getCharLayer(j).add(kv.char_);
				}
			}
		}
		return f;
	}
	
	
	private List<Kv> buildKvs(){
		List<Kv> kvs=new ArrayList<>();
		for(Entry<String,Integer> e:char_code.entrySet()){
			kvs.add(new Kv(e.getKey(),e.getValue()));
		}
		Collections.sort(kvs, new Comparator<Kv>() {
			@Override
			public int compare(Kv o1, Kv o2) {
				return o1.code-o2.code;
			}
		});
		return kvs;
	}
	
	private class Kv{
		public String char_;
		public int code;
		public Kv(String char_, int code) {
			this.char_ = char_;
			this.code = code;
		}
	}

}
