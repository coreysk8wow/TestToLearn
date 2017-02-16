package tij.concurrency.catchingExceptions;

public class CaptureUncaughtException2 {

	public static void main(String[] args) {
		Thread t = new Thread(new ExceptionThread2());
		t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
		t.start();
	}

}

