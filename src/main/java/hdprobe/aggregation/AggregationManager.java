package hdprobe.aggregation;

import hdprobe.FlowPDU;

import java.util.ArrayList;
import java.util.List;


public class AggregationManager {

	private List<Aggregatable> aggregators = new ArrayList<Aggregatable>();
	
	public void registerAggregator(Aggregatable aggregator) {
		aggregators.add(aggregator);
	}
	
	public void aggregate(FlowPDU pdu) {
		for (Aggregatable agg : aggregators) {
			agg.aggregate(pdu);
		}
	}
	
	public void setAggregators(List<Aggregatable> aggregators) {
		for (Aggregatable aggregator : aggregators) {
			registerAggregator(aggregator);
		}
	}
}
