package edu.iastate.cs228.hw2;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

class StoutListTest {

	@Test
	void test() {
		assertEquals(2, 2);
		StoutList<Integer> list = new StoutList<>(4);
		list.add(2);
		list.add(10);
		list.add(null);
		list.add(1);
		list.add(5);
		list.add(20);
		list.add(11);
		list.add(2);
		System.out.println(list.toStringInternal());
		System.out.println(list.contains(5));
		System.out.println(list.contains(12));
		System.out.println(list.toString());
		
		ListIterator<Integer> iter = list.listIterator();
		try {
			iter.set(6);
		} catch(IllegalStateException e) {
			System.out.println("Nothing to set");
		}
		System.out.println(list.toStringInternal(iter));
		iter.next();
		System.out.println(list.toStringInternal(iter));
		iter.next();
		System.out.println(list.toStringInternal(iter));
		iter.next();
		System.out.println(list.toStringInternal(iter));
		iter.next();
		System.out.println(list.toStringInternal(iter));
		iter.set(25);
		System.out.println(list.toStringInternal(iter));
		iter.next();
		System.out.println(list.toStringInternal(iter));
		iter.set(55);
		System.out.println(list.toStringInternal(iter));
		iter.next();
		System.out.println(list.toStringInternal(iter));
		iter.set(8);
		System.out.println(list.toStringInternal(iter));
		try {
			iter.next();
			iter.next();
		} catch(NoSuchElementException e) {
			System.out.println("No more elements");
		}
		System.out.println(list.toStringInternal(iter));
		iter.previous();
		System.out.println(list.toStringInternal(iter));
		iter.previous();
		System.out.println(list.toStringInternal(iter));
		iter.previous();
		System.out.println(list.toStringInternal(iter));
		iter.previous();
		System.out.println(list.toStringInternal(iter));
		iter.previous();
		System.out.println(list.toStringInternal(iter));
		iter.previous();
		System.out.println(list.toStringInternal(iter));
		try {
			iter.previous();
			iter.previous();
		} catch(NoSuchElementException e) {
			System.out.println("No more elements");
		}
		System.out.println(list.toStringInternal(iter));
		iter.set(6);
		System.out.println(list.toStringInternal(iter));
		iter.next();
		iter.set(21);
		System.out.println(list.toStringInternal(iter));
		System.out.println(list.toStringInternal());
		
		iter = list.listIterator(2);
		try {
			iter = list.listIterator(7);
		} catch(IndexOutOfBoundsException e) {
			System.out.println("Index out of bounds");
		}
		System.out.println(list.toStringInternal(iter));
		iter.previous();
		System.out.println(list.toStringInternal(iter));
		iter.set(30);
		System.out.println(list.toStringInternal(iter));
		iter.next();
		System.out.println(list.toStringInternal(iter));
		iter.set(20);
		System.out.println(list.toStringInternal(iter));
		iter.previous();
		System.out.println(list.toStringInternal(iter));
		iter.set(25);
		System.out.println(list.toStringInternal(iter));
		System.out.println(list.get(1));
		System.out.println(list.get(4));
		System.out.println(list.contains(1));
		System.out.println(list.contains(6));
		
		StoutList<Character> list2 = new StoutList<>(4);
		list2.add('A');
		list2.add('B');
		list2.add(null);
		list2.add('C');
		list2.add('D');
		list2.add('E');
		System.out.println(list2.toStringInternal());
		list2.add('V');
		System.out.println(list2.toStringInternal());
		list2.add('W');
		System.out.println(list2.toStringInternal());
		ListIterator<Character> iter2 = list2.listIterator();
		System.out.println(list2.toStringInternal(iter2));
		list2.add(2, 'X');
		System.out.println(list2.toStringInternal());
		list2.add(2, 'Y');
		System.out.println(list2.toStringInternal());
		list2.add(2, 'Z');
		System.out.println(list2.toStringInternal());
		list2.add(3, 'L');
		System.out.println(list2.toStringInternal());
		list2.add(6, 'R');
		System.out.println(list2.toStringInternal());
		list2.add(0, 'M');
		System.out.println(list2.toStringInternal());
		list2.add(0, 'Q');
		System.out.println(list2.toStringInternal());
		list2.add(7, 'S');
		System.out.println(list2.toStringInternal());
		list2.add(7, 'T');
		System.out.println(list2.toStringInternal());
		try {
			list2.add(20, 'T');
		} catch(IndexOutOfBoundsException e) {
			System.out.println("Index out of bounds");
		}
		list2.remove(0);
		System.out.println(list2.toStringInternal());
		list2.remove(0);
		System.out.println(list2.toStringInternal());
		list2.remove(0);
		System.out.println(list2.toStringInternal());
		list2.remove(0);
		System.out.println(list2.toStringInternal());
		try {
			list2.remove(12);
		} catch(IndexOutOfBoundsException e) {
			System.out.println("Index out of bounds");
		}
		list2.remove(9);
		System.out.println(list2.toStringInternal());
		list2.remove(9);
		System.out.println(list2.toStringInternal());
		list2.remove(4);
		System.out.println(list2.toStringInternal());
		list2.remove(4);
		System.out.println(list2.toStringInternal());
		list2.remove(7);
		System.out.println(list2.toStringInternal());
		try {
			list2.remove(7);
		} catch(IndexOutOfBoundsException e) {
			System.out.println("Index out of bounds");
		}
//		System.out.println(list2.toStringInternal(iter2));
//		iter2.next();
//		System.out.println(list2.toStringInternal(iter2));
//		iter2.next();
//		System.out.println(list2.toStringInternal(iter2));
		System.out.println(list2.toStringInternal(iter2));
//		iter2.remove();
//		System.out.println(list2.toStringInternal(iter2));
//		iter2.previous();
//		System.out.println(list2.toStringInternal(iter2));
//		iter2.remove();
//		System.out.println(list2.toStringInternal(iter2));
		iter2.next();
		System.out.println(list2.toStringInternal(iter2));
		iter2.remove();
		System.out.println(list2.toStringInternal(iter2));
		iter2.next();
		System.out.println(list2.toStringInternal(iter2));
		iter2.next();
		System.out.println(list2.toStringInternal(iter2));
		iter2.remove();
		System.out.println(list2.toStringInternal(iter2));
		iter2.next();
		System.out.println(list2.toStringInternal(iter2));
		iter2.remove();
		System.out.println(list2.toStringInternal(iter2));
		
		iter2.next();
		System.out.println(list2.toStringInternal(iter2));
		iter2.add('Z');
		System.out.println(list2.toStringInternal(iter2));
		
		System.out.println(list.toStringInternal());
		list.sort();
		System.out.println(list.toStringInternal());
		
		System.out.println(list2.toStringInternal());
		list2.sortReverse();
		System.out.println(list2.toStringInternal());
		
	}

}
