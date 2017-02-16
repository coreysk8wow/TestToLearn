package testGenerics;

import java.util.ArrayList;
import java.util.List;

public class Circle extends Shape {

    @Override
    public void draw() {
	System.out.println("Draw Circle.");
    }
    
    public void printCircle(List<? extends Shape> shapes) {
	
    }
    
    public static void main(String[] args) {
	List<Circle> circles = new ArrayList<Circle>();
	circles.add(new Circle());
	circles.add(new SmallCircle());
	
	Circle c = new Circle();
	c.drawAll(circles);
	c.printCircle(circles);
    }
}
