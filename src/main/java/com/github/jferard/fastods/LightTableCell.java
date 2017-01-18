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
package com.github.jferard.fastods;

import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Julien Férard
 */
public class LightTableCell implements TableCellWalker {
	private final HeavyTableRow row;
	private int c;

	LightTableCell(final HeavyTableRow row) {
		this.row = row;
		this.c = 0;
	}

	@Override
	public void appendXMLToTableRow(final XMLUtil util,
									final Appendable appendable) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getBooleanValue() {
		return this.row.getBooleanValue(this.c);
	}

	@Override
	public void setBooleanValue(final boolean value) {
		this.row.setBooleanValue(this.c, value);
	}

	@Override
	public int getColumnsSpanned() {
		return this.row.getColumnsSpanned(this.c);
	}

	@Override
	public void setColumnsSpanned(final int n) {
		this.row.setColumnsSpanned(this.c, n);
	}

	@Override
	public String getCurrency() {
		return this.row.getCurrency(this.c);
	}

	@Override
	public String getCurrencyValue() {
		return this.row.getCurrencyValue(this.c);
	}

	@Override
	public String getDateValue() {
		return this.row.getDateValue(this.c);
	}

	@Override
	public void setDateValue(final Date value) {
		this.row.setDateValue(this.c, value);
	}

	@Override
	public String getFloatValue() {
		return this.row.getFloatValue(this.c);
	}

	@Override
	public void setFloatValue(final Number value) {
		this.row.setFloatValue(this.c, value);
	}

	@Override
	public String getPercentageValue() {
		return this.row.getPercentageValue(this.c);
	}

	@Override
	public void setPercentageValue(final Number value) {
		this.row.setPercentageValue(this.c, value);
	}

	@Override
	public int getRowsSpanned() {
		return this.row.getRowsSpanned(this.c);
	}

	@Override
	public void setRowsSpanned(final int n) {
		this.row.setRowsSpanned(this.c, n);
	}

	@Override
	public String getStringValue() {
		return this.row.getStringValue(this.c);
	}

	@Override
	public void setStringValue(final String value) {
		this.row.setStringValue(this.c, value);
	}

	@Override
	public String getStyleName() {
		return this.row.getStyleName(this.c);
	}

	@Override
	public String getTimeValue() {
		return this.row.getTimeValue(this.c);
	}

	@Override
	public void setTimeValue(final long timeInMillis) {
		this.row.setTimeValue(this.c, timeInMillis);
	}

	@Override
	public String getTooltip() {
		return this.row.getTooltip(this.c);
	}

	@Override
	public void setTooltip(final String tooltip) {
		this.row.setTooltip(this.c, tooltip);
	}

	@Override
	public Type getValueType() {
		return this.row.getValueType(this.c);
	}

	@Override
	public boolean hasNext() {
		return this.c < this.row.getColumnCount() - 1;
	}

	@Override
	public boolean hasPrevious() {
		return this.c > 0 && this.c <= this.row.getColumnCount();
	}

	@Override
	public void lastCell() {
		this.c = this.row.getColumnCount();
	}

	@Override
	public void next() {
		if (this.c >= this.row.getColumnCount() - 1)
			throw new IndexOutOfBoundsException();
		this.c++;
	}

	@Override
	public void previous() {
		if (this.c <= 0)
			throw new IndexOutOfBoundsException();
		this.c--;
	}

	@Override
	public void setCellValue(final CellValue value) {
		this.row.setCellValue(this.c, value);
	}

	@Override
	public void setCurrencyValue(final float value, final String currency) {
		this.row.setCurrencyValue(this.c, value, currency);
	}

	@Override
	public void setCurrencyValue(final int value, final String currency) {
		this.row.setCurrencyValue(this.c, value, currency);
	}

	@Override
	public void setCurrencyValue(final Number value, final String currency) {
		this.row.setCurrencyValue(this.c, value, currency);
	}

	@Override
	public void setDateValue(final Calendar cal) {
		this.row.setDateValue(this.c, cal);
	}

	@Override
	public void setFloatValue(final float value) {
		this.row.setFloatValue(this.c, value);
	}

	@Override
	public void setFloatValue(final int value) {
		this.row.setFloatValue(this.c, value);
	}

	/**
	 * @deprecated Shortcut for {@code setCellValue(c, CellValue.fromObject(object))}
	 */
	@Override
	@Deprecated
	public void setObjectValue(final Object value) {
		this.row.setObjectValue(this.c, value);
	}

	@Override
	public void setPercentageValue(final float value) {
		this.row.setPercentageValue(this.c, value);
	}

	@Override
	public void setStyle(final TableCellStyle style) {
		this.row.setStyle(this.c, style);
	}

	@Override
	public void to(final int i) {
		if (i < 0)
			throw new IndexOutOfBoundsException();
		this.c = i;
	}
}
