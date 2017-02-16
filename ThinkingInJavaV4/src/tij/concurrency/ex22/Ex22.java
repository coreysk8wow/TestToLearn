package tij.concurrency.ex22;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Flag {
	private boolean flag;
	
	public synchronized boolean getFlag() {
		return flag;
	}
	
	public synchronized void setFlag(boolean flag) {
		this.flag = flag;
	}
}

public class Ex22 {
	private Flag flag = new Flag();
	
	public static void main(String[] args) throws InterruptedException {
		Ex22 ex22 = new Ex22();
		ExecutorService exec = Executors.newCachedThreadPool();

/*		exec.execute(ex22.new ClearFlag1());
		TimeUnit.SECONDS.sleep(1);
		exec.execute(ex22.new SetFlag1());
*/		
		exec.execute(ex22.new ClearFlag2());
		TimeUnit.SECONDS.sleep(1);
		exec.execute(ex22.new SetFlag2());

		exec.shutdown();
	}
	
	private class SetFlag1 implements Runnable {
		
		@Override
		public void run() {
			try {
				TimeUnit.SECONDS.sleep(2);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			flag.setFlag(true);
		}
	}
	private class ClearFlag1 implements Runnable {
		
		@Override
		public void run() {
			// busy waiting
			while (!Thread.currentThread().interrupted()) {
				synchronized (flag) {
					if (flag.getFlag()) {
						flag.setFlag(false);
						System.out.println("Flag status is cleared.");
						return;
					}
				}
			}
		}
	}

	
	private class SetFlag2 implements Runnable {
		
		@Override
		public void run() {
			try {
				TimeUnit.SECONDS.sleep(2);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}

			synchronized (flag) {
				flag.setFlag(true);
				flag.notifyAll();
			}
		}
	}
	private class ClearFlag2 implements Runnable {
		
		@Override
		public void run() {
			synchronized (flag) {
    			while (!flag.getFlag()) {
    				try {
    					flag.wait();
    				}
    				catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    			}
    			flag.setFlag(false);
    			System.out.println("Flag status is cleared.");
			}
			
		}
	}
	
}


