/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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
 * A WriteUtil helps to write data to file.
 * @author Julien Férard
 */
public class WriteUtil {
	private static final int MAX_INT = 1000;
	private final String[] ints;

	/**
	 * @return a WriteUtil with the default max int in cache.
	 */
	public static WriteUtil create() {
		return new WriteUtil(MAX_INT);
	}

	/**
	 * @param maxInt the max int in cache
	 */
	WriteUtil(final int maxInt) {
		this.ints = new String[2*maxInt];
	}

	/**
	 * @param value the value to convert to String
	 * @return the same value as a String
	 */
	public String toString(final int value) {
		if (-MAX_INT <= value && value < MAX_INT) {
			final int i = value + MAX_INT;
			if (this.ints[i] == null) {
				this.ints[i] = Integer.toString(value);
			}
			return this.ints[i];
		} else
			return Integer.toString(value);
	}
}
