package hdprobe;

import java.util.Iterator;

public interface FlowPacket {

	public static final int NETFLOW = 0;
	public static final int SFLOW = 1;
	
	Iterator<? extends FlowPDU> pdu();
	Router getRouter();
}
