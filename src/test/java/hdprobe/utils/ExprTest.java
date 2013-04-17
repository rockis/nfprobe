package hdprobe.utils;

import static org.junit.Assert.*;

import hdprobe.utils.expr.Expression;
import hdprobe.utils.expr.ExpressionContext;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class ExprTest {

	@Test
	public void test() {
		KVGroup g = new KVGroup();
		g.addEntry(new KVEntry("bps", 111));
		g.addEntry(new KVEntry("pps", 55555));
		g.addEntry(new KVEntry("val", 6666));
		g.addEntry(new KVEntry("group", null));
		
		ExpressionContext context = ExpressionContext.getContext();
		Expression expr = new Expression("${bps}|${pps}|${val}|${group?default('--')}", context);
		String ex = expr.parse(g);
		assertEquals(ex, "111|55555|6666|--");
		
	}
	
	@Test
	public void test2() {
		ExpressionContext context = ExpressionContext.getContext();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Expression expr2 = new Expression("${__DATE__?format('yyyy-MM-dd HH:mm:ss')}", context);
		assertEquals(sdf.format(new Date()), expr2.parse());
		
	}

}
