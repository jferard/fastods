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

import com.github.jferard.fastods.Table;

import java.util.Locale;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
@SuppressWarnings("PMD.UnusedLocalVariable")
public class PositionUtil {
	public static class Position {
		public static final int ORD_A = 'A';
		private final EqualityUtil equalityUtil;
		private final int column;
		private final int row;

		Position(final EqualityUtil equalityUtil, final int row, final int column) {
			this.equalityUtil = equalityUtil;
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
		
		@Override
		public int hashCode() {
			return this.equalityUtil.hashInts(this.row, this.column);
		}

		public int getColumn() {
			return this.column;
		}

		public int getRow() {
			return this.row;
		}

		/**
		 * 0 -> A
		 * ...
		 * 25 -> Z
		 * 26 -> AA
		 * 27 -> AB
		 * ...
		 * 52 -> BA
		 * 1023->AMJ
		 *
		 * @return
		 */
		public String toCellAddress() {
			final StringBuilder sb = new StringBuilder();
			int col = this.column;
			while (col >= 26) {
				sb.insert(0, (char) (ORD_A + (col % 26)));
				col = col/26 - 1;
			}
			sb.insert(0, (char) (ORD_A + col));
			sb.append(this.row+1);
			return sb.toString();
		}

		public String toCellAddress(final Table table) {
			return table.getName()+"."+this.toCellAddress();
		}
	}

	private static final int BEGIN_DIGIT = 3;
	private static final int BEGIN_LETTER = 0;
	private static final int FIRST_DIGIT = 4;
	private static final int FIRST_LETTER = 1;
	private static final int OPT_DIGIT = 5;

	private static final int OPT_SECOND_LETTER = 2;
	private final EqualityUtil equalityUtil;

	public PositionUtil(final EqualityUtil equalityUtil) {
		this.equalityUtil = equalityUtil;
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
		return new Position(this.equalityUtil, row - 1, col - 1);
	}
	
	public Position getPosition(final int row, final int col) {
		return new Position(this.equalityUtil, row, col);
	}

	public String toCellAddress(final int row, final int col) {
		return this.getPosition(row, col).toCellAddress();
	}

	public String toCellAddress(final Table table, final int row, final int col) {
		return this.getPosition(row, col).toCellAddress(table);
	}

	public String toRangeAddress(final int row1, final int col1, final int row2, final int col2) {
		return this.toCellAddress(row1, col1)+":"+this.toCellAddress(row2, col2);
	}

	public String toRangeAddress(final Table table, final int row1, final int col1, final int row2, final int col2) {
		return this.toCellAddress(table, row1, col1)+":"+this.toCellAddress(table, row2, col2);
	}

}
