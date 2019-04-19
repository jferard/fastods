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

/**
 * An util to checkStyleName equality between objects
 * @author Julien Férard
 */
public class EqualityUtil {
	/**
	 * @param o1 the first object
	 * @param o2 the second object
	 * @return true if the objects are different
	 */
	public boolean different(final Object o1, final Object o2) {
		if (o1 == null) {
			return o2 != null;
		} else {
			return !o1.equals(o2);
		}
	}

	/**
	 * @param o1 the first object
	 * @param o2 the second object
	 * @return true if the objects are equal
	 */
	public boolean equal(final Object o1, final Object o2) {
		if (o1 == null) {
			return o2 == null;
		} else {
			return o1.equals(o2);
		}
	}

	/**
	 * Hash a bunch of objects
	 * @param objects the objects
	 * @return a hash code
	 */
	public int hashObjects(final Object... objects) {
		final int prime = 31;
		int result = 1;
		for (final Object object : objects) {
			result = prime * result
					+ ((object == null) ? 0 : object.hashCode());
		}
		return result;
	}

    /**
     * Hash a bunch of integers
     * @param integers the integers
     * @return a hash code
     */
	public int hashInts(final int... integers) {
		final int prime = 31;
		int result = 1;
		for (final int integer : integers)
			result = prime * result + integer;
		return result;
	}	
}
