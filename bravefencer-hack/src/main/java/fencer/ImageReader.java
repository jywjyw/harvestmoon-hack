package fencer;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.Palette;
import common.Util;

public class ImageReader {
	
	public static BufferedImage read(RandomAccessFile in, int imgWidth, int imgHeight, Palette pal) throws IOException {
		if(pal.getColorCount()==16) {
			return readAs4bit(in, imgWidth, imgHeight, pal);
		} else if(pal.getColorCount()==256) {
			return readAs8bit(in, imgWidth, imgHeight, pal);
		} else {
			throw new UnsupportedOperationException("not available clut width:("+pal.getColorCount()+")");
		}
	}
	
	private static BufferedImage readAs4bit(RandomAccessFile in, int imgWidth, int imgHeight, Palette pal) throws IOException {
		int displayW = imgWidth*4;	//4bit下显示宽度*4;
		BufferedImage output = new BufferedImage(displayW, imgHeight, BufferedImage.TYPE_INT_ARGB);	
		WritableRaster raster = output.getRaster();
		
		Set<Integer> indexes = new HashSet<>();
		byte[] buf = new byte[imgWidth*2];
		int x=0,y=0;
		int[][] colorBuf = new int[2][4];
		boolean _break=false;
		while(true) {
			in.read(buf);
			for(byte b : buf) {
				int i1 = b>>>4&0xf, i2 = b&0xf;
				indexes.add(i1);
				indexes.add(i2);
				colorBuf[0]=pal.to32Rgba(i2);
				colorBuf[1]=pal.to32Rgba(i1);
				for(int[] i : colorBuf) {
					raster.setPixel(x++, y, i);	//rgba
					if(x>=displayW) {
						x=0;
						y++;
						if(y>=imgHeight) {
							_break=true;
							break;
						}
					}
				}
			}
			if(_break)break;
		}
		return output;
	}
	
	private static BufferedImage readAs8bit(RandomAccessFile in, int imgWidth, int imgHeight, Palette pal) throws IOException {
		int displayW = imgWidth*2;	//8bit下显示宽度*2;
		final BufferedImage output = new BufferedImage(displayW,imgHeight, BufferedImage.TYPE_INT_ARGB);	
		WritableRaster raster = output.getRaster();
		
		byte[] buf = new byte[imgWidth*2];
		int x=0,y=0;
		boolean _break=false;
		while(true) {
			in.read(buf);
			for(byte b : buf) {
				int[] color =pal.to32Rgba(b&0xff);
				raster.setPixel(x++, y, color);	//rgba
				if(x>=displayW) {
					x=0;
					y++;
					if(y>=imgHeight) {
						_break=true;
						break;
					}
				}
			}
			if(_break)break;
		}
		
		return output;
	}
	
}
