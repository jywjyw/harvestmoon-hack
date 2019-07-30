package test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class TtplOper {
	
	public static void main(String[] args) throws DocumentException {
		String fromDir = "D:\\Trance In Motion\\split\\", toDir = "D:\\Trance In Motion\\favor\\";
		Element items = new SAXReader().read(new File("D:\\Trance In Motion\\151-160.ttpl")).getRootElement().element("items");
		List<File> froms = new ArrayList<>();
		for(Object o : items.elements()) {
			Element i = (Element)o;
//			String filename = String.format("%s - %s.mp3", i.attributeValue("SongArtist"), i.attributeValue("SongName"));
			String filename = i.attributeValue("title")+".mp3";
			if(filename.startsWith("null"))
				System.out.println("");
			File from = new File(fromDir+filename);
			if(!from.exists()) throw new RuntimeException("not exist.."+from);
			froms.add(from);
		}
		for(File f : froms) {
			boolean move = f.renameTo(new File(toDir+f.getName()));
			if(!move) throw new RuntimeException("move failure.."+f);
		}
		System.out.println("over...");
	}

}
