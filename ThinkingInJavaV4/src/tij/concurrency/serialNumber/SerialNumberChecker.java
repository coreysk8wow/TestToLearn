package tij.concurrency.serialNumber;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class SerialNumberGenerator {
	private int serialNumber = 0; // No need to use volatile here, synchronized ensure volatility.
	public synchronized int nextSerialNumber() {
		return serialNumber++;
	}
}

class CircularSet {
	private int[] snArr; // serial number array
	private int len;
	private int index = 0;
	
	protected CircularSet(int size) {
		snArr = new int[size];
		len = size;
		for (int i = 0; i < size; i++) {
			snArr[i] = -1;
		}
	}
	
	public synchronized void add(int n) {
		snArr[index++ % len] = n;
	}
	
	public synchronized boolean contains(int n) {
		for (int i : snArr) {
			if (i == n)
				return true;
		}
		return false;
	}
}

public class SerialNumberChecker implements Runnable { 
	private static final int NUM_THREDS = 10;
	private static final int SIZE_CIRCULAR_SET = 1000;
	private final CircularSet serials = new CircularSet(SIZE_CIRCULAR_SET);
	private final SerialNumberGenerator snGen = new SerialNumberGenerator();
	
	@Override
	public void run() {
		int sn = 0;
		while(true) {
			sn = snGen.nextSerialNumber();
			if (serials.contains(sn)) {
				System.out.println("Duplicate: " + sn);
				System.exit(0);
			}
			serials.add(sn);
		}
	}
	
	public static void main(String args[]) {
		ExecutorService exec = Executors.newCachedThreadPool();
		SerialNumberChecker snc = new SerialNumberChecker();
		for (int i = 0; i < NUM_THREDS; i++) {
			exec.execute(snc);
		}
	}
	
}

