package hdprobe.aggregation;

import hdprobe.FlowPDU;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractAggregatorContainer implements Aggregatable, AggregatorContainer {

	protected Map<AggregationKey, Aggregator> aggregators = new ConcurrentHashMap<AggregationKey, Aggregator>();
	protected AggregationKeyFactory keyFactory;
	protected AggregatorFactory objFactory;
	protected AggregationFilter filter;
	protected int capacity;
	protected int granularity;
	
	public AbstractAggregatorContainer(AggregationKeyFactory keyFactory, AggregatorFactory objFactory, int granularity, int capacity) {
		this.keyFactory = keyFactory;
		this.objFactory = objFactory;
		this.granularity = granularity;
		this.capacity = capacity;
	}
	
	public AbstractAggregatorContainer(AggregationKeyFactory keyFactory, AggregatorFactory objFactory, int granularity) {
		this(keyFactory, objFactory, granularity, 0);
	}
	
	public void setFilter(AggregationFilter filter) {
		this.filter = filter;
	}

	public boolean isAccept(FlowPDU pdu) {
		if (this.filter != null) {
			return this.filter.isAccept(pdu);
		}
		return true;
	}
	
	public void traversal(AggregatorVisitor visitor) {
		for (Iterator<Map.Entry<AggregationKey, Aggregator>> iter = aggregators.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<AggregationKey, Aggregator> entry = iter.next();
			if (!visitor.visit(entry.getValue())) {
				aggregators.remove(entry.getKey());
			}
		}
	}
}
