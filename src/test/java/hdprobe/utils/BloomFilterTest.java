package hdprobe.utils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Date;
import java.util.HashMap;

import hdprobe.netflow.BloomTable;

import org.junit.Test;

public class BloomFilterTest {

	@Test
	public void test() {
		BloomTable<String, String> btable = new BloomTable<>(3000);
		HashMap<String, String> htable = new HashMap<String, String>();
		
		int ipint = IPUtils.ip4toint("192.168.1.1");
		int ipcount = 50000;
		for(int i = 0; i < ipcount; i++) {
			btable.put(IPUtils.int2ip(ipint + i), "" + i);
			htable.put(IPUtils.int2ip(ipint + i), "" + i);
		}
		
		long now = new Date().getTime();
		int times = 1000000;
		for (int i = 0; i < times; i++) {
			btable.get("192.168.5.5");
		}
		System.out.printf("btable use %d \n", new Date().getTime() - now);
		
		now = new Date().getTime();
		for (int i = 0; i < times; i++) {
			htable.get("192.168.5.6");
		}
		System.out.printf("htable use %d \n", new Date().getTime() - now);
		
		InetAddress address;
	}

}
