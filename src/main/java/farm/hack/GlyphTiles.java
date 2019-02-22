package farm.hack;

import java.awt.image.BufferedImage;
import java.util.List;

public class GlyphTiles{
	List<List<BufferedImage>> left,right;
	public GlyphTiles(List<List<BufferedImage>> left, List<List<BufferedImage>> right) {
		this.left = left;
		this.right = right;
	}
}