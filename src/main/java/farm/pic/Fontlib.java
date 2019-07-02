package farm.pic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import common.Conf;
import common.Img4bitUtil;
import common.Palette;
import farm.Pic;
import farm.Picpack;
import farm.PicpackOper;

public class Fontlib implements PicHandler {

	@Override
	public void exportBoy(File f0, Picpack p0, Picpack d000, String exportDir) throws IOException {
//		Palette pal = new Palette(16, Conf.getRawFile("clut/912-254.16"));
		Palette pal = new Palette(16, Conf.getRawFile("clut/944-254.16"));
		Pic p = p0.pics.get(3);
		BufferedImage img = Img4bitUtil.readRomToBmp(new ByteArrayInputStream(p.extractImg()), p.w, p.h, pal);
		ImageIO.write(img, "bmp", new File(exportDir+"fontR.bmp"));
		p = p0.pics.get(4);
		img = Img4bitUtil.readRomToBmp(new ByteArrayInputStream(p.extractImg()), p.w, p.h, pal);
		ImageIO.write(img, "bmp", new File(exportDir+"fontL.bmp"));
	}

	@Override
	public void exportGirl(Picpack p0, Picpack d000, String exportDir) throws IOException {
	}

	@Override
	public void importBoy(File f0, Picpack d000) throws IOException {
	}

	@Override
	public void importGirl(File f0, Picpack pacD000) throws IOException {
	}

}
