package tij.concurrency.ex28;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

class LiftOff {
	private static int taskCount = 0;
	private final int id = taskCount++;
	private int countDown = 10;
	
	LiftOff() {}
	LiftOff(int countDown) {
		this.countDown = countDown;
	}
	
	String status() {
		return "#" + id + "(" + (countDown > 0 ? countDown : "LiftOff!") + "). ";
	}
	
	public void countDownAndLaunch() {
		while (countDown-- > 0) {
			System.out.println(status());
			Thread.yield();
		}
	}
	
}

// As a consumer
class LiftOffRunner implements Runnable {
	private SpaceCenter center;
	
	LiftOffRunner(SpaceCenter center) {
		this.center = center; 
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				center.getRockets().take().countDownAndLaunch();
			}
		}
		catch (InterruptedException ie) {
			System.out.println("Waking from BlockingQueue.take() @" + Thread.currentThread().getName());
		}
		System.out.println("Exiting LiftOffRunner. @" + Thread.currentThread().getName());
	}
	
}

class LiftOffSupplier implements Runnable {
	private SpaceCenter center;
	
	LiftOffSupplier (SpaceCenter center) {
		this.center = center;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				center.getRockets().put(new LiftOff());
			}
		}
		catch (InterruptedException e) {
			System.out.println("Waking from BlockingQueue.put() @" + Thread.currentThread().getName());
		}
		System.out.println("Exiting LiftOffSupplier. @" + Thread.currentThread().getName());
	}
	
}

class SpaceCenter {
	private BlockingQueue<LiftOff> rockets;
	
	SpaceCenter() {
		rockets = new LinkedBlockingQueue<LiftOff>();
	}
	SpaceCenter(BlockingQueue<LiftOff> queue) {
		rockets = queue;
	}
	
	BlockingQueue<LiftOff> getRockets() {
		return rockets;
	}
	
	void getKey() {
		try {
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void test(String msg, BlockingQueue<LiftOff> queue) throws InterruptedException {
		System.out.println(msg);
		
		LiftOffRunner runner = new LiftOffRunner(this);
		LiftOffSupplier supplier = new LiftOffSupplier(this);
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(runner);
		exec.execute(supplier);

		getKey();
		exec.shutdownNow();
		
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Finished " + msg + " test.\n");
	}

}

public class TestBlockingQueues {
	
	public static void main(String[] args) throws InterruptedException {
		SpaceCenter center = new SpaceCenter();
		center.test("LinkedBlockingQueue", new LinkedBlockingQueue<LiftOff>());
//		center.test("ArrayBlockingQueue", new ArrayBlockingQueue<LiftOff>(3));
//		center.test("SynchronousQueue", new SynchronousQueue<LiftOff>());
	}

}
