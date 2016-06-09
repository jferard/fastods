package com.github.jferard.fastods;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.github.jferard.fastods.HeavyTableCell.Type;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.XMLUtil;

public class LightTableCell implements TableCellWalker {
	private int i;
	private final HeavyTableRow row;

	LightTableCell(final HeavyTableRow row) {
		this.row = row;
		this.i = 0;
	}

	@Override
	public void appendXMLToTableRow(final XMLUtil util,
			final Appendable appendable) throws IOException {
//		this.row.appendXMLToTableRow(util, appendable, this.i);
	}

	@Override
	public String getBooleanValue() {
		return this.row.getBooleanValue(this.i);
	}

	@Override
	public int getColumnsSpanned() {
		return this.row.getColumnsSpanned(this.i);
	}

	@Override
	public String getCurrency() {
		return this.row.getCurrency(this.i);
	}

	@Override
	public String getCurrencyValue() {
		return this.row.getCurrencyValue(this.i);
	}

	@Override
	public String getDateValue() {
		return this.row.getDateValue(this.i);
	}

	@Override
	public String getFloatValue() {
		return this.row.getFloatValue(this.i);
	}

	@Override
	public String getPercentageValue() {
		return this.row.getPercentageValue(this.i);
	}

	@Override
	public int getRowsSpanned() {
		return this.row.getRowsSpanned(this.i);
	}

	@Override
	public String getStringValue() {
		return this.row.getStringValue(this.i);
	}

	@Override
	public String getStyleName() {
		return this.row.getStyleName(this.i);
	}

	@Override
	public String getTimeValue() {
		return this.row.getTimeValue(this.i);
	}

	@Override
	public Type getValueType() {
		return this.row.getValueType(this.i);
	}

	@Override
	public boolean hasNext() {
		return this.i < this.row.getColumnCount() - 1;
	}

	@Override
	public boolean hasPrevious() {
		return this.i > 0;
	}

	@Override
	public void next() {
		if (this.i >= this.row.getColumnCount() - 1)
			throw new IndexOutOfBoundsException();
		this.i++;
	}

	@Override
	public void nextCell() {
		this.i = this.row.getColumnCount();
	}

	@Override
	public void previous() {
		if (this.i <= 0)
			throw new IndexOutOfBoundsException();
		this.i--;
	}

	@Override
	public void setBooleanValue(final boolean value) {
		this.row.setBooleanValue(this.i, value);
	}

	@Override
	public void setColumnsSpanned(final int n) {
		this.row.setColumnsSpanned(this.i, n);
	}

	@Override
	public void setCurrencyValue(final float value, final String currency) {
		this.row.setCurrencyValue(this.i, value, currency);
	}

	@Override
	public void setCurrencyValue(final int value, final String currency) {
		this.row.setCurrencyValue(this.i, value, currency);
	}

	@Override
	public void setCurrencyValue(final Number value, final String currency) {
		this.row.setCurrencyValue(this.i, value, currency);
	}

	@Override
	public void setDateValue(final Calendar cal) {
		this.row.setDateValue(this.i, cal);
	}

	@Override
	public void setDateValue(final Date value) {
		this.row.setDateValue(this.i, value);
	}

	@Override
	public void setFloatValue(final float value) {
		this.row.setFloatValue(this.i, value);
	}

	@Override
	public void setFloatValue(final int value) {
		this.row.setFloatValue(this.i, value);
	}

	@Override
	public void setFloatValue(final Number value) {
		this.row.setFloatValue(this.i, value);
	}

	@Override
	public void setObjectValue(final Object value) {
		this.row.setObjectValue(this.i, value);
	}

	@Override
	public void setPercentageValue(final float value) {
		this.row.setPercentageValue(this.i, value);
	}

	@Override
	public void setPercentageValue(final Number value) {
		this.row.setPercentageValue(this.i, value);
	}

	@Override
	public void setRowsSpanned(final int n) {
		this.row.setRowsSpanned(this.i, n);
	}

	@Override
	public void setStringValue(final String value) {
		this.row.setStringValue(this.i, value);
	}

	@Override
	public void setStyle(final TableCellStyle style) {
		this.row.setStyle(this.i, style);
	}

	@Override
	public void setTimeValue(final long timeInMillis) {
		this.row.setTimeValue(this.i, timeInMillis);
	}

	@Override
	public void to(final int i) {
		if (i < 0)
			throw new IndexOutOfBoundsException();
		this.i = i;
	}
}
