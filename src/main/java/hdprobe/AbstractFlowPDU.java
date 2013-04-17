package hdprobe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.WrapDynaBean;

public abstract class AbstractFlowPDU implements FlowPDU {

	private Map<String, Object> extraProperties = new ConcurrentHashMap<String, Object>();

	private WrapDynaBean properties = new WrapDynaBean(this);
	
	@Override
	public void setProperty(String propertyName, Object property) {
		extraProperties.put(propertyName, property);
	}

	@Override
	public Object getProperty(String propertyName) {
		if (extraProperties.containsKey(propertyName)) {
			return extraProperties.get(propertyName);
		}
		return properties.get(propertyName);
	}

}
