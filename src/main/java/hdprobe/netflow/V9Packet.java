package hdprobe.netflow;

import hdprobe.FlowPDU;
import hdprobe.Router;
import hdprobe.utils.IoBufferReader;

import java.util.Iterator;
import java.util.List;


/**
 * FLOW HEADER FORMAT
 *
 * Bytes	Contents	Description
 * 0-1 		| version 	
 * 2-3 		| count		
 * 4-7 		| SysUptime
 * 8-11     | unix_secs
 * 12-15	| PackageSequence
 * 16-19 	| Source ID
 */
public final class V9Packet extends NFPacket  {

	public static final int HEADER_LEN = 24;
	
	private int pduCount = 0;
	
	private int sysUptime;
	private int currentSecs;
	private int flowSequence;
	private int sourceId;
	private List<V9PDU> pdulist;
	
	public V9Packet(IoBufferReader buffer, Router router) {
		super(buffer, router);
		this.pduCount = buffer.nextShort(); //2-3
		this.sysUptime = buffer.nextInt();  //4-7
		this.currentSecs = buffer.nextInt(); //8-11
		this.flowSequence = buffer.nextInt(); //12-15
		this.sourceId = buffer.nextInt();     //16-19
		this.pdulist = V9FlowSet.parseFlows(this, buffer.next());
	}
	
	public int getPduCount() {
		return pduCount;
	}
	
	public int getSysUptime() {
		return sysUptime;
	}

	public int getCurrentSecs() {
		return currentSecs;
	}

	public int getFlowSequence() {
		return flowSequence;
	}

	public int getEngineId() {
		return sourceId;
	}

	public Iterator<? extends FlowPDU> pdu() {
		return pdulist.iterator();
	}
}

