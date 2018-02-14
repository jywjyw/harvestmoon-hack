package fencer;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Searcher {
	public static void main(String[] args) {
		Searcher.search("MAIN", Integer.parseInt("3600", 16));
	}
	
	public static void search(String cd, int addr){
		if(addr<0x800){
			System.err.println("out of rage");
			return;
		}
			
		InputStreamReader is = new InputStreamReader(Searcher.class.getResourceAsStream("indexes.txt"));
		HashMap<String,ArrayList<CDpart>> indexes = new Gson().fromJson(is, new TypeToken<HashMap<String,ArrayList<CDpart>>>(){}.getType());
		for(CDpart c : indexes.get(cd)){
			if(addr>=c.start && addr<=c.end){
				System.out.println("founded!!  "+c.name);
			}
		}
	}
	
}
