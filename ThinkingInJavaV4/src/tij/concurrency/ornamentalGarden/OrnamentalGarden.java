package tij.concurrency.ornamentalGarden;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Counter {
	private int count = 0;

	public synchronized int increment() {
		return ++count;
	}

	public synchronized int value() {
		return count;
	}
}

class Entrance implements Runnable {
	private static final Counter counter = new Counter();
	private static final List<Entrance> entrances = new ArrayList<Entrance>();
	private static volatile boolean canceled;
	private int number;
	private int id;
	private final Random rand = new Random();
	
	public Entrance(int id) {
		this.id = id;
		entrances.add(this);
	}

	public static void cancel() {
		// Atomic operation
		canceled = true;
	}

	private synchronized int getValue() {
		return number;
	}

	@Override
	public void run() {
		while (!canceled) {
			synchronized (this) {
				number++;
			}
			System.out.println(this + " Total: " + counter.increment());
			try {
				TimeUnit.MILLISECONDS.sleep(rand.nextInt(100));
			}
			catch (InterruptedException e) {
				System.out.println("Sleep interrupted.");
			}
		}
		System.out.println("Stopping " + this);
	}

	public static int getTotalCount() {
		return counter.value();
	}
	
	public static int sumEntrances() {
		int sum = 0;
		for (Entrance entrance : entrances) {
			sum += entrance.number;
		}
		return sum;
	}
	
	@Override
	public String toString() {
		return "Entrance " + id + ": " + getValue();
	}
}

public class OrnamentalGarden {

	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 6; i++)	 {
			exec.execute(new Entrance(i));
		}
		exec.shutdown();
		
		try {
			TimeUnit.SECONDS.sleep(3);
		}
		catch (InterruptedException e) {}
		
		Entrance.cancel();
		
		try {
			if (!exec.awaitTermination(300, TimeUnit.MILLISECONDS))
				System.out.println("Some tasks were not terminated!");
		}
		catch (InterruptedException e) {}
		
		System.out.println("Total: " + Entrance.getTotalCount());
		System.out.println("Sum of Entrances: " + Entrance.sumEntrances());
	}

}
