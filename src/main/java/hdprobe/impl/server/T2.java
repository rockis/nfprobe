package hdprobe.impl.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class T2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Integer> vm = new HashMap<String, Integer>();
		vm.put("a", 1);
		vm.put("b", 11);
		vm.put("c", 87);
		vm.put("d", 4);
		vm.put("e", 3);
		vm.put("f", 9);
		List<Map.Entry<String, Integer>> ls = new ArrayList<Map.Entry<String, Integer>>(vm.entrySet());
		Collections.sort(ls, new Comparator<Map.Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
			
		});
		for (Iterator<Map.Entry<String, Integer>> iterator = ls.iterator(); iterator.hasNext();) {
			Map.Entry<String, Integer> type = iterator.next();
			System.out.println(type.getKey() + "=" + type.getValue());
		}
	}

}
