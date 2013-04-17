package hdprobe.aggregation;

public class DirectedTrafficAggregatorFactory implements AggregatorFactory {

	@Override
	public Aggregator createObject(	AggregationKey key, int granularity) {
		return new DirectedTrafficAggregator(key, granularity);
	}

}
