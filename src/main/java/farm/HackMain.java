package farm;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import common.Conf;
import common.Util;

public class HackMain {
	
	public static void main(String[] args) throws IOException {
		BinSplitPacker bin = BinSplitPacker.newInstance();
		bin.split(Conf.bin);
		
		Map<String,FontData> fonts = new LinkedHashMap<>();//TODO
		String chars = Util.readTxt("testfont.txt");
		for(char c: chars.toCharArray()){
			fonts.put(c+"", new FontData(c+"", 100));
		}
		new FontLibHacker().hack(bin, fonts);
		
		bin.pack(Conf.hackbin);
		bin.dispose();
		System.out.println("hack complete. use isopatcher next.");
	}

}
