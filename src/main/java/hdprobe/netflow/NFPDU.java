package hdprobe.netflow;

import hdprobe.AbstractFlowPDU;

public abstract class NFPDU extends AbstractFlowPDU {
	
	public abstract int getDuration();
	
	public abstract int getFirstSeen();
	
	public abstract int getLastSeen();
	
}