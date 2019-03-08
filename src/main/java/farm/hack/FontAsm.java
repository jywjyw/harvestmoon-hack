package farm.hack;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import common.Conf;
import common.Util;
import common.instruction.MipsCompiler;

public class FontAsm {
	
	public static void hackBoy(String exe) throws IOException{
		MipsCompiler c = new MipsCompiler();
		InputStream asm = Thread.currentThread().getContextClassLoader().getResourceAsStream("boy/font.asm");
		byte[] bs = c.compileFile(asm);
		byte[] ret=new byte[140*4];
		System.arraycopy(bs, 0, ret, 0, bs.length);
		
		RandomAccessFile f=new RandomAccessFile(exe, "rw");
		f.seek(Conf.getExeOffset(0x8001a34c));
		f.write(ret);
		f.close();
	}
	
	public static void hackGirl(String exe) throws IOException{
		MipsCompiler c = new MipsCompiler();
		InputStream asm = Thread.currentThread().getContextClassLoader().getResourceAsStream("girl/font.asm");
		byte[] bs = c.compileFile(asm);
		byte[] ret=new byte[131*4];
		System.arraycopy(bs, 0, ret, 0, bs.length);
		
		RandomAccessFile f=new RandomAccessFile(exe, "rw");
		f.seek(Conf.getExeOffset(0x80019724));
		f.write(ret);
		f.close();
	}

}
