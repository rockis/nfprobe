package hdprobe.netflow;

import java.util.List;

import hdprobe.utils.IoBufferReader;

public class V9DataFlowSet extends V9FlowSet{

	public static final int HEADER_LEN = 4; // flowsetid(2), length(2)
	
	private V9Packet packet;
	private int templateId;
	
	private int flowcount;
	
	private V9Template template;
	
	public V9DataFlowSet(V9Packet packet, IoBufferReader buffer) {
		super(packet.getRouter().getAddress(), packet.getEngineId(), buffer);
		this.packet = packet;
		this.templateId = flowsetId;
		template = V9TemplateManager.getTemplate(packet
				.getRouter().getAddress(), packet.getEngineId(),
				flowsetId);
	}
	
	public void parse(List<V9PDU> flows) {
		if (template == null) {
			return;
		}
		flowcount = (length - HEADER_LEN) / template.getFieldScopeLen();
		IoBufferReader buffer = this.buffer;
		for (int i = 0; i < flowcount; i++) {
			flows.add(new V9PDU(this.packet, template, buffer));
			buffer = buffer.next();
		}
	}

	public int getTemplateId() {
		return templateId;
	}
	
	public int getFlowcount() {
		return flowcount;
	}
	
	
}
