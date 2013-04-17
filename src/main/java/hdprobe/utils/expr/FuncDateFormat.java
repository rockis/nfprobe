package hdprobe.utils.expr;

import java.text.SimpleDateFormat;
import java.util.Date;


public class FuncDateFormat implements ExprFunction{

	@Override
	public String getName() {
		return "format";
	}
	
	@Override
	public String call(Object param, Object format) {
		SimpleDateFormat sdf = new SimpleDateFormat((String)format);
		return sdf.format((Date)param);
	}

	@Override
	public Class<?>[] forClasses() {
		return new Class[]{ Date.class };
	}
	
}
