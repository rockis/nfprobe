package hdprobe.utils.expr;


import hdprobe.utils.KVGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ExpressionContext {
	
	
	private class KVExprFuncName {
		
		private Class<?> clazz;
		private String funcName;
		public KVExprFuncName(String funcName, Class<?> clazz) {
			this.clazz = clazz;
			this.funcName= funcName;
			assert funcName != null;
		}
		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(clazz.hashCode()).append(funcName.hashCode()).toHashCode();
		}
		@Override
		public boolean equals(Object obj) {
			if (obj == null || obj instanceof KVExprFuncName == false) {
				return false;
			}
			KVExprFuncName n = (KVExprFuncName)obj;
			return new EqualsBuilder().append(n.funcName, funcName).append(n.clazz, clazz).isEquals();
		}
	}
	
	private Map<KVExprFuncName, ExprFunction> funcMap = new HashMap<KVExprFuncName, ExprFunction>();
	
	private Map<String, ExprVariable<?>> varMap = new HashMap<String, ExprVariable<?>>();
	
	private List<Class<? extends ExprFunction>> funcClasses = new ArrayList<Class<? extends ExprFunction>>();
	private List<Class<? extends ExprVariable<?>>> varClasses = new ArrayList<Class<? extends ExprVariable<?>>>();
	
	private static ExpressionContext instance;
	
	public ExpressionContext() {
		funcClasses.add(FuncDateFormat.class);
		funcClasses.add(FuncDefault.class);
		
		varClasses.add(VarDate.class);
		varClasses.add(VarTimestamp.class);
	}
	
	public void build() {
		try {
			for (Class<? extends ExprFunction> clazz : funcClasses) {
				ExprFunction func = clazz.newInstance();
				String funcName = func.getName();
				
				Class<?>[] forClasses = func.forClasses();
				for (int i = 0; i < forClasses.length; i++) {
					funcMap.put(new KVExprFuncName(funcName, forClasses[i]), func);
				}
			}
			
			for (Class<? extends ExprVariable<?>> clazz : varClasses) {
				ExprVariable<?> var = clazz.newInstance();
				String varName = var.getName();
				varMap.put(varName, var);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e.getMessage());
		} 
	}
	
	public void setFunctions(List<ExprFunction> funcs) {
		for (ExprFunction func : funcs) {
			String funcName = func.getName();
			Class<?>[] forClasses = func.forClasses();
			for (int i = 0; i < forClasses.length; i++) {
				funcMap.put(new KVExprFuncName(funcName, forClasses[i]), func);
			}
		}
	}
	
	public void setVariables(List<ExprVariable<?>> vars) {
		for (ExprVariable<?> var : vars) {
			String varName = var.getName();
			varMap.put(varName, var);
		}
	}
	
	public static ExpressionContext getContext() {
		if (instance == null) {
			instance = new ExpressionContext();
			instance.build();
		}
		return instance;
	}
	
	public Expression parseExpression(String expression) {
		return new Expression(expression, this);
	}
	
	public String parse(String expression) {
		Expression expr = new Expression(expression, this);
		return expr.parse();
	}
	
	public String parse(String expression, KVGroup kvGroup) {
		Expression expr = new Expression(expression, this);
		return expr.parse(kvGroup);
	}
	
	public ExprFunction getFunc(String funcName, Class<?> objClass) {
		ExprFunction func = funcMap.get(new KVExprFuncName(funcName, objClass));
		if (func == null) {
			throw new IllegalArgumentException("No such function " + funcName);
		}
		return func;
	}
	
	public Object getVariable(String varName) {
		ExprVariable<?> func = varMap.get(varName);
		if (func == null) {
			throw new IllegalArgumentException("No such variable " + varName);
		}
		return func.call();
	}
	
	public String getValue(Object obj, String funcName) {
		return getFunc(funcName, obj.getClass()).call(obj, null);
	}
	
	public String getValue(Object obj, String funcName, Object funcParam) {
		return getFunc(funcName, obj == null ? Object.class : obj.getClass()).call(obj, funcParam);
	}
}
