package farm.hack;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import common.Conf;
import common.Img4bitUtil;
import common.Palette;
import common.PixelFontGen;
import common.Util;
import common.VramImg;
import common.VramImg.VramImg4bitWriter;

public class FourLayerFontGen {
	
	public static void main(String[] args) throws IOException  {
		FourLayerChars charLayers = new FourLayerChars();
		for(int i=0;i<21*21;i++){
			charLayers.put("中");
			charLayers.put("国");
			charLayers.put("智");
			charLayers.put("造");
		}
		VramImg vram=build4LayerFont(charLayers, 63*4, Conf.desktop+"方正像素12.ttf", 12);
		List<Palette> pals=build4Palette((short)0xff7f);
		for(int i=0;i<pals.size();i++){
			Palette p = pals.get(i);
			BufferedImage img = Img4bitUtil.readRomToBmp(new ByteArrayInputStream(vram.data), vram.w, vram.h, p);
			ImageIO.write(img, "bmp", new File(Conf.desktop+i+".bmp"));
		}
	}
	
	public static VramImg build4LayerFont(FourLayerChars charLayers, int w, String fontFile, int fontsize){
		int charcount=charLayers.getCharLayer(0).size()*charLayers.chars.size();
		int layer=4,
			marginRight=w%fontsize,
			column=w/fontsize,
			charPerLayer=charcount/layer,
			h=(charPerLayer%column>0 ? charPerLayer/column+1 : charPerLayer/column) * fontsize;
		VramImg vram = new VramImg4bitWriter(w, h).build();
		for(int i=0;i<layer;i++){
			String chars=Util.join(charLayers.getCharLayer(i),"",null);
			List<BufferedImage> tiles=PixelFontGen.genBmpTiles(chars, fontFile, fontsize, 0);
			BufferedImage charimg=Img4bitUtil.jointTiles(tiles, column, marginRight, PixelFontGen.BACKGROUND_COLOR);
			vram = addLayer(charimg, vram, i+1);
		}
		return vram;
	}
	
	
	public static List<Palette> build4Palette(short fontColor){
		List<Palette> pals=new ArrayList<>();
		short bgColor=0;
		ByteBuffer pal=ByteBuffer.allocate(32);
		for(int i=0;i<8;i++){
			pal.putShort(bgColor);
		}
		for(int i=8;i<16;i++){
			pal.putShort(fontColor);
		}
		pals.add(new Palette(16, pal.array()));
		pal.clear();
		
		for(int i=0;i<2;i++){
			for(int j=0;j<4;j++) pal.putShort(bgColor);
			for(int k=0;k<4;k++) pal.putShort(fontColor);
		}
		pals.add(new Palette(16, pal.array()));
		pal.clear();
		
		for(int i=0;i<4;i++){
			pal.putShort((short) 0);
			pal.putShort((short) 0);
			pal.putShort(fontColor);
			pal.putShort(fontColor);
		}
		pals.add(new Palette(16, pal.array()));
		pal.clear();
		
		for(int i=0;i<8;i++){
			pal.putShort(bgColor);
			pal.putShort(fontColor);
		}
		pals.add(new Palette(16, pal.array()));
		pal.clear();
		
		return pals;
	}
	
	
	private static VramImg addLayer(BufferedImage img, VramImg lastLayer, int layerNum){
		Iterator<Byte> it=lastLayer.get4bitIterator();
		VramImg4bitWriter ret = new VramImg4bitWriter(img.getWidth(), img.getHeight());
		int[] pixel=new int[4];
		for(int y=0;y<img.getHeight();y++){
			for(int x=0;x<img.getWidth();x++){	
				img.getRaster().getPixel(x, y, pixel);
				byte last=it.next();
				if(pixel[0]==0){ //字体色
					ret.addPixelIndex(last | 1<<(4-layerNum));	//layerNum start from 1
				}else if(pixel[0]==255){ //背景色
					ret.addPixelIndex(last);
				}else{
					throw new RuntimeException();
				}
			}
		}
		return ret.build();
	}
	
}
