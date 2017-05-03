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

import com.github.jferard.fastods.util.FullList;
import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.List;

/**
 * WHERE ? content.xml/office:document-content/office:body/office:spreadsheet/
 * table:table/table:table-row
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableColdCell {
	private final TableRow parent;
	private final XMLUtil xmlUtil;
	private final int rowIndex;
	private int columnsSpanned;
	private String currency;
	private boolean hasSpans;
	private int rowsSpanned;
	private Text text;
	private String tooltip;
	private TooltipParameter tooltipParameter;
	private String formula;

	public TableColdCell(final TableRow parent, final XMLUtil xmlUtil, final int rowIndex) {
		this.parent = parent;
		this.xmlUtil = xmlUtil;
		this.rowIndex = rowIndex;
	}

	public static TableColdCell create(final TableRow parent,
                                       final XMLUtil xmlUtil, final int rowIndex) {
		return new TableColdCell(parent, xmlUtil, rowIndex);
	}

	/**
	 * @return 0 if no span, -1 if the cell is a covered cell
	 */
	public int getColumnsSpanned() {
		return this.columnsSpanned;
	}

	public String getCurrency() {
		return this.currency;
	}

	public int getRowsSpanned() {
		return this.rowsSpanned;
	}

	public Text getText() {
		return this.text;
	}

	public String getTooltip() {
		return this.tooltip;
	}

	public void setColumnsSpanned(final int n) {
		this.columnsSpanned = n;
	}

	public void setText(final Text text) {
		this.text = text;
	}

	public void setTooltip(final String tooltip) {
		String escapedXMLContent = this.xmlUtil.escapeXMLContent(tooltip);
		if (escapedXMLContent.contains("\n")) {
			escapedXMLContent = escapedXMLContent.replaceAll("\r?\n", "</text:p><text:p>");
		}

		this.tooltip = escapedXMLContent;
	}


	public void setTooltip(final String tooltip, final Length width, final Length height, final boolean visible) {
		this.setTooltip(tooltip);
		this.tooltipParameter = TooltipParameter.create(width, height, visible);
	}

	/**
	 * @param util a util class to write XML data
	 * @param appendable the object to wich data will be appended
	 * @param covered true if the cell is covered
	 * @throws IOException if data can't be appended
	 */
	public void appendXMLToTable(final XMLUtil util,
								 final Appendable appendable,
								 final boolean covered) throws IOException {

		if (this.formula != null)
			util.appendAttribute(appendable, "table:formula", "="+this.formula);


		if (!this.isCovered()) {
			if (this.columnsSpanned != 0) {
				util.appendEAttribute(appendable,
						"table:number-columns-spanned", this.columnsSpanned);
			}
			if (this.rowsSpanned != 0) {
				util.appendEAttribute(appendable,
						"table:number-rows-spanned", this.rowsSpanned);
			}
		}

		if (this.text == null && this.tooltip == null) {
			appendable.append("/>");
		} else { // something between <cell> and </cell>
			appendable.append(">");
			if (this.text != null) {
				this.text.appendXMLContent(util, appendable);
			}
			if (this.tooltip != null) {
				appendable.append("<office:annotation");
				if (this.tooltipParameter != null) {
					this.tooltipParameter.appendXMLToTable(util, appendable);
				}
				appendable.append("><text:p>")
					.append(this.tooltip)
					.append("</text:p></office:annotation>");
			}
			appendable.append("</table:table-cell>");
		}
	}

	public boolean isCovered() {
		return this.columnsSpanned == -1;
	}

	public void setCurrency(final String currency) {
		this.currency = currency; // escape here
	}

	public void setCovered() {
		this.columnsSpanned = -1;
	}

	public void setFormula(final String formula) {
		this.formula = formula;
	}

	public void setRowsSpanned(int n) {
		this.rowsSpanned = n;
	}
}
