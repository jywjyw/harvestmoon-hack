package farm.hack;

import java.io.IOException;

import common.Conf;
import common.IsoPatcher;
import common.Util;
import farm.BinSplitPacker;

public class Hack {
	
	public static void main(String[] args) throws IOException {
		String exe=Conf.boyoutdir+Conf.EXE_BOY;
		Util.copyFile(Conf.boyjpdir+Conf.EXE_BOY, exe);
		BinSplitPacker bin = new BinSplitPacker();
		String splitDir=Conf.desktop+"boyjp\\";
		bin.split(Conf.boyjpdir, splitDir);
		
		Enc enc=new Enc();
		enc.fillGBK();
		new FontLibRebuilder().rebuild(bin.getFile(0), enc);
		
		bin.pack(splitDir, Conf.boyoutdir);
		IsoPatcher.patch(Conf.boyoutdir, Conf.boyoutdir+"harvest_boy_jp.iso");
		System.out.println("finished.");
	}

}
