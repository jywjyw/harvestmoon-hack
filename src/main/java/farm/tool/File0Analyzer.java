package farm.tool;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import common.Conf;
import common.Img4bitUtil;
import common.Img8bitUtil;
import common.Palette;
import common.RscLoader;
import common.Util;
import farm.Pic;
import farm.dump.Uncompressor;

public class File0Analyzer {
	public static void main(String[] args) throws IOException {
		a1();
//		print800();
	}
	
	public static void print800() throws IOException{
		RandomAccessFile in = new RandomAccessFile(Conf.desktop+"harvest\\0", "r");
		for(int i=0;i<in.length()/0x800;i++){
			in.seek(i*0x800);
			short head = in.readShort();
			if(head==0x0d10 || head==0x0e00)
				System.out.println(Integer.toHexString((int)in.getFilePointer()-2));
		}
		in.close();
		
	}
	
	//d000/
	public static void a1() throws IOException {
		final List<String> lines=new ArrayList<>();
		RscLoader.load("boy/img.properties", "gbk", new RscLoader.Callback() {
			@Override
			public void doInline(String line) {
				lines.add(line);
			}
		});
		
		Palette pal16=Palette.init16Grey(),pal256=Palette.init256();
		RandomAccessFile in = new RandomAccessFile(Conf.desktop+"harvest\\0", "r");
		for(String line:lines){
			String[] arr=line.split(",");
			try {
				int boundary=Integer.parseInt(arr[0],16);
				int picInd=0;
				in.seek(boundary);
				int pacFlag = in.readByte();
				in.seek(in.getFilePointer()-1);
				if(pacFlag==0xE){
					in.seek(Integer.parseInt(arr[1],16));
				}
				in.skipBytes(4);
				while(true){
					try {
						Pic p=new Pic();
						int size_type=Util.hilo(in.readInt());
						if(size_type==0) {
							System.out.println("stop,current offset="+Integer.toHexString((int)in.getFilePointer()));
							break;
						}
						p.flag = (byte) (size_type>>>24);
						p.x = Util.hiloShort(in.readShort());
						p.y = Util.hiloShort(in.readShort());
						p.w = Util.hiloShort(in.readShort());
						p.h = Util.hiloShort(in.readShort());
						int addr=(int) (in.getFilePointer()-12);
						System.out.printf("addr=%X,x=%d,y=%d,w=%d,h=%d,high=%d,lower=%d\n",addr,p.x,p.y,p.w,p.h,p.flag&0xc,p.flag&3);
						if(p.w>1024 || p.h>512) {
							System.err.println("illegal w&h");
							break;
						}
//					if(p.type&3==2) -> 800181ec; //judge lower 2 bits, maybe 0==4bit, 1=8bit, 2==clut
//					if(p.type&0xc!=0) -> 8001837c; //judge high 2 bits, if==0 , unzip
						
						byte[] data;
						int isCompressed;
						if((p.flag&0xc)==0){
							byte[] realData=new byte[(((size_type&0xffffff)+3)>>>2)<<2];	//be multiple of 4
							in.read(realData);
							data=Uncompressor.uncomp(new ByteArrayInputStream(realData));
							isCompressed=1;
						} else {
							data=new byte[size_type&0xffffff];
							in.read(data);
							isCompressed=0;
						}
						in.seek(Util.get4Multiple((int)in.getFilePointer()));	//must be multiple of 4
						
						int imgType=p.flag&3;
						if(imgType==0){	//0==4bit, 1=8bit, 2==clut
							BufferedImage img = Img4bitUtil.readRomToPng32(new ByteArrayInputStream(data), p.w, p.h, pal16);
							ImageIO.write(img, "png", new File(String.format("%sdump/%X-%d-%d-%dx%d.png",Conf.desktop,boundary,picInd,isCompressed,p.x,p.y)));
						} else if(imgType==1){
							BufferedImage img = Img8bitUtil.readRomToPng(new ByteArrayInputStream(data), p.w, p.h, pal256);
							ImageIO.write(img, "png", new File(String.format("%sdump/%X-%d-%d-%dx%d.png",Conf.desktop,boundary,picInd,isCompressed,p.x,p.y)));
						} else if(imgType==2) {
							try {
								////ignore clut
								BufferedImage img = Img8bitUtil.readRomToPng(new ByteArrayInputStream(data), p.w, p.h, pal256);
								ImageIO.write(img, "png", new File(String.format("%sdump/%X-%d-%d-%dx%d.png",Conf.desktop,boundary,picInd,isCompressed,p.x,p.y)));
								if(data.length==Palette.PAL16_CAPACITY){
//									pal16=new Palette(16, p.data);
								} else if(data.length==Palette.PAL256_CAPACITY){
//									pal256=new Palette(256, p.data);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						picInd++;
					} catch (EOFException e) {
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		in.close();
	}

}
