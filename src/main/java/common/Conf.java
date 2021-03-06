package common;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Conf {
	
	public static String boyjpdir,boyendir,boyiso,girljpdir,girlendir,girliso,desktop;
	public static final int 
	SECTOR = 0x800,
	charW=12,charH=12,	//每个字体宽高
	FILE0_REAL_SIZE = 0x3e9d918,	//FILE0的真实大小,后面对齐到0x800个字节
	SCRIPT_PREFIX_LEN = 0X1800,		//对话文本前的未知数据
	SCRIPT_GROUP_LEN = 0x2000		//对话文本前的未知数据+0x800的对话文本为一组
	;
	public static final String BIN = "A_FILE.BIN", HDT = "A_FILE.HDT",
			BOY_JP_EXE="SLPS_024.89", BOY_EN_EXE="SLUS_011.15",
			GIRL_JP_EXE="SLPS_030.87"
			;
	static {
		InputStream is=null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.properties");
			Properties conf = new Properties();
			conf.load(is);
			boyjpdir = conf.getProperty("boyjpdir");
			boyendir = conf.getProperty("boyendir");
			girljpdir = conf.getProperty("girljpdir");
			girlendir = conf.getProperty("girlendir");
			boyiso = conf.getProperty("boyiso");
			girliso = conf.getProperty("girliso");
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
	
	public static int getExeOffset(int memAddr){
		return memAddr-0x8000f800;
	}
	
	public static int getExeAddr(int offset){
		return 0x8000f800+offset;
	}
	public static void main(String[] args) {
		System.out.println(Integer.toHexString(getExeAddr(0x461a2)));
	}

}
