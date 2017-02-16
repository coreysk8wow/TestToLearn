package testStatic;

public class Pistol
{
	static
	{
		System.out.println("Pistol initialized.");
	}
	
	private String name = "Luge"; 
	
	public Pistol()
	{
		System.out.println("Pistol " + this.name);	
	}

	public static void load()
	{
		Ammo ammo = new Ammo("9mm");
	}
}
