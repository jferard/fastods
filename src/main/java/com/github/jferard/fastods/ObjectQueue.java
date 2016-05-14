/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
*    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.jferard.fastods;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Martin Schulz<br>
 * 
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *         <br>
 * 
 *         This file ObjectQueue.java is part of SimpleODS.
 *
 */
public class ObjectQueue<T> {
	/** Guava's like creator 
	 * @return the newly created ObjectQueue
	 */
	public static <U> ObjectQueue<U> newQueue() {
		return new ObjectQueue<U>();
	}
	
	/**
	 * The list with all objects.
	 */
	private List<T> list;
	
	private ObjectQueue() {
		this.list = new LinkedList<T>();
	}

	/**
	 * Appends the specified element to the end of this list.
	 * 
	 * @param o
	 *            element to be appended to this list
	 * @return true
	 */
	public boolean add(final T o) {
		this.list.add(o);
		return true;
	}

	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element.
	 * 
	 * @param n
	 *            index of the element to replace.
	 * @param o
	 *            element to be stored at the specified position.
	 * @return true
	 */
	private boolean set(final int n, final T o) {
		this.list.set(n, o);
		return true;
	}

	/**
	 * Set an object at position n.<br>
	 * If n is > the number of current elements, 'null' elements are added to
	 * fill up the list.<br>
	 * If n is inside the current number of element, the element at position n
	 * is replaced.<br>
	 * No Exception is thrown when n is out of range.
	 * 
	 * @param n
	 *            index of the element to replace.
	 * @param o
	 *            element to be stored at the specified position.
	 * @return true - object was set,<br>
	 *         false - n is invalid, object not set
	 */
	public boolean setAt(final int n, final T o) {
		if (n < 0) {
			return false;
		}

		if (n >= this.list.size()) {
			for (int x = this.list.size(); x <= n; x++) {
				this.add(null);
			}
			this.set(n, o);
			return true;
		}
		this.set(n, o);
		return true;
	}

	/**
	 * Get the object at position n.<br>
	 * Return null if n is outside of the list.
	 * 
	 * @param n
	 *            The position of the object to be returned.
	 * @return The object at position n
	 */
	public T get(final int n) {
		if (n < 0 || n >= this.list.size()) {
			return null;
		}
		return (this.list.get(n));
	}

	/**
	 * @return The number of object in this ObjectQueue
	 */
	public int size() {
		return this.list.size();
	}

	/**
	 * Print all objects in this ObjectQueue to System.out.
	 */
	public void printAll() {
		for (int n = 0; n < this.list.size(); n++) {
			T o = this.list.get(n);

			if (o == null) {
				System.out.println(n + "==null");
			} else {
				System.out.println(n);
			}
		}
	}

}
