package tij.concurrency.threadLocal;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadLocalVariableHolder {
	private static final ThreadLocal<Integer> value = 
			new ThreadLocal<Integer>(){
		private final Random r = new Random();
		@Override
		protected Integer initialValue() {
			int n = r.nextInt(1000);
			System.out.println("n #" + Thread.currentThread().getId() + ": " + n);
			return n;
		}
	};
	
	public static Integer get() {
		return value.get();
	}
	
	public static void increment() {
		value.set(value.get() + 1);
	}
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		Accessor accessor = new Accessor();
		for (int i = 0; i < 5; i++) {
			exec.execute(accessor);
		}
		try {
			TimeUnit.MILLISECONDS.sleep(5);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			exec.shutdownNow();
		}
		exec.shutdownNow();
	}

}

class Accessor implements Runnable {
	@Override
	public void run() {
//		lol();
		while(!Thread.currentThread().isInterrupted()) {
			ThreadLocalVariableHolder.increment();
			System.out.println(this);
			lol();
			Thread.yield();
		}
	}
	
	private void lol() {
		try {
			Class<Thread> threadClass = (Class<Thread>) Thread.currentThread().getClass();
			Field threadLocalsField = threadClass.getDeclaredField("threadLocals");
			threadLocalsField.setAccessible(true);
			threadLocalsField.set(Thread.currentThread(), null);
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
		catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return "#" + Thread.currentThread().getId() + ": " + ThreadLocalVariableHolder.get();
	}
}
