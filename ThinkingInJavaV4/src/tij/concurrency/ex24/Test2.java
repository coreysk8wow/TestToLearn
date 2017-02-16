package tij.concurrency.ex24;

import java.util.concurrent.TimeUnit;

class Task1 implements Runnable {

	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(5);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("task1");
	}
	
}

class Task2 implements Runnable {

	@Override
	public void run() {
		System.out.println("task2");
	}
	
}


public class Test2 {
	
	public static void main(String[] args) {
		Thread t1 = new Thread(new Task1());
		Thread t2 = new Thread(new Task2());
		
		t1.start();
		t2.start();
		try {
			t1.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("main");
	}

}
