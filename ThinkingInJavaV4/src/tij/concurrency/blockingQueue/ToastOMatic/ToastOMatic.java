package tij.concurrency.blockingQueue.ToastOMatic;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Toast {
	enum Status {DRY, BUTTERED, JAMMED}
	private Status status = Status.DRY;
	private static int count = 0;
	private final int id = count++;
	
	void butter() {
		status = Status.BUTTERED;
	}
	
	void jam() {
		status = Status.JAMMED;
	}
	
	int getId() {
		return id;
	}
	
	Status getStatus() {
		return status;
	}
	
	synchronized static int getCount() {
		return count;
	}
	
	@Override
	public String toString() {
		return "Toast " + id + ": " + status;
	}
}

class Toaster implements Runnable {
	private ToastKitchen kitchen;
	
	Toaster(ToastKitchen kitchen) {
		this.kitchen = kitchen;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted() && Toast.getCount() < 100) {
//				TimeUnit.MILLISECONDS.sleep(20);
				Toast toast = new Toast();
				System.out.println(toast);
				kitchen.getDryToastsQueue().put(toast);
			}
		}
		catch (InterruptedException ie) {
			System.out.println("Toaster interrupted @" + Thread.currentThread().getName());
		}
		System.out.println("Exiting Toaster @" + Thread.currentThread().getName());
	}
	
}

class Butterer implements Runnable {
	private ToastKitchen kitchen;
	
	Butterer(ToastKitchen kitchen) {
		this.kitchen = kitchen;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
//				TimeUnit.MILLISECONDS.sleep(10);
				Toast toast = kitchen.getDryToastsQueue().take();
				toast.butter();
				System.out.println(toast);
				kitchen.getButteredToastsQueue().put(toast);
			}
		}
		catch (InterruptedException e) {
			System.out.println("Butterer interrupted @" + Thread.currentThread().getName());
		}
		System.out.println("Exiting Butterer @" + Thread.currentThread().getName());
	}
	
}

class Jammer implements Runnable {
	private ToastKitchen kitchen;
	
	Jammer(ToastKitchen kitchen) {
		this.kitchen = kitchen;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
//				TimeUnit.MILLISECONDS.sleep(10);
				Toast toast = kitchen.getButteredToastsQueue().take();
				toast.jam();
				System.out.println(toast);
				kitchen.getFinishedToastsQueue().put(toast);
				
				if (toast.getId() == 99) {
					kitchen.setTimeToShutdown(true);
					synchronized (kitchen.getShutdownLock()) {
						kitchen.getShutdownLock().notify();
					}
					break;
				}
			}
		}
		catch (InterruptedException e) {
			System.out.println("Jammer interrupted @" + Thread.currentThread().getName());
		}
		System.out.println("Exiting Jammer @" + Thread.currentThread().getName());
	}
	
}

class Eater implements Runnable {
	private ToastKitchen kitchen;
	private int counter = 0;
	
	Eater(ToastKitchen kitchen) {
		this.kitchen = kitchen;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Toast toast = kitchen.getFinishedToastsQueue().take();
				if (toast.getId() != counter++ || toast.getStatus() != Toast.Status.JAMMED) {
					System.err.println(">>>>Error: " + toast);
					System.exit(1);
				}
				else {
					System.out.println("Chomp! " + toast);
				}
			}
		}
		catch (InterruptedException ie) {
			System.out.println("Eater interrupted @" + Thread.currentThread().getName());
		}
		System.out.println("Exiting Eater @" + Thread.currentThread().getName());
	}
	
}

class ToastKitchen {
	private BlockingQueue<Toast> dryToastsQueue = new ArrayBlockingQueue<Toast>(12);
	private BlockingQueue<Toast> butteredToastsQueue = new ArrayBlockingQueue<Toast>(12);
	private BlockingQueue<Toast> finishedToastsQueue = new ArrayBlockingQueue<Toast>(12);
	
	private boolean isTimeToShutdown;
	private Object shutdownLock = new Object();
	
	BlockingQueue<Toast> getDryToastsQueue() {
		return dryToastsQueue;
	}

	BlockingQueue<Toast> getButteredToastsQueue() {
		return butteredToastsQueue;
	}
	
	BlockingQueue<Toast> getFinishedToastsQueue() {
		return finishedToastsQueue;
	}
	
	Object getShutdownLock() {
		return shutdownLock;
	}
	
	boolean isTimeToShutdown() {
		return isTimeToShutdown;
	}

	void setTimeToShutdown(boolean isTimeToShutdown) {
		this.isTimeToShutdown = isTimeToShutdown;
	}

	void createToast() {
		ExecutorService exec = Executors.newCachedThreadPool();
		System.out.println("Starting a toast kitchen.");
		exec.execute(new Toaster(this));
		exec.execute(new Butterer(this));
		exec.execute(new Jammer(this));
		exec.execute(new Eater(this));
		
		synchronized (shutdownLock) {
			while (!isTimeToShutdown) {
				try {
					shutdownLock.wait();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		exec.shutdownNow();
		while (!exec.isTerminated());
		System.out.println("Cleaning the kitchen.");
		System.out.println("Toast kitchen is closed.");
	}
}

public class ToastOMatic {
	
	public static void main(String[] args) {
		ToastKitchen kitchen = new ToastKitchen();
		kitchen.createToast();
	}

}
