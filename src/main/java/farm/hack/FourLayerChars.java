package farm.hack;

import java.util.ArrayList;
import java.util.List;

public class FourLayerChars {
	public List<List<String>> chars=new ArrayList<>();
	int arrInd=0;
	
	public FourLayerChars(){
		for(int i=0;i<4;i++){
			chars.add(new ArrayList<>());
		}
	}
	
	public void put(String c){
		chars.get(arrInd).add(c);
		arrInd++;
		if(arrInd==4){
			arrInd=0;
		}
	}
	
	public List<String> getCharLayer(int i){
		return chars.get(i);
	}
	
	public int totalSize(){
		int size=0;
		for(int i=0;i<4;i++){
			size += chars.get(i).size();
		}
		return size;
	}

}
