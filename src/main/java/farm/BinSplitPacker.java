package farm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import common.Conf;
import common.Util;

public class BinSplitPacker {
	
	public static void main(String[] args) throws IOException {
		BinSplitPacker bin = new BinSplitPacker();
		String splitdir = Conf.desktop+"harvest\\";
		bin.split(Conf.girljpdir, splitdir);
//		System.out.println("waiting for modify bin by crystaltile2 ....");
//		bin.pack(splitdir, Conf.outdir);
//		IsoPatcher.patch(Conf.outdir, Conf.outdir+"boy_hack.iso");
//		System.out.println("finished.");
	}
	
	private List<File> splitFiles = new ArrayList<>();
	

	public void split(String binDir, String splitDir) throws IOException{
		File splitDir_ = new File(splitDir);
		if(!splitDir_.exists()) splitDir_.mkdirs();
		RandomAccessFile hdt = new RandomAccessFile(binDir+Conf.HDT, "r");
		
		List<Integer> addrs = new ArrayList<>();
		try {
			while(true){
				int addr = Util.hilo(hdt.readInt());
				addrs.add(addr);
			}
		} catch (IOException e) {
		}
		hdt.close();
		
		RandomAccessFile binFile = new RandomAccessFile(new File(binDir+Conf.BIN), "r");
		for(int i=0;i<addrs.size()-1;i++){	//最后一个是文件边界
			binFile.seek(addrs.get(i));
			int len=addrs.get(i+1)-addrs.get(i);
			byte[] buf = new byte[len];
			binFile.read(buf);
			
			File split = new File(splitDir+i);
			FileOutputStream fos = new FileOutputStream(split);
			fos.write(buf);
			fos.flush();
			fos.close();
			splitFiles.add(split);
		}
		binFile.close();
		System.out.println("BIN has been splitted to "+splitDir);
	}
	
	public File getFile(int index){
		return splitFiles.get(index);
	}
	
	public void pack(String splitDir, String outDir) throws IOException{
		FileOutputStream bin = new FileOutputStream(outDir+Conf.BIN);
		byte[] buf = new byte[1024];
		int len=0;
		for(File f : splitFiles){
			FileInputStream fis = new FileInputStream(f);
			while((len=fis.read(buf))!=-1){
				bin.write(buf, 0, len);
			}
			fis.close();
		}
		bin.close();
		
		
		FileOutputStream hdt = new FileOutputStream(outDir+Conf.HDT);
		ByteBuffer hdtDat = ByteBuffer.allocate(0x800);		//file length must be multiple of 0x800, or isopatcher.java will occur error.
		hdtDat.putInt(0);
		long addr=0;
		for(File f:splitFiles){
			addr+=f.length();
			hdtDat.putInt(Util.hilo((int)addr));
		}
		hdt.write(hdtDat.array());
		hdt.close();
	}
	
}
