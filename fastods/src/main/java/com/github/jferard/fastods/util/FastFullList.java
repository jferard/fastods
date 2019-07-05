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

/**
 * A FullList is similar to a list, but is infinite. Every element that was not set
 * has a special blank value.
 *
 * @param <E> type of the elements
 * @author J. Férard
 */
public class FastFullList<E> implements Iterable<E> {
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * @param capacity the capacity needed
     * @param <F>      the type of the elements
     * @return a new full list
     */
    public static <F> FastFullList<F> newListWithCapacity(final int capacity) {
        return new FastFullList<F>(capacity, null);
    }

    /**
     * @param <F> the type of the elements
     * @return a builder
     */
    public static <F> FastFullListBuilder<F> builder() {
        return new FastFullListBuilder<F>();
    }

    /**
     * @param elements the elements
     * @param <F>      the type of the elements
     * @return a new full list starting with the elements
     */
    public static <F> FastFullList<F> newList(final F... elements) {
        final FastFullList<F> l = new FastFullList<F>(FastFullList.DEFAULT_CAPACITY, null);
        for (int i = 0; i < elements.length; i++) {
            l.set(i, elements[i]);
        }
        return l;

    }

    /**
     * @param <F> the type of the elements
     */
    public static class FastFullListBuilder<F> {
        private F blankElement;
        private int capacity;

        /**
         * Set the blank element
         *
         * @param blankElement the blank element
         * @return this for fluent style
         */
        public FastFullListBuilder<F> blankElement(final F blankElement) {
            this.blankElement = blankElement;
            return this;
        }

        /**
         * @param capacity the need capacity
         * @return this for fluent style
         */
        public FastFullListBuilder<F> capacity(final int capacity) {
            this.capacity = capacity;
            return this;
        }

        /**
         * @return the full list
         */
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
     * @param blankElement the value of cells that were not set.
     */
    @SuppressWarnings("unchecked")
    public FastFullList(final int capacity, final E blankElement) {
        this.capacity = capacity;
        this.blankElement = blankElement;
        this.arr = (E[]) new Object[this.capacity];
        this.size = 0;
    }

    /**
     * @return the size really used, that is the index of the last non blank element + 1.
     */
    public int usedSize() {
        return this.size;
    }

    /**
     * Set an element
     *
     * @param index   the index
     * @param element the element
     */
    public void set(final int index, final E element) {
        final int lastIndex = this.size - 1;
        if (index < lastIndex) {
            this.arr[index] = element;
        } else if (index > lastIndex) { // index >= this.size
            if (element != this.blankElement) {
                this.addMissingBlanks(index);
                this.arr[index] = element;
            }
        } else {
            if (element == this.blankElement) {
                this.removeTrail();
            } else {
                this.arr[index] = element;
            }
        }
    }

    /**
     * post condition: this.size == index + 1
     */
    @SuppressWarnings("unchecked")
    private void addMissingBlanks(final int index) {
        if (this.capacity <= index) {
            this.capacity = index * 2 + 1;
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
     * post condition: this.list.get(this.list.size()-1) != blankElement
     */
    private void removeTrail() {
        int last = this.size - 2;
        while (last >= 0 && this.arr[last] == this.blankElement) {
            last--;
        }
        this.size = last + 1;
    }

    /**
     * @param index the index
     * @return the element at the index
     */
    public E get(final int index) {
        if (index < this.size) {
            return this.arr[index];
        } else {
            return this.blankElement;
        }
    }

    /**
     * @param fromIndex starting index
     * @param toIndex   last index + 1
     * @return the sublist view.
     */
    public List<E> subList(final int fromIndex, final int toIndex) {
        return Arrays.asList(this.arr).subList(fromIndex, toIndex);
    }

    @Override
    public Iterator<E> iterator() {
        return Arrays.asList(this.arr).subList(0, this.size).iterator();
    }
}
