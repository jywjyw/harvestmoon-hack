package farm;

public class Patch {
	private byte[] data;
	private int addr; // 从哪段地址开始覆盖旧数据
	private int overwriteLength; // 多少旧数据要被覆盖,为0代表仅插入,和data.length相同时代表仅覆盖,不增加ROM长度
	
	public Patch(byte[] data, int addr, int overwriteLength) {
		this.data = data;
		this.addr = addr;
		this.overwriteLength = overwriteLength;
		if(this.overwriteLength>data.length){
			throw new RuntimeException("overwriteLength不能大于patch文件的大小");
		}
	}

	public byte[] getData() {
		return data;
	}

	public int getAddr() {
		return addr;
	}

	public int getOverwriteLength() {
		return overwriteLength;
	}

	public int getDiff() {
		return data.length-overwriteLength;
	}
}