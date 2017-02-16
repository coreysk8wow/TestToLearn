package ObjectComparison;

import java.util.Comparator;

public class BookComparator implements Comparator<BookComparator>
{
	private int id;
	private String firstname;
	private String lastname;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	protected BookComparator(int id, String firstname, String lastname) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	@Override
	public int compare(BookComparator o1, BookComparator o2) {
		int result = 0;
		result = o1.getFirstname().compareToIgnoreCase(o2.getFirstname());
		if (result == 0)
			result = o1.getLastname().compareToIgnoreCase(o2.getLastname());
		
		return result;
	}

}
