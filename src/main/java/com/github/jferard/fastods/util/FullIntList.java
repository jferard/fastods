/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
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
 * ****************************************************************************/
package com.github.jferard.fastods.util;

import java.util.Arrays;
import java.util.RandomAccess;

public class FullIntList implements RandomAccess {
	private int[] arr;
	private int size;

	/**
	 * Empty list with the specified initial capacity.
	 */
	public FullIntList(final int capacity) {
		if (capacity < 0)
			throw new IllegalArgumentException("Capacity cant't be negative: "
					+ capacity);
		this.arr = new int[capacity];
		this.size = 0;
	}

	/**
	 * Constructs an empty list with an initial capacity of twenty ints.
	 */
	public FullIntList() {
		this.arr = new int[20];
		this.size = 0;
	}

	/**
	 */
	public int size() {
		return this.size;
	}

	/**
	 */
	public boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 */
	public int get(final int index) {
		if (index >= this.size)
			return 0;
		return this.arr[index];
	}

	/**
	 */
	public int set(final int index, int element) {
		int p;
		if (index >= this.size) {
			p = 0;
			if (element != 0) {
				int capacity = this.arr.length;
				if (index >= capacity) {
					do {
						capacity += capacity >> 1;
					} while (index >= capacity);
					this.arr = Arrays.copyOf(this.arr, capacity);
				}
				this.arr[index] = element;
				this.size = index+1;
			}
		} else {
			p = this.arr[index];
			if (element == 0 && index == this.size - 1) { // remove trailing elements
				do {
					this.size--;
				} while (this.size >= 1 && this.arr[this.size - 1] == 0);
			} else {
				this.arr[index] = element;
			}
		}
		return p;
	}
}
