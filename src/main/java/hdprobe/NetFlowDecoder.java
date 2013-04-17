package hdprobe;

import org.apache.mina.core.buffer.IoBuffer;

import hdprobe.netflow.NFPacket;
import hdprobe.netflow.V5Packet;
import hdprobe.netflow.V9Packet;
import hdprobe.utils.IoBufferReader;

public class NetFlowDecoder implements FlowDecoder {

	private Router router;
	public NetFlowDecoder(Router router) {
		this.router = router;
	}
	
	public NFPacket decode(IoBuffer buffer) {
		IoBufferReader reader = new IoBufferReader(buffer);
		int version = reader.currentShort();
		if (version == 5) {
			return new V5Packet(reader.next(), router);
		}else if (version == 9) {
			return new V9Packet(reader.next(), router);
		}
		return null; //not supported
	}
}
