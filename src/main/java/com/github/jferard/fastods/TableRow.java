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
package com.github.jferard.fastods;

/**
 * @author Martin Schulz<br>
 * 
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *         <br>
 * 
 *         This file TableRow.java is part of SimpleODS.
 *
 */
public class TableRow {
	private String Style = "ro1";

	private ObjectQueue qTableCells = new ObjectQueue();

	public TableRow() {
	};

	/**
	 * Added 0.5.1:<br>
	 * Get a TableCell, if no TableCell was present at this nCol, create a new
	 * one with a default of TableCell.STYLE_STRING and a content of "".
	 * 
	 * @param nCol
	 * @return The TableCell for this position, maybe a new TableCell
	 */
	public TableCell getCell(final int nCol) {
		TableCell tc = (TableCell) this.qTableCells.get(nCol);
		if (tc == null) {
			tc = new TableCell(TableCell.STYLE_STRING, "");
			this.qTableCells.setAt(nCol, tc);
		}
		return tc;
	}

	/**
	 * Set TableCell object at position nCol.<br>
	 * If there is already a TableCell object, the old one is overwritten.
	 * 
	 * @param nCol
	 *            The column for this cell
	 * @param tc
	 */
	public void setCell(final int nCol, final TableCell tc) {
		this.qTableCells.setAt(nCol, tc);
	}

	/**
	 * 
	 * @param nCol
	 *            The column for this cell
	 * @param nValuetype
	 * @param sValue
	 */
	public void setCell(final int nCol, final int nValuetype,
			final String sValue) {
		TableCell tc = (TableCell) this.qTableCells.get(nCol);
		if (tc == null) {
			tc = new TableCell(nValuetype, sValue);
		} else {
			tc.setValueType(nValuetype);
			tc.setValue(sValue);
		}
		this.qTableCells.setAt(nCol, tc);
	}

	/**
	 * Set the cell to the content of string, the value type of the cell is
	 * TableCell.STYLE_STRING .
	 * 
	 * @param nCol
	 *            The column to set
	 * @param sValue
	 *            The value to be used
	 */
	public void setCell(final int nCol, final String sValue) {
		this.setCell(nCol, TableCell.STYLE_STRING, sValue);
	}

	/**
	 * Set the cell style for the cell at nCol to ts.
	 * 
	 * @param nCol
	 *            The column number
	 * @param ts
	 *            The table style to be used
	 */
	public void setCellStyle(final int nCol, final TableStyle ts) {

		TableCell tc = (TableCell) this.qTableCells.get(nCol);
		if (tc == null) {
			// Create an empty cell
			this.setCell(nCol, TableCell.STYLE_STRING, "");
			tc = (TableCell) this.qTableCells.get(nCol);
		}

		tc.setStyle(ts.getName());

	}

	public String getStyle() {
		return this.Style;
	}

	public void setStyle(final String s) {
		this.Style = s;
	}

	/**
	 * @return The ObjectQueue with all TableCell objects
	 */
	public ObjectQueue getCells() {
		return this.qTableCells;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	public String[] toXML() {
		ObjectQueue qRc = new ObjectQueue();

		int nNullFieldCounter = 0;

		qRc.add("<table:table-row table:style-name=\"" + this.getStyle()
				+ "\">");

		for (int n = 0; n < this.qTableCells.size(); n++) {
			TableCell tc = (TableCell) this.qTableCells.get(n);
			if (tc == null) {
				nNullFieldCounter++;
			} else {
				if (nNullFieldCounter > 0) {
					qRc.add("<table:table-cell table:number-columns-repeated=\""
							+ nNullFieldCounter + "\"/>");
					nNullFieldCounter = 0;
				}
				qRc.add(tc.toXML());
			}
		}
		qRc.add("</table:table-row>");

		String[] sReturn = new String[qRc.size()];
		for (int n = 0; n < qRc.size(); n++) {
			sReturn[n] = (String) qRc.get(n);
		}

		return sReturn;
	}

}
