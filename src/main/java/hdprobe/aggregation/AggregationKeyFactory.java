package hdprobe.aggregation;

import hdprobe.FlowPDU;

public interface AggregationKeyFactory {
	public AggregationKey createKey(FlowPDU pdu);
}
