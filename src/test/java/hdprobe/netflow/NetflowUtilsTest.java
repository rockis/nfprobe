package hdprobe.netflow;

import static org.junit.Assert.*;

import java.util.Date;

import hdprobe.utils.IPUtils;

import org.junit.Test;

public class NetflowUtilsTest {


	@Test
	public void testint2ip() throws Exception {
		String ip = "192.168.1.1";
		int times = 1000000;
		long now = new Date().getTime();
		for (int i = 0; i < times; i++) {
			IPUtils.ip4toint(ip);
		}
		System.out.printf("int2ip:%d\n", times / (new Date().getTime() - now));
		String b = IPUtils.int2ip(IPUtils.ip4toint(ip));
		assertEquals(b, ip);
	}

	@Test
	public void testint2ip4() throws Exception {
		int[] ipint = IPUtils.iptoint("192.168.1.1");
		int times = 1000000;
		long now = new Date().getTime();
		for (int i = 0; i < times; i++) {
			String b = IPUtils.int2ip(ipint);
		}
		System.out.printf("int2ip2:%d\n", times / (new Date().getTime() - now));
		assertEquals(IPUtils.int2ip(ipint), "192.168.1.1");
	}
	
	@Test
	public void testint2ip6() throws Exception {
		int[] ipint = IPUtils.ip6toint("2001::98");
		long now = new Date().getTime();
		int times = 1000000;
		for (int i = 0; i < times; i++) {
			String b = IPUtils.int2ip(ipint);
		}
		System.out.printf("int2ip6:%d\n", times / (new Date().getTime() - now));
		assertEquals(IPUtils.int2ip(ipint), "2001:0:0:0:0:0:0:98");
	}

}
