package hdprobe.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import hdprobe.FlowPacket;
import hdprobe.Router;
import hdprobe.RouterConfig;

public class FileRouterConfig implements RouterConfig {

	private Map<String, Router> routers = new ConcurrentHashMap<String, Router>();
	
	public void init() {
		routers.put("192.168.1.14", new Router("192.168.1.14", 1, FlowPacket.NETFLOW));
		routers.put("192.168.1.13", new Router("192.168.1.13", 1, FlowPacket.NETFLOW));
		routers.put("10.28.18.16", new Router("10.28.18.16", 1, FlowPacket.NETFLOW));
		routers.put("10.24.103.15", new Router("10.24.103.15", 1, FlowPacket.NETFLOW));
		
	}
	
	@Override
	public Router getRouter(String routerAddr) {
		return routers.get(routerAddr);
	}
}
