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
		String dir = "C:\\Users\\me\\Desktop\\farm-jp\\";
		new BinSplitPacker(dir).split(dir+File.separator+"A_FILE.bin");
	}
	
	public static BinSplitPacker newInstance(){
		String splitDir = System.getProperty("java.io.tmpdir");
		if(!splitDir.endsWith(File.separator))
			splitDir+=File.separator;
		splitDir+="harvest"+File.separator;
		return new BinSplitPacker(splitDir);
	}
	
	private List<File> splitFiles = new ArrayList<>();
	private String splitDir;
	
	public BinSplitPacker(String splitDir) {
		this.splitDir = splitDir;
	}

	public void split(String bin) throws IOException{
		File splitDir_ = new File(splitDir);
		if(!splitDir_.exists()) splitDir_.mkdirs();
		String parentDir = new File(bin).getParent()+File.separator;
		RandomAccessFile hdt = new RandomAccessFile(new File(parentDir+"A_FILE.HDT"), "r");
		
		List<Integer> addrs = new ArrayList<>();
		try {
			while(true){
				int addr = Util.hilo(hdt.readInt());
				addrs.add(addr);
			}
		} catch (IOException e) {
		}
		hdt.close();
		
		RandomAccessFile binFile = new RandomAccessFile(new File(bin), "r");
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
	}
	
	public File getFile(int index){
		return splitFiles.get(index);
	}
	
	public void pack(String targetBin) throws IOException{
		FileOutputStream bin = new FileOutputStream(targetBin);
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
		
		String parentDir = new File(targetBin).getParent()+File.separator;
		FileOutputStream hdt = new FileOutputStream(parentDir+"A_FILE.HDT");
		hdt.write(buildHDT());
		hdt.close();
	}
	
	public void dispose(){
		for(File f : splitFiles){
			f.delete();
		}
		new File(splitDir).delete();
	}
	
	
	public String getSplitDir(){
		return splitDir;
	}
	
	private byte[] buildHDT(){
		ByteBuffer buf = ByteBuffer.allocate((splitFiles.size()+1)*4);
		buf.putInt(0);
		long addr=0;
		for(File f:splitFiles){
			addr+=f.length();
			buf.putInt(Util.hilo((int)addr));
		}
		return buf.array();
	}
}
