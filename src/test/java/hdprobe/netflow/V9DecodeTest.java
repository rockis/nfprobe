package hdprobe.netflow;

import static org.junit.Assert.*;

import hdprobe.FlowPDU;
import hdprobe.Router;
import hdprobe.utils.IoBufferReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlContext;

import org.apache.mina.core.buffer.IoBuffer;
import org.junit.Test;

public class V9DecodeTest {

		
	@Test
	public void test() throws Exception {
		FileInputStream fis = new FileInputStream(new File(V9DecodeTest.class.getClassLoader().getResource("v9.pcap").getFile()));
		long size = fis.getChannel().size();
		byte[] buffer = new byte[(int)size - 82];
		fis.skip(82);
		fis.read(buffer);
		fis.close();
		
		IoBuffer buff = IoBuffer.allocate(buffer.length);
		buff.put(buffer);
		IoBufferReader reader = new IoBufferReader(buff);
		V9Packet p = (V9Packet)NFPacket.decode(reader, new Router("localhost", 1000));
		assertEquals(p.getVersion(), 9);
		assertEquals(p.getPduCount(), 26);
		assertEquals(p.getEngineId(), 17);
		int index = 0;
		
		for (Iterator<? extends FlowPDU> pduiter = p.pdu(); pduiter.hasNext();) {
			FlowPDU pdu = pduiter.next();
			if (pdu == null) {
				continue;
			}
			if (index == 0) {
				assertEquals(pdu.getSrcAddr(), "50.50.191.239");
				assertEquals(pdu.getDstAddr(), "211.1.142.235");
				assertEquals(pdu.getOutput(), 32);
				assertEquals(pdu.getInput(), 45);
				
				assertEquals(pdu.getNexthop(), "219.158.1.95");
				
				assertEquals(pdu.getPackets(), 1 * 1000);
				assertEquals(pdu.getBytes(), 64 * 1000);
				assertEquals(pdu.getSrcPort(), 9461);
				assertEquals(pdu.getDstPort(), 80);
				assertEquals(pdu.getProtocol(), 6);
				assertEquals(pdu.getTcpFlags(), 0x02);
				assertEquals(pdu.getTos(), 0x00);
			}
			if (index == 1) {
				assertEquals(pdu.getSrcAddr(), "50.50.191.240");
				assertEquals(pdu.getDstAddr(), "211.1.142.236");
				assertEquals(pdu.getInput(), 45);
				assertEquals(pdu.getOutput(), 32);
				
				assertEquals(pdu.getNexthop(), "219.158.1.95");
				
				assertEquals(pdu.getPackets(), 1 * 1000);
				assertEquals(pdu.getBytes(), 64 * 1000);
				assertEquals(pdu.getSrcPort(), 9461);
				assertEquals(pdu.getDstPort(), 80);
				assertEquals(pdu.getProtocol(), 6);
				assertEquals(pdu.getTcpFlags(), 0x02);
				assertEquals(pdu.getTos(), 0x00);
			}
			index++;
			if (index == 2) {
				break;
			}
			
		}
	}

	@Test
	public void testNoTpl() throws Exception {
		FileInputStream fis = new FileInputStream(new File(V9DecodeTest.class.getClassLoader().getResource("v9notpl.pcap").getFile()));
		long size = fis.getChannel().size();
		byte[] buffer = new byte[(int)size - 82];
		fis.skip(82);
		fis.read(buffer);
		fis.close();
		
		IoBuffer buff = IoBuffer.allocate(buffer.length);
		buff.put(buffer);
		IoBufferReader reader = new IoBufferReader(buff);
		V9Packet p = (V9Packet)NFPacket.decode(reader, new Router("localhost", 1000));
		assertEquals(p.getVersion(), 9);
		assertEquals(p.getPduCount(), 26);
		assertEquals(p.getEngineId(), 17);
		for (Iterator<? extends FlowPDU> pduiter = p.pdu(); pduiter.hasNext();) {
			FlowPDU pdu = pduiter.next();
			if (pdu == null) {
				continue;
			}
		}
	}
}
