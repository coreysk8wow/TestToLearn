/**
 * This dining philosopher is deadlock free.
 */
package tij.concurrency.philosopher;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedDiningPhilosophers {
	/*
	 * Try assign 0 to PONDER_FACTOR, 
	 * the smaller the PONDER_FACTOR, the easier deadlocking happens, 
	 * but in this FixedDiningPhilosophers problem, it's deadlocking free.
	 */
	static final int PONDER_FACTOR = 0;
	static final int NUM_PHILOSOPHERS = 5;
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		Chopstick[] sticks = new Chopstick[NUM_PHILOSOPHERS];
		for (int i = 0; i < sticks.length; i++) {
			sticks[i] = new Chopstick();
		}
		for (int i = 0; i < NUM_PHILOSOPHERS - 1; i++) {
			exec.execute(new Philosopher(sticks[i], sticks[(i + 1) % NUM_PHILOSOPHERS], i, PONDER_FACTOR));
		}
		/*
		 * If the last Philosopher is initialized to try to get the left chopstick first 
		 * and then the right, that Philosopher will never prevent the Philosopher 
		 * on the immediate right from picking up their its chopstick.
		 */
		exec.execute(new Philosopher(sticks[0], sticks[NUM_PHILOSOPHERS - 1], NUM_PHILOSOPHERS - 1, PONDER_FACTOR));
		
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
