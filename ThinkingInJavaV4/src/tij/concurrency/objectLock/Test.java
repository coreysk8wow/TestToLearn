package tij.concurrency.objectLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

	/*public static void main(String[] args) {
		Task task = new Task();
		for (int i = 0; i < 5; i++) {
			Thread t = new Thread(task);
			t.start();
//			Thread.yield();
		}
	}*/
	
	public static void main(String[] args) {
		Task task = new Task();
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			exec.execute(task);  // 
//			exec.execute(new Task());
		}
		exec.shutdown();
	}

}

class Task implements Runnable {

	@Override
	public void run() {
		print();
	}

	public synchronized void print() {
		for (int i = 0; i < 5; i++) {
			System.out.println(Thread.currentThread() + " : " + i);
			Thread.yield();
		}
	}
}