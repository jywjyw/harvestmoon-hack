package farm.hack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import common.Conf;
import common.RscLoader;

public class GlyphDrawer {
	
	public static void main(String[] args) throws IOException {
//		String font=Conf.desktop+"SourceHanSans-Normal.ttf";
		String font=Conf.desktop+"方正像素12.ttf";
		GlyphDrawer g=new GlyphDrawer(font,0,10);
		BufferedImage img=g.generateGlyph("我");
		ImageIO.write(img, "bmp", new File(Conf.desktop+"glyph.bmp"));
	}
	
	public GlyphDrawer(){
//		this(Conf.getRawFile("Zpix.ttf"), -1,9);
		this(Conf.getRawFile("方正像素12.ttf"),0,10);
//		this(Conf.desktop+"华康POP1体W5.ttf",0,10);
	}
	
	Font font;
	int baseX,baseY;
	Map<String,BufferedImage> specGlyph = new HashMap<>();
	
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
		
		try {
			final BufferedImage glyphs = ImageIO.read(new File(Conf.getRawFile("glyph/spec_glyph.bmp")));
			int[] pixel=new int[3];
			for(int y=0;y<glyphs.getHeight();y++){
				for(int x=0;x<glyphs.getWidth();x++){
					glyphs.getRaster().getPixel(x, y, pixel);
					if(pixel[0]!=0 && pixel[0]!=255){
						throw new UnsupportedOperationException("spec_glyph.bmp contains illegal pixel");
					}
				}
			}
			RscLoader.load(new File(Conf.getRawFile("glyph/spec_glyph.conf")), "gbk", new RscLoader.Callback() {
				@Override
				public void doInline(String line) {
					String[] arr=line.split("="), xy=arr[1].split(",");
					int x=Integer.parseInt(xy[0])*12, y=Integer.parseInt(xy[1])*12;
					specGlyph.put(arr[0], glyphs.getSubimage(x, y, 12, 12));
				}
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public BufferedImage generateGlyph(String ch){
		if(specGlyph.containsKey(ch)){
			return specGlyph.get(ch);
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
