package hdprobe.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IPUtils {

	public static String int2ip(int ipint) {
		byte[] addr = new byte[4];
		addr[0] = (byte) ((ipint >> 24) & 0xFF);
		addr[1] = (byte) ((ipint >> 16) & 0xFF);
		addr[2] = (byte) ((ipint >> 8) & 0xFF);
		addr[3] = (byte) ((ipint) & 0xFF);
		try {
			return InetAddress.getByAddress(addr).getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	public static final String int2ip(int[] ipint) {
		if (ipint.length == 4 && ipint[0] == 0 && ipint[1] == 0 && ipint[2] == 0) {
			ipint = new int[]{ ipint[3] };
		}
		byte[] addr = new byte[ipint.length * 4];
		try {
			int addr_index = 0;
			for (int i = 0; i < ipint.length; i++) {
				addr[addr_index++] = (byte) ((ipint[i] >> 24) & 0xFF);
				addr[addr_index++] = (byte) ((ipint[i] >> 16) & 0xFF);
				addr[addr_index++] = (byte) ((ipint[i] >> 8) & 0xFF);
				addr[addr_index++] = (byte) ((ipint[i]) & 0xFF);
			}
			return InetAddress.getByAddress(addr).getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	public static int ip4toint(String ip) {
		try {
			InetAddress address = InetAddress.getByName(ip);// 在给定主机名的情况下确定主机的 IP 址。
			byte[] bytes = address.getAddress();// 返回此 InetAddress 对象的原始 IP 地址
			return bytes2int(bytes);
		} catch (Exception e) {
			return -1;
		}
	}

	public static int byte2int(byte b) {
		int l = b & 0x7F;
		if (b < 0) {
			l |= 0x80;
		}
		return l;
	}
	
	public static int bytes2int(byte[] bytes) {
		return bytes2int(bytes[0], bytes[1], bytes[2], bytes[3]);
	}
	
	public static int bytes2int(byte b0, byte b1, byte b2, byte b3) {
		int a, b, c, d;
		a = byte2int(b0);
		b = byte2int(b1);
		c = byte2int(b2);
		d = byte2int(b3);
		int result = (a << 24) | (b << 16) | (c << 8) | d;
		return result;
	}

	public static int[] ip6toint(String ip) {
		try {
			InetAddress a = InetAddress.getByName(ip);
			byte[] bs = a.getAddress();
			int[] ipint = new int[] {
				bytes2int(bs[0], bs[1], bs[2], bs[3]),
				bytes2int(bs[4], bs[5], bs[6], bs[7]),
				bytes2int(bs[8], bs[9], bs[10], bs[11]),
				bytes2int(bs[12], bs[13], bs[14], bs[15]),
			};
			
			return ipint;
		} catch (UnknownHostException e) {
			return new int[] { 0, 0, 0, 0 };
		}
	}

	public static int[] iptoint(String ip) {
		if (ip.indexOf(':') >= 0) {
			return ip6toint(ip);
		}
		return new int[] { 0, 0, 0, ip4toint(ip) };
	}
	

	public static List<String> unfold(String iprange) {
		if (iprange.indexOf('/') > 0) {
			return unfoldnetwork(iprange);
		}
		return null;
	}
	
	public static List<String> unfoldnetwork(String network)  {
		String[] peer = network.split("/");
		int ipint = ip4toint(peer[0]);
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < (1 << (32 - Integer.parseInt(peer[1]))) - 1 ; i++) {
			result.add(int2ip(ipint + i));
		}
		return result;
	}
	
	public static void main(String[] args) {
		Date now = new Date();
		for (int i = 0; i < 101; i++) {
			unfold("10.10.10.10/16");
		}
		System.out.println(new Date().getTime() - now.getTime());
	}
}
