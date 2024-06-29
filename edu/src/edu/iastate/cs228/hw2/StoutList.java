package edu.iastate.cs228.hw2;

import java.util.AbstractSequentialList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Implementation of the list interface based on linked nodes that store
 * multiple items per node. Rules for adding and removing elements ensure that
 * each node (except possibly the last one) is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E> {
	/**
	 * Default number of elements that may be stored in each node.
	 */
	private final static int DEFAULT_NODESIZE = 4;

	/**
	 * Number of elements that can be stored in each node.
	 */
	private final int nodeSize;

	/**
	 * Dummy node for head. It should be private but set to public here only for
	 * grading purpose. In practice, you should always make the head of a linked
	 * list a private instance variable.
	 */
	public Node head;

	/**
	 * Dummy node for tail.
	 */
	private Node tail;

	/**
	 * Number of elements in the list.
	 */
	private int size;

	/**
	 * Constructs an empty list with the default node size.
	 */
	public StoutList() {
		this(DEFAULT_NODESIZE);
	}

	/**
	 * Constructs an empty list with the given node size.
	 * 
	 * @param nodeSize number of elements that may be stored in each node, must be
	 *                 an even number
	 */
	public StoutList(int nodeSize) {
		if (nodeSize <= 0 || nodeSize % 2 != 0)
			throw new IllegalArgumentException();

		// dummy nodes
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		this.nodeSize = nodeSize;
	}

	/**
	 * Constructor for grading only. Fully implemented.
	 * 
	 * @param head
	 * @param tail
	 * @param nodeSize
	 * @param size
	 */
	public StoutList(Node head, Node tail, int nodeSize, int size) {
		this.head = head;
		this.tail = tail;
		this.nodeSize = nodeSize;
		this.size = size;
	}
	
	/**
	 * gets the number of elements in the entire list
	 */
	@Override
	public int size() {
		return this.size;
	}
	
	/**
	 * adds an element to the next available spot
	 */
	@Override
	public boolean add(E item) {
		Node pos = tail.previous;

		if (item == null) {
			link(pos);
		} else if (pos == head || pos.count == nodeSize) {
			link(item, pos);
			size++;
		} else {
			pos.addItem(item);
			size++;
		}

		return true;
	}
	
	/**
	 * helper method for adding a new node to the list
	 * @param pos
	 */
	public void link(Node pos) {
		Node node = new Node();
		node.next = tail;
		node.previous = pos;
		pos.next = node;
		tail.previous = node;
	}
	
	/**
	 * helper method for adding a new node to the list
	 * also takes an item to add too
	 * @param item
	 * @param pos
	 */
	public void link(E item, Node pos) {
		Node node = new Node();
		node.addItem(item);
		node.next = tail;
		node.previous = pos;
		pos.next = node;
		tail.previous = node;
	}
	
	/**
	 * adds an element to specific spot in the list 
	 * 		(A, B, -, -) (C, -, -, -)
	 * index 0  1  2  3   4  5  6  7
	 * will not allow spaces. any element added to the end of a list with space will fall to the
	 * the front of the list in that node until no space is in front
	 */
	@Override
	public void add(int pos, E item) {
		Node cur = head.next;

		for (; pos > 3; pos -= 4) {
			if (cur == tail) {
				throw new IndexOutOfBoundsException();
			}
			cur = cur.next;
		}

		if (size == 0) {
			Node node = new Node();
			node.addItem(pos, item);
			node.next = tail;
			node.previous = cur;
			cur.next = node;
			tail.previous = node;
			size++;
		} else if (cur.count == 0) {
			if (cur.previous != head && cur.previous.count < nodeSize) {
				cur.addItem(cur.previous.count, item);
				size++;
			} else if (cur == tail && cur.previous.count == nodeSize) {
				Node node = new Node();
				node.addItem(pos, item);
				node.next = tail;
				node.previous = cur;
				cur.next = node;
				tail.previous = node;
				size++;
			}
		} else if (cur.count < nodeSize) {
			if (pos < cur.count) {
				cur.addItem(pos, item);
			} else {
				cur.addItem(cur.count, item);
			}
			size++;
		} else {
			Node node = new Node();
			for (int i = 0; i < nodeSize / 2; i++) {
				node.data[i] = cur.data[nodeSize / 2 + i];
				node.count++;
				cur.data[nodeSize / 2 + i] = null;
				cur.count--;
			}
			if (cur.count <= nodeSize / 2) {
				cur.addItem(cur.count, item);
			} else {
				node.addItem(cur.count - nodeSize / 2, item);
			}
			node.next = cur.next;
			node.previous = cur;
			cur.next = node;
			size++;
		}
	}
	
	/**
	 * removes an element at that index
	 * 		(A, B, -, -) (C, -, -, -)
	 * index 0  1         2         
	 * and does exactly what the assignment explained to do
	 */
	@Override
	public E remove(int pos) {
		// TODO Auto-generated method stub
		if (pos >= size) {
			throw new IndexOutOfBoundsException();
		}

		Node cur = head.next;

		while (pos > cur.count - 1) {
			pos -= cur.count;
			cur = cur.next;
		}

		if (cur.next == tail && cur.count == 1) {
			tail.previous = cur.previous;
			cur.previous.next = tail;
			size--;
		} else if (cur.next == tail || cur.count > nodeSize / 2) {
			cur.removeItem(pos);
			size--;
		} else {
			Node successor = cur.next;
			if (successor.count > nodeSize / 2) {
				cur.removeItem(pos);
				cur.addItem(cur.count, successor.data[0]);
				successor.removeItem(0);
				size--;
			} else {
				cur.removeItem(pos);
				for (int i = 0, j = successor.count; i < j; i++) {
					cur.addItem(cur.count, successor.data[0]);
					successor.removeItem(0);
				}
				cur.next = successor.next;
				successor.next.previous = cur;
				size--;
			}
		}

		return null;
	}

	/**
	 * Sort all elements in the stout list in the NON-DECREASING order. You may do
	 * the following. Traverse the list and copy its elements into an arr, deleting
	 * every visited node along the way. Then, sort the arr by calling the
	 * insertionSort() method. (Note that sorting efficiency is not a concern for
	 * this project.) Finally, copy all elements from the arr back to the stout
	 * list, creating new nodes for storage. After sorting, all nodes but (possibly)
	 * the last one must be full of elements.
	 * 
	 * Comparator<E> must have been implemented for calling insertionSort().
	 */
	public void sort() {
		if (size == 0) {
			return;
		}
		
		@SuppressWarnings("unchecked")
		E[] arr = (E[]) new Comparable[size];
		Comparator<E> comp = (p1, p2) -> p1.compareTo(p2);

		array(arr);
		insertionSort(arr, comp);
		newList(arr);
	}

	/**
	 * Sort all elements in the stout list in the NON-INCREASING order. Call the
	 * bubbleSort() method. After sorting, all but (possibly) the last nodes must be
	 * filled with elements.
	 * 
	 * Comparable<? super E> must be implemented for calling bubbleSort().
	 */
	public void sortReverse() {
		if (size == 0) {
			return;
		}

		@SuppressWarnings("unchecked")
		E[] arr = (E[]) new Comparable[size];

		array(arr);
		bubbleSort(arr);
		newList(arr);
	}
	
	/**
	 * helper method for copying elements from array into a new list
	 * @param arr
	 */
	public void newList(E[] arr) {
		head.next = tail;
		tail.previous = head;
		size = 0;
		Node cur = head;

		for (int i = 0; i < arr.length; i += nodeSize) {
			Node temp = new Node();
			for (int j = 0; j < nodeSize && i + j < arr.length; j++) {
				temp.data[j] = arr[i + j];
				temp.count++;
			}
			temp.next = tail;
			temp.previous = cur;
			cur.next = temp;
			tail.previous = temp;
			cur = temp;
			size += temp.count;
		}
	}
	
	/**
	 * helper method for copying elements from a list into an array
	 * @param arr
	 */
	public void array(E[] arr) {
		int index = 0;
		Node cur = head.next;

		while (cur != tail) {
			for (int i = 0; i < cur.count; i++) {
				arr[index++] = cur.data[i];
			}
			Node next = cur.next;
			cur.previous.next = cur.next;
			cur.next.previous = cur.previous;
			cur = next;
		}
	}
	
	/**
	 * makes new iterator
	 */
	@Override
	public Iterator<E> iterator() {
		return new StoutListIterator();
	}
	
	/**
	 * makes new listiterator
	 */
	@Override
	public ListIterator<E> listIterator() {
		return new StoutListIterator();
	}
	
	/**
	 * makes new listiterator with curser at specific index
	 */
	@Override
	public ListIterator<E> listIterator(int index) {
		return new StoutListIterator(index);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes.
	 */
	public String toStringInternal() {
		return toStringInternal(null);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes and the position of the iterator.
	 *
	 * @param iter an iterator for this list
	 */
	public String toStringInternal(ListIterator<E> iter) {
		int count = 0;
		int position = -1;
		if (iter != null) {
			position = iter.nextIndex();
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node pos = head.next;
		while (pos != tail) {
			sb.append('(');
			E data = pos.data[0];
			if (data == null) {
				sb.append("-");
			} else {
				if (position == count) {
					sb.append("| ");
					position = -1;
				}
				sb.append(data.toString());
				++count;
			}

			for (int i = 1; i < nodeSize; ++i) {
				sb.append(", ");
				data = pos.data[i];
				if (data == null) {
					sb.append("-");
				} else {
					if (position == count) {
						sb.append("| ");
						position = -1;
					}
					sb.append(data.toString());
					++count;

					// iterator at end
					if (position == size && count == size) {
						sb.append(" |");
						position = -1;
					}
				}
			}
			sb.append(')');
			pos = pos.next;
			if (pos != tail)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Node type for this list. Each node holds a maximum of nodeSize elements in an
	 * arr. Empty slots are null.
	 */
	private class Node {
		/**
		 * arr of actual data elements.
		 */
		// Unchecked warning unavoidable.
		@SuppressWarnings("unchecked")
		public E[] data = (E[]) new Comparable[nodeSize];

		/**
		 * Link to next node.
		 */
		public Node next;

		/**
		 * Link to previous node;
		 */
		public Node previous;

		/**
		 * Index of the next available offset in this node, also equal to the number of
		 * elements in this node.
		 */
		public int count; // a.k.a offset

		/**
		 * Adds an item to this node at the first available offset. Precondition: count
		 * < nodeSize
		 * 
		 * @param item element to be added
		 */
		void addItem(E item) {
			if (count < nodeSize) {
				data[count++] = item;
			}

			// useful for debugging
			// System.out.println("Added " + item.toString() + " at index " + count + " to
			// node " + arrs.toString(data));
		}

		/**
		 * Adds an item to this node at the indicated offset, shifting elements to the
		 * right as necessary.
		 * 
		 * Precondition: count < nodeSize
		 * 
		 * @param offset arr index at which to put the new element
		 * @param item   element to be added
		 */
		void addItem(int offset, E item) {
			if (count >= nodeSize) {
				return;
			}
			for (int i = count - 1; i >= offset; --i) {
				data[i + 1] = data[i];
			}
			++count;
			data[offset] = item;
			// useful for debugging
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + arrs.toString(data));
		}

		/**
		 * Deletes an element from this node at the indicated offset, shifting elements
		 * left as necessary. Precondition: 0 <= offset < count
		 * 
		 * @param offset
		 */
		void removeItem(int offset) {
			@SuppressWarnings("unused")
			E item = data[offset];
			for (int i = offset + 1; i < nodeSize; ++i) {
				data[i - 1] = data[i];
			}
			data[count - 1] = null;
			--count;
		}
	}

	private class StoutListIterator implements ListIterator<E> {
		/**
		 * node for keeping track of the position
		 * I usually name all of my nodes like this but had to change it a bit here and there
		 * to cur for current because some methods took an argument called pos and it got annoying
		 */
		private Node pos;
		
		/**
		 * keeps track of the outside index
		 * 		(A, B, -, -) (C, -, -, -)
		 * index 0  1         2  
		 */
		private int index;
		
		/**
		 * keeps track of the inside index for nodes
		 * 		(A, B, -, -) (C, D, E, F)
		 * index 0  1         0  1  2  3
		 */
		private int innerIndex;
		
		/**
		 * used for keeping track of last movement 
		 */
		private boolean next;
		
		/**
		 * used for keeping track of last movement 
		 */
		private boolean prev;

		/**
		 * Default constructor
		 */
		public StoutListIterator() {
			pos = head.next;
		}

		/**
		 * Constructor finds node at a given position.
		 * 
		 * @param pos
		 */
		public StoutListIterator(int pos) {
			if (pos < 0 || pos >= size) {
				throw new IndexOutOfBoundsException();
			}
			this.pos = head.next;

			index = 0;

			// Search for correct node
			while (index + this.pos.count <= pos) {
				index += this.pos.count;
				this.pos = this.pos.next;
			}

			// Set innerIndex to the correct position
			// Literally spent waaay too much time figuring this out
			innerIndex = pos - index;
			index = pos;
			next = false;
			prev = false;
		}
		
		/**
		 * checks to see if the list has a next element
		 */
		@Override
		public boolean hasNext() {
			if (index < size) {
				return true;
			}
			return false;
		}
		
		/**
		 * moves the cursor to the next element
		 */
		@Override
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			next = true;
			prev = false;

			E item = pos.data[innerIndex++];

			if (innerIndex == pos.count) {
				pos = pos.next;
				innerIndex = 0;

			}
			index++;

			return item;
		}
		
		/**
		 * removes an element from the list at the cursor based on its last movement
		 * using the list add method for all the math stuff
		 */
		@Override
		public void remove() {
			if (!next && !prev) {
				throw new IllegalStateException();
			}
			
			if (next) {
				StoutList.this.remove(--index);
			} else {
				StoutList.this.remove(index);
			}

			next = false;
			prev = false;
		}

		/**
		 * checks to see if the list has a previous element
		 */
		@Override
		public boolean hasPrevious() {
			if (index > 0) {
				return true;
			}
			return false;
		}

		/**
		 * moves the cursor to the previous element
		 */
		@Override
		public E previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}

			next = false;
			prev = true;

			if (innerIndex == 0) {
				pos = pos.previous;
				innerIndex = pos.count;
			}

			E item = pos.data[--innerIndex];
			index--;

			return item;
		}

		/**
		 * gets the next index
		 */
		@Override
		public int nextIndex() {
			return index;
		}
		
		/**
		 * gets the previous index
		 */
		@Override
		public int previousIndex() {
			return index - 1;
		}

		/**
		 * changes the element to a different element based on the cursor location and its movement
		 */
		@Override
		public void set(E e) {
			if (!next && !prev) {
				throw new IllegalStateException();
			}

			if (next) {
				if (innerIndex - 1 < 0) {
					pos.previous.data[pos.previous.count - 1] = e;
				} else {
					pos.data[innerIndex - 1] = e;
				}
			} else {
				pos.data[innerIndex] = e;
			}
		}

		/**
		 * adds an element to the list at the cursor based on its last movement
		 * using the list add method for all the math stuff
		 */
		@Override
		public void add(E e) {
			if (!next && !prev) {
				throw new IllegalStateException();
			}
			
			if (next) {
				StoutList.this.add(index++, e);
			} else {
				StoutList.this.add(index, e);
			}

			next = false;
			prev = false;
		}
	}

	/**
	 * Sort an arr arr[] using the insertion sort algorithm in the NON-DECREASING
	 * order.
	 * 
	 * @param arr  arr storing elements from the list
	 * @param comp comparator used in sorting
	 */
	private void insertionSort(E[] arr, Comparator<? super E> comp) {
		for (int i = 1; i < arr.length; i++) {
			E key = arr[i];
			int j = i - 1;
			while (j >= 0 && arr[j].compareTo(key) > 0) {
				arr[j + 1] = arr[j];
				j = j - 1;
			}
			arr[j + 1] = key;
		}
	}

	/**
	 * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a
	 * description of bubble sort please refer to Section 6.1 in the project
	 * description. You must use the compareTo() method from an implementation of
	 * the Comparable interface by the class E or ? super E.
	 * 
	 * @param arr arr holding elements from the list
	 */
	private void bubbleSort(E[] arr) {
		int n = arr.length;
        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < n; i++) {
                if (arr[i - 1].compareTo(arr[i]) < 0) {
                    E temp = arr[i - 1];
                    arr[i - 1] = arr[i];
                    arr[i] = temp;
                    swapped = true;
                }
            }
            n--;
        } while (swapped);
	}

}