package ls.learning.concurrency.timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TestTimer {
	public static void main(String[] args) throws InterruptedException {
		Timer timer = new Timer();
		timer.schedule(new ThrowTask(), 1);
		TimeUnit.SECONDS.sleep(1);
		timer.schedule(new ThrowTask(), 1);
		TimeUnit.SECONDS.sleep(5);
	}
	
	static class ThrowTask extends TimerTask {

		@Override
		public void run() {
			throw new RuntimeException();
		}
		
	}
}
