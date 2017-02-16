package tij.concurrency.taskCooperation.waitNotify;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * This test is going to test two threads waiting on the same object, 
 * and get notified by notifyAll();
 */
public class TestNotifyWait implements Runnable{
	private Lock lock = new ReentrantLock();
	
	public synchronized void do1() {
//		lock.lock();
		try {
			wait();
			System.out.println("do1: " + Thread.currentThread().getName());
		}
		catch (InterruptedException e) {
			System.out.println("Interrupted exception");
		}
//		lock.unlock();
	}

	public synchronized void wakeup() {
//		lock.lock();
//			notify();notify();
		notifyAll();
//		lock.unlock();
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		TestNotifyWait tnw = new TestNotifyWait();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(tnw);
		exec.execute(tnw);
		exec.shutdown();
		TimeUnit.SECONDS.sleep(1);
		/*exec.shutdownNow();
		TimeUnit.SECONDS.sleep(1);*/
		tnw.wakeup();
	}

	@Override
	public void run() {
		do1();

	}

}


