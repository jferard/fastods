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

import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 9.1.4 table:table-cell
 *
 * A TableColdCell represents the "cold" part of a cell, that means the part that is absent of most of
 * the cells.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
class TableColdCell {
	private final XMLUtil xmlUtil;
	private int columnsSpanned;
	private String currency;
	private int rowsSpanned;
	private Text text;
	private String tooltip;
	private TooltipParameter tooltipParameter;
	private String formula;

	/**
	 * Create an new "cold cell"
	 * @param xmlUtil an util
	 */
	TableColdCell(final XMLUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
	}

	/**
	 * @param xmlUtil an util
	 * @return the cold cell
	 */
	public static TableColdCell create(final XMLUtil xmlUtil) {
		return new TableColdCell(xmlUtil);
	}

	/**
	 * @return the currency (see 19.369 office:currency)
	 */
	public String getCurrency() {
		return this.currency;
	}

	/**
	 * 19.676 table:number-columns-spanned
	 * Create a span over columns
	 * @param n the number of columns
	 */
	public void setColumnsSpanned(final int n) {
		this.columnsSpanned = n;
	}

	/**
	 * Set a text in the cell
	 * @param text the text
	 */
	public void setText(final Text text) {
		this.text = text;
	}

	/**
	 * Set a tooltip
	 * @param tooltip the content
	 */
	public void setTooltip(final String tooltip) {
		String escapedXMLContent = this.xmlUtil.escapeXMLContent(tooltip);
		if (escapedXMLContent.contains("\n")) {
			escapedXMLContent = escapedXMLContent.replaceAll("\r?\n", "</text:p><text:p>");
		}

		this.tooltip = escapedXMLContent;
	}


	/**
	 * Set a tooltip of a given size
	 * @param tooltip the content
	 * @param width the width of the tooltip
	 * @param height the height of the tooltip
	 * @param visible true if the tooltip is visible
	 */
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

	/**
	 * 9.1.5 table:covered-table-cell
	 * @return true if the cell is covered
	 */
	public boolean isCovered() {
		return this.columnsSpanned == -1;
	}

	/**
	 * Set the currency (see 19.369 office:currency)
	 * @param currency the currency
	 */
	public void setCurrency(final String currency) {
		this.currency = currency; // escape here
	}

	/**
	 * 9.1.5 table:covered-table-cell
	 * Set the covered flag on this cell
	 */
	public void setCovered() {
		this.columnsSpanned = -1;
	}

	/**
	 * 19.642 table:formula
	 * Set a formula in this cell
	 * @param formula the formula, without = sign
	 */
	public void setFormula(final String formula) {
		this.formula = formula;
	}

	/**
	 * 19.678 table:number-rows-spanned
	 * Create a span over rows
	 * @param n the number of rows
	 */
	public void setRowsSpanned(final int n) {
		this.rowsSpanned = n;
	}
}
