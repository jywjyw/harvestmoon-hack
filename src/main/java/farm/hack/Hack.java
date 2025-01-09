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
		String excel="C:\\Users\\lenovo\\Desktop\\girl-jp(1).xlsx";
//		String excel="D:\\workspace1\\harvestmoon-hack\\translation\\girl-jp.xlsx";
//		hackBoy(new File(excel), Conf.boyjpdir, Conf.desktop+"harvest\\", Conf.boyiso);
		hackGirl(new File(excel), Conf.girljpdir, Conf.desktop+"harvest\\", Conf.girliso);
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
	
	public static void hackGirl(File excel, String jpdir, String splitdir, String iso) throws IOException {
		String outdir=new File(iso).getParent()+File.separator;
		try {
			String exe=outdir+Conf.GIRL_JP_EXE;
			Util.copyFile(jpdir+Conf.GIRL_JP_EXE, exe);
			FontAsm.hackGirl(exe);
			
			BinSplitPacker bin = new BinSplitPacker();
			bin.split(jpdir, splitdir);
			
			Encoding enc=new Encoding();
			new FloatTextImporter(enc).import_(excel,new File(exe),"EXE");
			for(int i:new int[]{11,17,20,22,24,26}){
				new FloatTextImporter(enc).import_(excel, bin.getFile(i), i+"");
			}
			new ScriptImporter(enc).importGirl(bin,excel);;
			
			VramImg[] lr = new FontLibRebuilder().rebuild(enc);
			enc.saveAsTbl(outdir+"harvest-girl.tbl");
			new AllPicture().importGirl(bin, lr);
			
			bin.pack(splitdir, outdir);
			IsoPatcher.patch("girl/isopatcher.properties", outdir, iso);
			System.out.println("ISO ["+iso+"]has patched successfully. ");
		} finally {
			new File(outdir+Conf.BIN).delete();
			new File(outdir+Conf.HDT).delete();
			new File(outdir+Conf.GIRL_JP_EXE).delete();
		}
	}

}
