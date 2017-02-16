package testGenerics;

import java.util.ArrayList;
import java.util.List;

// http://stackoverflow.com/questions/2575363/generics-list-extends-animal-is-same-as-listanimal
public class TestExtendSuper {
	
	public static void test(List<Animal> list) {
		list.add(new Dog());
	}
	
	public static void main(String[] args) {
		
		List<Animal> list = new ArrayList();
		
		test(list);
	}

}

class LifeForm {}
class Human extends LifeForm {}
class Animal extends LifeForm {}
class Dog extends Animal {}
class Cat extends Animal {}
class Puppy extends Dog {}
