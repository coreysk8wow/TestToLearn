package tij.concurrency.ex11;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Fibonacci implements Runnable {
	private static int taskCount = 0;
	private FibonacciGenerator fg;
	private int taskId = taskCount++;
	
	public Fibonacci(FibonacciGenerator fg) {
		this.fg = fg;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < 5; i++) {
			fg.next();
		}
	}
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		FibonacciGenerator fg = new FibonacciGenerator();
		for (int i = 0; i < 5; i++) {
			exec.execute(new Fibonacci(fg));
		}
		exec.shutdown();
	}
	
}

class FibonacciGenerator {
	private int num1 = 0;
	private int num2 = 1;
	
	public synchronized void next() {
		int nextValue = num1 + num2;
		num1 = num2;
		num2 = nextValue;
		print(nextValue);
	}
	
	public void print(int val) {
		System.out.println("fibonacci number: " + val);
	}
}
