package ABCDE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

public class A_Z
{

	public static void main(String[] args) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		BigDecimal bound;
		BigDecimal multiplicator;
		String quit = null;
		String input1, input2;
		
		try
		{
			while(true)
			{
				if("Y".equalsIgnoreCase(quit))
				{
					break;
				}
				
				System.out.println("Input bound & multiplicator:");
				
				input1 = reader.readLine();
				input2 = reader.readLine();
				if(!input1.matches("[0-9]+") || !input2.matches("[0-9]+"))
				{
					System.out.println("Only numbers are valid.\n");
				}
				else
				{
					bound = new BigDecimal(input1);
					multiplicator = new BigDecimal(input2);
					
					double start = System.nanoTime();    
					
					BigDecimal ABCDE = new BigDecimal(0);
					BigDecimal EDCBA = new BigDecimal(0);
					BigDecimal one = new BigDecimal(1);
					
					System.out.println("\n--------------------------------");
					for (; EDCBA.compareTo(bound) == -1; ABCDE = ABCDE.add(one))
					{
						EDCBA = new BigDecimal(new StringBuffer(ABCDE.toString()).reverse().toString());

						if ((multiplicator.multiply(ABCDE)).equals(EDCBA))
						{
							System.out.println(ABCDE.toString() + " × " + multiplicator + " = " + EDCBA.toString());
						}
					}
					System.out.println("--------------------------------");
					
					double elapsedTime = System.nanoTime() - start;
					System.out.println("\nElapsed time is " + elapsedTime/1000000000 + " seconds");
					System.out.println("Quit? (y/n)");
					quit = reader.readLine();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			reader.close();
		}
	}

}
