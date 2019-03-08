package common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HexSearcher {
	
	public static void main(String[] args) throws IOException {
		String dir = Conf.desktop+"harvest\\";
		System.out.println("searching...");
		HexSearcher.searchDir(dir, "00d00634d855000c", null);
//		HexSearcher.searchDir(dir, "00d005340200063c", null);
//		HexSearcher.searchDir(dir, "00d005340200063c0030c634", null);
//		HexSearcher.searchDir(dir, "00d006341000a3af", null);
		
		System.out.println("finish...");
	}
	
	public interface Callback{
		void onFound(File f, long addr);
	}
	
	public static void searchDir(String dir, String query, final Callback cb){
		final byte[] q = Util.decodeHex(query.replace(" ", ""));
		DirLooper.loop(dir, new DirLooper.Callback() {
			@Override
			public void handleFile(File f) {
				search(f, q, cb);
			}
		});
	}
	
	public static void searchFile(File f, String query, Callback cb) {
		search(f, Util.decodeHex(query.replace(" ", "")), cb);
	}
	
	public static void search(File f, byte[] query, Callback cb){
		try {
			PushbackInputStream is = new PushbackInputStream(new ByteArrayInputStream(Util.loadFile(f)), query.length);
			long addr=0;
			byte[] buf = new byte[query.length];
			while(is.read(buf)==buf.length){
				if(Arrays.equals(query, buf)){
					if(cb==null){
						System.err.printf("%s : found!! %X\n", f, addr);
					} else {
						cb.onFound(f, addr);
					}
				}
				is.unread(buf, 1, buf.length-1);
				addr++;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static List<Integer> searchSingleFile(File f, byte[] query){
		try {
			PushbackInputStream is = new PushbackInputStream(new ByteArrayInputStream(Util.loadFile(f)), query.length);
			int addr=0;
			byte[] buf = new byte[query.length];
			List<Integer> ret = new ArrayList<>();
			while(is.read(buf)==buf.length){
				if(Arrays.equals(query, buf)){
					ret.add(addr);
				}
				is.unread(buf, 1, buf.length-1);
				addr++;
			}
			return ret;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
