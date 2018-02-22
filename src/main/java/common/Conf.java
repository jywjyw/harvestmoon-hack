package common;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Conf {
	
	public static String bin,hackbin, export,desktop;
	public static final int 
	SECTOR = 0x800,
	charW=12,charH=12,	//每个字体宽高
	FILE0_REAL_SIZE = 0x3e9d918,	//FILE0的真实大小,后面对齐到0x800个字节
	SCRIPT_PREFIX_LEN = 0X1800,		//对话文本前的未知数据
	SCRIPT_GROUP_LEN = 0x2000		//对话文本前的未知数据+0x800的对话文本为一组
	
	;
	static {
		InputStream is=null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.prop");
			Properties conf = new Properties();
			conf.load(is);
			bin = conf.getProperty("bin");
			assertNotnull(bin);
			hackbin = conf.getProperty("hackbin");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
	}
	
	private static void assertNotnull(Object o) {
		if(o==null)throw new RuntimeException("conf.prop初始化失败..");
	}

}
