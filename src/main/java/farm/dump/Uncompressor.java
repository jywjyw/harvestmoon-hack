package farm.dump;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
		
		System.out.println("after uncomp:"+uncomp.length);	
		
		FileOutputStream fos=new FileOutputStream(Conf.desktop+"uncomp");
		fos.write(uncomp);
		fos.close();
		
		Palette pal=new Palette(16, Conf.getRawFile("clut/912-254.16"));	//clut of default font = 944,254
		BufferedImage img=Img4bitUtil.readRomToBmp(new ByteArrayInputStream(uncomp), 63, 252, pal);
		ImageIO.write(img, "bmp", new File(Conf.desktop+"leftfont.bmp"));
	}
	
	public static byte[] uncomp(InputStream is) throws IOException{
		ByteArrayOutputStream os=new ByteArrayOutputStream();
		int n=0;
		while((n=is.read())!=-1){
			if((n<<24)>0){			//repeat next n bytes, n<=127
				int next=is.read();
				for(int i=0;i<n;i++){
					os.write(next);
				}
			} else if((n<<24)<0){	//just copy next n bytes, 1<=n<=128
				int count=0xff-n+1;
				for(int i=0;i<count;i++){
					os.write(is.read());
				}
			} else {				//00 means end
				break;
			}
		}
		return os.toByteArray();
	}
	
}
