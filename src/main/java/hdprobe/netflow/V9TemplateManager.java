package hdprobe.netflow;

import java.util.HashMap;
import java.util.Map;

public class V9TemplateManager {

	private static Map<String, V9Template> templates = new HashMap<String, V9Template>();
	
	public static void setTemplate(V9Template template) {
		String key = template.getRouterAddr() + "|" + template.getEngineId() + "|" + template.getTemplateId();
		templates.put(key, template);
	}
	
	public static V9Template getTemplate(String routerAddr, int engineId, int templateId) {
		String key = routerAddr + "|" + engineId + "|" + templateId;
		return templates.get(key);
	}
}