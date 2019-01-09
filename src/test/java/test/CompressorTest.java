package test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import common.Conf;
import common.Util;
import farm.dump.Uncompressor;
import farm.hack.Compressor;

public class CompressorTest {
	
	@Test
	public void test1() throws IOException{
		File uncomp=new File(Thread.currentThread().getContextClassLoader().getResource("uncomp").getPath());
		
		FileInputStream uncompIs=new FileInputStream(uncomp);
		byte[] comp=Compressor.compress(uncompIs);
		uncompIs.close();
		System.out.println("compressed:"+comp.length);
		
		File reuncomp=new File(Conf.desktop+"reuncomp");
		FileOutputStream reuncompOs=new FileOutputStream(reuncomp);
		reuncompOs.write(Uncompressor.uncomp(new ByteArrayInputStream(comp)));
		reuncompOs.close();
		
		Assert.assertEquals(Util.md5(reuncomp), Util.md5(uncomp));
	}
	
}
