package farm.hack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import common.Conf;

public class GlyphDrawer {
	
	public static void main(String[] args) throws IOException {
		GlyphDrawer g=new GlyphDrawer(Conf.desktop+"方正像素12.ttf",0,10);
		BufferedImage img=g.generateGlyph("我");
		ImageIO.write(img, "bmp", new File(Conf.desktop+"glyph.bmp"));
	}
	
	public GlyphDrawer(){
		this(Conf.desktop+"Zpix.ttf", -1,9);
//		this(Conf.desktop+"方正像素12.ttf",0,10);
	}
	
	Font font;
	int baseX,baseY;
	List<String> specGlyph = new ArrayList<>();
	
	public GlyphDrawer(String fontFile, int baseX, int baseY){
		try {
			InputStream is = new FileInputStream(fontFile); 
			this.font = Font.createFont(Font.TRUETYPE_FONT, is);
			this.baseX=baseX;
			this.baseY=baseY;
			is.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
//		specGlyph.add("0");
//		specGlyph.add("1");
//		specGlyph.add("2");
//		specGlyph.add("3");
//		specGlyph.add("4");
//		specGlyph.add("5");
//		specGlyph.add("6");
//		specGlyph.add("7");
//		specGlyph.add("8");
//		specGlyph.add("9");
		specGlyph.add("○");
		specGlyph.add("※");
		specGlyph.add("△");
		specGlyph.add("□");
		specGlyph.add("/");
		specGlyph.add("[up]");
		specGlyph.add("[heart]");
		specGlyph.add("[unheart]");
		specGlyph.add("～");
		specGlyph.add("_");
		specGlyph.add("[repeat]");
		specGlyph.add("[chicken]");
		specGlyph.add("[dog]");
		specGlyph.add("[horse]");
		specGlyph.add("[cow]");
		specGlyph.add("[fish]");
		specGlyph.add("&");
		specGlyph.add("[star]");
		specGlyph.add("[unstar]");
		specGlyph.add("[LD]");
		specGlyph.add("[LU]");
		specGlyph.add("[RU]");
		specGlyph.add("[RD]");
		specGlyph.add("[king]");
		specGlyph.add("[one]");
		specGlyph.add("[two]");
		specGlyph.add("[three]");
		specGlyph.add("[four]");
		specGlyph.add("[five]");
		specGlyph.add("[six]");
	}
	
	public BufferedImage generateGlyph(String ch){
		if(specGlyph.contains(ch)){
			//TODO load customized glyph
			return null;
		} else {
			char[] chars=ch.toCharArray();
			if(chars.length>1) throw new UnsupportedOperationException("字库无法识别:" + ch);
			return draw(chars[0]);
		}
	}
	
	private BufferedImage draw(char c){
		if(!font.canDisplay(c)) throw new UnsupportedOperationException("字库无法识别:" + c);
		int fontsize=12;
		Font myfont=font.deriveFont(Font.PLAIN, fontsize);
		
		BufferedImage img = new BufferedImage(fontsize, fontsize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setColor(Color.WHITE);	
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		
		g.setColor(Color.BLACK);
		g.setFont(myfont);
//		System.out.println(g.getFontMetrics(myfont).getAscent());
//		System.out.println(g.getFontMetrics(myfont).getDescent());
		g.drawString(c+"", baseX, baseY);	//向上修正N个像素,因为书写起始位置向上偏了点
		g.dispose();
		return img;
	}
	

}
