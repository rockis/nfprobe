package hdprobe.impl.server;


import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

public class Test {

	static int PORT = 9995;
	
	private void run() throws IOException {
		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
		acceptor.setHandler(new Handler(this));

		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		chain.addLast("logger", new LoggingFilter());

		DatagramSessionConfig dcfg = acceptor.getSessionConfig();
		dcfg.setReuseAddress(true);
		
		acceptor.bind(new InetSocketAddress(PORT));
		System.out.println("UDPServer listening on port " + PORT);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new Test().run();
	}

}
