package com.shashi.practise.ds.list;

public class ReverseLinkedList {
	
	static Node head;
	
	static class Node {
		
		int data;
		Node next;
		
		Node(int data){
			this.data = data;
			this.next = null;
		}
	}
	
	public static void main(String[] args) {
		
		ReverseLinkedList list = new ReverseLinkedList();
		list.head = new Node(10);
		list.head.next = new Node(20);
		list.head.next.next = new Node(30);
		list.head.next.next.next = new Node(40);
		list.head.next.next.next.next = new Node(50);
		
		System.out.println("Given Linked List - ");
		list.printList(head);
		head = list.reverseList(head);
		System.out.println();
		System.out.println("Reversed Linked List - ");
		list.printList(head);
	}

	private Node reverseList(Node head2) {
		Node prev = null;
		Node current = head2;
		Node next = null;
		
		while(current != null) {
			next = current.next;
			current.next = prev;
			prev = current;
			current = next;
		}
		head2 = prev;
		return head2;
	}

	private void printList(Node head3) {
		while(head3 != null) {
			System.out.print(head3.data +" ");
			head3 = head3.next;
		}	
	}
}
