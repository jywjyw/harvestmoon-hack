package farm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import common.Conf;
import common.Util;

public class FontLibHacker {
	
	public static void main(String[] args) throws IOException {
		Map<String,FontData> fonts = new HashMap<>();
		new FontLibHacker().hack(new BinSplitPacker("C:\\Users\\me\\Desktop\\farm-jp\\"), fonts);
	}
	
	public void hack(BinSplitPacker bin, Map<String,FontData> fonts) throws IOException{
		rebuildCode(fonts);
		generateFontModel(fonts);
		
		ArrayList<FontData> fontsList = new ArrayList<>(fonts.values());
		//当字库数不够时,补空白
		int listSize = fontsList.size();
		for(int i=0;i<714-listSize;i++){
			FontData f = new FontData();
			f.imgdata = new byte[6*Conf.charH];
			Arrays.fill(f.imgdata, (byte)0);
			fontsList.add(f);
		}
		
		byte[] left = build(fontsList.subList(0, 20*21), 20, CHAR_PER_ROW, 0);
//		left = new byte[0x7673];
//		Arrays.fill(left, (byte)0xff);
		byte[] right = build(fontsList.subList(20*21, fontsList.size()), 14, CHAR_PER_ROW, 0);
		if(right.length!=0x52b0) throw new RuntimeException("右字库大小有误");
		
		File file0 = bin.getFile(0);
		RandomAccessFile file = new RandomAccessFile(file0,"rw");
		
		file.seek(0x7c);
		file.writeShort(Util.hiloShort(right.length));
		file.writeShort(4); //4or6 ok, 0 half
		file.skipBytes(8);
		file.write(right);
		
//		byte[] buf = new byte[0x52b0];
//		file.seek(0x88);
//		file.read(buf);
		
		file.seek(0x5338);
//		file.writeShort(Util.hiloShort(left.length));
		file.writeShort(0x7376);
		file.writeShort(4);//4:non-compress data,0:compress, 6palette
		file.skipBytes(8);
		file.write(left);
		file.close();
		
//		Patcher.patch(file0, Conf.FILE0_REAL_SIZE, left, 0x5344, 0x7673);
	}
	
	public void generateFontModel(Map<String,FontData> fonts){
		StringBuilder chars = new StringBuilder();
		for(Entry<String,FontData> e:fonts.entrySet()){
			if(e.getValue().imgdata==null)
				chars.append(e.getValue().char_);
		}
		char[] cs = chars.toString().toCharArray();
		try {
			List<byte[]> models = new FontModelGenerator().generate(cs);
			for(int i=0;i<cs.length;i++){
				fonts.get(String.valueOf(cs[i])).imgdata = models.get(i);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected static final int CHAR_PER_ROW = 21, CHAR_PER_COLUMN = 21;	//每个材质页中, 各行包含多个字, 各列包含多少字  
	protected static final int PIXEL_PER_BYTE = 2;	//每个字节能容纳多个像素点
	
	private byte[] build(List<FontData> fonts, int rowCount, int charsPerRow, int zeroPixels){
		int lineBytes = (Conf.charW*CHAR_PER_ROW+zeroPixels)/PIXEL_PER_BYTE,	//单个材质页中,每个扫描线占多少字节 
			rowBytes = lineBytes*Conf.charH;	//一个字列占多少字节
			
		//把所有字符排成21列,n行
		ByteBuffer[] rows = new ByteBuffer[rowCount];
		int rowIndex=0;
		for(int i=0;i<fonts.size();i+=CHAR_PER_ROW) {
			ByteBuffer buf = ByteBuffer.allocate(rowBytes);
			for(int j=0;j<Conf.charH*6;j+=6) {
				for(int k=0;k<CHAR_PER_COLUMN;k++) {
					buf.put(Arrays.copyOfRange(fonts.get(i+k).imgdata, j, j+6));
				}
				if(zeroPixels>0) 
					buf.put(new byte[zeroPixels/PIXEL_PER_BYTE]);
			}
			rows[rowIndex++]=buf;
		}
		
		
		ByteBuffer finalImg = ByteBuffer.allocate(rowBytes*rowCount);
		for(ByteBuffer b : rows){
			finalImg.put(b.array());
		}
		return finalImg.array();
	}
	
	//重建code
	void rebuildCode(Map<String,FontData> fonts){
		short code=0;
		for(Entry<String,FontData> e : fonts.entrySet()) {
			e.getValue().code=code;
			code++;
		}
		
		try {
			OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(new File(Conf.desktop+"新码表.tbl")), "gbk");
			for(Entry<String,FontData> e : fonts.entrySet()) {
				os.write(Util.toHexString(e.getValue().getLittleEndianCode())+"="+e.getKey()+"\n");
			}
			os.flush();
			os.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	

}
