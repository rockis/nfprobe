package hdprobe.utils.expr;

import java.util.Date;


public class VarTimestamp implements ExprVariable<Integer> {

	@Override
	public String getName() {
		return "__TIMESTAMP__";
	}

	@Override
	public Integer call() {
		return (int)(new Date().getTime() / 1000);
	}
}
