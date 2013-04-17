package hdprobe.aggregation;

import hdprobe.FlowPDU;


public interface Aggregatable {

	public void aggregate(FlowPDU pdu);
	
}
