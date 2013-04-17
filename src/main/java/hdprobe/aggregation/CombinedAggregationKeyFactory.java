package hdprobe.aggregation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import hdprobe.FlowPDU;
import hdprobe.utils.KVEntry;
import hdprobe.utils.KVGroup;

public class CombinedAggregationKeyFactory implements AggregationKeyFactory {

	private String[] pduProps;
	private Map<String, String> propsNameMap = new HashMap<String, String>();
	private Map<String, String> extraProps = new HashMap<String, String>();
	
	public CombinedAggregationKeyFactory(String... pduProps) {
		this.pduProps = pduProps;
	}

	public void setPropsNameMap(Map<String, String> propsNameMap) {
		this.propsNameMap = propsNameMap;
	}

	public void setExtraProps(Map<String, String> extraProps) {
		this.extraProps = extraProps;
	}

	@Override
	public AggregationKey createKey(FlowPDU pdu) {
		return new CombinedAggregationKey(pduProps, propsNameMap, pdu, extraProps);
	}
	
	class CombinedAggregationKey implements AggregationKey {

		private KVGroup propsGroup = new KVGroup();
		
		CombinedAggregationKey(String[] pduProps, Map<String, String> propsNameMap, FlowPDU pdu, Map<String, String> extraProps) {
			for (int i = 0; i < pduProps.length; i++) {
				propsGroup.addEntry(new KVEntry(pduProps[i], pdu.getProperty(pduProps[i]), propsNameMap.get(pduProps[i])));
			}
			for (Iterator<Map.Entry<String, String>> iter = extraProps.entrySet().iterator(); iter.hasNext();) {
				Map.Entry<String, String> entry = iter.next();
				propsGroup.addEntry(new KVEntry(entry.getKey(), entry.getValue()));
			}
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null || obj instanceof CombinedAggregationKey == false ) {
				return false;
			}
			return propsGroup.equals(((CombinedAggregationKey)obj).propsGroup);
		}

		@Override
		public int hashCode() {
			return propsGroup.hashCode();
		}

		@Override
		public KVGroup export() {
			return propsGroup.copy();
		}
	}
}
