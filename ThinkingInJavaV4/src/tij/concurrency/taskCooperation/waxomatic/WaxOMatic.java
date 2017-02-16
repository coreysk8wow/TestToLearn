package tij.concurrency.taskCooperation.waxomatic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Car {
	private boolean waxOn;
	
	public synchronized void waxed() {
		waxOn = true;
		notifyAll();
	}
	
	public synchronized void buffed() {
		waxOn = false;
		notifyAll();
	}
	
	public synchronized void waitForBuffing() throws InterruptedException {
		// Use if instead of while, cooperation between tasks will has problems. 
		// Run WaxOMatic3.java to test it.
//		if (waxOn)
		while (waxOn)
			wait();
	}
	
	public synchronized void waitForWaxing() throws InterruptedException {
		// Use if instead of while, cooperation between tasks will has problems. 
		// Run WaxOMatic3.java to test it.
//		if (!waxOn)
		while (!waxOn)
			wait();
	}
}

class WaxOn implements Runnable {
	private Car car;
	public WaxOn(Car car) {
		this.car = car;
	}
	
	@Override
	public void run() {
		try {
    		while (!Thread.currentThread().interrupted()) {
    			System.out.println("Wax on!");
    			TimeUnit.MILLISECONDS.sleep(500);
    			car.waxed();
    			
    			car.waitForBuffing();
    		}
		}
    	catch (InterruptedException e) {
    		System.out.println("Exiting WaxOff via interrupt.");
		}
		System.out.println("Ending WaxOff task.");
	}
}

class WaxOff implements Runnable {
	private Car car;
	public WaxOff(Car car) {
		this.car = car;
	}
	
	@Override
	public void run() {
		try {
    		while (!Thread.currentThread().interrupted()) {
    			car.waitForWaxing();
    			
    			System.out.println("Wax off! Done.");
    			TimeUnit.MILLISECONDS.sleep(500);
    			car.buffed();
    		}
		}
		catch (InterruptedException e) {
			System.out.println("Exiting WaxOn via interrupt.");
		}
		System.out.println("Ending WaxOn task.");
	}
}

public class WaxOMatic {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		Car car = new Car();
		exec.execute(new WaxOn(car));
		exec.execute(new WaxOff(car));
		TimeUnit.SECONDS.sleep(10);
		exec.shutdownNow();
	}
}

