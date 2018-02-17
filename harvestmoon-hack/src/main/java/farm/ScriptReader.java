package farm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import common.Util;

/**
 * 读取ROM中的文本区
 */
public class ScriptReader {
	
	public static String readUntilFF(byte[] script, CharTable charTable)  {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(script));
		int buf=0;
		StringBuilder ret = new StringBuilder();
		try {
			while(true) {
				buf = dis.readUnsignedShort();
				String char_ = charTable.getChar(buf);
				if(char_==null){
					ret.append(String.format("{%04X}", buf));
				} else {
					ret.append(char_);
				}
				if(buf==0xffff){
					break;
				}
			}
		} catch (IOException e) {
		}
		Util.close(dis);
		return ret.toString();
	}
}
