package tij.concurrency.semaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {
	private static final int SIZE = 25;
	
	public static void main(String[] args) throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(SIZE);
		final Pool<Fat> pool = new Pool<Fat>(Fat.class, SIZE);
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0;  i < SIZE; i++)
			exec.execute(new CheckoutTask<Fat>(pool, latch));
		
		latch.await();
		System.out.println("All CheckoutTasks created");
		List<Fat> list	 = new ArrayList<Fat>();
		for (int i = 0; i < SIZE; i++) {
			Fat f = pool.checkOut();
			System.out.println(i + ": main() thread checked out");
			f.operation();
			list.add(f);
		}
		Future<?> blocked = exec.submit(new Runnable() {
			@Override
			public void run() {
				try {
					pool.checkOut();
				}
				catch (InterruptedException e) {
					System.out.println("Canceled.");
				}
			}
		});
		TimeUnit.SECONDS.sleep(2);
		blocked.cancel(true);
		System.out.println("Checking in objects in " + list);
		for (Fat fat : list)
			pool.checkIn(fat);
		for (Fat fat : list)
			pool.checkIn(fat);
		exec.shutdown();
	}
	
}

class Fat {
	private volatile double d;
	private static int counter = 0;
	private final int id = counter++;
	
	public Fat() {
		for (int i = 1; i < 10000; i++)
			d += (Math.PI + Math.E) / i;
	}
	
	public void operation() {
		System.out.println(this);
	}
	
	public String toString()	 {
		return "Fat id: " + id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fat other = (Fat) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}

class CheckoutTask<T> implements Runnable {
	private static int counter = 0;
	private final int id = counter++;
	private Pool<T> pool;
	private CountDownLatch latch;
	
	public CheckoutTask(Pool<T> pool, CountDownLatch latch) {
		this.pool = pool;
		this.latch = latch;
	}
	
	@Override
	public void run() {
		try {
			T item = pool.checkOut();
			System.out.println(this + " checked out " + item);
			TimeUnit.SECONDS.sleep(1);
			System.out.println(this + " checking in " + item);
			pool.checkIn(item);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		latch.countDown();
		System.out.println("latch count : " + latch.getCount());
	}
	
	public String toString() {
		return "CheckoutTask " + id + " ";
	}
	
}
