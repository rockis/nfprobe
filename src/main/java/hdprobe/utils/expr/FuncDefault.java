package hdprobe.utils.expr;


public class FuncDefault implements ExprFunction {

	@Override
	public String getName() {
		return "default";
	}

	@Override
	public String call(Object param1, Object param2) {
		if (param1 != null) {
			return param1.toString();
		}
		return param2.toString();
	}

	@Override
	public Class<?>[] forClasses() {
		return new Class[]{ Object.class };
	}

}
