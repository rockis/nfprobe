package hdprobe.impl.server;

import hdprobe.FlowPDU;
import hdprobe.FlowPacket;
import hdprobe.Router;
import hdprobe.RouterConfig;
import hdprobe.aggregation.AggregationManager;
import hdprobe.netflow.NFPacket;
import hdprobe.utils.IoBufferReader;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Iterator;


import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Class the extends IoHandlerAdapter in order to properly handle connections
 * and the data the connections send
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class Handler extends IoHandlerAdapter {

	private Test server;
	
	private AggregationManager manager;
	
	private RouterConfig routerConfig;
	public Handler(Test server) {
		this.server = server;
		ApplicationContext ctx = new ClassPathXmlApplicationContext("hdprobe.xml");
		manager = ctx.getBean("hdprobe.aggregation.manager", AggregationManager.class);
		routerConfig = ctx.getBean("hdprobe.routerconfig", RouterConfig.class);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		session.close(true);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if (message instanceof IoBuffer) {
			InetSocketAddress routerAddr = (InetSocketAddress)session.getRemoteAddress();
			IoBuffer buffer = (IoBuffer) message;
			Router router = routerConfig.getRouter("10.24.103.15");
			if (router == null) {
				return;
			}
			FlowPacket packet = router.getFlowDecorder().decode(buffer);
			for (Iterator<? extends FlowPDU> iter = packet.pdu(); iter.hasNext();) {
				FlowPDU pdu = iter.next();
				manager.aggregate(pdu);
			}
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("Session closed...");
		SocketAddress remoteAddress = session.getRemoteAddress();
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("Session created...");
		SocketAddress remoteAddress = session.getRemoteAddress();
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		System.out.println("Session idle...");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("Session Opened...");
	}

}