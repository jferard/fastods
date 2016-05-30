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
package com.github.jferard.fastods.util;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.github.jferard.fastods.NamedObject;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file Util.java is part of FastODS.
 */
@SuppressWarnings("PMD.UnusedLocalVariable")
public class Util {
	public static class Position {
		private final int row;
		private final int column;

		private Position(int row, int column) {
			this.row = row;
			this.column = column;
		}

		public int getRow() {
			return this.row;
		}

		public int getColumn() {
			return this.column;
		}
	}

	public Util() {
	}

	/**
	 * Copy all styles from one OdsFile to another OdsFile.<br>
	 * The target file will then contain all styles from the source file.
	 *
	 * @param source
	 *            The source file
	 * @param target
	 *            The target file.
	 */
	/*
	public void copyStylesFromTo(final OdsFile source, final OdsFile target) {
		target.setStyles(source.getStyles());
		target.getContent().setPageStyles(source.getContent().getPageStyles());
		target.getContent()
				.setTableStyles(source.getContent().getTableStyles());
		target.getContent().setTextStyles(source.getContent().getTextStyles());
	}*/

	public boolean equal(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		} else {
			return o1.equals(o2);
		}
	}

	public <T extends NamedObject> Optional<T> findElementByName(
			final List<T> list, final String name) {
		// Check is a style with this name exists and replace if yes
		final ListIterator<T> listIterator = list.listIterator();
		while (listIterator.hasNext()) {
			final T curElement = listIterator.next();
			if (curElement.getName().equals(name)) {
				return Optional.of(curElement);
			}
		}
		return Optional.absent();
	}

	/**
	 * Convert a cell position string like B3 to the column number.
	 *
	 * @param sPos
	 *            The cell position in the range 'A1' to 'IV65536'
	 * @return The row, e.g. A1 will return 0, B1 will return 1, E1 will return
	 *         4
	 */
	public Position getPosition(final String sPos) {
		final String s = sPos.toUpperCase(Locale.US);
		int len = s.length();
		int status = 0;

		int nRow = 0;
		int nCol = 0;
		int n = 0;
		while (n < len) {
			final char c = s.charAt(n);
			switch (status) {
			case 0: // opt $
			case 3: // opt $
				status++;
				if (c == '$')
					n++;
				break;
			case 1: // mand letter
				if ('A' <= c && c <= 'Z') {
					nCol = c - 'A' + 1;
					status = 2;
					n++;
				} else
					return null;
				break;
			case 2: // opt letter
				if (c < 'A' || c > 'Z') {
					status = 3;
				} else {
					nCol = nCol * 26 + c - 'A' + 1;
					n++;
				}
				break;
			case 4: // mand digit
				if ('0' <= c && c <= '9') {
					nRow = c - '0';
					status = 5;
					n++;
				} else
					return null;
				break;
			case 5: // opt digit
				if ('0' <= c && c <= '9') {
					nRow = nRow * 10 + c - '0';
					n++;
				} else
					return null;
				break;
			default:
				return null;
			}
		}
		return new Position(nRow - 1, nCol - 1);
	}

	/**
	 * Wraps an OutputStream in a BufferedWriter
	 *
	 * @param out
	 *            the stream
	 * @return the writer
	 */
	public Writer wrapStream(final OutputStream out, int size) {
		return new BufferedWriter(new OutputStreamWriter(out, Charsets.UTF_8),
				size);
	}

	/**
	 * XML Schema Part 2, 3.2.6 duration
	 * "'P'yyyy'Y'MM'M'dd'DT'HH'H'mm'M'ss.SSS'S'"
	 *
	 * @param milliseconds
	 * @return
	 */
	public String formatTimeInterval(final long milliseconds) {
		long curMilliseconds = milliseconds;
		StringBuilder sb = new StringBuilder().append('P');
		final long days = TimeUnit.MILLISECONDS.toDays(curMilliseconds);
		sb.append(days).append("DT");
		curMilliseconds -= TimeUnit.DAYS.toMillis(days);

		final long hours = TimeUnit.MILLISECONDS.toHours(curMilliseconds);
		sb.append(hours).append('H');
		curMilliseconds -= TimeUnit.HOURS.toMillis(hours);

		final long minutes = TimeUnit.MILLISECONDS.toMinutes(curMilliseconds);
		sb.append(minutes).append('M');
		curMilliseconds -= TimeUnit.MINUTES.toMillis(minutes);

		final long seconds = TimeUnit.MILLISECONDS.toSeconds(curMilliseconds);
		sb.append(seconds);
		curMilliseconds -= TimeUnit.SECONDS.toMillis(seconds);

		sb.append('.').append(curMilliseconds).append('S');
		return sb.toString();
	}
}
