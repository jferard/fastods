/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.fastods.util;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file UniqueList.java is part of FastODS.
 *
 */
public class UniqueList<T extends NamedObject> extends AbstractList<T>
		implements List<T> {
	private final Map<String, T> elementByName;
	private final List<T> list;

	public UniqueList() {
		this.list = new LinkedList<T>();
		this.elementByName = new HashMap<String, T>();
	}

	@Override
	public void add(final int index, final T element) {
		final String elementName = element.getName();
		if (this.elementByName.containsKey(elementName))
			throw new IllegalArgumentException(
					"Element " + element + " already in list");

		this.elementByName.put(elementName, element);
		this.list.add(index, element);
	}

	@Override
	public T get(final int index) {
		return this.list.get(index);
	}

	public T get(final Object o) {
		return this.elementByName.get(o);
	}

	public Set<String> keySet() {
		return this.elementByName.keySet();
	}

	@Override
	public T remove(final int index) {
		final T element = this.list.remove(index);
		this.elementByName.remove(element.getName());
		return element;
	}

	@Override
	public boolean remove(final Object o) {
		if (this.elementByName.containsKey(o)) {
			final T t = this.elementByName.get(o);
			this.elementByName.remove(o);
			this.list.remove(t);
			return true;
		} else
			return false;
	}

	@Override
	public T set(final int index, final T element) {
		final String elementName = element.getName();
		if (this.elementByName.containsKey(elementName))
			throw new IllegalArgumentException(
					"Element " + elementName + " already in list");

		this.elementByName.put(elementName, element);
		return this.list.set(index, element);
	}

	@Override
	public int size() {
		return this.list.size();
	}
}
