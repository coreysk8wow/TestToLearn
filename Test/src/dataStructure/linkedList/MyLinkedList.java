package dataStructure.linkedList;

public class MyLinkedList
{
	public static LNode createLinkedList(final int length)
	{
		LNode headNode = new LNode(null, null);
		LNode node, preNode = headNode;
		for(int i = 0; i < length; i++)
		{
			node = new LNode(null, null);
			preNode.setNext(node);
			preNode = node;
		}
		
		return headNode;
	}
	
	public static void initLinkedList(final LNode headNode)
	{
		LNode node = headNode.getNext();
		int i = 0;
		while(node != null)
		{
			node.setData(String.valueOf(i + 1));
			i++;
			node = node.getNext();
		}
	}
	
	public static void traverseLinkedList(final LNode headNode)
	{
		LNode node = headNode.getNext();
		while(node != null)
		{
			System.out.print(node.getData().toString() + ", ");
			node = node.getNext();
		}
	}

}
