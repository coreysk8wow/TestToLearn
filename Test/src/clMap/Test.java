package clMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import clMap.CLSimpleMap.Pair;

public class Test {

	private void test01() {
		ArrayList arrayList = new ArrayList();
		List list = arrayList;
		
		if (arrayList == list) {
			System.out.println("yes, ==");
		}
		
		if (arrayList.equals(list)) {
			System.out.println("yes, equals()");
		}
	}
	
	private void test02() {
		Map map = new HashMap();
		if (map.entrySet() != null) {
			System.out.println("yes");
		}
	}
	
	private void testCLSimpleMap() {
		CLSimpleMap clsMap = new CLSimpleMap();
		clsMap.put("1", "abc");
		clsMap.put("2", "cde");
		
		Map aMap = new LinkedHashMap();
		aMap.put("1", "Mouse");
		aMap.put("2", "Cat");
		clsMap.put("HashMap", aMap);
		
		
		// Traverse the CLMap one by one
		Object obj01 = clsMap.get("1");
		Object obj02 = clsMap.get("2");
		Object obj03 = clsMap.get("HashMap");

		System.out.println((String) obj01);
		System.out.println((String) obj02);
		
		Map map = (Map) obj03;
		Iterator keySetItr = map.keySet().iterator();
		while (keySetItr.hasNext()) {
			Object key = keySetItr.next();
			System.out.println("[" + (String) key + ", " + (String) map.get(key) + "]");
		}
		
		
		System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++\n");
		// Traverse the CLMap using keySet()
		Set keySet = clsMap.keySet();
		for (Object obj : keySet) {
			
			System.out.println((String) obj + ", " + clsMap.get(obj).toString());
		}
		
		
		System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++\n");
		// Traverse the CLMap using pairSet()
		Set<Pair> pairSet = clsMap.pairSet();
		for (Pair pair : pairSet) {
			System.out.println((String) pair.getKey() + ", " + pair.getValue());
		}
		
	}
	
	
	public static void main(String[] args) {
		Test testCLSimpleMap = new Test();
		testCLSimpleMap.testCLSimpleMap();
		
/*		Test test02 = new Test();
		test02.test02();*/
		
	}

}
