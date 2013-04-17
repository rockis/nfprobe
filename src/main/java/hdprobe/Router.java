package hdprobe;


public class Router {

	private String address;
	private int defaultSamplingRate;
	private int flowType;

	public Router(String address, int defaultSamplingRate, int flowType) {
		this.address = address;
		this.defaultSamplingRate = defaultSamplingRate;
		this.flowType = flowType;
	}

	public String getAddress() {
		return address;
	}

	public int getDefaultSamplingRate() {
		return defaultSamplingRate;
	}

	public int getFlowType() {
		return flowType;
	}
	
	public FlowDecoder getFlowDecorder() {
		if (flowType == FlowPacket.NETFLOW) {
			return new NetFlowDecoder(this);
		}
		return null;
	}
	
}
