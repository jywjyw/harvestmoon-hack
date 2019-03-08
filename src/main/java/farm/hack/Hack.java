package farm.hack;

import java.io.File;
import java.io.IOException;

import common.Conf;
import common.IsoPatcher;
import common.Util;
import common.VramImg;
import farm.BinSplitPacker;
import farm.pic.AllPicture;

public class Hack {
	
	public static void main(String[] args) throws IOException {
		hackBoy(Conf.desktop+"boy-jp.xlsx", Conf.outdir+"boy-hack.iso");
//		hackGirl(Conf.desktop+"girl-jp.xlsx", Conf.outdir+"girl-hack.iso");
	}
	
	public static void hackBoy(String excel, String iso) throws IOException {
		try {
			String exe=Conf.outdir+Conf.BOY_JP_EXE;
			Util.copyFile(Conf.boyjpdir+Conf.BOY_JP_EXE, exe);
			FontAsm.hackBoy(exe);
			
			BinSplitPacker bin = new BinSplitPacker();
			String splitDir=Conf.desktop+"harvest\\";
			bin.split(Conf.boyjpdir, splitDir);
			
			Encoding enc=new Encoding();
			new ScriptImporter(enc).importBoy(bin,new File(excel));;
			
			VramImg[] lr = new FontLibRebuilder().rebuild(enc);
			enc.saveAsTbl(Conf.outdir+"harvest-boy.tbl");
			new AllPicture().importBoy(bin, lr);
			
			bin.pack(splitDir, Conf.outdir);
			IsoPatcher.patch("boy/isopatcher.properties", Conf.outdir, iso);
			System.out.println("finished.");
		} finally {
			new File(Conf.outdir+Conf.BIN).delete();
			new File(Conf.outdir+Conf.HDT).delete();
			new File(Conf.outdir+Conf.BOY_JP_EXE).delete();
		}
	}
	
	public static void hackGirl(String excel, String iso) throws IOException {
		try {
			String exe=Conf.outdir+Conf.GIRL_JP_EXE;
			Util.copyFile(Conf.girljpdir+Conf.GIRL_JP_EXE, exe);
			FontAsm.hackGirl(exe);
			
			BinSplitPacker bin = new BinSplitPacker();
			String splitDir=Conf.desktop+"harvest\\";
			bin.split(Conf.girljpdir, splitDir);
			
			Encoding enc=new Encoding();
			new ScriptImporter(enc).importGirl(bin,new File(excel));;
			
			VramImg[] lr = new FontLibRebuilder().rebuild(enc);
			enc.saveAsTbl(Conf.outdir+"harvest-girl.tbl");
			new AllPicture().importGirl(bin, lr);
			
			bin.pack(splitDir, Conf.outdir);
			IsoPatcher.patch("girl/isopatcher.properties", Conf.outdir, Conf.outdir+"girl_hack.iso");
			System.out.println("finished.");
		} finally {
			new File(Conf.outdir+Conf.BIN).delete();
			new File(Conf.outdir+Conf.HDT).delete();
			new File(Conf.outdir+Conf.GIRL_JP_EXE).delete();
		}
	}

}
