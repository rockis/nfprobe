package hdprobe.aggregation;

import hdprobe.FlowPDU;


public interface AggregationFilter {
	public boolean isAccept(FlowPDU pdu);
}
