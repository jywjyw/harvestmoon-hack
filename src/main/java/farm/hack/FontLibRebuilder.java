package farm.hack;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.List;

import javax.imageio.ImageIO;

import common.Conf;
import common.Img4bitUtil;
import common.Palette;
import common.VramImg;
import farm.Pic;
import farm.Picpack;

public class FontLibRebuilder {
	
	public void rebuild(File gf, Encoding enc) throws IOException{
		RandomAccessFile file=new RandomAccessFile(gf, "rw");
		int pac0Entrance=0, pac0DataLen=0xCAE0, pac0Capacity=0xD000;
		file.seek(pac0Entrance);
		byte[] pac0Bytes=new byte[pac0DataLen];
		file.read(pac0Bytes);
		Picpack pac0=Picpack.load(pac0Bytes, pac0Capacity);
		
		GlyphTiles lr = enc.convertToLeftRightGlyphs(enc.fillGlyph());
		pac0.modify(4, (short)64, (short)253, buildLeftFont(lr));	//左字库图原宽高为:63*252,高度+1,留给调色板
		
		Pic rightPic=pac0.pics.get(3);
		VramImg right=FourLayerGen.gen(lr.right, 252, rightPic.h);
		pac0.modify(3, rightPic.w, rightPic.h, right.data);
		
		file.seek(pac0Entrance);
		file.write(pac0.rebuild());
		file.close();
	}
	
	private byte[] buildLeftFont(GlyphTiles lr) throws IOException{
		VramImg left = FourLayerGen.gen(lr.left, 256, 252);
		byte[] pals=toBytes(FourLayerGen.build4Palette((short)0x2104));	//2104:黑, 03e0:蓝
		ByteBuffer newleft=ByteBuffer.allocate(left.data.length+Palette.PAL16_CAPACITY*4);
		newleft.put(left.data);
		newleft.put(pals);
		return newleft.array();
	}
	
	private byte[] toBytes(List<Palette> pals){
		ByteBuffer buf=ByteBuffer.allocate(Palette.PAL16_CAPACITY*pals.size());
		for(Palette p:pals){
			buf.put(p.raw);
		}
		return buf.array();
	}
	
}
