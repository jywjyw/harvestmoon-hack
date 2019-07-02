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
		String excel="C:\\Users\\Administrator\\Documents\\Tencent Files\\329682470\\FileRecv\\boy-jp3.xlsx";
		hackBoy(new File(excel), Conf.boyjpdir, Conf.desktop+"harvest\\", Conf.boyiso);
//		hackGirl(Conf.desktop+"girl-jp.xlsx", Conf.outdir+"girl-hack.iso");
	}
	
	public static void hackBoy(File excel, String jpdir, String splitdir, String iso) throws IOException {
		String outdir=new File(iso).getParent()+File.separator;
		try {
			String exe=outdir+Conf.BOY_JP_EXE;
			Util.copyFile(jpdir+Conf.BOY_JP_EXE, exe);
			FontAsm.hackBoy(exe);
			
			BinSplitPacker bin = new BinSplitPacker();
			bin.split(jpdir, splitdir);
			
			System.out.println("importing from "+excel);
			Encoding enc=new Encoding();
			new FloatTextImporter(enc).import_(excel,new File(exe), "EXE");
			for(int i:new int[]{11,21,23,25,27,29}){
				new FloatTextImporter(enc).import_(excel, bin.getFile(i), i+"");
			}
			new ScriptImporter(enc).importBoy(bin,excel);;
			
			VramImg[] lr = new FontLibRebuilder().rebuild(enc);
			enc.saveAsTbl(outdir+"harvest-boy.tbl");
			new AllPicture().importBoy(bin, lr);
			
			bin.pack(splitdir, outdir);
			IsoPatcher.patch("boy/isopatcher.properties", outdir, iso);
			System.out.println(iso+" has been patched. ");
		} finally {
			new File(outdir+Conf.BIN).delete();
			new File(outdir+Conf.HDT).delete();
			new File(outdir+Conf.BOY_JP_EXE).delete();
		}
	}
	
	public static void hackGirl(File excel, String iso) throws IOException {
		try {
			String exe=Conf.boyiso+Conf.GIRL_JP_EXE;
			Util.copyFile(Conf.girljpdir+Conf.GIRL_JP_EXE, exe);
			FontAsm.hackGirl(exe);
			
			BinSplitPacker bin = new BinSplitPacker();
			String splitDir=Conf.desktop+"harvest\\";
			bin.split(Conf.girljpdir, splitDir);
			
			Encoding enc=new Encoding();
			new FloatTextImporter(enc).import_(excel,new File(exe),"EXE");
			for(int i:new int[]{17,20,22,24,26}){
				new FloatTextImporter(enc).import_(excel, bin.getFile(i), i+"");
			}
			new ScriptImporter(enc).importGirl(bin,excel);;
			
			VramImg[] lr = new FontLibRebuilder().rebuild(enc);
			enc.saveAsTbl(Conf.boyiso+"harvest-girl.tbl");
			new AllPicture().importGirl(bin, lr);
			
			bin.pack(splitDir, Conf.boyiso);
			IsoPatcher.patch("girl/isopatcher.properties", Conf.boyiso, Conf.boyiso+"girl_hack.iso");
			System.out.println("ISO ["+iso+"]has patched successfully. ");
		} finally {
			new File(Conf.boyiso+Conf.BIN).delete();
			new File(Conf.boyiso+Conf.HDT).delete();
			new File(Conf.boyiso+Conf.GIRL_JP_EXE).delete();
		}
	}

}
