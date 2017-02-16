package ObjectComparison;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestObjectComparisonComparable
{
	public static void main(String[] args)
	{
		BookComparable book1 = new BookComparable(1, "A", "C");
		BookComparable book2 = new BookComparable(2, "b", "B");
		BookComparable book3 = new BookComparable(3, "C", "A");
		BookComparable book4 = new BookComparable(4, "C", "d");
	
		List<BookComparable> list = new ArrayList();
		list.add(book3);
		list.add(book1);
		list.add(book2);
		list.add(book4);
		
		
/*		List list = new ArrayList();
		list.add(1);
		list.add(2);
		list.add(0);*/
		
		Collections.sort(list);
		
		for(BookComparable book : list)
		{
			System.out.println(book.getId());
		}
	}
}
