/**
 * This is almost the same with the example on page 1208.
 * This is almost the same with Restaurant.java
 * I use the lock of restaurant instead of the locks of restaurant.waiter and restaurant.chef, 
 * in this way, it's more readable, but less efficient.
 * 
 * @author Liu Shun
 * Sep 21, 2013
 *
 */
package tij.concurrency.producersConsumes.restaurant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Meal2 {
	private final int orderNum;
	
	Meal2(int orderNum) {
		this.orderNum = orderNum;
	}
	
	public String toString() {
		return "Meal " + orderNum;
	}
}

class Waiter2 implements Runnable {
	private Restaurant2 restaurant; 
	
	Waiter2(Restaurant2 r) {
		restaurant = r;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().interrupted()) {
				synchronized (restaurant) {
					while (restaurant.meal == null)
						restaurant.wait();
				}
				System.out.println("Waiter got " + restaurant.meal);
				synchronized (restaurant) {
					restaurant.meal = null;
					restaurant.notifyAll();
				}
			}
		}
		catch (InterruptedException e) {
			System.out.println("Waiter interrupted.");
		}
		finally {
			System.out.println("Waiter: Bye!");
		}
	}
	
}

class Chef2 implements Runnable {
	private Restaurant2 restaurant;
	private int count;
	
	Chef2(Restaurant2 r) {
		this.restaurant = r;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().interrupted()) {
				synchronized (restaurant) {
					while (restaurant.meal != null)
						restaurant.wait();
				}
				if (++count == 10) {
					System.out.println("Out of food. Closing.");
					restaurant.exec.shutdownNow();
					return; // You may remove this return.
				}
				System.out.println("Chef: Order up!");
				synchronized (restaurant) {
					restaurant.meal = new Meal2(count);
					restaurant.notifyAll();
				}
			}
		}
		catch (InterruptedException e) {
			System.out.println("Chef interrupted.");
		}
		finally {
			System.out.println("Chef: Bye!");
		}
	}
	
}

public class Restaurant2 {
	Meal2 meal;
	Chef2 chef = new Chef2(this);
	Waiter2 waiter = new Waiter2(this);
	ExecutorService exec;
	
	Restaurant2() {
		exec = Executors.newCachedThreadPool();
		exec.execute(chef);
		exec.execute(waiter);
	}
	
	public static void main(String[] args) {
		new Restaurant2();
	}
}

