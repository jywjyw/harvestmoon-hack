package farm.tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import common.Conf;
import common.Util;
import common.instruction.MipsCompiler;

public class FontAsm {
	
	public static void main(String[] args) {
	}
	
	public static void hack(String exe) throws IOException{
		MipsCompiler c = new MipsCompiler();
		InputStream asm = Thread.currentThread().getContextClassLoader().getResourceAsStream("font.asm");
		byte[] bs = c.compileFile(asm);
		byte[] ret=new byte[140*4];
		System.arraycopy(bs, 0, ret, 0, bs.length);
		System.out.println(Util.hexEncode(ret));
		
		RandomAccessFile f=new RandomAccessFile(exe, "rw");
		f.seek(Conf.getExeOffset(0x8001a34c));
		f.write(ret);
		f.close();
	}

}
