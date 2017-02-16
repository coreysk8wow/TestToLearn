package dataStructure.linkedList;

public class test
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		LNode headNode = MyLinkedList.createLinkedList(7);
		MyLinkedList.initLinkedList(headNode);
		MyLinkedList.traverseLinkedList(headNode);
	}

}
