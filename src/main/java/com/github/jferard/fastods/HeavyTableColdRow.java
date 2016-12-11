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
package com.github.jferard.fastods;

import java.io.IOException;
import java.util.List;

import com.github.jferard.fastods.util.FullList;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * WHERE ? content.xml/office:document-content/office:body/office:spreadsheet/
 * table:table/table:table-row
 *
 * @author Julien Férard
 * @author Martin Schulz
 *
 */
public class HeavyTableColdRow {
	public static HeavyTableColdRow create(final Table parent,
			final int rowIndex, final int columnCapacity) {
		return new HeavyTableColdRow(parent, rowIndex, columnCapacity);
	}

	private final int columnCapacity;
	private List<Integer> columnsSpanned;
	private List<String> currencies;
	private boolean hasSpans;
	private final Table parent;
	private final int rowIndex;
	private List<Integer> rowsSpanned;
	private List<Text> texts;
	private List<String> tooltips;

	public HeavyTableColdRow(final Table parent, final int rowIndex,
			final int columnCapacity) {
		this.parent = parent;
		this.rowIndex = rowIndex;
		this.columnCapacity = columnCapacity;
	}

	/**
	 * @param i
	 * @return 0 if no span, -1 if the cell is a covered cell
	 */
	public int getColumnsSpanned(final int i) {
		if (this.columnsSpanned == null)
			return 0;
		else
			return this.columnsSpanned.get(i);
	}

	public String getCurrency(final int i) {
		if (this.currencies == null)
			return null;
		else
			return this.currencies.get(i);
	}

	public int getRowsSpanned(final int i) {
		if (this.rowsSpanned == null)
			return 0;
		else
			return this.rowsSpanned.get(i);
	}

	public Text getText(final int i) {
		if (this.texts == null)
			return null;
		else
			return this.texts.get(i);
	}

	public String getTooltip(final int i) {
		if (this.tooltips == null)
			return null;
		else
			return this.tooltips.get(i);
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 *
	 * @param colIndex
	 *            The column, 0 is the first column
	 * @param rowMerge
	 * @param columnMerge
	 */
	public void setCellMerge(final int colIndex, final int rowMerge,
			final int columnMerge) {
		if (rowMerge < 0 || columnMerge < 0)
			return;
		if (rowMerge <= 1 && columnMerge <= 1)
			return;

		if (this.columnsSpanned == null) {
			this.columnsSpanned = FullList.<Integer> builder().blankElement(0)
					.capacity(this.columnCapacity).build();
		}

		final Integer s = this.columnsSpanned.get(colIndex);
		if (s == -1) // already spanned
			return;

		if (rowMerge > 1) {
			if (this.rowsSpanned == null)
				this.rowsSpanned = FullList.<Integer> builder().blankElement(0)
						.capacity(this.columnCapacity).build();
			this.rowsSpanned.set(colIndex, rowMerge);
		}

		if (columnMerge > 1)
			this.columnsSpanned.set(colIndex, columnMerge);

		this.hasSpans = true;

		// add negative span for covered cells
		for (int c = 1; c < columnMerge; c++)
			this.columnsSpanned.set(colIndex + c, -1);
		for (int r = 1; r < rowMerge; r++) {
			final HeavyTableRow row = this.parent
					.getRowSecure(this.rowIndex + r);
			row.setCovered(colIndex, columnMerge);
		}
	}

	public void setColumnsSpanned(final int colIndex, final int n) {
		if (n <= 1)
			return;

		if (this.columnsSpanned == null)
			this.columnsSpanned = FullList.<Integer> builder().blankElement(0)
					.capacity(this.columnCapacity).build();

		final Integer s = this.columnsSpanned.get(colIndex);
		if (s == -1)
			return;

		this.columnsSpanned.set(colIndex, n);
		// add negative span for covered cells
		for (int c = 1; c < n; c++)
			this.columnsSpanned.set(colIndex + c, -1);
		this.hasSpans = true;
	}

	/* (non-Javadoc)
	 * @see com.github.jferard.fastods.TableCell#setRowsSpanned(int)
	 */
	public void setRowsSpanned(final int colIndex, final int n) {
		if (n <= 1)
			return;

		if (this.rowsSpanned == null)
			this.rowsSpanned = FullList.<Integer> builder().blankElement(0)
					.capacity(this.columnCapacity).build();
		if (this.columnsSpanned != null
				&& this.columnsSpanned.get(colIndex) == -1)
			return;

		this.rowsSpanned.set(colIndex, n);
		// add negative span for covered cells
		for (int r = 1; r < n; r++) {
			final HeavyTableRow row = this.parent
					.getRowSecure(this.rowIndex + r);
			row.setCovered(colIndex);
		}

		this.hasSpans = true;
	}

	public void setText(final int i, final Text text) {
		if (this.texts == null)
			this.texts = FullList.newListWithCapacity(this.columnCapacity);

		this.texts.set(i, text);
	}

	public void setTooltip(final int i, final String tooltip) {
		if (this.tooltips == null)
			this.tooltips = FullList.newListWithCapacity(this.columnCapacity);

		this.tooltips.set(i, tooltip);
	}

	public void appendXMLToTable(final XMLUtil util,
			final Appendable appendable, final int colIndex,
			final boolean covered) throws IOException {
		if (this.hasSpans && !covered) {
			if (this.columnsSpanned != null) {
				final Integer colSpan = this.columnsSpanned.get(colIndex);
				if (colSpan != null && colSpan > 1) {
					util.appendEAttribute(appendable,
							"table:number-columns-spanned", colSpan);
				}
			}
			if (this.rowsSpanned != null) {
				final Integer rowSpan = this.rowsSpanned.get(colIndex);
				if (rowSpan != null && rowSpan > 1) {
					util.appendEAttribute(appendable,
							"table:number-rows-spanned", rowSpan);
				}
			}
		}

		if (this.texts == null && this.tooltips == null) {
			appendable.append("/>");
		} else { // something between <cell> and </cell>
			appendable.append(">");
			if (this.texts != null) {
				final Text text = this.texts.get(colIndex);
				if (text != null) {
					text.appendXMLContent(util, appendable);
				}
			}
			if (this.tooltips != null) {
				final String tooltip = this.tooltips.get(colIndex);
				if (tooltip != null) {
					appendable.append("<office:annotation><text:p>")
							.append(tooltip)
							.append("</text:p></office:annotation>");
				}
			}
			appendable.append("</table:table-cell>");
		}
	}

	public boolean isCovered(int colIndex) {
		if (this.columnsSpanned == null)
			return false;
		else
			return this.columnsSpanned.get(colIndex) == -1;
	}

	public void setCurrency(int i, String currency) {
		if (this.currencies == null)
			this.currencies = FullList.newListWithCapacity(this.columnCapacity);

		this.currencies.set(i, currency); // escape here
	}

	public void setCovered(int colIndex) {
		if (this.columnsSpanned == null)
			this.columnsSpanned = FullList.<Integer> builder().blankElement(0)
					.capacity(this.columnCapacity).build();
		this.columnsSpanned.set(colIndex, -1);
		this.hasSpans = true;
	}

	public void setCovered(int colIndex, int n) {
		if (this.columnsSpanned == null)
			this.columnsSpanned = FullList.<Integer> builder().blankElement(0)
					.capacity(this.columnCapacity).build();

		for (int c = 0; c < n; c++)
			this.columnsSpanned.set(colIndex + c, -1);
		this.hasSpans = true;
	}
}
