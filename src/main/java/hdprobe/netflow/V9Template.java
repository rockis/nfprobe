package hdprobe.netflow;

import java.util.ArrayList;
import java.util.List;

import hdprobe.utils.IPUtils;
import hdprobe.utils.IoBufferReader;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class V9Template extends V9FlowSet {

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

	public static final int BYTES_64 = 25;

	public static final int PKTS_64 = 24;

	public static final int MAC_ADDR = 25;

	public static final int VLAN_ID = 26;

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

	private int templateId;
	private int fieldCount = 0;
	private int fieldScopeLen = 0;

	private List<TemplateField> fields = new ArrayList<TemplateField>();

	public V9Template(String routerAddr, int engineId, IoBufferReader buffer) {
		super(routerAddr, engineId, buffer);
		templateId = buffer.nextShort();
		fieldCount = buffer.nextShort(); // field count
		for (int i = 0; i < fieldCount; i++) {
			int fieldType = buffer.nextShort();
			int fieldLen = buffer.nextShort();
			fields.add(new TemplateField(fieldType, fieldLen));
			fieldScopeLen += fieldLen;
		}
		V9TemplateManager.setTemplate(this);
	}

	public List<TemplateField> getFields() {
		return fields;
	}

	public Flow decodePDU(IoBufferReader buffer) {
		Flow flow = new Flow();
		for (TemplateField field : fields) {
			switch (field.fieldType) {
			case IPV4_SRC_ADDR:
				flow.srcAddr = IPUtils.int2ip(buffer.nextInt());
				break;
			case IPV4_DST_ADDR:
				flow.dstAddr = IPUtils.int2ip(buffer.nextInt());
				break;
			case IPV4_NEXT_HOP:
			case BGP_NEXT_HOP:
				flow.nexthop = IPUtils.int2ip(buffer.nextInt());
				break;
			case INPUT_SNMP:
				flow.input = buffer.nextShort();
				break;
			case OUTPUT_SNMP:
				flow.output = buffer.nextShort();
				break;
			case L4_SRC_PORT:
				flow.srcPort = buffer.nextShort();
				break;
			case L4_DST_PORT:
				flow.dstPort = buffer.nextShort();
				break;
			case TCP_FLAGS:
				flow.tcpFlags = buffer.nextByte();
				break;
			case PROT:
				flow.protocol = buffer.nextByte();
				break;
			case SRC_AS:
				flow.srcAS = buffer.nextShort();
				break;
			case DST_AS:
				flow.dstAS = buffer.nextShort();
				break;
			case DST_MASK:
			case IPV6_DST_MASK:
				flow.dstMasklen = buffer.nextByte();
				break;
			case SRC_MASK:
			case IPV6_SRC_MASK:
				flow.srcMasklen = buffer.nextByte();
				break;
			case IPV6_SRC_ADDR:
				flow.srcAddr = IPUtils.int2ip(new int[] { buffer.nextInt(),
						buffer.nextInt(), buffer.nextInt(), buffer.nextInt() });
				break;
			case IPV6_DST_ADDR:
				flow.dstAddr = IPUtils.int2ip(new int[] { buffer.nextInt(),
						buffer.nextInt(), buffer.nextInt(), buffer.nextInt() });
				break;
			case IPV6_NEXT_HOP:
			case BGP_IPV6_NEXT_HOP:
				flow.nexthop = IPUtils.int2ip(new int[] { buffer.nextInt(),
						buffer.nextInt(), buffer.nextInt(), buffer.nextInt() });
				break;
			case FIRST_SWITCHED:
				flow.firstSeen = buffer.nextInt();
				break;
			case LAST_SWITCHED:
				flow.lastSeen = buffer.nextInt();
				break;
			case SAMPLING_INTERVAL:
				flow.samplingRate = buffer.nextInt();
			case OUT_BYTES:
			case IN_BYTES:
				flow.bytes = buffer.nextInt();
				break;
			case OUT_PKTS:
			case IN_PKTS:
				flow.packets = buffer.nextInt();
				break;
			default:
				buffer.skip(field.fieldLen);
				break;
			}
			
		}
		return flow;
	}

	public int getFieldScopeLen() {
		return fieldScopeLen;
	}

	public int getTemplateId() {
		return templateId;
	}

	public boolean equals(Object template) {
		if (template == null || template instanceof V9Packet == false) {
			return false;
		}
		V9Template v9t = (V9Template) template;
		return new EqualsBuilder().append(v9t.routerAddr, routerAddr)
				.append(v9t.engineId, engineId)
				.append(v9t.templateId, templateId).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.routerAddr)
				.append(this.engineId).append(this.templateId).toHashCode();
	}

	class TemplateField {
		private int fieldType;
		private int fieldLen;

		public TemplateField(int fieldType, int fieldLen) {
			this.fieldType = fieldType;
			this.fieldLen = fieldLen;
		}
	}
}
