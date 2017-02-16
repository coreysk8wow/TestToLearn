package clMap;

import java.util.Set;

public interface CLMap {
	void put(Object key, Object value);
	
	Object get(Object key);
	
	long size();

	Set keySet();
	
	Set pairSet();
}
