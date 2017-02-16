package tij.concurrency.ex21;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ex21 {
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		Waiting w = new Waiting();
		exec.execute(w);
		exec.execute(new Notifying(w));
		exec.shutdown();
		
	}

}

class Waiting implements Runnable {
	private boolean waiting;
	
	public synchronized boolean isWaiting() {
		return waiting;
	}

	@Override
	public void run() {
		try {
			synchronized (this) {
				waiting = true;
				while (waiting) {
					wait();
					waiting = false;
				}
			}
			System.out.println("after wait()");
		}
		catch (InterruptedException e) {
			System.out.println("Interrupted exception");
		}
	}
}

class Notifying implements Runnable {
	private Waiting w;
	
	public Notifying(Waiting w) {
		this.w = w;
	}
	
	@Override
	public void run() {
		System.out.println("Notifying");
		while (!Thread.currentThread().interrupted()) {
			synchronized (w) {
				if (w.isWaiting()) {
					w.notifyAll();
					return;
				}
			}
		}
	}
	
}