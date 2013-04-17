package hdprobe.aggregation;

public class TrafficAggregatorFactory implements AggregatorFactory{
	
	@Override
	public Aggregator createObject(AggregationKey key, int granularity) {
		TrafficAggregator agg = new TrafficAggregator(key, granularity);
		return agg;
	}
}
