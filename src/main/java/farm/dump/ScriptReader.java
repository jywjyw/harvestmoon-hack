package farm.dump;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import common.Util;

/**
 * 读取ROM中的文本区
 */
public class ScriptReader {
	
	public interface Callback {
		/**
		 * @param index 这2个字节是第几次,例如,前2个字节的index=0
		 * @param char_
		 * @param unsignedShort
		 * @param isCtrl
		 */
		void every2Bytes(int index, String char_, int unsignedShort, boolean isCtrl);	
	}
	
	public static void readUntilFFFF(byte[] script, Charset cs, Callback c){
		DataInputStream is = new DataInputStream(new ByteArrayInputStream(script));
		int buf=0,index=0;
		try {
			while(true) {
				buf = Util.hiloShort(is.readUnsignedShort());
				String char_ = cs.getChar(buf);
				if(char_!=null){
					c.every2Bytes(index,char_, buf, char_.startsWith("{"));
				} else {
					c.every2Bytes(index,String.format("{%04X}", buf), buf, true);
				}
				if(buf==0xffff){
					break;
				}
				index++;
			}
		} catch (IOException e) {}
		Util.close(is);
	}
	
	public static String readUntilFFFF(byte[] script, Charset charTable)  {
		final StringBuilder ret = new StringBuilder();
		readUntilFFFF(script, charTable, new Callback() {
			@Override
			public void every2Bytes(int index,String s, int unsignedShort, boolean isCtrl) {
				ret.append(s);
			}
		});
		return ret.toString();
	}
}
