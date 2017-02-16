package ABCDE;

public class ABCDE
{

	public static void main(String[] args)
	{
		Integer ABCDE = 0;
		Integer EDCBA = 0;

		for (; EDCBA < 100000; ABCDE++)
		{
			EDCBA = Integer.valueOf(new StringBuffer(ABCDE.toString())
					.reverse().toString());

			if (ABCDE * 4 == EDCBA)
			{
				System.out.println(ABCDE + " × 4 = " + EDCBA);
			}
		}
	}

}
