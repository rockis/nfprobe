package hdprobe.aggregation;

import hdprobe.export.Exportable;
import hdprobe.utils.KVGroup;

public interface Aggregator extends Aggregatable, Exportable {

	public int getGranularity();
	
	public AggregationKey getKey();
	
	public boolean check(KVGroup group);
}
