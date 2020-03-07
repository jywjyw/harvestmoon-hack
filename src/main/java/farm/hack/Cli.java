package farm.hack;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Scanner;

public class Cli {

	public static void main(String[] args) throws IOException {
		System.out.println("/////////////////////////////");
		System.out.println("使用前准备：");
		System.out.println("1、把日版镜像文件命名为harvestmoon.iso放到本目录下,注意备份");
		System.out.println("2、把Excel译文放到本目录下");
		System.out.println("/////////////////////////////");
		System.out.println();
		
		String dir=System.getProperty("user.dir")+File.separator;
		File[] excels=new File(dir).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".xlsx");
			}
		});
		if(excels==null || excels.length==0){
			System.err.println("没有找到Excel文件,请把Excel放到本目录并重新运行");
			return;
		}
		for(int i=0;i<excels.length;i++){
			System.out.printf("[%d] : %s\n", i+1, excels[i].getName());
		}
		System.out.println("请输入要导入excel的序号: ");
		Scanner scan = new Scanner(System.in);
		int selIndex=Integer.parseInt(scan.nextLine())-1;
		scan.close();
		String splitDir=System.getProperty("java.io.tmpdir")+"harvestmoongirlsplit"+File.separator;
//		Hack.hackBoy(excels[selIndex], dir+"bin"+File.separator, splitDir, dir+"harvestmoon.iso");
		Hack.hackGirl(excels[selIndex], dir+"bin"+File.separator, splitDir, dir+"harvestmoon.iso");
	}

}
