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

public class Here implements PicHandler{

	@Override
	public void exportBoy(File f0, Picpack p0, Picpack d000, String exportDir) throws IOException {
		Palette pal = new Palette(16, Conf.getRawFile("clut/832-129.16"));
		Picpack pac=PicpackOper.loadE(f0, 0x2f800,0x30b3c, 0x35000);
		Pic p=pac.pics.get(12);
		BufferedImage img = Img4bitUtil.readRomToBmp(new ByteArrayInputStream(p.extractImg()), p.w, p.h, pal);
		ImageIO.write(img, "bmp", new File(exportDir+Here.class.getSimpleName()+".bmp"));
	}

	@Override
	public void exportGirl(Picpack p0, Picpack d000, String exportDir) throws IOException {
		
	}

	@Override
	public void importBoy(File f0, Picpack d000) throws IOException {
		Palette pal = new Palette(16, Conf.getRawFile("clut/832-129.16"));
		BufferedImage img = ImageIO.read(new File(Conf.getRawFile("pic/Here-boy.bmp")));
		VramImg vram=Img4bitUtil.toVramImg(img, pal);
		Picpack pac=PicpackOper.loadE(f0, 0x2f800,0x30b3c, 0x35000);
		pac.modify(12, (short)vram.w, (short)vram.h, vram.data);
		PicpackOper.write(f0, 0x2f800, pac);
	}

	@Override
	public void importGirl(File f0, Picpack d000) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
