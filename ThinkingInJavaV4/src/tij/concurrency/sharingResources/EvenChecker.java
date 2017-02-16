package tij.concurrency.sharingResources;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EvenChecker implements Runnable {
	private IntGenerator evenGenerator;
	private int id;
	
	public EvenChecker(IntGenerator evenGenerator, int id) {
		this.evenGenerator = evenGenerator;
		this.id = id;
	}
	
	@Override
	public void run() {
		int value = 0;
		while (!evenGenerator.isCanceled()) {
			value = evenGenerator.next();
			if (value % 2 != 0) {
				evenGenerator.cancel();
				System.out.println(value + " is not even.");
			}
		}
	}

	public static void check(IntGenerator evenGenerator, int numCheckers) {
		System.out.println("Press Control-C to exit");
		
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < numCheckers; i++) {
			exec.execute(new EvenChecker(evenGenerator, i));
		}
		exec.shutdown();
	}
	public static void check(IntGenerator evenGenerator) {
		check(evenGenerator, 10);
	}
	
}
