package hdprobe.aggregation;

import hdprobe.FlowPDU;

public class SimpleAggregatorContainer extends AbstractAggregatorContainer {

	public SimpleAggregatorContainer(AggregationKeyFactory keyFactory, AggregatorFactory objFactory, int granularity, int capacity) {
		super(keyFactory, objFactory, granularity, capacity);
	}
	
	public SimpleAggregatorContainer(AggregationKeyFactory keyFactory, AggregatorFactory objFactory, int granularity) {
		this(keyFactory, objFactory, granularity, 0);
	}
	
	@Override
	public void aggregate(FlowPDU pdu) {
		if (!isAccept(pdu)) {
			return;
		}
		AggregationKey key = keyFactory.createKey(pdu);
		Aggregator obj = aggregators.get(key);
		if (obj == null) {
			if (capacity != 0 && aggregators.size() >= capacity) {
				return;
			}
			obj = objFactory.createObject(key, granularity);
			aggregators.put(key, obj);
		}
		obj.aggregate(pdu);
	}

}
