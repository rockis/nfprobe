package hdprobe.netflow;

import java.util.Date;

import hdprobe.AbstractFlowPDU;
import hdprobe.Router;
import hdprobe.utils.IoBufferReader;

/**
 * 
 * Bytes	Contents	Description
 * 0-1 		| version 	
 * 2-3 		| count		
 * 4-7 		| SysUptime
 * 8-11     | unix_secs
 * 12-15	| PackageSequence
 * 16-19 	| Source ID
 * 20 		| others
 */
public final class V9PDU extends NFPDU{

	private V9Packet nfpacket;
	
	private BitPackets xps30;
	private BitPackets xps60;
	private BitPackets xps500;
	
	private int duration;
	
	private V9FlowSet.Flow valueset;
	
	public V9PDU(V9Packet nfpacket, V9Template template, IoBufferReader buffer) {
		this.valueset = template.decodePDU(buffer);
		
		int samplingRate = nfpacket.getRouter().getDefaultSamplingRate();
		if (valueset.getSamplingRate() != 0) {
			samplingRate = valueset.getSamplingRate();
		}
		long packets = valueset.getPackets() * samplingRate;
		long bytes   = valueset.getBytes() * samplingRate;
		this.duration= (valueset.getLastSeen() - valueset.getFirstSeen()) / 1000;
		
		this.xps30 = new BitPackets(packets, bytes, duration, 30);
		this.xps60 = new BitPackets(packets, bytes, duration, 60);
		this.xps30 = new BitPackets(packets, bytes, duration, 500);
	}
	
	public Router getRouter() {
		return nfpacket.getRouter();
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
	/**
	 * 流持续时间
	 * 单位：秒
	 */
	public int getDuration() {
		return duration;
	}

	public int getInput() {
		return valueset.getInput();
	}

	public int getOutput() {
		return valueset.getOutput();
	}

	public int getFirstSeen() {
		return valueset.getFirstSeen();
	}

	public int getLastSeen() {
		return valueset.getLastSeen();
	}

	public int getSrcPort() {
		return valueset.getSrcPort();
	}

	public int getDstPort() {
		return valueset.getDstPort();
	}

	public int getTcpFlags() {
		return valueset.getTcpFlags();
	}

	public int getProtocol() {
		return valueset.getProtocol();
	}

	public int getTos() {
		return valueset.getTos();
	}

	public int getSrcAS() {
		return valueset.getSrcAS();
	}

	public int getDstAS() {
		return valueset.getDstAS();
	}

	public String getSrcAddr() {
		return valueset.getSrcAddr();
	}

	public String getDstAddr() {
		return valueset.getDstAddr();
	}

	public String getNexthop() {
		return valueset.getNexthop();
	}

	public int getSrcMasklen() {
		return valueset.getSrcMasklen();
	}

	public int getDstMasklen() {
		return valueset.getDstMasklen();
	}

	public int getSamplingRate() {
		return valueset.getSamplingRate();
	}

	
	
}
