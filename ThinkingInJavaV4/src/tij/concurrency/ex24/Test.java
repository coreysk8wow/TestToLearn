package tij.concurrency.ex24;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Write1 implements Runnable {

	@Override
	public void run() {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(Test.OUTPUT_FILE, true)));
			for (int i = 0; i < 100; i++)
				out.print("a, ");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (out != null)
				out.close();
		}
	}
}

class Write2 implements Runnable {

	@Override
	public void run() {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(Test.OUTPUT_FILE, true)));
			for (int i = 0; i < 100; i++)
				out.print("b, ");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (out != null)
				out.close();
		}
	}
	
}


public class Test {
	public static final File OUTPUT_FILE = new File("d:/d.txt");
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new Write1());
		exec.execute(new Write2());
		exec.shutdown();
	}	

}
