package farm.hack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import common.Palette;
import common.VramImg;

public class FontLibRebuilder {
	
	public VramImg[] rebuild(Encoding enc) throws IOException {
		GlyphTiles lr = enc.convertToLeftRightGlyphs(enc.fillGlyph());
		//左字库图原宽高为:63*252,高度+1,留给调色板
		VramImg left=new VramImg(64,253,buildLeftFont(lr));
		VramImg right=FourLayerFontGen.gen(lr.right, 252, 168);	//右字库的压缩率高于1,所以不压缩
		return new VramImg[]{left,right};
	}
	
	private byte[] buildLeftFont(GlyphTiles lr) throws IOException{
		VramImg left = FourLayerFontGen.gen(lr.left, 256, 252);
		byte[] pals=toBytes(FourLayerFontGen.build4Palette((short)0x2104));	//2104:黑, 03e0:蓝 for test
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
