package testGenerics;

import java.util.List;

public abstract class Shape {
    public abstract void draw();
    
    public void drawAll(List<? extends Shape> shapes) {
	for(Shape s : shapes) {
	    s.draw();
	}
    }
}
