package farm.pic;

import java.io.File;
import java.io.IOException;

import farm.Picpack;

public interface PicHandler {
	
	void exportBoy(Picpack p0, Picpack d000, String exportDir) throws IOException;
	void exportGirl(Picpack p0, Picpack d000, String exportDir) throws IOException;
	
	void importBoy(File f0, Picpack d000) throws IOException;
	void importGirl(File f0, Picpack d000) throws IOException;

}
