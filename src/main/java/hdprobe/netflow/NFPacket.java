package hdprobe.netflow;

import org.apache.mina.core.buffer.IoBuffer;

import hdprobe.FlowPacket;
import hdprobe.Router;
import hdprobe.utils.IoBufferReader;

public abstract class NFPacket implements FlowPacket{
	
	protected int version;
	protected Router router;
	
	protected int pduCount = 0;
	
	protected final IoBufferReader buffer;
	
	public NFPacket(IoBufferReader buffer, Router router) {
		this.buffer = buffer;
		this.router = router;
		this.version = buffer.nextShort();
	}
	
	public int getVersion() {
		return version;
	}
	
	public IoBuffer getBuffer() {
		return buffer.getBuffer();
	}
	
	@Override
	public Router getRouter() {
		return router;
	}
}

