package tij.concurrency.catchingExceptions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NaiveExceptionHandling {

	public static void main(String[] args) {
		try {
			ExecutorService exec = Executors.newCachedThreadPool();
			exec.execute(new ExceptionThread());
		}
		catch (RuntimeException re) {
			System.out.println("Exception has been handled.");
		}
	}

}
