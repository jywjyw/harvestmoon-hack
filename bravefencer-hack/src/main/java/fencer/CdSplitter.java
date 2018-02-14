package fencer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

import common.Util;

public class CdSplitter {
	public static final int SECTOR = 0x800, PAC_MAGIC = 0x50414300, SQV_MAGIC = 0x2E737176;
	
	public static void main(String[] args) throws IOException {
//		int size = 2049;
//		if(size%2048>0){
//			size = size/2048*2048+2048;
//		}
//		System.out.println(size);
		new CdSplitter().extract();
	}
	public void extract() throws IOException {
		Map<String,List<CDpart>> locations = new HashMap<>();
		
		String dir = "D:\\ps3\\hanhua\\musashi-JP\\";
		String[] cds = new String[]{"MAIN","SC01","SC02","SC03","SC04","SC05","SC06","SC07"};
		List<File> subcds = new ArrayList<>();
		for(String cd:cds){
			List<CDpart> parts = new ArrayList<>();
			RandomAccessFile cdfile = new RandomAccessFile(dir+cd+".CD", "r");
			Map<Integer,Integer> entrance_size = new LinkedHashMap<>();
			int subfilecount = cdfile.readUnsignedByte();
			cdfile.seek(8);
			int index=0;
			for(int i=0;i<subfilecount;i++){
				int entrance = Util.hilo(cdfile.readInt())*SECTOR;
				int size = Util.hilo(cdfile.readInt());
				entrance_size.put(entrance, size);
				parts.add(new CDpart(cd+"/"+index++, entrance, entrance+size));
			}
			
			new File(dir+cd).mkdirs();
			index=0;
			for(Entry<Integer,Integer> e:entrance_size.entrySet()){
				int entrace = e.getKey();
				cdfile.seek(entrace);
				subcds.add(saveSubCd(cdfile, dir+cd+File.separator, index, e.getKey(), e.getValue()));
				index++;
			}
			cdfile.close();
			locations.put(cd, parts);
		}
		
		printIndexes(locations);
		
		
		for(File subcd:subcds){
			RandomAccessFile fis = new RandomAccessFile(subcd,"r");
			int header = fis.readInt();
			fis.close();
			
			if(header == PAC_MAGIC){
				unpac(subcd);
				subcd.delete();
			} else if(header == SQV_MAGIC){
				String index = subcd.getName().split("-")[0];
				subcd.renameTo(new File(subcd.getParent()+File.separator+index+".sqv"));
			}
		}
	}
	
	private File saveSubCd(RandomAccessFile cdfile, String dir, int index, int entrance, int size) throws IOException{
		File subfile = new File(dir+String.format("%03d-%08X", index, entrance));
		FileOutputStream fos = new FileOutputStream(subfile);
		byte[] buf = new byte[size];
		cdfile.read(buf);
		fos.write(buf);
		fos.close();
		return subfile;
	}
	
	private void unpac(File file) throws IOException{
		RandomAccessFile pacChainFile = new RandomAccessFile(file, "r");
		String indexInCd = file.getName().split("-")[0];
		File dir = new File(file.getParent()+File.separator+indexInCd);
		dir.mkdir();
		boolean endPac=false;
		int pacAddr = 0, index=0;
		while(!endPac){
			pacChainFile.seek(pacAddr);
			int header = pacChainFile.readInt();
			int pacType = pacChainFile.readUnsignedByte();
			endPac = pacChainFile.readUnsignedByte()==1;
			pacChainFile.skipBytes(6);
			int size = Util.hilo(pacChainFile.readInt());
			pacChainFile.skipBytes(SECTOR-0x10);
			byte[] buf = new byte[size-SECTOR];
			pacChainFile.read(buf);
			
			FileOutputStream fos = new FileOutputStream(dir.getAbsolutePath()+File.separator+index+"."+pacType);
			fos.write(buf);
			fos.close();
			
			if(size%SECTOR>0){
				size = size/SECTOR*SECTOR+SECTOR;
			}
			pacAddr += size;
			index++;
		}
		pacChainFile.close();
	}
	
	private void printIndexes(Map<String,List<CDpart>> fileIndexes){
		String json = new Gson().toJson(fileIndexes);
		System.out.println(json);
	}

}
