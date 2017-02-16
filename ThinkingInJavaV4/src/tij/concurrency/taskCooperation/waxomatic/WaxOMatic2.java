package tij.concurrency.taskCooperation.waxomatic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Car2 extends Car {
	private boolean waxOnStep1;
	private boolean waxOnStep2;
	
	public synchronized void waxedStep1() {
		waxOnStep1 = true;
		notifyAll();
	}
	
	public synchronized void waxedStep2() {
		waxOnStep2 = true;
		notifyAll();
	}
	
	public synchronized void buffedStep1() {
		waxOnStep1 = false;
		notifyAll();
	}
	
	public synchronized void buffedStep2() {
		waxOnStep2 = false;
		notifyAll();
	}
	
	public synchronized void waitForBuffingStep1() throws InterruptedException {
//		if (waxOnStep1)
		while (waxOnStep1)
			wait();
	}
	
	public synchronized void waitForBuffingStep2() throws InterruptedException {
//		if (waxOnStep2)
		while (waxOnStep2)
			wait();
	}
	
	public synchronized void waitForWaxingStep1() throws InterruptedException {
//		if (!waxOnStep1)
		while (!waxOnStep1)
			wait();
	}
	
	public synchronized void waitForWaxingStep2() throws InterruptedException {
//		if (!waxOnStep2)
		while (!waxOnStep2)
			wait();
	}
}

class WaxOn2 implements Runnable {
	private Car2 car;
	public WaxOn2(Car2 car) {
		this.car = car;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().interrupted()) {
				System.out.println("Wax on step 1.");
				TimeUnit.MILLISECONDS.sleep(500);
				car.waxedStep1();
				
				car.waitForBuffingStep1();
				
				System.out.println("Wax on step 2.");
				TimeUnit.MILLISECONDS.sleep(500);
				car.waxedStep2();
				
				car.waitForBuffingStep2();
			}
		}
		catch (InterruptedException e) {
			System.out.println("Exiting WaxOn2 via interrupt.");
		}
		System.out.println("Ending WaxOn2 task.");
	}
}

class WaxOff2 implements Runnable {
	private Car2 car;
	public WaxOff2(Car2 car) {
		this.car = car;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().interrupted()) {
				car.waitForWaxingStep1();
				
				System.out.println("Wax off step 1.");
				TimeUnit.MILLISECONDS.sleep(500);
				car.buffedStep1();
				
				car.waitForWaxingStep2();
				
				System.out.println("Wax off step 2. Done.");
				TimeUnit.MILLISECONDS.sleep(500);
				car.buffedStep2();
			}
		}
		catch (InterruptedException e) {
			System.out.println("Exiting WaxOff2 via interrupt.");
		}
		System.out.println("Ending WaxOff2 task.");
	}
}

public class WaxOMatic2 {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		Car2 car = new Car2();
		exec.execute(new WaxOn2(car));
		exec.execute(new WaxOff2(car));
		TimeUnit.SECONDS.sleep(10);
		exec.shutdownNow();
	}

}
