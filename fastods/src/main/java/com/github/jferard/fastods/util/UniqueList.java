/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods.util;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A UniqueList is an ordered set of named objects : ordered as a list, but with
 * no duplicates names. If a new object having a name that is the name of an
 * object already present in the list is inserted with {@code add} or
 * {@code set}, then an {@code IllegalArgumentException} is thrown.
 *
 * @param <T> a NamedObject type.
 * @author Julien Férard
 */
public class UniqueList<T extends NamedObject> extends AbstractList<T> implements List<T> {
    private final Map<String, T> elementByName;
    private final List<T> list;

    /**
     * Creates the list
     */
    public UniqueList() {
        this.list = new LinkedList<T>();
        this.elementByName = new HashMap<String, T>();
    }

    /*
     * @see java.util.List#add(int, java.lang.Object)
     * @throws IllegalArgumentException if the element exists in the list
     */
    @Override
    public void add(final int index, final T element) {
        final String elementName = element.getName();
        if (this.elementByName.containsKey(elementName)) {
            throw new IllegalArgumentException("Element " + element + " already in list");
        }

        this.elementByName.put(elementName, element);
        this.list.add(index, element);
    }

    @Override
    public T get(final int index) {
        return this.list.get(index);
    }

    /**
     * @param name the name
     * @return the element of the list that has the name.
     */
    public T getByName(final String name) {
        return this.elementByName.get(name);
    }

    /**
     * @return the name set of the objects stored in the list
     */
    public Set<String> nameSet() {
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
        if (this.elementByName.containsValue(o)) {
            this.elementByName.remove(((NamedObject) o).getName());
            this.list.remove(o);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param name the name of the element to remove
     * @return the previous element of that name, null otherwise.
     */
    public T removeByName(final String name) {
        if (this.elementByName.containsKey(name)) {
            final T t = this.elementByName.get(name);
            this.elementByName.remove(name);
            this.list.remove(t);
            return t;
        } else {
            return null;
        }
    }

    @Override
    public T set(final int index, final T element) {
        final String elementName = element.getName();
        if (this.elementByName.containsKey(elementName)) {
            throw new IllegalArgumentException("Element " + elementName + " already in list");
        }

        this.elementByName.put(elementName, element);
        return this.list.set(index, element);
    }

    @Override
    public int size() {
        return this.list.size();
    }
}
