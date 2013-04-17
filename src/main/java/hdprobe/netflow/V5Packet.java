package hdprobe.netflow;

import hdprobe.FlowPDU;
import hdprobe.Router;
import hdprobe.utils.IoBufferReader;

import java.nio.Buffer;
import java.util.Iterator;

/**
 * FLOW HEADER FORMAT
 *
 * Bytes	Contents	Description
 * 0-1	version	NetFlow export format version number
 * 2-3	count	Number of flows exported in this packet (1-30)
 * 4-7	sys_uptime	Current time in milliseconds since the export device booted
 * 8-11	unix_secs	Current count of seconds since 0000 UTC 1970
 * 12-15	unix_nsecs	Residual nanoseconds since 0000 UTC 1970
 * 16-19	flow_sequence	Sequence counter of total flows seen
 * 20	engine_type	Type of flow-switching engine
 * 21	engine_id	Slot number of the flow-switching engine
 * 22-23	sampling_interval	First two bits hold the sampling mode; remaining 14 bits hold value of sampling interval
 *
 */
public final class V5Packet extends NFPacket  {

	public static final int HEADER_LEN = 24;
	
	private int samplingRate = 1;
	
	private int pduCount = 0;
	
	private int sysUptime;
	private int currentSecs;
	private int currentNSecs;
	private int flowSequence;
	private int engineType;
	private int engineId;
	
	public V5Packet(IoBufferReader buffer, Router router) {
		super(buffer, router);
		this.pduCount = buffer.nextShort();
		this.sysUptime = buffer.nextInt();
		this.currentSecs = buffer.nextInt();
		this.currentNSecs = buffer.nextInt();
		this.flowSequence = buffer.nextInt();
		this.engineType = buffer.nextByte();
		this.engineId = buffer.nextByte();
		int samplingRate = buffer.nextShort();
		this.samplingRate = samplingRate == 0 ? router.getDefaultSamplingRate() :  samplingRate;
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

	public int getCurrentNSecs() {
		return currentNSecs;
	}

	public int getFlowSequence() {
		return flowSequence;
	}

	public int getEngineType() {
		return engineType;
	}

	public int getEngineId() {
		return engineId;
	}

	public final int getSamplingRate() {
		return samplingRate;
	}
	
	public Iterator<? extends FlowPDU> pdu() {
		return new V5PDUIterator(this.buffer);
	}
	
	class V5PDUIterator implements Iterator<FlowPDU>  {
		private int index = 0;
		private IoBufferReader buffer;
		
		private V5PDUIterator(IoBufferReader buffer) {
			this.buffer = buffer;
		}
		
		@Override
		public boolean hasNext() {
			return index < V5Packet.this.pduCount;
		}

		@Override
		public FlowPDU next() {
			index++;
			V5PDU pdu = new V5PDU(V5Packet.this, buffer);
			buffer = buffer.next();
			return pdu;
		}

		@Override
		public void remove() {
		}
	}
}
