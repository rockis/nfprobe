package hdprobe.utils;

import hdprobe.FlowPDU;
import hdprobe.aggregation.AggregationFilter;

import java.util.HashMap;
import java.util.Map;

public class ProtocolAggregator  {

	private static Map<Integer, String> protocolTable = new HashMap<Integer, String>();
	static {
		protocolTable.put(0,   "IP");
		protocolTable.put(1,   "ICMP");
		protocolTable.put(2,   "IGMP");
		protocolTable.put(4,   "IPIP");
		protocolTable.put(6,   "TCP");
		protocolTable.put(8,   "EGP");
		protocolTable.put(12,  "PUP");
		protocolTable.put(17,  "UDP");
		protocolTable.put(22,  "IDP");
		protocolTable.put(29,  "TP");
		protocolTable.put(33,  "DCCP");
		protocolTable.put(41,  "IPV6");
		protocolTable.put(43,  "ROUTING");
		protocolTable.put(44,  "RSVP");
		protocolTable.put(47,  "GRE");
		protocolTable.put(50,  "ESP");
		protocolTable.put(51,  "AH");
		protocolTable.put(58,  "ICMPV6");
		protocolTable.put(59,  "NONE");
		protocolTable.put(60,  "DSTOPS");
		protocolTable.put(88,  "EIGRP");
		protocolTable.put(92,  "MTP");
		protocolTable.put(98,  "ENCAP");
		
		protocolTable.put(103,  "PIM");
		protocolTable.put(108,  "COMP");
		protocolTable.put(132,  "SCTP");
		protocolTable.put(136,  "UDPLITE");
		protocolTable.put(255,  "RAW");
	}
	

}
                                                                 