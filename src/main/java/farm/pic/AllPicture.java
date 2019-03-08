package farm.pic;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import common.Conf;
import common.VramImg;
import farm.BinSplitPacker;
import farm.Picpack;
import farm.PicpackOper;

public class AllPicture  {
	
	public static void main(String[] args) throws IOException {
		new AllPicture().exportBoy(new File(Conf.desktop+"harvest/0"), Conf.desktop+"pic/");
	}
	
	public void exportBoy(File file0, String exportDir) throws IOException {
		Picpack p0=PicpackOper.loadD(file0, 0, 0xd000);
		Picpack d000 = PicpackOper.loadE(file0, 0xd000, 0xdd14, 0x2f800);
		
		new Continue().exportBoy(p0,d000, exportDir);
		new Logo().exportBoy(p0,d000, exportDir);
		new Marucome().exportBoy(p0,d000, exportDir);
		new Newgame().exportBoy(p0,d000, exportDir);
		new Start().exportBoy(p0,d000, exportDir);
		new Victor().exportBoy(p0,d000, exportDir);
		new Fontlib().exportBoy(p0,d000, exportDir);
	}
	
	public void exportGirl(File file0, String exportDir) throws IOException {
		Picpack p0=PicpackOper.loadD(file0, 0, 0xd000);
		Picpack d000 = PicpackOper.loadE(file0, 0xd000, 0xdd14, 0x30000);
		
		new Continue().exportGirl(p0,d000, exportDir);
		new Logo().exportGirl(p0,d000, exportDir);
		new Marucome().exportGirl(p0,d000, exportDir);
		new Newgame().exportGirl(p0,d000, exportDir);
		new Start().exportGirl(p0,d000, exportDir);
		new Victor().exportGirl(p0,d000, exportDir);
	}

	public void importBoy(BinSplitPacker bin, VramImg[] lr) throws IOException {
		File f0=bin.getFile(0);
		Picpack p0=PicpackOper.loadD(f0, 0, 0xd000);
		Picpack d000 = PicpackOper.loadE(f0, 0xd000, 0xdd14, 0x2f800);
		
		new Continue().importBoy(f0, d000);
//		new Logo().importBoy(f0, d000);
		new Marucome().importBoy(f0, d000);
		new Newgame().importBoy(f0, d000);
		new Start().importBoy(f0, d000);
//		new Victor().importBoy(f0, d000);
		
		reduceBoyD000(d000);
		
		int newD000Pos = 0xd800, newCapacity=0x2f800-newD000Pos;
		byte[] newpac = d000.adjustCapacity(newCapacity).rebuild();
		if(newpac.length>newCapacity) throw new UnsupportedOperationException("new pacD000 capacity must not be greater than "+newCapacity);
		
		RandomAccessFile f31 = new RandomAccessFile(bin.getFile(31), "rw");
		f31.seek(0x26f1);
		f31.write(newD000Pos>>>8);
		f31.seek(0x2735);
		f31.write(newD000Pos>>>8);
		f31.seek(0x2c9d);		//此处为pac0的size
		f31.write(newD000Pos>>>8);
		f31.close();
		RandomAccessFile f32 = new RandomAccessFile(bin.getFile(32), "rw");
		f32.seek(0xf6d);
		f32.write(newD000Pos>>>8);
		f32.seek(0xfb1);
		f32.write(newD000Pos>>>8);
		f32.close();
		
		VramImg left=lr[0];
		p0.modify(4, (short)left.w, (short)left.h, left.data);
		VramImg right=lr[1];
		p0.modify(3, (short)right.w, (short)right.h, right.data);
		
		RandomAccessFile f = new RandomAccessFile(f0, "rw");
		f.write(p0.adjustCapacity(newD000Pos).rebuild());
		f.seek(newD000Pos);
		f.write(newpac);
		f.close();
	}
	
	private void reduceBoyD000(Picpack d000) throws IOException{
		d000.modify(13,(short)4,(short)1,new byte[2]);
		//删除掉一些马后, 把它们的XYWH指向第12马,用于Sprite指令
		d000.modifyHeadE(0x8c, PicpackOper.buildXYWH((short)461, (short)297, (short)(52/4), (short)42));
	}
	

	public void importGirl(BinSplitPacker bin, VramImg[] lr) throws IOException {
		File f0=bin.getFile(0);
		Picpack p0=PicpackOper.loadD(f0, 0, 0xd000);
		Picpack d000 = PicpackOper.loadE(f0, 0xd000, 0xdd14, 0x30000);
		
		new Continue().importGirl(f0, d000);
//		new Logo().importGirl(f0, d000);
		new Marucome().importGirl(f0, d000);
		new Newgame().importGirl(f0, d000);
		new Start().importGirl(f0, d000);
//		new Victor().importGirl(f0, d000);
		
		reduceGirlD000(d000);
		
		int newD000Pos = 0xd800, newCapacity=0x30000-newD000Pos;
		byte[] newpac = d000.adjustCapacity(newCapacity).rebuild();
		if(newpac.length>newCapacity) throw new UnsupportedOperationException("new pacD000 capacity must not be greater than "+newCapacity);
		
		RandomAccessFile f30 = new RandomAccessFile(bin.getFile(30), "rw");
		f30.seek(0x2595);
		f30.write(newD000Pos>>>8);
		f30.seek(0x25d5);
		f30.write(newD000Pos>>>8);
		f30.seek(0x2b05);		//此处为pac0的size
		f30.write(newD000Pos>>>8);
		f30.close();
		RandomAccessFile f31 = new RandomAccessFile(bin.getFile(31), "rw");
		f31.seek(0xb41);
		f31.write(newD000Pos>>>8);
		f31.seek(0xb81);
		f31.write(newD000Pos>>>8);
		f31.close();
		
		VramImg left=lr[0];
		p0.modify(4, (short)left.w, (short)left.h, left.data);
		VramImg right=lr[1];
		p0.modify(3, (short)right.w, (short)right.h, right.data);
		
		RandomAccessFile f = new RandomAccessFile(f0, "rw");
		f.write(p0.adjustCapacity(newD000Pos).rebuild());	//enlarge pac0
		f.seek(newD000Pos);
		f.write(newpac);
		f.close();
	}
	
	private void reduceGirlD000(Picpack d000) throws IOException{
		for(int ind:new int[]{12,13})
			d000.modify(ind,(short)4,(short)1,new byte[2]);
		for(int offset:new int[]{0x84,0x8c})
			d000.modifyHeadE(offset, PicpackOper.buildXYWH((short)499, (short)256, (short)(48/4), (short)42));
	}

}
