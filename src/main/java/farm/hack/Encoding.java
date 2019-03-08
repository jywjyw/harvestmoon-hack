package farm.hack;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import common.Conf;
import common.RscLoader;
import common.RscLoader.Callback;

public class Encoding {
	private static final int L_LIMIT=0x1B8, R_LIMIT=0x02DE;
	private int charCapacity=2940;	//0~02DE的字符容量
	private Map<String,Integer> 
		char_code=new HashMap<>(),
		char_count=new HashMap<>();
	
	private LinkedList<Integer> codePool=new LinkedList<>();
	
	public Encoding(){
		int c;
		for(int i=0;i<=R_LIMIT;i++){
			c=i;
			codePool.add(c);
			c+=0x300;
			codePool.add(c);
			c+=0x300;
			codePool.add(c);
			c+=0x300;
			codePool.add(c);
		}
		RscLoader.load("init_enc.gbk", "gbk", new Callback() {
			@Override
			public void doInline(String line) {
				String[] arr=line.split("=");
				String ch=arr[1];
				int code=Integer.parseInt(arr[0],16);
				if(codePool.contains(code)){
					codePool.remove(new Integer(code));
					char_code.put(ch, code);
					char_count.put(ch,99999);
				}else{
					char_code.put(ch, code);
					charCapacity++;
				}
			}
		});
	}
	
	public Integer getCode(String char_){
		return char_code.get(char_);
	}
	
	public int put(String char_){
		Integer code=codePool.poll();
		if(code==null) throw new UnsupportedOperationException("too many char, only support char count:"+charCapacity);
		char_code.put(char_,code);
		return code;
	}
	
	public int size(){
		return char_code.size();
	}
	
	private Map<Integer,Char> buildSortedKv(){
		List<Kv> kvs=new ArrayList<>();
		for(Entry<String,Integer> e:char_code.entrySet()){
			kvs.add(new Kv(e.getKey(),e.getValue()));
		}
		Collections.sort(kvs, new Comparator<Kv>() {
			@Override
			public int compare(Kv o1, Kv o2) {
				return o1.code-o2.code;
			}
		});
		Map<Integer,Char> ret=new LinkedHashMap<>();
		for(Kv k:kvs){
			ret.put(k.code, new Char(k.char_));
		}
		return ret;
	}
	
	public Map<Integer,Char> fillGlyph() throws IOException{
		fillGBK();	//fill to max
		Map<Integer,Char> map = buildSortedKv();
		GlyphDrawer glyphDrawer = new GlyphDrawer();
		for(Entry<Integer,Char> e:map.entrySet()){
			if(e.getKey()<=0x0c00){
				e.getValue().glyph = glyphDrawer.generateGlyph(e.getValue().ch);
			}
		}
		return map;
	}
	
	public GlyphTiles convertToLeftRightGlyphs(Map<Integer,Char> chars){
		List<List<BufferedImage>> left = new ArrayList<>(4),right = new ArrayList<>(4);
		for(int i=0;i<4;i++){
			left.add(new ArrayList<BufferedImage>());
			right.add(new ArrayList<BufferedImage>());
		}
		
		int[] bases = new int[]{0x0,0x0300,0x0600,0x0900};
		for(int j=0;j<bases.length;j++) {
			for(int i=0;i<=L_LIMIT;i++){
				Char ch=chars.get(bases[j]+i);
				if(ch!=null){
					if(ch.glyph==null) throw new UnsupportedOperationException("glyph required: "+ch.ch);
					left.get(j).add(ch.glyph);
				}
			}
		}
		
		
		for(int j=0;j<bases.length;j++) {
			for(int i=L_LIMIT+1;i<=R_LIMIT;i++){
				Char ch=chars.get(bases[j]+i);
				if(ch!=null){
					if(ch.glyph==null) throw new UnsupportedOperationException("glyph required: "+ch.ch);
					right.get(j).add(ch.glyph);
				}else{
					System.out.println();
				}
			}
		}
		return new GlyphTiles(left,right);
	}
	
	private void fillGBK(){
		for(int a=0xb0;a<=0xf7;a++){
			for(int b=0xa1;b<=0xfe;b++){
				if(size()>=charCapacity) break;
				try {
					String zh = new String(new byte[]{(byte)a,(byte)b},"gbk");
					if(getCode(zh)==null)	put(zh);
				} catch (UnsupportedEncodingException e) {}
			}
		}
	}
	
	public void saveAsTbl(String outFile){
		Map<Integer,Char> sorted = buildSortedKv();
		try {
			OutputStreamWriter fos = new OutputStreamWriter(new FileOutputStream(outFile),"gbk");
			for(Entry<Integer,Char> e:sorted.entrySet()){
				fos.write(String.format("%04X=%s\n", e.getKey(),e.getValue().ch));
			}
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class Kv{
		public String char_;
		public int code;
		public Kv(String char_, int code) {
			this.char_ = char_;
			this.code = code;
		}
	}

}
