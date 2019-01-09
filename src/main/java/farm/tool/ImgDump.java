package farm.tool;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.imageio.ImageIO;

import common.Conf;
import common.Img4bitUtil;
import common.Palette;
import farm.Pic;
import farm.Picpack;

public class ImgDump {
	
	public static void main(String[] args) throws IOException {
		RandomAccessFile file=new RandomAccessFile(Conf.desktop+"boyjp/0", "r");
		int pac0Entrance=0, pac0Capacity=0xD000;
//		int pac0DataLen=0xCAE0;
		int pac0DataLen=51342;
		file.seek(pac0Entrance);
		byte[] pac0Bytes=new byte[pac0DataLen];
		file.read(pac0Bytes);
		file.close();
		
		Picpack pac0=Picpack.load(pac0Bytes, pac0Capacity);
		Palette pal=Palette.init16Grey();
		for(int i=0;i<pac0.pics.size();i++){
			Pic p=pac0.pics.get(i);
			BufferedImage img = Img4bitUtil.readRomToBmp(new ByteArrayInputStream(p.data), p.w, p.h, pal);
			ImageIO.write(img, "bmp", new File(Conf.desktop+"boydump/"+i+".bmp"));
		}
	}

}
