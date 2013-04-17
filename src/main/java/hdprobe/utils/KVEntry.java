package hdprobe.utils;

public class KVEntry {
	
	private String key;
	private Object value;
	private String alias;
	
	
	public KVEntry(String key, Object value) {
		this(key, value, key);
	}
	
	public KVEntry(String key, Object value, String alias) {
		this.key = key;
		this.value = value;
		this.alias = alias;
	}
	
	public String getKey() {
		return key;
	}
	
	public Object getValue() {
		return value;
	}

	public String getAlias() {
		return alias;
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj instanceof KVEntry == false) {
			return false;
		}
		KVEntry bc = (KVEntry)obj;
		return value.equals(bc.value) && key.equals(bc.key);
	}
}