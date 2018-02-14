package fencer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.imageio.ImageIO;

public class BraveFencer {
	public static void main(String[] args) throws Exception {
		fontViewer2();
	}
	
	/**
	 * MAIN.CD， 0x30ecec开始, 字体11*11双色1bpp, 每行16位,每个字占内存22字节
	 * @param args
	 * @throws IOException 
	 */
	public static void fontViewer() throws Exception {
		int charCount = 618, imgw = 16, imgh = charCount*11;	//618
		BufferedImage img = new BufferedImage(imgw, imgh, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setColor(Color.WHITE);	
		g.fillRect(0, 0, imgw, imgh);
		
		RandomAccessFile cd = new RandomAccessFile(new File("D:\\ps3\\hanhua\\musashi\\MAIN.CD"), "r");
		cd.seek(0x30ec4c);//00001e0021002100210021002100	//0x30ec4c
		int buf=0;
		for(int i=0;i<imgh;i++) {
			buf = cd.readUnsignedShort();
			int x=0;
			for(int j=15;j>=0;j--) {
				int point = buf>>>j&1;
				int color = point==1?Color.BLACK.getRGB() : Color.WHITE.getRGB();
				img.setRGB(x++, i, color);
			}
		}
		System.out.println(cd.getFilePointer());
		cd.close();
		ImageIO.write(img, "bmp", new File("C:\\Users\\Administrator\\Desktop\\font.bmp"));
	}
	
	public static void fontViewer2() throws Exception {
//		RandomAccessFile cd = new RandomAccessFile(new File("D:\\ps3\\hanhua\\musashi\\MAIN.CD.dir\\FILE_004"), "r");
		RandomAccessFile cd = new RandomAccessFile(new File("C:\\Users\\Administrator\\Desktop\\ram.bin"), "r");
		cd.seek(0);
		int charCount = (int) (cd.length()/22), imgw = 16, imgh = charCount*11;
		BufferedImage img = new BufferedImage(imgw, imgh, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setColor(Color.WHITE);	
		g.fillRect(0, 0, imgw, imgh);
		
		int buf=0;
		try {
			for(int i=0;i<imgh;i++) {
				buf = cd.readUnsignedShort();
				int x=0;
				for(int j=15;j>=0;j--) {
					int point = buf>>>j&1;
					int color = point==1?Color.BLACK.getRGB() : Color.WHITE.getRGB();
					img.setRGB(x++, i, color);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			System.out.println(cd.getFilePointer());
			cd.close();
			ImageIO.write(img, "bmp", new File("C:\\Users\\Administrator\\Desktop\\font2.bmp"));
	}

}
