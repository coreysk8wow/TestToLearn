package tij.concurrency.taskCooperation.waxomatic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class WaxOn3 extends WaxOn {
	private Car car;
	WaxOn3(Car car) {
		super(car);
	}
}

class WaxOn4 extends WaxOn {
	private Car car;
	WaxOn4(Car car) {
		super(car);
	}
}

class WaxOff4 extends WaxOff {
	private Car car;
	WaxOff4(Car car) {
		super(car);
	}
}

public class WaxOMatic3 {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		Car car = new Car();
		exec.execute(new WaxOn3(car));
		exec.execute(new WaxOn4(car));
		exec.execute(new WaxOff4(car));
		TimeUnit.SECONDS.sleep(10);
		exec.shutdownNow();
	}

}
