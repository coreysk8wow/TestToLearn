package testGc;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class TestCollectionGc {

	public void testMap() throws InterruptedException {
		Map map = new HashMap();
//		Map map = new TreeMap();
		
		for (int i = 1; i <= 3; i++) {
			map.put(new Integer(1), new Element(i));
		}
		for (int i = 10; i <=13; i++) {
			map.put(new Integer(3), new Element(i));
		}
		map.put(new Integer(22), new Element(22));
		map.put(new Integer(23), new Element(23));
//		Object obj = map.get(1);
//		map = null;
		
		System.gc();
		TimeUnit.SECONDS.sleep(5);
		System.out.println("map size: " + map.size());
		System.out.println(((Element)map.get(1)).i);
	}
	
	public void testList() throws InterruptedException {
//		List list = new ArrayList();
		List list = new LinkedList();
		for (int i = 0; i < 10; i++) {
			list.add(new Element(i));
		}
		
		System.gc();
		TimeUnit.SECONDS.sleep(5);
		
		System.out.println("arraylist size: " + list.size());
	}
	
	
	
	public static void main(String[] args) throws InterruptedException {
		new TestCollectionGc().testMap();
//		new TestCollectionGc().testList();
	}

}

class Element {
	public int i = 0;
	
	public Element(int i) {
		this.i = i;
	}
	
	protected void finalize() {
		System.out.println("GC Element i = " + i);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + i;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Element other = (Element) obj;
		if (i != other.i)
			return false;
		return true;
	}
}