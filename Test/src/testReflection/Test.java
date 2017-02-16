package testReflection;

import java.lang.reflect.Field;

public class Test {
	public static void changeColour(Apple apple) throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Class<Apple> clazz = Apple.class;
		Field field = clazz.getDeclaredField("colour");
		field.setAccessible(true);
		System.out.println(field.get(apple));
		field.set(apple, "red");
		System.out.println(field.get(apple));
	}
	
	
	public static void main(String[] args) throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		changeColour(new Apple());
	}

}
