package common;


import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Util {
	
//	public static List<byte[]> rebuild(File textFile,
//			int pointerCount, long textStartPos, long textEndPos) throws Exception {
//		
//		CharTable charTable = new CharTable();
//		Element xml = new SAXReader().read(textFile).getRootElement();
//		if(xml.elements().size()!=pointerCount) throw new RuntimeException();
//		List<byte[]> textArray = new ArrayList<>();
//		Set<String> illegalChar = new LinkedHashSet<>();
//		ByteBuffer buf = ByteBuffer.allocate(50000);
//		for(int i=1;i<=pointerCount;i++) {
//			Element e = xml.element("_"+i);
//			String chinese = e.elementTextTrim("chinese");
//			DataInputStream is =null;
//			try {
//				is = new DataInputStream(new ByteArrayInputStream(chinese.getBytes("gbk")));
//			} catch (UnsupportedEncodingException e3) {}
//			
//			StringBuilder specialChar = null;
//			int mode = 0; //0=normal, 控制符=1, 特殊字=2;
//			while(true) {
//				try {
//					byte b = is.readByte();
//					if(mode==1) {
//						specialChar.append((char)b);
//						if(b=='}') {
//							CtrlChar.toBytes(buf, specialChar.toString());
//							mode=0;
//						}
//					} else if(mode==2){
//						specialChar.append((char)b);
//						if(b==']') {
//							Integer code = charTable.getCode(specialChar.toString());
//							if(code==null)System.out.println(specialChar.toString());
//							buf.putShort(code.shortValue());
//							mode=0;
//						}
//					} else {
//						if(b=='{') {
//							mode=1;
//							specialChar=new StringBuilder();
//							specialChar.append((char)b);
//						} else if(b=='['){
//							mode=2;
//							specialChar=new StringBuilder();
//							specialChar.append((char)b);
//						} else {
//							String char_ = null;
//							if(b>=0&&b<=0x7f) {
//								char_ = String.valueOf((char)b);
//							} else {
//								char_ = new String(new byte[]{b, is.readByte()}, "gbk");
//							}
//							Integer code = charTable.getCode(char_);
//							if(code!=null) {
//								buf.putShort(code.shortValue());
//							} else {
//								illegalChar.add(char_);
//							}
//						}
//					}
//				} catch(EOFException e1) {
//					break;
//				} catch(IOException e2) {
//					break;
//				}
//			}
//			
//			byte[] bytes = Arrays.copyOf(buf.array(), buf.position());
////			System.out.println(chinese);
////			for(byte b : bytes) {
////				System.out.printf("%02X ", b);
////			}
////			System.out.println();
//			textArray.add(bytes);
//			buf.clear();
//			buf.position(0);
//		}
//		if(!illegalChar.isEmpty()) {
//			throw new RuntimeException(illegalChar.size()+" illegal char="+show(illegalChar));
//		}
//		if(textArray.size()!=pointerCount) {
//			throw new RuntimeException("not eq pointer size");
//		}
//		//TODO warning:剧情对话中,每个01f0后的字符数<=15. 要统计每个字用了多少次，作排名。
//		
//		
//		long totalLen = 0;
//		for(byte[] bs : textArray) {
//			totalLen+=bs.length;
//		}
//		if(totalLen>textEndPos-textStartPos) {
//			throw new RuntimeException("too long");
//		} 
//		return textArray;
//		
//	}
	
	public static int hilo(int i) {
		return i>>>24|i>>>8&0xff00|i<<8&0xff0000|i<<24&0xff000000;
	}
	
	public static int hiloShort(int i) {
		return i>>>8&0xff|i<<8&0xff00;
	}
	
	private static String show(Set<String> set) {
		StringBuilder sb = new StringBuilder();
		for(String s : set) {
			sb.append(s+" ");
		}
		return sb.toString();
	}
	
	/** 
	 * MD5加密
	 * @param cont 要加密的字节数组 
	 * @return    加密后的字符串 
	 */  
	public static String toMd5(byte[] cont){  
	    try {  
	        MessageDigest md = MessageDigest.getInstance("MD5");  
	        md.update(cont);  
	        byte[] byteDigest = md.digest();  
	        int i;  
	        StringBuilder buf = new StringBuilder();  
	        for (int offset = 0; offset < byteDigest.length; offset++) {  
	            i = byteDigest[offset];  
	            if (i < 0)  i += 256;  
	            if (i < 16) buf.append("0"); 
	            buf.append(Integer.toHexString(i));
	        }
//	        return buf.toString().substring(8, 24);		//16位加密     
	        return buf.toString();						//32位加密    
	    } catch (NoSuchAlgorithmException e) {  
	        throw new RuntimeException(e); 
		}
	}
	
	/**
	 * 把request.getParameterMap()转成字符串, 适用于查看POST参数
	 * @param paramMap
	 * @param charset 是否要把参数urlencode
	 * @return
	 */
	public static String paramMapToString(Map paramMap, String charset)	{
		if(paramMap.size() == 0)	return "";
		StringBuilder sb = new StringBuilder();
		for(Object o : paramMap.entrySet())	{
			Entry e = (Entry)o;
			if(e.getValue() instanceof String[])	{
				for(String val : (String[])e.getValue())	{
					if(charset != null)	{
						try {
							val = URLEncoder.encode(val, charset);
						} catch (UnsupportedEncodingException e1) {
							e1.printStackTrace();
						}
					}
					sb.append(e.getKey()).append("=").append(val).append("&");
				}
			} else {
				String value = (String)e.getValue();
				if(charset != null)	{
					try {
						value = URLEncoder.encode(value, charset);
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
				}
				sb.append(e.getKey()).append("=").append(value).append("&");
			}
		}
		if(sb.toString().endsWith("&"))	{
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
	public static String join(List<String> list, String splitter)	{
		return join(list, splitter, null);
	}
	
	/**
	 * 将字符串集合分隔拼接成一条字符串
	 * @param list 集合
	 * @param splitter 分隔符
	 * @param replacement 将与分隔符有冲突的字符串替换
	 * @return
	 */
	public static String join(List<String> list, String splitter, String replacement)	{
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<list.size(); i++)	{
			if(replacement != null && list.get(i) != null)	{
				sb.append(list.get(i).replace(splitter, replacement));
			} else	{
				sb.append(list.get(i));
			}
			if(i < list.size() - 1)	{
				sb.append(splitter);
			}
		}
		return sb.toString();
	}
	
	public static String toHexString(byte[] bs) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<bs.length;i++) {
			sb.append(String.format("%02X", bs[i]&0xff));
		}
		return sb.toString();
	}
	
	/**
	 * 关闭多个流
	 * @param closeable
	 */
	public static void close(Closeable...closeable)	{
		for(Closeable c : closeable)	{
			try {
				if(c != null)	c.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static byte[] copyPartFile(String file, long startPos, int length) {
		byte[] buf = new byte[length];
		try {
			RandomAccessFile bin = new RandomAccessFile(new File(file), "r");
			bin.seek(startPos);
			bin.read(buf);
			bin.close();
			return buf;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void overwriteFile(String file, long startPos, byte[] data) {
		try {
			RandomAccessFile bin = new RandomAccessFile(new File(file), "rw");
			bin.seek(startPos);
			bin.write(data);
			bin.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void writeFile(File target, byte[] data){
		try {
			FileOutputStream fos = new FileOutputStream(target);
			fos.write(data);
			fos.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
