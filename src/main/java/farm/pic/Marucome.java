package farm.pic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import common.Conf;
import common.Img4bitUtil;
import common.Palette;
import common.PixelConverter;
import common.VramImg;
import farm.Pic;
import farm.Picpack;
import farm.PicpackOper;

public class Marucome implements PicHandler {
	@Override
	public void exportBoy(File f0, Picpack p0, Picpack d000, String exportDir) throws IOException {
		export(d000, exportDir, 70, 69);
	}

	@Override
	public void exportGirl(Picpack p0, Picpack d000, String exportDir) throws IOException {
		export(d000, exportDir, 61, 60);
	}

	public void export(Picpack d000, String exportDir, int picInd1, int picInd2) throws IOException {
		Palette pal = new Palette(16, Conf.getRawFile("clut/newgame.16"));
		Pic left = d000.pics.get(picInd1);
		BufferedImage leftimg= Img4bitUtil.readRomToBmp(new ByteArrayInputStream(left.extractImg()), left.w, left.h, pal);
		
//		Pic right= d000.pics.get(picInd2);
//		BufferedImage rightimg = Img4bitUtil.readRomToBmp(new ByteArrayInputStream(right.extractImg()), right.w, right.h, pal);
//		
//		BufferedImage img=new BufferedImage(leftimg.getWidth()+rightimg.getWidth(), leftimg.getHeight(), BufferedImage.TYPE_INT_RGB);
//		int[] buf=new int[3];
//		for(int y=0;y<leftimg.getHeight();y++){
//			for(int x=0;x<leftimg.getWidth();x++){
//				leftimg.getRaster().getPixel(x, y, buf);
//				img.getRaster().setPixel(x, y, buf);
//			}
//		}
//		for(int y=0;y<rightimg.getHeight();y++){
//			for(int x=0;x<rightimg.getWidth();x++){
//				rightimg.getRaster().getPixel(x, y, buf);
//				img.getRaster().setPixel(x+leftimg.getWidth(), y, buf);
//			}
//		}
		
		ImageIO.write(leftimg, "bmp", new File(exportDir+Marucome.class.getSimpleName()+".bmp"));
	}
	

	@Override
	public void importBoy(File f0, Picpack d000) throws IOException {
		import_(f0, d000, 69, 0x23c);
	}
	
	@Override
	public void importGirl(File f0, Picpack d000) throws IOException {
		import_(f0, d000, 60, 0x1f4);
	}
	
	public void import_(File f0, Picpack d000, int incInd, int xyPos) throws IOException {
		BufferedImage qr = Qrcode.gen();
		Palette pal = new Palette(16, Conf.getRawFile("clut/newgame.16"));
		VramImg vram=Img4bitUtil.toVramImg(qr, new PixelConverter() {
			@Override
			public int toPalIndex(int[] pixel) {
				if(pixel[0]==0)			return 1;
				else if(pixel[0]==1)	return 15;
				throw new UnsupportedOperationException();
			}
		});
//		ImageIO.write(Img4bitUtil.readRomToBmp(new ByteArrayInputStream(vram.data), vram.w, vram.h, pal), "bmp", new File(Conf.desktop+"qrcode.bmp"));
		short newX=(short)620, newY=(short)406;
		d000.modify(incInd, newX,newY, (short)vram.w, (short)vram.h, vram.data);
		d000.modifyHeadE(xyPos, PicpackOper.buildXYWH(newX,newY,(short)vram.w,(short)vram.h));
	}

}
