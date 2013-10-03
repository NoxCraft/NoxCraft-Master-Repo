package com.noxpvp.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class Cycler<E> implements ListIterator<E>{
	private int index;
	
	private ArrayList<E> data;
	
	public Cycler(Collection<E> data)
	{
		this.data = new ArrayList<E>(data);
		index = 0;
	}
	
	public Cycler(int size)
	{
		data = new ArrayList<E>(size);
		index = 0;
	}
	
	public List<E> getList() { return data; }
	
	/**
	 * Retrieves the current object on the current index / iteration.
	 *
	 * @return the current object.
	 */
	public E current() { return data.get(currentIndex()); }
	
	/**
	 * Retrieves the current index.
	 * 
	 * @return 0 - (Cycler Size)
	 */
	public int currentIndex() { return index;}
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
		return data.get(currentIndex());
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
		return data.get(currentIndex());
	}

	public int nextIndex() {
		return currentIndex() >= (data.size() - 1) ? 0 : (currentIndex() + 1);
	}

	public int previousIndex() {
		return currentIndex() <= 0 ? (data.size()-1) : (currentIndex() - 1);
	}

	public void remove() {
		data.remove(currentIndex());
		if (data.size() <= currentIndex())
			index = data.size() - 1;
	}

	public void set(E e) {
		data.set(currentIndex(), e);
	}

	public void add(E e) {
		data.add(currentIndex(), e);
	}

}