package ls.learning.concurrency.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestExecutor {

	private void testShutdown() {
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.submit(new Callable<Object>() {
			public Object call() throws Exception {
				TimeUnit.SECONDS.sleep(2);
				return null;
			}
		});
		exec.submit(new Callable<Object>() {
			public Object call() throws Exception {
				TimeUnit.SECONDS.sleep(3);
				return null;
			}
		});
		exec.shutdown();
		System.out.println(exec.isShutdown());
		System.out.println(exec.isTerminated());
		try {
			exec.awaitTermination(5, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
//		exec.shutdownNow();
		System.out.println(exec.isShutdown());
		System.out.println(exec.isTerminated());
	}
	
	private void testFutureTimeout_Get() throws InterruptedException, ExecutionException {
		ExecutorService exec = Executors.newCachedThreadPool();
		Future<Void> f = exec.submit(new Callable<Void>() {
			public Void call() throws Exception {
				for (int i = 0; i < 1000; i++) {
					System.out.println(new Date());
					TimeUnit.SECONDS.sleep(5);
				}
				
				return null;
			}
		});
		exec.shutdown();
		try {
			f.get(2, TimeUnit.SECONDS);
		}
		catch (TimeoutException e) {
			System.out.println("get timeout 2 secs");
			f.cancel(true);
			e.printStackTrace();
		}
	}
	
	private void testFutureTimeout_invokeAll() throws InterruptedException, ExecutionException {
		ExecutorService exec = Executors.newCachedThreadPool();
		List<Callable<Void>> tasks = new ArrayList<Callable<Void>>();
		tasks.add(new Callable<Void>() {
			public Void call() throws Exception {
				for (int i = 0; i < 1000; i++) {
					System.out.println(new Date());
					TimeUnit.SECONDS.sleep(5);
				}
				
				return null;
			}
		});
		List<Future<Void>> fList = exec.invokeAll(tasks, 20, TimeUnit.SECONDS);
		exec.shutdown();
		
		try {
			fList.get(0).get(7, TimeUnit.SECONDS);
		}
		catch (TimeoutException e) {
			System.out.println("get timeout 2 secs");
//			f.cancel(true);
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		TestExecutor test = new TestExecutor();
//		test.testShutdown();
		test.testFutureTimeout_invokeAll();
		
	}

}
