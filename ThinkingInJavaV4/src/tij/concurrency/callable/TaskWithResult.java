package tij.concurrency.callable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskWithResult implements Callable<String> {
	private static int taskCount = 0;
	private final int taskId = taskCount++;
	private String result;
	
	@Override
	public String call() throws Exception {
		result = "Task id: " + taskId + ", thread id: " + Thread.currentThread().getId() 
				+ ", current time: " + System.currentTimeMillis();
//		Thread.yield();
		return result;
	}
	
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		List<Future<String>> resultArr = new ArrayList<Future<String>>();
		for (int i = 0; i < 7; i++) {
			resultArr.add(exec.submit(new TaskWithResult()));
		}
		exec.shutdown();
		
		for (Future<String> f : resultArr) {
			try {
				System.out.println(f.get());
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

}