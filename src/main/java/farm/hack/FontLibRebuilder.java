package farm.hack;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.List;

import common.Conf;
import common.Palette;
import common.VramImg;
import farm.Pic;
import farm.Picpack;

public class FontLibRebuilder {
	
	public void rebuild(File gf, Enc enc) throws IOException{
		RandomAccessFile file=new RandomAccessFile(gf, "rw");
		int pac0Entrance=0, pac0DataLen=0xCAE0, pac0Capacity=0xD000;
		file.seek(pac0Entrance);
		byte[] pac0Bytes=new byte[pac0DataLen];
		file.read(pac0Bytes);
		Picpack pac0=Picpack.load(pac0Bytes, pac0Capacity);
		String font=Conf.desktop+"Zpix.ttf";
		
		VramImg left=FourLayerFontGen.build4LayerFont(enc.getLeftChars(), 64*4, font, 12);
		byte[] pals=toBytes(FourLayerFontGen.build4Palette((short)0x3e0));	//write to (896,0), overwrite 1st line of left font
		System.arraycopy(pals, 0, left.data, 0, pals.length);
		Pic leftPic=pac0.pics.get(4);
		pac0.modify(4, (short)64, leftPic.h, left.data);
		
		FourLayerChars rightChars=enc.getRightChars();
		if(rightChars!=null){
			VramImg right=FourLayerFontGen.build4LayerFont(rightChars, 63*4, font, 12);
			Pic rightPic=pac0.pics.get(3);
			pac0.modify(3, rightPic.w, rightPic.h, right.data);
		}
		
		file.seek(pac0Entrance);
		file.write(pac0.rebuild());
		file.close();
	}
	
	private byte[] toBytes(List<Palette> pals){
		ByteBuffer buf=ByteBuffer.allocate(Palette.PAL16_CAPACITY*pals.size());
		for(Palette p:pals){
			buf.put(p.raw);
		}
		return buf.array();
	}
	
}
