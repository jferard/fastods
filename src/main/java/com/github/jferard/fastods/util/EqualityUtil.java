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

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 */
@SuppressWarnings("PMD.UnusedLocalVariable")
public class EqualityUtil {
	public EqualityUtil() {
	}

	public boolean different(final Object o1, final Object o2) {
		if (o1 == null) {
			return o2 != null;
		} else {
			return !o1.equals(o2);
		}
	}

	public boolean equal(final Object o1, final Object o2) {
		if (o1 == null) {
			return o2 == null;
		} else {
			return o1.equals(o2);
		}
	}

	public int hashObjects(final Object... objects) {
		final int prime = 31;
		int result = 1;
		for (Object object : objects) {
			result = prime * result
					+ ((object == null) ? 0 : object.hashCode());
		}
		return result;
	}
	
	public int hashInts(final int... integers) {
		final int prime = 31;
		int result = 1;
		for (int integer : integers)
			result = prime * result + integer;
		return result;
	}	
}
