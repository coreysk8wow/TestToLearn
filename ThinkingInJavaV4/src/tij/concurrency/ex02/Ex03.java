package tij.concurrency.ex02;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ex03 implements Runnable {
	private static int taskCount = 0;
	private final int id = taskCount++;
	
	public Ex03() {
	}
	
	@Override
	public void run() {
		System.out.println("Task " + id + " is starting.");
		for (int i = 0; i < 3; i++) {
			System.out.println("From run(" + i + ") id:" + id + ", Thread id:" + Thread.currentThread().getId());
//			Thread.yield();
		}
		System.out.println("Task done. id:" + id);
	}

	public static void main(String[] args) throws InterruptedException {
		ExecutorService singleExec = Executors.newSingleThreadExecutor();
		ExecutorService fixedExec = Executors.newFixedThreadPool(2);
		ExecutorService cachedExec = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			cachedExec.execute(new Ex03());
//			 Thread.sleep(1000); // When using CachedThreadPool, if uncomment this line, 
			                     //you can find that a CachedThreadPool will reuse threads when there's threads available.
		}
		cachedExec.shutdown();
	}

}
