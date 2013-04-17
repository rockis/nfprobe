package hdprobe;

import org.apache.mina.core.buffer.IoBuffer;

public interface FlowDecoder {
	public FlowPacket decode(IoBuffer buff);
}
