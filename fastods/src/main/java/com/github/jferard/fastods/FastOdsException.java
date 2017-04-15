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

package com.github.jferard.fastods;

/**
 * FastOdsException represents an exception in FastOds.
 * @author Julien Férard
 * @author Martin Schulz
 */
public class FastOdsException extends Exception {
	private static final long serialVersionUID = 6239730778542315077L;

	/**
	 * @param tableName the name of the table that is unknown
	 * @return an exception
	 */
	public static FastOdsException unkownTableName(final String tableName) {
		return new FastOdsException("Unknown table name ["+tableName+"]");
	}

	/**
	 * @param message the message
	 */
	public FastOdsException(final String message) {
		super(message);
	}
}
