package fencer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.Util;

public class DirHexSearch {
	
	public static void main(String[] args) throws IOException {
		String dir = "D:\\ps3\\hanhua\\musashi-JP\\java\\";
		new DirHexSearch().search(dir, "38 00 00 00 0C 80 03 3C 44 8B 63 94 E8 FF BD 27");
		System.out.println("finish...");
	}
	
	List<File> files = new ArrayList<>();
	
	private byte[] toBytes(String search){
		String[] arr = search.trim().split(" ");
		byte[] q = new byte[arr.length];
		for(int i=0;i<arr.length;i++){
			q[i]= Integer.valueOf(arr[i],16).byteValue();
		}
		return q;
	}
	
	public void search(String dir, String search) throws IOException {
		byte[] q = toBytes(search);
		File target = new File(dir);
		iteratorDir(target);
		System.out.println(files.size());
		byte[] buf = new byte[q.length];
		for(int i=0;i<files.size();i++) {
			File f = files.get(i);
			System.out.println(i+" -> "+f);
			RandomAccessFile file = null;
			try {
				file = new RandomAccessFile(files.get(i), "r");
				for(long j=0;j<f.length();j++){
					file.seek(j);
					file.read(buf);
					if(equals(q, buf)){
						System.err.printf("%s : found!! 0x%08X\n", f, j);
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				Util.close(file);
			}
		}
		
		
	}

	public void iteratorDir(File file) {
		file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(pathname.isDirectory()) {
					iteratorDir(pathname);
				} else {
					files.add(pathname);
				}
				return true;
			}
		});
	}
	
	private boolean equals(byte[] a1, byte[] a2) {
//        if (a1==null || a2==null)
//            return false;
        return Arrays.equals(a1, a2);

//        if (a2.length != a1.length)
//            return false;
//
//        for (int i=0; i<a1.length; i++)
//            if (a1[i] != a2[i])
//                return false;
//
//        return true;
    }

}
