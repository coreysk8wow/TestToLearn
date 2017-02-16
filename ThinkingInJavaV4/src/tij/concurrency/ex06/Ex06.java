package tij.concurrency.ex06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Ex06 implements Runnable {
	private static int[] randomSleepingTimeArray;
	private static int taskCount = 0;
	private int taskId = taskCount++;
	
	public static void init(int numTasks) {
		randomSleepingTimeArray = new int[numTasks];
		Random r = new Random();
		for (int i = 0; i < numTasks; i++) {
			randomSleepingTimeArray[i] = r.nextInt(10);
		}
	}
	
	@Override
	public void run() {
		System.out.println("Task " + taskId + ", before sleeping. Thread id: " + Thread.currentThread().getId());
		try {
			TimeUnit.SECONDS.sleep(randomSleepingTimeArray[taskId]);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Task " + taskId + ", after sleeping for " + randomSleepingTimeArray[taskId] + " seconds." +
				" Thread id: " + Thread.currentThread().getId());
	}

	
	public static void main(String[] args) {
		int numTasks = 0;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			numTasks = Integer.parseInt(br.readLine());
			System.out.println("num of tasks : " + numTasks);
			
			Ex06.init(numTasks);
			ExecutorService exec = Executors.newCachedThreadPool();
			for (int i = 0; i < numTasks; i++) {
				exec.execute(new Ex06());
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			System.err.println("IO error.");
		}
		finally {
			try {
				br.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
