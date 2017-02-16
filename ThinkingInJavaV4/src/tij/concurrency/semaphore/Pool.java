package tij.concurrency.semaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Pool<T> {
	private int size;
	private List<T> items = new ArrayList<T>();
	private boolean[] checkedOut;
	private Semaphore available;
	
	public Pool(Class<T> clazz, int size) {
		this.size = size;
		checkedOut = new boolean[size];
		available = new Semaphore(size, true);
		
		for (int i = 0; i < size; i++) {
			try {
				items.add(clazz.newInstance());
			}
			catch (InstantiationException e) {
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public T checkOut() throws InterruptedException	 {
		// acquire() should NOT be called in getItem(). Calling it inside getItem() may cause dead locking.
		available.acquire(); 
		return getItem(); 
	}
	
	public void checkIn(T item) {
		if (releaseItem(item)) {
			available.release();
		}
	}
	
	public synchronized T getItem() {
		for (int i = 0; i < size; i++) {
			if (!checkedOut[i]) {
				checkedOut[i] = true;
				return items.get(i);
			}
		}
		return null;
	}
	
	public synchronized boolean releaseItem(T item) {
		for (int i = 0; i < size; i++) {
			if (items.get(i).equals(item) && checkedOut[i]) {
				checkedOut[i] = false;
				return true;
			}
		}
		
		return false;
	}
	
}
