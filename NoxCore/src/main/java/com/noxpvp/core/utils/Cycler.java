package com.noxpvp.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class Cycler<E> implements ListIterator<E>{
	private int index;
	
	private ArrayList<E> data;
	
	public Cycler(Collection<E> da)
	{
		data = new ArrayList<E>(da);
		index = 0;
	}
	
	public Cycler(int size)
	{
		data = new ArrayList<E>(size);
		index = 0;
	}
	
	public List<E> getList() { return data; }
	
	/*
	 * Warning this method will cause it to loop infinite on iteration.
	 * 
	 * YOU MUST BREAK THE LOOP YOURSELF!
	 * 
	 * (non-Javadoc)
	 * @see java.util.ListIterator#hasNext()
	 */
	public boolean hasNext() {
		return true;
	}
	
	public E peekNext() {
		return data.get(nextIndex());
	}

	public E next() {
		index = nextIndex();
		return data.get(index);
	}

	/*
	 * Warning this method will cause it to loop infinitely on iteration.
	 * 
	 * YOU MUST BREAK THE LOOP YOURSELF!
	 * 
	 * (non-Javadoc)
	 * @see java.util.ListIterator#hasPrevious()
	 */
	public boolean hasPrevious() {
		return true;
	}

	public E peekPrevious() {
		return data.get(previousIndex());
	}
	
	public E previous() {
		index = previousIndex();
		return data.get(index);
	}

	public int nextIndex() {
		return index >= (data.size() - 1) ? 0 : (index + 1);
	}

	public int previousIndex() {
		return index <= 0 ? (data.size()-1) : (index - 1);
	}

	public void remove() {
		data.remove(index);
		if (data.size() <= index)
			index = data.size() - 1;
	}

	public void set(E e) {
		data.set(index, e);
	}

	public void add(E e) {
		data.add(index, e);
	}

}
