package hdprobe.utils.expr;

public interface ExprFunction {

	public String getName();
	
	public String call(Object param1, Object param2);
	
	public Class<?>[] forClasses();
	
}
