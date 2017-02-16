package tij.concurrency.ex26;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Meal {
	private final int orderNum;
	
	Meal(int orderNum) {
		this.orderNum = orderNum;
	}
	
	public String toString() {
		return "Meal " + orderNum;
	}
}

class BusBoy implements Runnable {
	Restaurant restaurant;
	
	BusBoy(Restaurant r) {
		this.restaurant = r;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().interrupted()) {
				synchronized (this) {
					while (!restaurant.needsCleaning)
						wait();
				}
				System.out.println("Bus boy cleans.\n");
				synchronized (restaurant.waiter) {
					restaurant.needsCleaning = false;
					restaurant.waiter.notifyAll();
				}
			}
		}
		catch (InterruptedException e) {
			System.out.println("BusBoy interrupted.");
		}
		finally {
			System.out.println("Bus boy: Bye!");
		}
	}
	
}

class Waiter implements Runnable {
	private Restaurant restaurant; 
	
	Waiter(Restaurant r) {
		restaurant = r;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().interrupted()) {
				synchronized (this) {
					while (restaurant.meal == null)
						wait();
				}
				System.out.println("Waiter got " + restaurant.meal);
				synchronized (restaurant.busBoy) {
					restaurant.needsCleaning = true;
					restaurant.busBoy.notifyAll();
				}
				synchronized (this) {
					while (restaurant.needsCleaning)
						wait();
				}
				synchronized (restaurant.chef) {
					restaurant.meal = null;
					restaurant.chef.notifyAll();
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

class Chef implements Runnable {
	private Restaurant restaurant;
	private int count;
	
	Chef(Restaurant r) {
		this.restaurant = r;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().interrupted()) {
				synchronized (this) {
					while (restaurant.meal != null)
						wait();
				}
				if (++count == 10) {
					System.out.println("Out of food. Closing.");
					restaurant.exec.shutdownNow();
					return; // You may remove this return.
				}
				System.out.println("Chef: Order up!");
				synchronized (restaurant.waiter) {
					restaurant.meal = new Meal(count);
					restaurant.waiter.notifyAll();
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

class Restaurant {
	Meal meal;
	boolean needsCleaning;
	
	Chef chef = new Chef(this);
	Waiter waiter = new Waiter(this);
	BusBoy busBoy = new BusBoy(this);
	ExecutorService exec;
	
	Restaurant() {
		exec = Executors.newCachedThreadPool();
		exec.execute(chef);
		exec.execute(waiter);
		exec.execute(busBoy);
	}
}

public class Ex26 {

	public static void main(String[] args) {
		new Restaurant();
	}

}
