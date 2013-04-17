package hdprobe.aggregation;

import java.util.HashMap;
import java.util.Map;

import hdprobe.FlowPDU;
import hdprobe.utils.Direction;

public class DirectedAggregationKeyFactory implements AggregationKeyFactory {

	private String prop;
	
	private Map<String, String> inDirectionPropMap = new HashMap<String, String>();
	private Map<String, String> outDirectionPropMap = new HashMap<String, String>();
	
	public DirectedAggregationKeyFactory(String prop) {
		this.prop = prop;
		
		inDirectionPropMap.put("ip", "dstAddr");
		inDirectionPropMap.put("port", "dstPort");
		inDirectionPropMap.put("as", "dstAS");
		inDirectionPropMap.put("snmpif", "input");
		inDirectionPropMap.put("masklen", "dstMasklen");
		inDirectionPropMap.put("country", "dstCountry");
		
		outDirectionPropMap.put("ip", "srcAddr");
		outDirectionPropMap.put("port", "srcPort");
		outDirectionPropMap.put("as", "srcAS");
		outDirectionPropMap.put("snmpif", "output");
		outDirectionPropMap.put("masklen", "srcMasklen");
		outDirectionPropMap.put("country", "srcCountry");
	}
	
	@Override
	public AggregationKey createKey(FlowPDU pdu) {
		return new DirectedAggregationKey(prop, pdu.getProperty(inDirectionPropMap.get(prop)), Direction.IN);
	}
	
	public AggregationKey createSecondaryKey(FlowPDU pdu) {
		return new DirectedAggregationKey(prop, pdu.getProperty(outDirectionPropMap.get(prop)),  Direction.OUT);
	}
	
}
