/*
*	SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
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
package org.simpleods;

import java.util.LinkedList;

/**
 * @author Martin Schulz<br>
 * 
 * Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net><br>
 * 
 * This file ObjectQueue.java is part of SimpleODS.
 *
 */
public class ObjectQueue {
	/**
	 * The list with all objects.
	 */
	private LinkedList list = new LinkedList();

	/**
	 * Appends the specified element to the end of this list.
	 * 
	 * @param o
	 *            element to be appended to this list
	 * @return true
	 */
	public boolean add(final Object o) {
		list.add(o);
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
	private boolean set(final int n, final Object o) {
		list.set(n, o);
		return true;
	}

	/**
	 * Set an object at position n.<br>
	 * If n is > the number of current elements, 'null' elements are added to fill up the list.<br>
	 * If n is inside the current number of element, the element at position n is replaced.<br>
	 * No Exception is thrown when n is out of range.
	 * 
	 * @param n
	 *            index of the element to replace.
	 * @param o
	 *            element to be stored at the specified position.
	 * @return true - object was set,<br>
	 *         false - n is invalid, object not set
	 */
	public boolean setAt(final int n, final Object o) {
		if (n < 0) {
			return false;
		}

		if (n >= list.size()) {
			for (int x = list.size(); x <= n; x++) {
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
	 * @param n The position of the object to be returned.
	 * @return The object at position n
	 */
	public Object get(final int n) {
		if (n < 0 || n >= list.size()) {
			return null;
		}
		return (list.get(n));
	}
	
	/**
	 * @return The number of object in this ObjectQueue
	 */
	public int size() {
		return list.size();
	}
	
	/**
	 * Print all objects in this ObjectQueue to System.out.
	 */
	public void printAll() {
		for (int n = 0; n < list.size(); n++) {
			Object o = (Object) list.get(n);

			if (o == null) {
				System.out.println(n + "==null");
			} else {
				System.out.println(n);
			}
		}
	}
	
}
