package farm.pic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import common.Conf;
import common.Img4bitUtil;
import common.Palette;
import common.VramImg;
import farm.Pic;
import farm.Picpack;
import farm.hack.Compressor;

public class Continue implements PicHandler {

	@Override
	public void exportBoy(File f0, Picpack p0, Picpack d000, String exportDir) throws IOException {
		export(d000, exportDir, 35);
	}
	
	@Override
	public void exportGirl(Picpack p0, Picpack d000, String exportDir) throws IOException {
		export(d000, exportDir, 28);
	}

	private void export(Picpack d000, String exportDir, int picInd) throws IOException{
		Palette pal = new Palette(16, Conf.getRawFile("clut/newgame.16"));
		Pic p = d000.pics.get(picInd);
		BufferedImage img = Img4bitUtil.readRomToBmp(new ByteArrayInputStream(p.extractImg()), p.w, p.h, pal);
		ImageIO.write(img, "bmp", new File(exportDir+Continue.class.getSimpleName()+".bmp"));
	}

	@Override
	public void importBoy(File f0, Picpack d000) throws IOException {
		import_(d000,35);
	}
	
	@Override
	public void importGirl(File f0, Picpack d000) throws IOException {
		import_(d000,28);
	}
	
	public void import_(Picpack d000, int picInd) throws IOException {
		Palette pal = new Palette(16, Conf.getRawFile("clut/newgame.16"));
		BufferedImage img = ImageIO.read(new File(Conf.getRawFile("pic/"+Continue.class.getSimpleName()+".bmp")));
		VramImg vram=Img4bitUtil.toVramImg(img, pal);
		Pic p = d000.pics.get(picInd);
		d000.modify(picInd, p.w, p.h, vram.data);
	}

}
