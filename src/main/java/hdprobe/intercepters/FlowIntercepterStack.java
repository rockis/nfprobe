package hdprobe.intercepters;

import java.util.ArrayList;
import java.util.List;

import hdprobe.FlowPDU;

public class FlowIntercepterStack implements FlowIntercepter {

	private List<FlowIntercepter> intercepters = new ArrayList<FlowIntercepter>();
	
	public void setIntercepters(List<FlowIntercepter> intercepters) {
		this.intercepters = intercepters;
	}
	
	@Override
	public boolean intercept(FlowPDU pdu) {
		for (FlowIntercepter intercepter : intercepters) {
			if (!intercepter.intercept(pdu)) {
				return false;
			}
		}
		return true;
	}
}
