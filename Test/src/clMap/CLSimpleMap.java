package clMap;

import java.util.HashSet;
import java.util.Set;

public class CLSimpleMap implements CLMap {

//	protected Set keys = new HashSet();
//	protected  List values = new LinkedList();
	private Set<Pair> pairs = new HashSet<Pair>();
	
	protected class Pair {
		private Object key;
		private Object value;
		
		protected Pair(Object key, Object value) {
			super();
			this.key = key;
			this.value = value;
		}
		
		public Object getKey() {
			return key;
		}
		public void setKey(Object key) {
			this.key = key;
		}

		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
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
			Pair other = (Pair) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

		private CLSimpleMap getOuterType() {
			return CLSimpleMap.this;
		}
	}

	@Override
	public void put(Object key, Object value) {
		Pair pair = getPair(key);
		if (pair != null) {
			pair.value = value;
		} else {
			pairs.add(new Pair(key, value));
		}
	}

	@Override
	public Object get(Object key) {
		Pair pair = getPair(key);
		return pair != null ? pair.value : null;
	}
	
	private Pair getPair(Object key) {
		for(Pair pair : pairs) {
			if (pair.key.equals(key)) {
				return pair;
			}
		}
		
		return null;
	}

	@Override
	public long size() {
		return pairs.size();
	}

	@Override
	public Set keySet() {
		if (pairs.size() == 0) {
			return null;
		}
		
		Set keySet = new HashSet();
		for (Pair pair : pairs) {
			keySet.add(pair.key);
		}
		
		return keySet;
	}

	@Override
	public Set<Pair> pairSet() {
		return pairs;
	}
	
	public static void main(String[] args) {
		CLSimpleMap.Pair pair1 = new CLSimpleMap().new Pair("1", "a");
		CLSimpleMap.Pair pair2 = new CLSimpleMap().new Pair("1", "b");
		if (!pair1.equals(pair2)) {
			System.out.println("yes");
		}
		
		
	}
	
}
