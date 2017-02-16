package classLoading;

public class Apple
{
	private String colour = "yellow";
	
	Apple()
	{
		System.out.println("colour = " + colour);
		colour = "green";
	}
	
	public static void main(String[] args)
	{
		Apple apple = new Apple();
	}

}
