package fencer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.imageio.ImageIO;

import common.Palette;

public class ImgBlockDisplay {
	
	public static void mainx(String[] args) throws IOException {
		RandomAccessFile file = new RandomAccessFile(new File("D:\\ps3\\hanhua\\musashi-JP\\java\\MAIN\\000\\0.1"), "r");
		file.seek(0);
		BufferedImage img = ImageReader.read(file, 32, 32*100, new Palette(16, "992-0"));
		ImageIO.write(img, "png", new File("C:\\Users\\Administrator\\Desktop\\xx.png"));
		
		file.close();
	}
	
	public static void main(String[] args) throws IOException {
		RandomAccessFile file = new RandomAccessFile(new File("D:\\ps3\\hanhua\\musashi-JP\\java\\MAIN\\000\\0.1"), "r");
		file.seek(0x2324);
		BufferedImage img = ImageReader.read(file, 176, 32, new Palette(256, "448-255-8bit"));
		ImageIO.write(img, "png", new File("C:\\Users\\Administrator\\Desktop\\xx.png"));
		
		file.close();
	}
	
}
