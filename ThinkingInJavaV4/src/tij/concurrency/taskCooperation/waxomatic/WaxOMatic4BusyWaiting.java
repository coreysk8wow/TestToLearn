package tij.concurrency.taskCooperation.waxomatic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Car4 extends Car {
	private boolean waxOn;
	
	public synchronized void waxed() {
		waxOn = true;
	}
	
	public synchronized void buffed() {
		waxOn = false;
	}
	
	public synchronized boolean isWaxOn() {
		return waxOn;
	}
	
	public void waitForBuffing() {}
	
	public void waitForWaxing() {}
}

class WaxOnBusyWaiting implements Runnable {
	private Car4 car;
	WaxOnBusyWaiting(Car4 car) {
		this.car = car;
	}
	
	@Override
	public void run() {
		try {
    		while (!Thread.currentThread().interrupted()) {
    			if (!car.isWaxOn()) {
    				System.out.println("Wax on!");
    				TimeUnit.MILLISECONDS.sleep(500);
    				car.waxed();
    			}
    		}
		}
		catch (InterruptedException e) {
			System.out.println("Exiting WaxOn via interrupt.");
		}
		System.out.println("Ending WaxOn task.");
	}
}

class WaxOffBusyWaiting implements Runnable {
	private Car4 car;
	WaxOffBusyWaiting(Car4 car) {
		this.car = car;
	}
	
	@Override
	public void run() {
		try {
    		while (!Thread.currentThread().interrupted()) {
    			if (car.isWaxOn()) {
    				System.out.println("Wax off!");
    				TimeUnit.MILLISECONDS.sleep(500);
    				car.buffed();
    			}
    		}
		}
		catch (InterruptedException e) {
			System.out.println("Exiting WaxOff via interrupt.");
		}
		System.out.println("Ending WaxOff task.");
	}
	
}

public class WaxOMatic4BusyWaiting {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		Car4 car = new Car4();
		exec.execute(new WaxOnBusyWaiting(car));
		exec.execute(new WaxOffBusyWaiting(car));
		TimeUnit.SECONDS.sleep(10);
		exec.shutdownNow();
	}

}
