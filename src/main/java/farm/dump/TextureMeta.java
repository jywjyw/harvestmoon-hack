package farm.dump;

public class TextureMeta {
	
	public int imgSize,bit,type;
	
	// 纹理在显存中的x,y坐标, 在显存中的宽高(即16位下的宽,通常图片是4位), 在split file中的起始位置
	public int x, y, w, h;

	public void validate(long fileSize) {
		if (x > 1024 || w <= 0 || w > 1024 || y > 512 || h <= 0 || h > 512)
			throw new UnsupportedOperationException("out of vram,not img");
	}
	
	public int get4bitWidth(){
		return w*4;	//4bit下的图片宽度
	}
}