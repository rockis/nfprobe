package hdprobe.aggregation;

public interface AggregatorFactory {
	public Aggregator createObject(AggregationKey key, int granularity);
	
}
