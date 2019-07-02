package farm.pic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import common.Conf;
import common.Img8bitUtil;
import common.Palette;
import common.VramImg;
import farm.Pic;
import farm.Picpack;

public class Logo implements PicHandler {

	@Override
	public void exportBoy(File f0, Picpack p0, Picpack d000, String exportDir) throws IOException {
		export(d000, exportDir, Conf.getRawFile("clut/logo.256"), 64);
	}
	
	@Override
	public void exportGirl(Picpack p0, Picpack d000, String exportDir) throws IOException {
		export(d000, exportDir, Conf.getRawFile("clut/logo-girl.256"), 65);
	}

	public void export(Picpack d000, String exportDir, String clut, int picInd) throws IOException {
		Palette pal = new Palette(256, clut);
		Pic p = d000.pics.get(picInd);
		BufferedImage img = Img8bitUtil.readRomToBmp(new ByteArrayInputStream(p.extractImg()), p.w, p.h, pal);
		ImageIO.write(img, "bmp", new File(exportDir+Logo.class.getSimpleName()+".bmp"));
	}

	@Override
	public void importBoy(File f0, Picpack d000) throws IOException {
		Palette pal = new Palette(256, Conf.getRawFile("clut/logo.256"));
		BufferedImage img = ImageIO.read(new File(Conf.getRawFile("pic/Logo.bmp")));
		VramImg vram=Img8bitUtil.toVramImg(img, pal);
		Pic p = d000.pics.get(64);
		d000.modify(64, p.w, p.h, vram.data);
	}

	@Override
	public void importGirl(File f0, Picpack d000) throws IOException {
		Palette pal = new Palette(256, Conf.getRawFile("clut/logo-girl.256"));
		BufferedImage img = ImageIO.read(new File(Conf.getRawFile("pic/Logo-girl.bmp")));
		VramImg vram=Img8bitUtil.toVramImg(img, pal);
		Pic p = d000.pics.get(65);
		d000.modify(65, p.w, p.h, vram.data);
	}
}
