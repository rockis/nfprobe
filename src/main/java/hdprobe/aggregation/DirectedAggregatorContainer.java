package hdprobe.aggregation;

import hdprobe.FlowPDU;
import hdprobe.utils.Direction;

public class DirectedAggregatorContainer extends AbstractAggregatorContainer {

	protected DirectedAggregationKeyFactory keyFactory;

	public DirectedAggregatorContainer(DirectedAggregationKeyFactory keyFactory,
			AggregatorFactory objFactory, int granularity, int capacity) {
		super(keyFactory, objFactory, granularity, capacity);
		this.keyFactory = keyFactory;
	}

	public DirectedAggregatorContainer(DirectedAggregationKeyFactory keyFactory,
			AggregatorFactory objFactory, int granularity) {
		this(keyFactory, objFactory, granularity, 0);
	}

	@Override
	public void aggregate(FlowPDU pdu) {
		System.out.printf("input :%d, output:%d\n", pdu.getInput(), pdu.getOutput());
		if (!isAccept(pdu)) {
			return;
		}
		
		if (pdu.getInput() != 39 && pdu.getOutput() != 39) {
			return;
		}
		if (pdu.getInput() == 39) {
			System.out.println("............ input");
		}
		AggregationKey primaryKey = keyFactory.createKey(pdu);
		AggregationKey secondaryKey = keyFactory.createSecondaryKey(pdu);
		DirectedTrafficAggregator primaryObj = (DirectedTrafficAggregator)aggregators.get(primaryKey);
		if (primaryObj == null) {
			if (capacity != 0 && aggregators.size() >= capacity) {
				return;
			}
			System.out.println("create primary key");
			System.out.println("create secondary key " + primaryKey );
			primaryObj = (DirectedTrafficAggregator)objFactory.createObject(primaryKey, granularity);
			aggregators.put(primaryKey, primaryObj);
		}
		if (primaryObj.getKey().toString().equals("snmpif=39")){
			System.out.println("primary =>" + primaryObj.getKey());
		}
		primaryObj.aggregate(pdu, Direction.IN);

		DirectedTrafficAggregator secondaryObj = (DirectedTrafficAggregator)aggregators.get(secondaryKey);

		if (secondaryObj == null) {
			if (capacity != 0 && aggregators.size() >= capacity) {
				return;
			}
			System.out.println("create secondary key " + secondaryKey );
			secondaryObj = (DirectedTrafficAggregator)objFactory.createObject(secondaryKey, granularity);
			aggregators.put(secondaryKey, secondaryObj);
		}
		
		if (secondaryObj != null) {
			secondaryObj.aggregate(pdu, Direction.OUT);
			if (secondaryObj.getKey().toString().equals("snmpif=39")){
				System.out.println("secondary =>" + secondaryObj.getKey());
			}
		}
		
	}
}
