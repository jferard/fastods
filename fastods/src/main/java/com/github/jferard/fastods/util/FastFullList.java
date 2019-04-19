/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FastFullList<E> implements Iterable<E> {
    private static final int DEFAULT_CAPACITY = 10;

    public static <F> FastFullList<F> newListWithCapacity(final int capacity) {
        return new FastFullList<F>(capacity, null);
    }

    public static <F> FastFullListBuilder<F> builder() {
        return new FastFullListBuilder<F>();
    }

    public static <F> FastFullList<F> newList(final F... elements) {
        final FastFullList<F> l = new FastFullList<F>(FastFullList.DEFAULT_CAPACITY, null);
        for (int i = 0; i < elements.length; i++) {
            l.set(i, elements[i]);
        }
        return l;

    }

    public static class FastFullListBuilder<F> {
        private F blankElement;
        private int capacity;

        public FastFullListBuilder<F> blankElement(final F blankElement) {
            this.blankElement = blankElement;
            return this;
        }

        public FastFullListBuilder<F> capacity(final int capacity) {
            this.capacity = capacity;
            return this;
        }

        public FastFullList<F> build() {
            return new FastFullList<F>(this.capacity, this.blankElement);
        }
    }

    private final E blankElement;
    private int capacity;
    private E[] arr;
    private int size;

    /**
     * @param capacity     the capacity, >= 10
     * @param blankElement
     */
    public FastFullList(final int capacity, final E blankElement) {
        this.capacity = capacity;
        this.blankElement = blankElement;
        this.arr = (E[]) new Object[this.capacity];
        this.size = 0;
    }

    public int usedSize() {
        return this.size;
    }

    public void set(final int index, final E element) {
        final int lastIndex = this.size - 1;
        if (index > lastIndex) { // index >= this.size
            if (element != this.blankElement) {
                this.addMissingBlanks(index);
                this.arr[index] = element;
            }
        } else if (index == lastIndex) {
            if (element == this.blankElement) {
                this.removeTrail();
            } else {
                this.arr[index] = element;
            }
        } else if (index < lastIndex) {
            this.arr[index] = element;
        }
    }

    /**
     * Postcondition: this.size == index + 1
     */
    private void addMissingBlanks(final int index) {
        if (this.capacity <= index) {
            this.capacity = index * 2;
            final E[] newArr = (E[]) new Object[this.capacity];
            System.arraycopy(this.arr, 0, newArr, 0, this.size);
            this.arr = newArr;
        }
        if (this.blankElement != null) {
            Arrays.fill(this.arr, this.size, index, this.blankElement);
        }
        this.size = index + 1;
    }

    /**
     * Postcondition: this.list.get(this.list.size()-1) != blankElement
     */
    private void removeTrail() {
        int last = this.size - 1;
        while (last >= 0 && this.arr[last] == this.blankElement) {
            last--;
        }
        this.size = last + 1;
    }

    public E get(final int index) {
        if (index < this.size) {
            return this.arr[index];
        } else {
            return this.blankElement;
        }
    }

    public List<E> subList(final int fromIndex, final int toIndex) {
        return Arrays.asList(this.arr).subList(fromIndex, toIndex);
    }

    @Override
    public Iterator<E> iterator() {
        return Arrays.asList(this.arr).subList(0, this.size).iterator();
    }
}
