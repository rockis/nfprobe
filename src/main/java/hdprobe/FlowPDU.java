package hdprobe;

public interface FlowPDU {
	
	public Router getRouter();
	
	public String getSrcAddr();

	public int getSrcPort();

	public String getDstAddr();

	public String getNexthop();

	public int getDstPort();

	public int getProtocol();

	public int getTos();
	
	public int getTcpFlags();

	public int getSrcAS();

	public int getDstAS();

	public int getInput() ;

	public int getOutput();

	public int getSrcMasklen();

	public int getDstMasklen();
	
	public int getDuration();

	public long getTimestamp();
	
	public BitPackets getBitPackets(int granularity);
	
	public static class BitPackets {
		public final long bytes;
		public final long packets;
		public final int granularity;
		public BitPackets(long packets, long bytes, double duration, int granularity) {
			this.granularity = granularity;
			if (duration > granularity) {
				double ratio = granularity / duration;
				this.packets = (int)(packets * ratio);
				this.bytes = (int)(bytes * ratio);
			}else{
				this.packets = packets;
				this.bytes = bytes;
			}
		}
	}
	
	public void setProperty(String propertyName, Object property);
	public Object getProperty(String propertyName);
}

