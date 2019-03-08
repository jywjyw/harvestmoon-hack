package farm.pic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import common.Conf;
import common.Img8bitUtil;
import common.Palette;
import farm.Pic;
import farm.Picpack;
import farm.PicpackOper;

public class Logo implements PicHandler {

	@Override
	public void exportBoy(Picpack p0, Picpack d000, String exportDir) throws IOException {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void importGirl(File f0, Picpack d000) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
