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

import java.util.Locale;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file Util.java is part of FastODS.
 */
@SuppressWarnings("PMD.UnusedLocalVariable")
public class PositionUtil {
	public static class Position {
		private final int column;
		private final int row;

		Position(final int row, final int column) {
			this.row = row;
			this.column = column;
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o)
				return true;

			if (!(o instanceof Position))
				return false;

			final Position other = (Position) o;
			return this.row == other.row && this.column == other.column;
		}

		public int getColumn() {
			return this.column;
		}

		public int getRow() {
			return this.row;
		}

	}

	private static final int BEGIN_DIGIT = 3;
	private static final int BEGIN_LETTER = 0;
	private static final int FIRST_DIGIT = 4;
	private static final int FIRST_LETTER = 1;
	private static final int OPT_DIGIT = 5;

	private static final int OPT_SECOND_LETTER = 2;

	public PositionUtil() {
	}

	/**
	 * Convert a cell position string like B3 to the column number.
	 *
	 * @param pos
	 *            The cell position in the range 'A1' to 'IV65536'
	 * @return The row, e.g. A1 will return 0, B1 will return 1, E1 will return
	 *         4
	 */
	public Position getPosition(final String pos) {
		final String s = pos.toUpperCase(Locale.US);
		final int len = s.length();
		int status = 0;

		int row = 0;
		int col = 0;
		int n = 0;
		while (n < len) {
			final char c = s.charAt(n);
			switch (status) {
			case BEGIN_LETTER: // opt $
			case BEGIN_DIGIT: // opt $
				status++;
				if (c == '$')
					n++;
				break;
			case FIRST_LETTER: // mand letter
				if ('A' <= c && c <= 'Z') {
					col = c - 'A' + 1;
					status = PositionUtil.OPT_SECOND_LETTER;
					n++;
				} else
					return null;
				break;
			case OPT_SECOND_LETTER: // opt letter
				if ('A' <= c && c <= 'Z') {
					col = col * 26 + c - 'A' + 1;
					n++;
				}
				status = PositionUtil.BEGIN_DIGIT;
				break;
			case FIRST_DIGIT: // mand digit
				if ('0' <= c && c <= '9') {
					row = c - '0';
					status = PositionUtil.OPT_DIGIT;
					n++;
				} else
					return null;
				break;
			case OPT_DIGIT: // opt digit
				if ('0' <= c && c <= '9') {
					row = row * 10 + c - '0';
					n++;
				} else
					return null;
				break;
			default:
				return null;
			}
		}
		return new Position(row - 1, col - 1);
	}
}
