package hdprobe.utils.expr;

public interface ExprVariable<T> {

	public String getName();
	
	public T call();
	
}
