package tij.enums.test;

public enum Colour {
	GREEN, YELLOW, RED;
	
	public static void main(String args[]) {
		System.out.println(Colour.class);
		System.out.println(Colour.GREEN.getClass());
		System.out.println(Colour.GREEN.getDeclaringClass());
		System.out.println(GREEN.equals(YELLOW));
		
		for (Colour c : Colour.values()) {
			System.out.println(c);
		}
		
		Colour c = Colour.valueOf(Colour.class, "GREEN");
		System.out.println(c);
		
	}

}
