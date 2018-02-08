/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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
	/**
	 * @return a new position util
	 */
	public static PositionUtil create() { return new PositionUtil(new EqualityUtil()); }

	/**
	 * a..z -> 26 letters
	 */
	public static final int ALPHABET_SIZE = 26;
	/**
	 * the base for computing
	 */
	public static final int ORD_A = 'A';

	/**
	 * A position (row + col)
	 */
	public static class Position {
		private final EqualityUtil equalityUtil;
		private final int column;
		private final int row;

		/**
		 * Create a position
		 * @param equalityUtil an util
		 * @param row the row
		 * @param column the column
		 */
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

		/**
		 * @return the column
		 */
		public int getColumn() {
			return this.column;
		}

		/**
		 * @return the row
		 */
		public int getRow() {
			return this.row;
		}

		/**
		 * Returns the address of a cell, in Excel/OO/LO format. Some examples:
		 * <ul>
		 * <li>0 gives "A"</li>
		 * <li>...</li>
		 * <li>25 gives "Z"</li>
		 * <li>26 gives "AA"</li>
		 * <li>27 gives "AB"</li>
		 * <li>...</li>
		 * <li>52 gives "BA"</li>
		 * <li>1023 gives "AMJ"</li>
		 * </ul>
		 *
		 * @return the Excel/OO/LO address
		 */
		public String toCellAddress() {
			final StringBuilder sb = new StringBuilder();
			int col = this.column;
			while (col >= ALPHABET_SIZE) {
				sb.insert(0, (char) (ORD_A + (col % ALPHABET_SIZE)));
				col = col/ ALPHABET_SIZE - 1;
			}
			sb.insert(0, (char) (ORD_A + col));
			sb.append(this.row+1);
			return sb.toString();
		}

		/**
		 * Return the Excel/OO/LO address of a cell, preceeded by the table name
		 * @param table the table
		 * @return the Excel/OO/LO address
		 */
		public String toCellAddress(final NamedObject table) {
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

	/**
	 * Create a new position util
	 * @param equalityUtil an util
	 */
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
					col = col * ALPHABET_SIZE + c - ORD_A + 1;
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

	/**
	 * @param row the row
	 * @param col the col
	 * @return the position
	 */
	public Position getPosition(final int row, final int col) {
		return new Position(this.equalityUtil, row, col);
	}

	/**
	 * Return the Excel/OO/LO address of a cell
	 * @param row the row
	 * @param col the col
	 * @return the Excel/OO/LO address
	 */
	public String toCellAddress(final int row, final int col) {
		return this.getPosition(row, col).toCellAddress();
	}

	/**
	 * the Excel/OO/LO address of a cell, preceeded by the table name
	 * @param row the row
	 * @param col the col
	 * @param table the table
	 * @return the Excel/OO/LO address
	 */
	public String toCellAddress(final Table table, final int row, final int col) {
		return this.getPosition(row, col).toCellAddress(table);
	}

	/**
	 * Return the Excel/OO/LO address of a range
	 * @param row1 the first row
	 * @param col1 the first col
	 * @param row2 the last row
	 * @param col2 the last col
	 * @return the Excel/OO/LO address
	 */
	public String toRangeAddress(final int row1, final int col1, final int row2, final int col2) {
		return this.toCellAddress(row1, col1)+":"+this.toCellAddress(row2, col2);
	}

	/**
	 * Return the Excel/OO/LO address of a range, preceeded by the table name
	 * @param row1 the first row
	 * @param col1 the first col
	 * @param row2 the last row
	 * @param col2 the last col
	 * @param table the table
	 * @return the Excel/OO/LO address
	 */
	public String toRangeAddress(final Table table, final int row1, final int col1, final int row2, final int col2) {
		return this.toCellAddress(table, row1, col1)+":"+this.toCellAddress(table, row2, col2);
	}

}
