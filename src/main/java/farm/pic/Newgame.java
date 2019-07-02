package farm.pic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import common.Conf;
import common.Img4bitUtil;
import common.Palette;
import common.VramImg;
import farm.Pic;
import farm.Picpack;
import farm.PicpackOper;

public class Newgame implements PicHandler {

	@Override
	public void exportBoy(File f0, Picpack p0, Picpack d000, String exportDir) throws IOException {
		export(d000, exportDir, 34);
	}
	
	@Override
	public void exportGirl(Picpack p0, Picpack d000, String exportDir) throws IOException {
		export(d000, exportDir, 27);
	}
	
	public void export(Picpack d000, String exportDir, int picInd) throws IOException {
		Palette pal = new Palette(16, Conf.getRawFile("clut/newgame.16"));
		Pic p = d000.pics.get(picInd);
		BufferedImage img = Img4bitUtil.readRomToBmp(new ByteArrayInputStream(p.extractImg()), p.w, p.h, pal);
		ImageIO.write(img, "bmp", new File(exportDir+Newgame.class.getSimpleName()+".bmp"));
	}

	@Override
	public void importBoy(File f0, Picpack d000) throws IOException {
		import_(d000, 34);
	}

	@Override
	public void importGirl(File f0, Picpack d000) throws IOException {
		import_(d000, 27);
	}
	
	public void import_(Picpack pacD000, int picInd) throws IOException {
		Palette pal = new Palette(16, Conf.getRawFile("clut/newgame.16"));
		BufferedImage img = ImageIO.read(new File(Conf.getRawFile("pic/"+Newgame.class.getSimpleName()+".bmp")));
		VramImg vram=Img4bitUtil.toVramImg(img, pal);
		Pic p = pacD000.pics.get(picInd);
		pacD000.modify(picInd, p.w, p.h, vram.data);
	}

}
