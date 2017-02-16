package testIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class TestListIterator {
    
    List list = new ArrayList();
    
    private void test() {
	Collections.addAll(list, "a", "b", "c", "d", "e");
	
	ListIterator lit = list.listIterator(2);
	System.out.println(lit.next());
	System.out.println(lit.next());
	System.out.println(lit.nextIndex());
	System.out.println(lit.next());
	
	System.out.println();
	
	ListIterator lit2 = list.listIterator(2);
	System.out.println(lit2.previous());
	System.out.println(lit2.previousIndex());
	System.out.println(lit2.previous());
//	System.out.println(lit2.previousIndex());
//	System.out.println(lit2.previous());
    }
    public static void main(String[] args) {
	new TestListIterator().test();
    }

}
