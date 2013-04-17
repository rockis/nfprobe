package hdprobe.utils;

public enum Direction {
	
	IN(0), OUT(1);
	
	private int code;

	private Direction(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
}
