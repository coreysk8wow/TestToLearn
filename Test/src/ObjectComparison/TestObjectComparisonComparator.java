package ObjectComparison;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestObjectComparisonComparator
{
	public static void main(String[] args)
	{
		BookComparator book1 = new BookComparator(1, "A", "B");
		BookComparator book2 = new BookComparator(2, "B", "C");
		BookComparator book3 = new BookComparator(3, "C", "D");
		BookComparator book4 = new BookComparator(4, "C", "C");
	
		List<BookComparator> list = new ArrayList<BookComparator>();
		list.add(book3);
		list.add(book4);
		list.add(book1);
		list.add(book2);
		
		Collections.sort(list, book1);
		
		for(BookComparator book : list)
		{
			System.out.println(book.getId());
		}
	}
}
