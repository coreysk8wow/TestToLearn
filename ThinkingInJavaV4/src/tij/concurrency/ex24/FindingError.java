package tij.concurrency.ex24;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import tij.tools.IO;
import tij.tools.StringUtils;

public class FindingError {

	public static void main(String[] args) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("k:/ex24.txt")));
			String line = null;
			long consumerNum = 0;
			// use this to quickly locate the line in the file.
			long lineNum = 0;
			Long productId = 0L;
			List<Long> wrongLines = new ArrayList<Long>();
			while ((line = br.readLine()) != null && ++lineNum > 0) {
				if (line.startsWith("Consumer")) {
					consumerNum++;
//					try {
						productId = StringUtils.findFirstNumber(line);
//					}
//					catch (NumberFormatException nfe) {
//						System.out.println("NumberFormatException, line number is " + lineNum);
//						return;
//					}
					if (productId != null && !productId.equals(consumerNum) )
						wrongLines.add(lineNum);
				}
				
			}
			System.out.println("Number of consumers: " + consumerNum);
			if (wrongLines.isEmpty())
				System.out.println("There is no wrong line.");
			else
				System.out.println("The first wrong line: " + wrongLines.get(0));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (br != null)
				try {
					br.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}

}
