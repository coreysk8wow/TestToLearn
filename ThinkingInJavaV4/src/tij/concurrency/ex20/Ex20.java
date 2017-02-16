package tij.concurrency.ex20;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// class CachedThreadPool
public class Ex20 {
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			exec.execute(new LiftOff());
		}
		exec.shutdownNow();
	}
}

class LiftOff implements Runnable {
	private int countDown = 11;
	private static int taskCount = 0;
	private int id = taskCount++;
	
	public String status() {
		return "#" + id + "(" + (countDown > 1 ? countDown : "LiftOff!") + "), ";
	}
	
	@Override
	public void run() {
		while (countDown-- > 1) {
			System.out.println(status());
			Thread.yield();
		}
		while (!Thread.currentThread().interrupted()) {}
		System.out.println("#" + id + " is interrupted.");
	}
}