package hdprobe.aggregation;

import hdprobe.FlowPDU;
import hdprobe.utils.Direction;
import hdprobe.utils.KVEntry;
import hdprobe.utils.KVGroup;

public class DirectedTrafficAggregator implements Aggregator {

	private AggregationKey  key;
	private int            granularity;
	
	private TrafficAggregator in;
	private TrafficAggregator out;
	
	private KVGroup lastCheck;
	
	public DirectedTrafficAggregator(AggregationKey key, int granularity) {
		this.key = key;
		this.granularity = granularity;
		in = new TrafficAggregator(key, granularity);
		out = new TrafficAggregator(key, granularity);
	}

	@Override
	public void aggregate(FlowPDU pdu) {
	}
	
	public void aggregate(FlowPDU pdu, Direction direction) {
		switch (direction){
		case IN:
			in.aggregate(pdu);
			break;
		case OUT:
			out.aggregate(pdu);
		}
	}

	@Override
	public KVGroup export() {
		return new KVGroup(key.export(), lastCheck);
	}

	@Override
	public int getGranularity() {
		return granularity;
	}

	@Override
	public AggregationKey getKey() {
		return key;
	}

	@Override
	public boolean check(KVGroup mergedGroup) {
		
		KVGroup groupIn = new KVGroup();
		boolean inUpdated  = in.check(groupIn);
		
		KVGroup groupOut = new KVGroup();
		boolean outUpdated = out.check(groupOut);
		
		if (inUpdated == false && outUpdated == false) {
			return false;
		}
		if (mergedGroup == null) {
			mergedGroup = new KVGroup();
		}
		KVEntry[] gin = groupIn.getEntries();
		KVEntry[] gout = groupOut.getEntries();
		for(int i = 0; i < groupIn.getEntries().length; i++) {
			mergedGroup.addEntry(new KVEntry("in_" + gin[i].getKey(), gin[i].getValue()));
		}
		for(int i = 0; i < groupOut.getEntries().length; i++) {
			mergedGroup.addEntry(new KVEntry("out_" + gout[i].getKey(), gout[i].getValue()));
		}
		lastCheck = mergedGroup.copy();
		return true;
	}

}
