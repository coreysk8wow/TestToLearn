package testStatic;

public class Child extends Parent {

    static {
	System.out.println("Child");
    }
    
    public static void main(String[] args) {
    }

}
