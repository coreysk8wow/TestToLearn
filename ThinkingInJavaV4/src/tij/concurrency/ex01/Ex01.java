package tij.concurrency.ex01;

public class Ex01 implements Runnable {
	private static int taskCount = 0;
	private final int id = taskCount++;
	
	public Ex01() {
	}
	
	@Override
	public void run() {
		System.out.println("Task " + id + " is starting.");
		for (int i = 0; i < 3; i++) {
			System.out.println("From run(" + i + ") id:" + id + ", Thread id:" + Thread.currentThread().getId());
			Thread.yield();
		}
		System.out.println("Task done. id:" + id);
	}

	
	public static void main(String[] args) {
//		Ex01 e = new Ex01();
		for (int i = 0; i < 5; i++) {
			new Thread(new Ex01()).start();
		}
	}

}
