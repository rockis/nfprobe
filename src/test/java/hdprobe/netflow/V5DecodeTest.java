package hdprobe.netflow;

import static org.junit.Assert.*;

import hdprobe.FlowDecoder;
import hdprobe.FlowPDU;
import hdprobe.FlowPacket;
import hdprobe.Router;
import hdprobe.utils.IoBufferReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.mina.core.buffer.IoBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class V5DecodeTest {

	private byte[] buffer;
	@Before
	public void setUp() throws Exception {
		InputStream pcapfile = V5DecodeTest.class.getClassLoader().getResourceAsStream("v5.pcap");
		pcapfile.skip(82);
		buffer = new byte[1506];
		pcapfile.read(buffer);
		pcapfile.close();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testPerf() { 
		int count = 1000;
		Date start = new Date();
		for(int i = 0; i < count; i++) {
			test();
		}
		System.out.println(count / (new Date().getTime() - start.getTime()));
	}

	@Test
	public void test() {
		IoBuffer buff = IoBuffer.allocate(buffer.length);
		buff.put(buffer);
		Router router = new Router("192.168.1.1", 1, FlowPacket.NETFLOW);
		FlowDecoder fd = router.getFlowDecorder();
		V5Packet p = (V5Packet)fd.decode(buff);
		assertEquals(p.getVersion(), 5);
		assertEquals(p.getPduCount(), 30);
		for (Iterator<? extends FlowPDU> pduiter = p.pdu(); pduiter.hasNext();) {
			FlowPDU pdu = (V5PDU)pduiter.next();
			System.out.println(pdu.getSrcAddr());
			System.out.println(pdu.getDstAddr());
			System.out.println(pdu.getOutput());
			System.out.println(pdu.getInput());
			assertEquals(pdu.getSrcAddr(), "3.3.3.3");
			assertEquals(pdu.getDstAddr(), "1.1.1.1");
			assertEquals(pdu.getNexthop(), "5.5.5.5");
			assertEquals(pdu.getOutput(), 44);
			assertEquals(pdu.getInput(), 33);
			assertEquals(pdu.getSrcPort(), 44);
			assertEquals(pdu.getDstPort(), 33);
			assertEquals(pdu.getProtocol(), 6);
			assertEquals(pdu.getTcpFlags(), 0x02);
			assertEquals(pdu.getTos(), 0x80);
		}
		
	}

}
