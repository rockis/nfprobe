package hdprobe.intercepters;

import hdprobe.FlowPDU;

public interface FlowIntercepter {

	public boolean intercept(FlowPDU pdu);
	
}
