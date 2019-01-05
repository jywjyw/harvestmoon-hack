package farm;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import common.Conf;
import common.IsoPatcher;
import farm.hack.FontLibHacker;

public class Hack {
	
	public static void main(String[] args) throws IOException {
		BinSplitPacker bin = new BinSplitPacker();
		String splitDir=Conf.desktop+"boyjp";
		bin.split(Conf.boyjpdir+Conf.BIN, splitDir);
		
		Map<String,FontData> fonts = new LinkedHashMap<>();
		String chars = "abcdefghijklmn";
		for(char c: chars.toCharArray()){
			fonts.put(c+"", new FontData(c+"", 100));
		}
		new FontLibHacker().hack(bin, fonts);
		
		bin.pack(splitDir, Conf.boyoutdir+Conf.BIN);
		IsoPatcher.patch(Conf.boyoutdir, Conf.boyoutdir+"harvest_boy_jp.iso");
	}

}
