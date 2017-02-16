package tij.concurrency.interruption;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Interrupting {
	private static ExecutorService exec = Executors.newCachedThreadPool();
	
	public static void test(Runnable r) throws InterruptedException {
		Future f = exec.submit(r);
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println("Interrupting " + r.getClass().getName());
		f.cancel(true);
		System.out.println("Interrupt sent to " + r.getClass().getName());
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		test(new SleepBlocked());
		test(new IOBlocked(System.in));
//		exec.shutdownNow();
		TimeUnit.SECONDS.sleep(3);
		System.out.println("Aborting with System.exit(0)");
		System.exit(0);
	}
}

class SleepBlocked implements Runnable {

	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(100);
		}
		catch (InterruptedException e) {
			System.out.println("Interrupted Exception");
		}
		System.out.println("Exiting SleepBlocked.run()");
	}
}

class IOBlocked implements Runnable {
	private InputStream in;
	public IOBlocked(InputStream in) {
		this.in = in;
	}
	
	@Override
	public void run() {
		System.out.println("Waiting for read():");
		try {
			in.read();
		}
		catch (IOException e) {
			if (Thread.currentThread().isInterrupted())
				System.out.println("Interrupted from blocked I/O");
			else
				throw new RuntimeException();
		}
		System.out.println("Exiting IOBlocked.run()");
	}
}

