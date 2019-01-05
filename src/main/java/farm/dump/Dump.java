package farm.dump;

import java.io.FileOutputStream;
import java.io.IOException;

import common.Conf;
import farm.BinSplitPacker;
import farm.Charset;

public class Dump {
	
	public static void main(String[] args) throws IOException {
		BinSplitPacker bin = new BinSplitPacker();
		bin.split(Conf.boyjpdir, Conf.desktop+"boyjp\\");
		byte[] bs = new ScriptMultilineExporter().exportExcel(bin, new Charset("jp.tbl"));
		FileOutputStream fos=new FileOutputStream(Conf.desktop+"boy-jp.xlsx");
		fos.write(bs);
		fos.close();
	}
}
