package tij.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.TimeUnit;

public final class IO {
	private static PrintWriter out;
	
	public static void setupOutput(File file, boolean append) throws IOException {
		if (out == null)
			synchronized (IO.class) {
				if (out == null)
					out = new PrintWriter(new BufferedWriter((new FileWriter(file, append))));
//					out = new PrintWriter(new FileWriter(file, append));
			}
	}
	public synchronized static void println(String txt) {
		if (out != null) {
			out.println(txt);
			out.flush();
		}
		else
			throw new RuntimeException("The IO connection hasn't been created of has just been closed.");
	}
	
	public static void closeOutput() throws IOException {
		if (out != null)
			synchronized (IO.class) {
				if (out != null) {
					out.close();
					out = null;
				}
			}
		else
			throw new RuntimeException("The IO connection hasn't been created of has just been closed.");
	}
	
	public synchronized static void appendText2File(String text, File file, boolean append) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter((new FileWriter(file, append))));
			out.println(text);
			out.flush();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (out != null)
				out.close();
		}
	}
	
	public synchronized static void appendText2File(String text, String path, boolean newline) {
		appendText2File(text, new File(path), newline);
	}
	
	
	public static void main(String[] args) {
		appendText2File("hello", "d:/a.txt", true);
		appendText2File("hello", "d:/a.txt", true);
	}

}
