package tij.concurrency.philosopher.ex31;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Shared resource
class ChopstickBin {
	private LinkedList<Chopstick> bin = new LinkedList<Chopstick>();
	private ThreadLocal<Chopstick[]> takenSticks = new ThreadLocal<Chopstick[]>();
	
	ChopstickBin(int chopstickCount) {
		for (int i = 0; i < chopstickCount; i++)
			bin.add(new Chopstick(i));
	}
	
	synchronized boolean takeTwo() throws InterruptedException {
		if (bin.size() >= 2) {
			Chopstick[] sticks = new Chopstick[2];
			sticks[0] = bin.pop();
			sticks[1] = bin.pop();
			takenSticks.set(sticks);
			return true;
		}
		return false;
	}
	
	synchronized void dropTwo() {
		bin.push(takenSticks.get()[0]);
		bin.push(takenSticks.get()[1]);
		takenSticks.remove();
	}
	
	ThreadLocal<Chopstick[]> getTakenSticks() {
		return takenSticks;
	}
	
	private class Chopstick {
		private final int id;
		
		private Chopstick(int id) {
			this.id = id;
		}
		
		@Override
		public String toString() {
			return "Chopstick " + id;
		}
	}
	
}

class Philosopher implements Runnable {
	private ChopstickBin bin;
	private final int id;
	private final int ponderFactorThink;
	private final int ponderFactorEat;
	private final Random rand = new Random();	
	
	Philosopher(ChopstickBin bin, int id, int ponderFactorThink, int ponderFactorEat) {
		this.bin = bin;
		this.id = id;
		this.ponderFactorThink = ponderFactorThink;
		this.ponderFactorEat = ponderFactorEat;
	}
	
	private void think() throws InterruptedException {
		if (ponderFactorThink == 0) return;
		TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactorThink * 250));
	}
	
	private void eat() throws InterruptedException {
		if (ponderFactorEat == 0) return;
		TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactorEat * 250));
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				System.out.println(this + " is thinking.");
				think();
				
				// Philosopher is hungry.
				System.out.println(this + " IS GRABBING two chopsticks from the bin.");
				if (!bin.takeTwo()) {
					System.out.println(this + " failed to grab two chopsticks from the bin.");
					break;
				}
				System.out.println(this + " HAS two chopsticks. "
						+ bin.getTakenSticks().get()[0] + " and "
						+ bin.getTakenSticks().get()[1]);
				
				System.out.println(this + " is eating.");
				eat();
				
				bin.dropTwo();
			}
		}
		catch (InterruptedException ie) {
			System.out.println(this + " is exiting via interrupt.");
		}
	}
	
	@Override
	public String toString() {
		return "Philosopher " + id;
	}
}

public class Ex31 {
	/*
	 * Try assign 0 to PONDER_FACTOR, the program will be deadlock quickly.
	 * The smaller the PONDER_FACTOR, the easier deadlocking happens.
	 */
	static final int PONDER_FACTOR_THINK = 5;
	static final int PONDER_FACTOR_EAT = 2;
	static final int NUM_PHILOSOPHERS = 5;
	static final int NUM_CHOPSTICKS = 5;
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		ChopstickBin bin = new ChopstickBin(NUM_CHOPSTICKS);
		for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
			exec.execute(new Philosopher(bin, i, PONDER_FACTOR_THINK, PONDER_FACTOR_EAT));
		}
		// Press Enter to quit.
		try {
			System.in.read();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		exec.shutdownNow();
	}

}
