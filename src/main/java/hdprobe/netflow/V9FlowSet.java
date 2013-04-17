package hdprobe.netflow;

import java.util.ArrayList;
import java.util.List;
import hdprobe.utils.IoBufferReader;

public abstract class V9FlowSet {

	public static final int IN_BYTES = 1;

	public static final int IN_PKTS = 2;

	public static final int FLOWS = 3;

	public static final int PROT = 4;

	public static final int SRC_TOS = 5;

	public static final int TCP_FLAGS = 6;

	public static final int L4_SRC_PORT = 7;

	public static final int IPV4_SRC_ADDR = 8;

	public static final int SRC_MASK = 9;

	public static final int INPUT_SNMP = 10;

	public static final int L4_DST_PORT = 11;

	public static final int IPV4_DST_ADDR = 12;

	public static final int DST_MASK = 13;

	public static final int OUTPUT_SNMP = 14;

	public static final int IPV4_NEXT_HOP = 15;

	public static final int SRC_AS = 16;

	public static final int DST_AS = 17;

	public static final int BGP_NEXT_HOP = 18;

	public static final int IPM_DPKTS = 19;

	public static final int IPM_DOCTETS = 20;

	public static final int LAST_SWITCHED = 21;

	public static final int FIRST_SWITCHED = 22;

	public static final int OUT_BYTES = 23;

	public static final int OUT_PKTS = 24;

	public static final int MIN_PKT_LNGTH = 25;

	public static final int MAX_PKT_LNGTH = 26;

	public static final int IPV6_SRC_ADDR = 27;

	public static final int IPV6_DST_ADDR = 28;

	public static final int IPV6_SRC_MASK = 29;

	public static final int IPV6_DST_MASK = 30;

	public static final int FLOW_LABEL = 31;

	public static final int ICMP_TYPE = 32;

	public static final int MUL_IGMP_TYPE = 33;

	public static final int SAMPLING_INTERVAL = 34;

	public static final int SAMPLING_ALGORITHM = 35;

	public static final int FLOW_ACTIVE_TIMEOUT = 36;

	public static final int FLOW_INACTIVE_TIMEOUT = 37;

	public static final int ENGINE_TYPE = 38;

	public static final int ENGINE_ID = 39;

	public static final int TOTAL_BYTES_EXPORTED = 40;

	public static final int TOTAL_EXPORT_PKTS_SENT = 41;

	public static final int TOTAL_FLOWS_EXPORTED = 42;

	public static final int FLOW_SAMPLER_ID = 48;

	public static final int FLOW_SAMPLER_MODE = 49;

	public static final int FLOW_SAMPLER_RANDOM_INTERVAL = 50;

	public static final int IP_PROTOCOL_VERSION = 60;

	public static final int DIRECTION = 61;

	public static final int IPV6_NEXT_HOP = 62;

	public static final int BGP_IPV6_NEXT_HOP = 63;

	public static final int IPV6_OPTION_HEADERS = 64;

	public static final int MPLS_LABEL_1 = 70;

	public static final int MPLS_LABEL_2 = 71;

	public static final int MPLS_LABEL_3 = 72;

	public static final int MPLS_LABEL_4 = 73;

	public static final int MPLS_LABEL_5 = 74;

	public static final int MPLS_LABEL_6 = 75;

	public static final int MPLS_LABEL_7 = 76;

	public static final int MPLS_LABEL_8 = 77;

	public static final int MPLS_LABEL_9 = 78;

	public static final int MPLS_LABEL_10 = 79;

	public static final int IN_DST_MAC = 80;

	public static final int OUT_SRC_MAC = 81;

	public static final int IF_NAME = 82;

	public static final int IF_DESC = 83;

	public static final int SAMPLER_NAME = 84;

	public static final int IN_PERMANENT_BYTES = 85;

	public static final int IN_PERMANENT_PKTS = 86;

	protected String routerAddr;
	protected int engineId;
	protected int flowsetId;
	protected int length;
	protected IoBufferReader buffer;

	public V9FlowSet(String routerAddr, int engineId, IoBufferReader buffer) {
		this.routerAddr = routerAddr;
		this.engineId = engineId;
		this.flowsetId = buffer.nextShort();
		this.length = buffer.nextShort();
		this.buffer = buffer;
	}

	public int getLength() {
		return length;
	}

	public String getRouterAddr() {
		return routerAddr;
	}

	public int getEngineId() {
		return engineId;
	}

	public int getFlowsetId() {
		return flowsetId;
	}

	public static List<V9PDU> parseFlows(V9Packet packet, IoBufferReader _buffer) {
		List<V9PDU> flows = new ArrayList<V9PDU>();
		int flowsetId;
		IoBufferReader buffer = _buffer.next();
		while (buffer.getTailLen() > 87) {
			flowsetId = buffer.currentShort();
			if (flowsetId == 0 || flowsetId == 1) {
				V9Template template = new V9Template(packet.getRouter()
						.getAddress(), packet.getEngineId(), buffer.next());
				buffer = buffer.next(template.getLength());
			} else if (flowsetId > 255) {
				V9DataFlowSet dataset = new V9DataFlowSet(packet, buffer.next());
				buffer = buffer.next(dataset.getLength());
				dataset.parse(flows);
			}else{
				System.out.println("?" + buffer.getTailLen());
			}
		}
		return flows;
	}
	
	class Flow {
		int input;
		int output;
		long packets;
		long bytes;
		int firstSeen;
		int lastSeen;
		double duration;
		int srcPort;
		int dstPort;
		int tcpFlags;
		int protocol;
		int tos;
		int srcAS;
		int dstAS;
		String srcAddr;
		String dstAddr;
		String nexthop;
		int srcMasklen;
		int dstMasklen;
		
		int samplingRate;
		
		public int getInput() {
			return input;
		}
		public int getOutput() {
			return output;
		}
		public long getPackets() {
			return packets;
		}
		public long getBytes() {
			return bytes;
		}
		public int getFirstSeen() {
			return firstSeen;
		}
		public int getLastSeen() {
			return lastSeen;
		}
		public double getDuration() {
			return duration;
		}
		public int getSrcPort() {
			return srcPort;
		}
		public int getDstPort() {
			return dstPort;
		}
		public int getTcpFlags() {
			return tcpFlags;
		}
		public int getProtocol() {
			return protocol;
		}
		public int getTos() {
			return tos;
		}
		public int getSrcAS() {
			return srcAS;
		}
		public int getDstAS() {
			return dstAS;
		}
		public String getSrcAddr() {
			return srcAddr;
		}
		public String getDstAddr() {
			return dstAddr;
		}
		public String getNexthop() {
			return nexthop;
		}
		public int getSrcMasklen() {
			return srcMasklen;
		}
		public int getDstMasklen() {
			return dstMasklen;
		}
		public int getSamplingRate() {
			return samplingRate;
		}
		
	}
}


