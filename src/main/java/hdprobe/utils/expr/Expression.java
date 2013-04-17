package hdprobe.utils.expr;

import hdprobe.utils.KVEntry;
import hdprobe.utils.KVGroup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {

	private static final Pattern pattern = Pattern.compile("\\$\\{([\\w_]+)(\\?([\\w_]+)(\\(('?[^\\)]+'?)?\\))?)?\\}");
	
	private ExpressionContext context;
	private Matcher matcher = null;
	
	public Expression(String expression, ExpressionContext context) {
		this.context = context;
		this.matcher = pattern.matcher(expression);
	}
	
	public String parse() {
		StringBuffer result = new StringBuffer();
		String kName     = null;
		String funcName  = null;
		String funcParam = null;
		while(matcher.find()) {
			kName     = matcher.group(1);
			funcName  = matcher.group(3);
			funcParam = matcher.group(5);
			Object var = context.getVariable(kName);
			if (funcName != null && funcParam == null) {
				parseObject(result, matcher, var, funcName, (Object)null);
			}else if(funcName == null && funcParam == null) {
				matcher.appendReplacement(result, var.toString());
			}else {
				parseObject(result,  matcher, var, funcName, parseFuncParam(funcParam));
			}
		}
		matcher.appendTail(result);
		matcher.reset();
		return result.toString();
	}
	
	public String parse(KVGroup kvGroup) {
		StringBuffer result = new StringBuffer();
		String kName     = null;
		String funcName  = null;
		String funcParam = null;
		while(matcher.find()) {
			kName     = matcher.group(1);
			funcName  = matcher.group(3);
			funcParam = matcher.group(5);
			if (funcName != null && funcParam == null) {
				parse(result, matcher, kvGroup, kName, funcName);
			}else if(funcName == null && funcParam == null) {
				parse(result, matcher, kvGroup, kName);
			}else if(isKvEntry(kvGroup, funcParam)) {
				parse(result, matcher, kvGroup, kName, funcName, kvGroup.getEntry(funcParam));
			}else {
				parse(result, matcher, kvGroup, kName, funcName, parseFuncParam(funcParam));
			}
		}
		matcher.appendTail(result);
		matcher.reset();
		return result.toString();
	}
	
	private Object parseFuncParam(String param) {
		param = param.trim();
		if (param.startsWith("'") && param.endsWith("'")) {
			return param.substring(1, param.length() - 1);
		}
		if (param.equals("true") || param.equals("false")) {
			return param.equals("true");
		}
		return Integer.parseInt(param);
	}
	
	private boolean isVariable(String kName) {
		return (kName.startsWith("__") && kName.endsWith("__"));
	}
	
	private boolean isKvEntry(KVGroup group, String value) {
		return group.hasEntry(value);
	}
	
	private void parse(StringBuffer result, Matcher matcher, KVGroup kvGroup, String kName, String funcName, Object funcParam) {
		if (isVariable(kName)) {
			parseObject(result, matcher, context.getVariable(kName), funcName, funcParam);
		}else{
			parseObject(result, matcher, kvGroup.getEntry(kName).getValue(), funcName, funcParam);
		}
	}

	private void parse(StringBuffer result, Matcher matcher, KVGroup kvGroup, String kName, String funcName, KVEntry funcParam) {
		if (isVariable(kName)) {
			parseObject(result, matcher, context.getVariable(kName), funcName, funcParam);
		}else{
			parseObject(result, matcher, kvGroup.getEntry(kName).getValue(), funcName, funcParam);
		}
	}
	
	private void parse(StringBuffer result, Matcher matcher, KVGroup kvGroup, String kName, String funcName) {
		if (isVariable(kName)) {
			parseObject(result, matcher, context.getVariable(kName), funcName, null);
		}else{
			parseObject(result, matcher, kvGroup.getEntry(kName).getValue(), funcName, (Object)null);
		}
	}

	private void parse(StringBuffer result, Matcher matcher, KVGroup kvGroup, String kName) {
		if (isVariable(kName)) {
			matcher.appendReplacement(result, context.getVariable(kName).toString());
		}else{
			matcher.appendReplacement(result, kvGroup.getEntry(kName).getValue().toString());
		}
	}
	
	private void parseObject(StringBuffer result, Matcher matcher, Object obj, String funcName, Object funcParam) {
		matcher.appendReplacement(result, context.getValue(obj, funcName, funcParam));
	}
	
	private void parseObject(StringBuffer result, Matcher matcher, Object obj, String funcName, KVEntry funcParam) {
		matcher.appendReplacement(result, context.getValue(obj, funcName, funcParam.getValue()));
	}
	
}
