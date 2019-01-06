package farm.dump;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import common.Conf;
import common.Img4bitUtil;
import common.Palette;

public class Uncompressor {
	public static void main(String[] args) throws IOException {
		FileInputStream is=new FileInputStream(Conf.desktop+"boyjp\\0");
		is.skip(0x5344);
		byte[] uncomp=uncomp(is);
		is.close();
		
		Palette pal=new Palette(16, Conf.getRawFile("clut/912-254.16"));
		BufferedImage img=Img4bitUtil.readRomToBmp(new ByteArrayInputStream(uncomp), 63, 252, pal);
		ImageIO.write(img, "bmp", new File(Conf.desktop+"leftfont.bmp"));
	}
	
	public static byte[] uncomp(InputStream is) throws IOException{
		ByteArrayOutputStream os=new ByteArrayOutputStream();
		int buf=0;
		while(true){
			buf=is.read();
			if((buf<<24)>0){
				int next=is.read();
				for(int i=0;i<buf;i++){
					os.write(next);
				}
			} else if((buf<<24)<0){
				int count=0xff-buf+1;
				for(int i=0;i<count;i++){
					os.write(is.read());
				}
			} else {
				return os.toByteArray();
			}
		}
		
	}

}
