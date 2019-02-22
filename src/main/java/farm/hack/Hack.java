package farm.hack;

import java.io.File;
import java.io.IOException;

import common.Conf;
import common.IsoPatcher;
import common.Util;
import farm.BinSplitPacker;
import farm.tool.FontAsm;

public class Hack {
	
	public static void main(String[] args) throws IOException {
		String exe=Conf.outdir+Conf.BOY_JP_EXE;
		Util.copyFile(Conf.boyjpdir+Conf.BOY_JP_EXE, exe);
		FontAsm.hack(exe);
		
		BinSplitPacker bin = new BinSplitPacker();
		String splitDir=Conf.desktop+"boyjp\\";
		bin.split(Conf.boyjpdir, splitDir);
		
		Encoding enc=new Encoding();
		File excel=new File(Conf.desktop+"boy-jp.xlsx");
		new ScriptImporter(enc).import_(bin,excel);;
		
		new FontLibRebuilder().rebuild(bin.getFile(0), enc);	//修改右字库并压缩后,并未超过原容量
		enc.saveAsTbl(Conf.desktop+"harvest-boy.tbl");
		
		bin.pack(splitDir, Conf.outdir);
		IsoPatcher.patch(Conf.outdir, Conf.outdir+"boy_hack.iso");
		System.out.println("finished.");
	}

}
