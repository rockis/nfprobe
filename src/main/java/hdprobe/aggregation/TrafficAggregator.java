package hdprobe.aggregation;

import java.util.Date;

import hdprobe.FlowPDU;
import hdprobe.utils.KVEntry;
import hdprobe.utils.KVGroup;


public class TrafficAggregator implements Aggregator {

	private AggregationKey  key;
	private int            granularity;
	private volatile long packets = 0;
	private volatile long bytes = 0;
	private volatile long flows = 0;
	private volatile long lastTime = 0;
	
	private Snapshot        lastCheck = new Snapshot(0, 0, 0, 0, 0);
	
	public TrafficAggregator(AggregationKey key, int granularity) {
		this.key = key;
		this.granularity = granularity;
	}

	public int getGranularity() {
		return granularity;
	}

	public AggregationKey getKey() {
		return key;
	}

	public void aggregate(FlowPDU pdu) {
		if (lastTime == 0) {
			lastTime = pdu.getTimestamp();
		}
		FlowPDU.BitPackets xps = pdu.getBitPackets(granularity);
		if (xps != null) {
			this.packets += xps.packets;
			this.bytes += xps.bytes;
		}
		this.flows++;
	}
	
	public boolean check(KVGroup group) {
		long duration = (new Date().getTime() - lastTime) / 1000;
		long bps = 0;
		long pps = 0;
		if (duration > 0) {
			bps = (bytes << 8) / duration;
			pps = packets / duration;
		}
		
		if (flows == 0) { // 如果统计周期内流量极小，则不统计
			lastCheck = new Snapshot(0, 0, 0, 0, 0);
			if (group != null) {
				group.merge(lastCheck.toExport());
			}
			return false;
		}
		Snapshot sp = new Snapshot(bytes, packets, bps, pps, flows);
		packets = 0;
		bytes = 0;
		flows = 0;
		lastTime = 0;
		lastCheck = sp;
		if (group != null) {
			group.merge(sp.toExport());
		}
		return true;
	}
	
	@Override
	public KVGroup export() {
		return new KVGroup(key.export(), lastCheck.toExport());
	}

	public static class Snapshot {
		public final long bytes;
		public final long packets;
        public final long bps;
        public final long pps;
        public final long flows;

        
        public Snapshot(long bytes, long packets, long bps, long pps, long flows) {
        	this.bytes = bytes;
        	this.packets = packets;
        	this.bps = bps;
        	this.pps = pps;
        	this.flows = flows;
        }
        
        public KVGroup toExport() {
        	KVGroup g = new KVGroup();
        	g.addEntry(new KVEntry("bytes",   bytes));
        	g.addEntry(new KVEntry("packets", packets));
        	g.addEntry(new KVEntry("bps",     bps));
        	g.addEntry(new KVEntry("pps",     pps));
        	g.addEntry(new KVEntry("flows",   flows));
        	return g;
        }
    }
}
