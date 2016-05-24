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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file FullList.java is part of FastODS.
 *
 */

public class FullList<E> implements List<E> {
	private final LinkedList<E> list;
	private E blankElement;

	private FullList(final E blankElement) {
		this.blankElement = blankElement;
		this.list = new LinkedList<E>();
	}

	private FullList(final E blankElement, LinkedList<E> list) {
		this.blankElement = blankElement;
		this.list = list;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean contains(Object o) {
		return o == this.blankElement || this.list.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return this.list.iterator();
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(E e) {
		return this.list.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return this.list.remove(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return this.list.listIterator();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		int sizeBefore = this.list.size();
		this.list.addAll(c);
		removeTrail();
		return this.list.size() != sizeBefore;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		int sizeBefore = this.list.size();
		this.addMissingBlanks(index);
		this.list.addAll(index, c);
		removeTrail();
		return this.list.size() != sizeBefore;
	}

	private void removeTrail() {
		while (this.list.size() >= 1
				&& this.list.get(this.list.size() - 1) == this.blankElement)
			this.list.removeLast();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.list.retainAll(c);
	}

	@Override
	public void clear() {
		this.list.clear();
	}

	@Override
	public E get(int index) {
		if (index >= this.list.size())
			return this.blankElement;
		return this.list.get(index);
	}

	@Override
	public E set(int index, E element) {
		E result;
		final int lastIndex = this.list.size() - 1;
		if (index < lastIndex)
			result = this.list.set(index, element);
		else if (index == lastIndex) { // last element
			if (element == this.blankElement) {
				result = this.list.removeLast();
				this.removeTrail();
			} else
				result = this.list.set(index, element);
		} else {
			result = this.blankElement;
			if (element != this.blankElement) {
				this.addMissingBlanks(index - 1);
				this.list.add(element);
			}
		}
		return result;
	}

	/** this.list.size() == index */
	private void addMissingBlanks(int index) {
		final int count = index - this.list.size();
		for (int i = 0; i < count; i++)
			this.list.add(this.blankElement);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return new FullList<E>(this.blankElement,
				(LinkedList<E>) this.list.subList(fromIndex, toIndex));
	}

	@Override
	public void add(int index, E element) {
		if (element != this.blankElement) {
			this.addMissingBlanks(index);
			this.list.add(index, element);
		}
	}

	@Override
	public String toString() {
		return this.list.toString();
	}

	@Override
	public E remove(int index) {
		if (index >= this.list.size())
			return this.blankElement;

		return this.list.remove(index);
	}

	@Override
	public boolean equals(Object o) {
		return this.list.equals(o);
	}

	@Override
	public int indexOf(Object o) {
		int index = this.list.indexOf(o);
		if (index == -1 && o == this.blankElement)
			return this.list.size();
		else
			return index;
	}

	@Override
	public int lastIndexOf(Object o) {
		int index = this.list.lastIndexOf(o);
		if (index == -1 && o == this.blankElement)
			return this.list.size();
		else
			return index;
	}

	@Override
	public int hashCode() {
		return this.list.hashCode();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return this.list.listIterator(index);
	}

	@Override
	public Object clone() {
		return this.list.clone();
	}

	@Override
	public Object[] toArray() {
		return this.list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.list.toArray(a);
	}

	@Override
	public Spliterator<E> spliterator() {
		return this.list.spliterator();
	}

	public static <E> List<E> newList() {
		return new FullList<E>(null);
	}
}
