package hdprobe.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class KVGroup {

	private Map<String, KVEntry> entries = new HashMap<String, KVEntry>();
	private List<String> entryNames = new ArrayList<String>();
	
	public KVGroup() {
		
	}
	
	public KVGroup(KVGroup... kvGroups) {
		for (int i = 0; i < kvGroups.length; i++) {
			merge(kvGroups[i]);
		}
	}
	
	public void addEntry(KVEntry entry) {
		entries.put(entry.getKey(), entry);
//		if (entry.getAlias() != null) {
//			System.out.println(entry.getKey() + "=" + entry.getValue());
//			entries.put(entry.getAlias(), entry);
//		}
		entryNames.add(entry.getKey());
	}
	
	public KVEntry[] getEntries() {
		return entries.values().toArray(new KVEntry[0]);
	}
	
	public KVEntry getEntry(String name) {
		KVEntry e = entries.get(name);
		if (e == null) {
			for(Iterator<Map.Entry<String, KVEntry>> iter = entries.entrySet().iterator(); iter.hasNext();) {
				System.out.println(iter.next().getKey());
			}
			throw new IllegalArgumentException("No such entry: " + name);
		}
		return e;
	}
	
	public boolean hasEntry(String name) {
		return entries.containsKey(name);
	}
	
	public KVGroup copy() {
		return new KVGroup(this);
	}
	
	public void merge(KVGroup otherGroup) {
		for (KVEntry e : otherGroup.getEntries()) {
			entries.put(e.getKey(), e);
			if (e.getAlias() != null) {
				entries.put(e.getAlias(), e);
			}
			entryNames.add(e.getKey());
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		for (String name : entryNames) {
			hcb.append(entries.get(name).hashCode());
		}
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj instanceof KVGroup == false) {
			return false;
		}
		KVGroup g = (KVGroup)obj;
		if (g.entryNames.size() != this.entryNames.size()) {
			return false;
		}
		for (int i = 0; i < g.entryNames.size(); i++) {
			if (!g.entries.get(g.entryNames.get(i)).equals(this.entries.get(this.entryNames.get(i)))) {
				return false;
			}
		}
		return true;
	}
}
