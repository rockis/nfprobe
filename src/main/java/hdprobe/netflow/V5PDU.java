package hdprobe.netflow;

import java.util.Date;

import hdprobe.Router;
import hdprobe.utils.IPUtils;
import hdprobe.utils.IoBufferReader;

/**
 * 
 * Bytes	Contents	Description
 * 0-3	srcaddr	Source IP address
 * 4-7	dstaddr	Destination IP address
 * 8-11	nexthop	IP address of next hop router
 * 12-13	input	SNMP index of input interface
 * 14-15	output	SNMP index of output interface
 * 16-19	dPkts	Packets in the flow
 * 20-23	dOctets	Total number of Layer 3 bytes in the packets of the flow
 * 24-27	first	SysUptime at start of flow
 * 28-31	last	SysUptime at the time the last packet of the flow was received
 * 32-33	srcport	TCP/UDP source port number or equivalent
 * 34-35	dstport	TCP/UDP destination port number or equivalent
 * 36		pad1	Unused (zero) bytes
 * 37		tcp_flags	Cumulative OR of TCP flags
 * 38		prot	IP protocol type (for example, TCP = 6; UDP = 17)
 * 39		tos	IP type of service (ToS)
 * 40-41	src_as	Autonomous system number of the source, either origin or peer
 * 42-43	dst_as	Autonomous system number of the destination, either origin or peer
 * 44	src_mask	Source address prefix mask bits
 * 45	dst_mask	Destination address prefix mask bits
 * 46-47	pad2	Unused (zero) bytes
 * @author zhanglei
 *
 */
public final class V5PDU extends NFPDU{

	public static final int PDU_LEN = 48;
			
	private V5Packet nfpacket;

	private String srcAddr;
	private String dstAddr;
	private String nexthop;
	
	private int srcPort;
	private int dstPort;
	
	private int protocol;
	
	private int input;
	private int output;
	private int firstSeen;
	private int lastSeen;
	private int duration;
	private int tcpFlags;
	private int tos;
	private int srcAS;
	private int dstAS;
	private int srcMasklen;
	private int dstMasklen;
	
	private BitPackets xps30;
	private BitPackets xps60;
	private BitPackets xps500;
	
	public V5PDU(V5Packet nfpacket, IoBufferReader buffer) {
		this.srcAddr = IPUtils.int2ip(buffer.nextInt());
		this.dstAddr = IPUtils.int2ip(buffer.nextInt());
		this.nexthop = IPUtils.int2ip(buffer.nextInt());
		this.input   = buffer.nextShort();
		this.output  = buffer.nextShort();
		int packets = buffer.nextInt() * nfpacket.getSamplingRate();
		int bytes   = buffer.nextInt() * nfpacket.getSamplingRate();
		this.firstSeen = buffer.nextInt();
		this.lastSeen  = buffer.nextInt();
		this.duration= (lastSeen - firstSeen) / 1000;
		this.srcPort = buffer.nextShort();
		this.dstPort = buffer.nextShort();
		buffer.skip(); //padding 1 byte
		this.tcpFlags = buffer.nextByte();
		this.protocol = buffer.nextByte();
		this.tos      = buffer.nextByte();
		this.srcAS      = buffer.nextShort();
		this.dstAS      = buffer.nextShort();
		this.srcMasklen = buffer.nextByte();
		this.dstMasklen = buffer.nextByte();
		buffer.skip(2); //padding 2 bytes
		
		this.xps30 = new BitPackets(packets, bytes, duration, 30);
		this.xps60 = new BitPackets(packets, bytes, duration, 60);
		this.xps500 = new BitPackets(packets, bytes, duration, 500);
	}
	
	public Router getRouter() {
		return nfpacket.getRouter();
	}

	public int getInput() {
		return input;
	}

	public int getOutput() {
		return output;
	}

	@Override
	public long getTimestamp() {
		return new Date().getTime();
	}

	@Override
	public BitPackets getBitPackets(int granularity) {
		switch(granularity) {
		case 30:
			return xps30;
		case 60:
			return xps60;
		case 500:
			return xps500;
		}
		return null;
	}


	public int getFirstSeen() {
		return firstSeen;
	}

	public int getLastSeen() {
		return lastSeen;
	}

	/**
	 * 流持续时间
	 * 单位：秒
	 */
	public int getDuration() {
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

	public int getSrcMasklen() {
		return srcMasklen;
	}

	public int getDstMasklen() {
		return dstMasklen;
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
}
