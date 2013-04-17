package hdprobe.utils.expr;

import java.util.Date;


public class VarDate implements ExprVariable<Date> {

	@Override
	public String getName() {
		return "__DATE__";
	}

	@Override
	public Date call() {
		return new Date();
	}

}
