package hdprobe.aggregation;

import hdprobe.utils.Direction;
import hdprobe.utils.KVEntry;
import hdprobe.utils.KVGroup;

public class DirectedAggregationKey implements AggregationKey {

	private KVGroup propsGroup = new KVGroup();
	private Direction direction;
	private String prop;
	private Object propValue;
	
	DirectedAggregationKey(String prop, Object propValue, Direction direction) {
		this.prop = prop;
		this.propValue = propValue;
		propsGroup.addEntry(new KVEntry(prop, propValue));
		this.direction = direction;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj instanceof DirectedAggregationKey == false ) {
			return false;
		}
		return propsGroup.equals(((DirectedAggregationKey)obj).propsGroup);
	}

	public Direction getDirection() {
		return direction;
	}

	@Override
	public int hashCode() {
		return propsGroup.hashCode();
	}

	@Override
	public KVGroup export() {
		return propsGroup.copy();
	}
	
	public String toString() {
		return prop + "=" + propValue.toString();
	}
}
