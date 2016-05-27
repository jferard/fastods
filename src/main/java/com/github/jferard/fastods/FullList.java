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
 * The class FullList represents a List that is unlimited.
 *
 */
public class FullList<E> implements List<E> {
	public static <E> List<E> newList() {
		return new FullList<E>(null);
	}
	
	public static <E> List<E> newList(E e) {
		return new FullList<E>(e);
	}

	private final E blankElement;

	private final LinkedList<E> list;

	private FullList(final E blankElement) {
		this.blankElement = blankElement;
		this.list = new LinkedList<E>();
	}

	private FullList(final E blankElement, final LinkedList<E> list) {
		this.blankElement = blankElement;
		this.list = list;
	}

	@Override
	public boolean add(final E e) {
		return this.list.add(e);
	}

	@Override
	public void add(final int index, final E element) {
		if (element != this.blankElement) {
			this.addMissingBlanks(index);
			this.list.add(index, element);
		}
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) {
		final int sizeBefore = this.list.size();
		this.list.addAll(c);
		this.removeTrail();
		return this.list.size() != sizeBefore;
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends E> c) {
		final int sizeBefore = this.list.size();
		this.addMissingBlanks(index);
		this.list.addAll(index, c);
		this.removeTrail();
		return this.list.size() != sizeBefore;
	}

	@Override
	public void clear() {
		this.list.clear();
	}

	@Override
	public Object clone() {
		return this.list.clone();
	}

	@Override
	public boolean contains(final Object o) {
		return o == this.blankElement || this.list.contains(o);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return this.list.containsAll(c);
	}

	@Override
	public boolean equals(final Object o) {
		return this.list.equals(o);
	}

	@Override
	public E get(final int index) {
		if (index >= this.list.size())
			return this.blankElement;
		return this.list.get(index);
	}

	@Override
	public int hashCode() {
		return this.list.hashCode();
	}

	@Override
	public int indexOf(final Object o) {
		final int index = this.list.indexOf(o);
		if (index == -1 && o == this.blankElement)
			return this.list.size();
		else
			return index;
	}

	@Override
	public boolean isEmpty() {
		return this.list.size() == 0;
	}

	@Override
	public Iterator<E> iterator() {
		return this.list.iterator();
	}

	@Override
	public int lastIndexOf(final Object o) {
		final int index = this.list.lastIndexOf(o);
		if (index == -1 && o == this.blankElement)
			return this.list.size();
		else
			return index;
	}

	@Override
	public ListIterator<E> listIterator() {
		return this.list.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(final int index) {
		return this.list.listIterator(index);
	}

	@Override
	public E remove(final int index) {
		if (index >= this.list.size())
			return this.blankElement;

		return this.list.remove(index);
	}

	@Override
	public boolean remove(final Object o) {
		return this.list.remove(o);
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		return this.list.removeAll(c);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		return this.list.retainAll(c);
	}

	@Override
	public E set(final int index, final E element) {
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

	@Override
	public int size() {
		return this.list.size();
	}

	@Override
	public Spliterator<E> spliterator() {
		return this.list.spliterator();
	}

	@Override
	public List<E> subList(final int fromIndex, final int toIndex) {
		return new FullList<E>(this.blankElement,
				(LinkedList<E>) this.list.subList(fromIndex, toIndex));
	}

	@Override
	public Object[] toArray() {
		return this.list.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return this.list.toArray(a);
	}

	@Override
	public String toString() {
		return this.list.toString();
	}

	/** this.list.size() == index */
	private void addMissingBlanks(final int index) {
		final int count = index - this.list.size();
		for (int i = 0; i < count; i++)
			this.list.add(this.blankElement);
	}

	private void removeTrail() {
		while (this.list.size() >= 1
				&& this.list.get(this.list.size() - 1) == this.blankElement)
			this.list.removeLast();
	}
}
