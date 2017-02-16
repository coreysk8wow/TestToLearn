package tij.concurrency.criticalSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class Pair {
	private int x, y;
	
	Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	Pair() {
		this(0, 0);
	}
	
	int getX() {return x;}
	int getY() {return y;}
	void incrementX() {x++;}
	void incrementY() {y++;}
	
	@Override
	public String toString() {
		return "Pair [x=" + x + ", y=" + y + "]";
	}
	
	class PairValuesNotEqualException extends RuntimeException {
		PairValuesNotEqualException() {
			super("Pair values not equal: " + Pair.this.toString());
		}
	}
	
	void checkState() {
		if (x != y)
			throw new PairValuesNotEqualException();
	}
}

abstract class PairManager {
	AtomicInteger checkCounter = new AtomicInteger(0);
	Pair p = new Pair();
	private List<Pair> storage = Collections.synchronizedList(new ArrayList<Pair>());
	
	synchronized Pair getPair() {
		return new Pair(p.getX(), p.getY()); 
	}
	
	// Assume this is a time consuming operation
	void store(Pair p) {
		storage.add(p);
		try {
			TimeUnit.MILLISECONDS.sleep(50);
		} 
		catch (InterruptedException ignore) {}
	}
	
	abstract void increment();
}

class PairManager1 extends PairManager {
	@Override
	synchronized void increment() {
		p.incrementX();
		p.incrementY();
		store(getPair());
	}
}

class PairManager2 extends PairManager {
	@Override
	void increment() {
		Pair temp;
		synchronized(this) {
			p.incrementX();
			p.incrementY();
			temp = getPair();
		}
		store(temp);
	}
}

class PairManipulator implements Runnable {
	private PairManager pm;
	PairManipulator(PairManager pm) {
		this.pm = pm;
	}
	
	@Override
	public void run() {
		while(true) {
			pm.increment();
		}
	}
	
	@Override
	public String toString() {
		return "Pair: " + pm.getPair() + " checkCounter = " + pm.checkCounter.get();
	}
}

class PairChecker implements Runnable {
	private PairManager pm;
	public PairChecker(PairManager pm) {
		this.pm = pm;
	}
	
	@Override
	public void run() {
		while(true) {
			pm.checkCounter.incrementAndGet();
			pm.getPair().checkState();
		}
	}
}

public class CriticalSection {

	static void testApproaches(PairManager pm1, PairManager pm2) {
		ExecutorService exec = Executors.newCachedThreadPool();
		PairManipulator
			pmani1 = new PairManipulator(pm1), 
			pmani2 = new PairManipulator(pm2);
		PairChecker
			pcheck1 = new PairChecker(pm1), 
			pcheck2 = new PairChecker(pm2);
		exec.execute(pmani1);
		exec.execute(pmani2);
		exec.execute(pcheck1);
		exec.execute(pcheck2);
		exec.shutdown();
		
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		}
		catch (InterruptedException e) {
			System.out.println("Sleep was interrupted.");
		}
		System.out.println("pmani1: " + pmani1 + "\npmani2: " + pmani2);
		System.exit(0);
		
	}
	
	public static void main(String[] args) {
		PairManager
			pm1 = new PairManager1(),
			pm2 = new PairManager2();
		testApproaches(pm1, pm2);
	}

}
