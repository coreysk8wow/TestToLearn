/**
 * Description of the dining philosophers problem: 
 * 
 * These philosophers spend part of their time thinking and part of their time eating. 
 * While they are thinking, they don’t need any shared resources, 
 * but they eat using a limited number of utensils. 
 * In the original problem description, the utensils are forks, 
 * and two forks are required to get spaghetti from a bowl in the middle of the table, 
 * but it seems to make more sense to say that the utensils are chopsticks. 
 * Clearly, each philosopher will require two chopsticks in order to eat.
 * 
 * A difficulty is introduced into the problem: 
 * As philosophers, they have very little money, so they can only afford five chopsticks 
 * (more generally, the same number of chopsticks as philosophers). 
 * These are spaced around the table between them. 
 * When a philosopher wants to eat, that philosopher must pick up 
 * the chopstick to the left and the one to the right. 
 * If the philosopher on either side is using a desired chopstick, 
 * our philosopher must wait until the necessary chopsticks become available.
 * 
 * If your Philosophers are spending more time thinking than eating,                       
 * then they have a much lower probability of requiring the shared resources (Chopsticks), 
 * and thus you can convince yourself that the program is deadlock free                    
 * (using a nonzero ponder value, or a large number of Philosophers),                      
 * even though it isn’t.                                                                   
 * This example is interesting precisely because                                           
 * it demonstrates that a program can appear to run correctly                              
 * but actually be able to deadlock.                                                       
 * 
 */
package tij.concurrency.philosopher;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Shared resource
class Chopstick {
	private boolean taken = false;
	
	synchronized void take() throws InterruptedException {
		while (taken)
			wait();
		taken = true;
	}
	
	synchronized void drop() {
		taken = false;
		notifyAll();
	}
}

class Philosopher implements Runnable {
	private final Chopstick left;
	private final Chopstick right;
	private final int id;
	private final int ponderFactor;
	private final Random rand = new Random();	
	
	Philosopher(Chopstick left, Chopstick right, int id, int ponderFactor) {
		this.left = left;
		this.right = right;
		this.id = id;
		this.ponderFactor = ponderFactor;
	}
	
	private void thinkOrEat() throws InterruptedException {
		if (ponderFactor == 0) return;
		TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactor * 250));
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				System.out.println(this + " is thinking.");
				thinkOrEat();
				
				// Philosopher is hungry.
				System.out.println(this + " IS GRABBING the chopstick on the left side.");
				left.take();
				System.out.println(this + " HAS the chopstick on the left side.");
				System.out.println(this + " IS GRABBING the chopstick on the right side.");
				right.take();
				System.out.println(this + " HAS the chopstick on the right side.");
				
				System.out.println(this + " is eating.");
				thinkOrEat();
				
				left.drop();
				right.drop();
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

public class DeadlockingDiningPhilosophers {
	/*
	 * Try assign 0 to PONDER_FACTOR, the program will be deadlock quickly.
	 * The smaller the PONDER_FACTOR, the easier deadlocking happens.
	 */
	static final int PONDER_FACTOR = 1;
	static final int NUM_PHILOSOPHERS = 5;
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		Chopstick[] sticks = new Chopstick[NUM_PHILOSOPHERS];
		for (int i = 0; i < sticks.length; i++) {
			sticks[i] = new Chopstick();
		}
		for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
			exec.execute(new Philosopher(sticks[i], sticks[(i + 1) % NUM_PHILOSOPHERS], i, PONDER_FACTOR));
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
