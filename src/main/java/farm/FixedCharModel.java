package farm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import common.Util;

public class FixedCharModel {
	
	private LinkedHashMap<String,FontData> map = new LinkedHashMap<>();
	
	public FixedCharModel(){
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("charmodel.prop");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String s=null;
			while((s=br.readLine())!=null) {
				String[] arr = s.split("=",3);
				FontData f = new FontData();
				f.char_=arr[0];
				f.imgdata = Base64.decodeBase64(arr[1]);
				if(arr.length>2) {
					f.position = Integer.parseInt(arr[2]);
				}
				map.put(f.char_, f);
			}
			Util.close(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Map<String,FontData> getClone() {
		return (Map<String, FontData>) map.clone();
	}
	
}
