package clMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import clMap.CLSimpleMap.Pair;

public class Test02 {

	private void test01() {
		Map map = new HashMap();
		map.put("user", "admin");
		map.put("gender", "female");
		map.put("age", "25");
		
		//this is for keySet testing
		Set set = map.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			System.out.println(key + ":" + map.get(key));
		}
		
		//this is for entrySet testing
		Set eSet = map.entrySet();
		Iterator is = eSet.iterator();
		while(is.hasNext()){
			Map.Entry entry = (Entry) is.next();
			System.out.println(entry.getKey()+" : " + entry.getValue());
		}
		
		System.out.println(map);
		
	}
	
	private static void test02() {
		CLMap map = new CLSimpleMap();
		map.put("user", "admin");
		map.put("gender", "female");
		map.put("age", "25");
		map.put("age", "77");
		
		System.out.println(map.size());
		
		//this is for keySet testing
		Iterator keySetItr = map.keySet().iterator();
		while(keySetItr.hasNext()) {
			Object key = keySetItr.next();
			System.out.println(key.toString() + ":" + map.get(key));
		}
		
		//this is for pairSet testing
		Iterator pairSetItr = map.pairSet().iterator();
		while(pairSetItr.hasNext()){
			Pair pr = (Pair) pairSetItr.next();
			System.out.println(pr.getKey() + ":" + pr.getValue());
		}
		
		
		CLSimpleMap.Pair pair1 = new CLSimpleMap().new Pair("1", "a");
		CLSimpleMap.Pair pair2 = new CLSimpleMap().new Pair("1", "b");
		if (pair1.equals(pair2)) {
			System.out.println("yes");
		}
		
		
	}
	
	public static void main(String[] args) {
		Test02.test02();
		
	}

}
