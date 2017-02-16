package tij.concurrency.ex19;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class A {

	public static void main(String[] args) throws InterruptedException {
		/*Thread t = new Thread() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(10);
				}
				catch (InterruptedException e) {
					System.out.println("Interrupted");
				}
			}
		};
		t.start();
		TimeUnit.SECONDS.sleep(3);
		t.interrupt();*/
		
		ExecutorService exec = Executors.newCachedThreadPool();
		Future f = exec.submit(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(10);
				}
				catch (InterruptedException e) {
					System.out.println("Interrupted");
				}
			}});
		TimeUnit.SECONDS.sleep(3);
//		f.cancel(true);
		exec.shutdownNow();
		TimeUnit.SECONDS.sleep(3);
		System.out.println("Exiting....");
	}

}
