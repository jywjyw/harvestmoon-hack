package farm.hack;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import common.Conf;
import farm.hack.SentenceSplitter.Callback;

public class SentenceSerializer {
	
	public static byte[] toBytes(final Encoding enc, String sentence){
		final ByteBuffer buf = ByteBuffer.allocate(Conf.SECTOR);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		SentenceSplitter.splitToWords(sentence, new Callback() {
			@Override
			public void onReadWord(boolean isCtrl, String word) {
				Integer code=enc.getCode(word);
				if(code==null){
					if(word.startsWith("{")&&word.endsWith("}")) {
						code= Integer.parseInt(word.substring(1,5), 16);
					}else{
						code=enc.put(word);
					}
				}
				buf.putShort(code.shortValue());
			}
		});
		return Arrays.copyOfRange(buf.array(), 0, buf.position());
	}
}
