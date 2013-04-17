package hdprobe.utils;

import org.apache.mina.core.buffer.IoBuffer;

public class IoBufferReader {

	private IoBuffer buffer;
	private int cursor = 0;
	private int offset = 0;
	
	public IoBufferReader(IoBuffer buffer) {
		this(buffer, 0);
	}
	
	public IoBufferReader(IoBuffer buffer, int offset) {
		this.buffer = buffer;
		this.cursor = offset;
		this.offset = offset;
	}
	
	public int currentIByte() {
		return 0xFF & this.buffer.get(cursor);
	}
	
	public int currentIByte(int offset) {
		return 0xFF & this.buffer.get(cursor + offset);
	}
	
	public int nextByte() {
		this.cursor++;
		return 0xFF & this.buffer.get(cursor - 1);
	}
	
	public int[] nextByteArray(int len) {
		int[] bytes = new int[len];
		for(int i = 0; i < len; i++) {
			bytes[i] = nextByte();
		}
		return bytes;
	}
	
	public int currentShort() {
		return this.buffer.getUnsignedShort(cursor);
	}
	
	public int nextShort() {
		this.cursor += 2;
		return this.buffer.getUnsignedShort(cursor - 2);
	}
	
	public int[] nextShortArray(int len) {
		int[] shorts = new int[len];
		for(int i = 0; i < len; i++) {
			shorts[i] = nextShort();
		}
		return shorts;
	}
	
	public int nextInt() {
		this.cursor += 4;
		return this.buffer.getInt(cursor - 4);
	}
	
	public int[] nextIntArray(int len) {
		int[] ints = new int[len];
		for(int i = 0; i < len; i++) {
			ints[i] = nextInt();
		}
		return ints;
	}
	
	public int getOffset() {
		return cursor;
	}
	
	public void skip() {
		this.cursor++;
	}
	
	public void skip(int len) {
		this.cursor += len;
	}
	
	public IoBufferReader next() {
		return new IoBufferReader(buffer, this.cursor);
	}
	
	public IoBufferReader next(int offset) {
		return new IoBufferReader(buffer, this.offset + offset);
	}
	
	public IoBuffer getBuffer() {
		return buffer;
	}
	
	public int getTailLen() {
		return buffer.capacity() - cursor;
	}
}
