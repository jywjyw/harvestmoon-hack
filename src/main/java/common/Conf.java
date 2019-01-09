package common;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Conf {
	
	public static String boyjpdir,boyoutdir,desktop;
	public static final int 
	SECTOR = 0x800,
	charW=12,charH=12,	//每个字体宽高
	FILE0_REAL_SIZE = 0x3e9d918,	//FILE0的真实大小,后面对齐到0x800个字节
	SCRIPT_PREFIX_LEN = 0X1800,		//对话文本前的未知数据
	SCRIPT_GROUP_LEN = 0x2000		//对话文本前的未知数据+0x800的对话文本为一组
	;
	public static final String EXE_BOY="SLPS_024.89", BIN = "A_FILE.BIN", HDT = "A_FILE.HDT";
	static {
		InputStream is=null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.properties");
			Properties conf = new Properties();
			conf.load(is);
			boyjpdir = conf.getProperty("boyjpdir");
			boyoutdir = conf.getProperty("boyoutdir");
			desktop= conf.getProperty("desktop");
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
	
	public static String getRawFile(String rawFile){
		return System.getProperty("user.dir")+File.separator+"raw"+File.separator+rawFile;
	}
	
	public static String getTranslateFile(String file){
		return System.getProperty("user.dir")+File.separator+"translation"+File.separator+file;
	}
	
	
	public static int getExeOffset(int memAddr){
		return memAddr-0x8000f800;
	}
	
	public static int getExeAddr(int offset){
		return 0x8000f800+offset;
	}

}
