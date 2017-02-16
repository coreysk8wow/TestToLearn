package tij.concurrency.ex18;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Ex18 {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 3; i++) {
    		exec.execute(new Runnable() {
    			@Override
    			public void run() {
//    				new Bed().sleep();
    				new Bed().doNothing();
//    				new Bed().exceptionHappensOnAThread();
    			}
    		});
		}
		
		TimeUnit.SECONDS.sleep(4);
		exec.shutdownNow();
		TimeUnit.SECONDS.sleep(4);
		exec.shutdownNow();
	}
}

class Bed {
	private static volatile boolean canceled = false;
	private static int taskCount = 0;
	private int id = taskCount++;
	
	public void sleep() {
		System.out.println("Sleeping on the bed");
		try {
			TimeUnit.SECONDS.sleep(100);
		}
		catch (InterruptedException e) {
			System.out.println("Sleeping is interrupted.");
		}
	}
	
	public void doNothing() {
		System.out.println("Do nothing on the bed.");
		while(!canceled) {
			if (Thread.interrupted())
				canceled = true;
			/* 
			 * interrupt() doesn't terminate a thread, it only set the interrupted status on a thread.
			 * Comment the above if block to prove. 
			 */
			
		}
		System.out.println("Interrupted. Threads are terminated.");
	}
	
	/*
	 * This method prove that, throwing an exception to console from a thread, doesn't impact the other threads.
	 */
	public void exceptionHappensOnAThread() {
		System.out.println("Starting...");
		while (true) {
			try {
				TimeUnit.SECONDS.sleep(1);
				if (id == 1)
					throw new RuntimeException();
				TimeUnit.SECONDS.sleep(100);
			}
			catch (InterruptedException e) {
				System.out.println("#" + id + " is interrupted.");
//				throw new RuntimeException();
			}
		}
	}
}