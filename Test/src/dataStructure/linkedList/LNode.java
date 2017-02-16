package dataStructure.linkedList;

import java.util.Comparator;

public class LNode implements Comparator<LNode>
{
	private Object data;
	
	private LNode next;

	public LNode()
	{
		super();
	}

	public LNode(Object data, LNode next)
	{
		super();
		this.data = data;
		this.next = next;
	}

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}

	public LNode getNext()
	{
		return next;
	}

	public void setNext(LNode next)
	{
		this.next = next;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((next == null) ? 0 : next.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LNode other = (LNode) obj;
		if (data == null)
		{
			if (other.data != null)
				return false;
		}
		else if (!data.equals(other.data))
			return false;
		if (next == null)
		{
			if (other.next != null)
				return false;
		}
		else if (!next.equals(other.next))
			return false;
		return true;
	}

	@Override
	public int compare(LNode o1, LNode o2)
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
