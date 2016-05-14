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
 *         This file Table.java is part of SimpleODS.<br>
 *         0.5.1 Changed all 'throw Exception' to 'throw SimpleOdsException'
 *
 */
public class Table {
	final static int TABLE_MAXROWNUMBER = 65536;
	final static int TABLE_MAXCOLUMNNUMBER = 256;
	private String sName;
	private String Style = "ta1";
	private int nLastRow = 0; // The highest row in the table TODO: Check if
								// this can be removed
	private int nLastCol = 0; // The highest column in the table TODO: Check if
								// this can be removed

	private ConfigItem CursorPositionX = new ConfigItem("CursorPositionX",
			"int", "0");
	private ConfigItem CursorPositionY = new ConfigItem("CursorPositionY",
			"int", "0");
	private ConfigItem HorizontalSplitMode = new ConfigItem(
			"HorizontalSplitMode", "short", "0");
	private ConfigItem VerticalSplitMode = new ConfigItem("VerticalSplitMode",
			"short", "0");
	private ConfigItem HorizontalSplitPosition = new ConfigItem(
			"HorizontalSplitPosition", "int", "0");
	private ConfigItem VerticalSplitPosition = new ConfigItem(
			"VerticalSplitPosition", "int", "0");
	private ConfigItem ActiveSplitRange = new ConfigItem("ActiveSplitRange",
			"short", "2");
	private ConfigItem PositionLeft = new ConfigItem("PositionLeft", "int",
			"0");
	private ConfigItem PositionRight = new ConfigItem("PositionRight", "int",
			"0");
	private ConfigItem PositionTop = new ConfigItem("PositionTop", "int", "0");
	private ConfigItem PositionBottom = new ConfigItem("PositionBottom", "int",
			"0");
	private ConfigItem ZoomType = new ConfigItem("ZoomType", "short", "0");
	private ConfigItem ZoomValue = new ConfigItem("ZoomValue", "int", "100");
	private ConfigItem PageViewZoomValue = new ConfigItem("PageViewZoomValue",
			"int", "60");

	private ObjectQueue qColumnStyles = new ObjectQueue();
	private ObjectQueue qTableRows = new ObjectQueue();

	public Table(String sName) {
		this.setName(sName);
	}

	/**
	 * @return The complete configuration information of this table in a string
	 *         array
	 */
	public String[] getConfig() {
		String[] sConfig = {
				"<config:config-item-map-entry config:name=\"" + this.getName()
						+ "\">",
				this.CursorPositionX.toXML(), this.CursorPositionY.toXML(),
				this.HorizontalSplitMode.toXML(), this.VerticalSplitMode.toXML(),
				this.HorizontalSplitMode.toXML(),
				this.VerticalSplitMode.toXML(),
				this.HorizontalSplitPosition.toXML(),
				this.VerticalSplitPosition.toXML(),
				this.ActiveSplitRange.toXML(), this.PositionLeft.toXML(),
				this.PositionRight.toXML(), this.PositionTop.toXML(),
				this.PositionBottom.toXML(), this.ZoomType.toXML(),
				this.ZoomValue.toXML(), this.PageViewZoomValue.toXML(),
				"</config:config-item-map-entry>", };

		return (sConfig);
	}

	public int getLastRow() {
		return this.nLastRow;
	}

	public int getLastCol() {
		return this.nLastCol;
	}

	public ObjectQueue getRows() {
		return this.qTableRows;
	}

	public ObjectQueue getColumnStyles() {
		return this.qColumnStyles;
	}

	/**
	 * Get the current TableStyle
	 * 
	 * @return The current TableStlye
	 */
	public String getStyle() {
		return this.Style;
	}

	/**
	 * Set a new TableStyle
	 * 
	 * @param style
	 *            The new TableStlye to be used
	 */
	public void setStyle(String style) {
		this.Style = style;
	}

	/**
	 * Get the name of this table.
	 * 
	 * @return The name of this table.
	 */
	public String getName() {
		return this.sName;
	}

	/**
	 * Set the name of this table.
	 * 
	 * @param name
	 *            The name of this table.
	 */
	public void setName(final String name) {
		this.sName = name;
	}

	/**
	 * Set the value of a cell.
	 * 
	 * @param nRow
	 *            The row (maximal 65536)
	 * @param nCol
	 *            The column (maximal 256)
	 * @param valuetype
	 *            The type of the value,
	 *            TableCell.STYLE_STRING,TableCell.STYLE_FLOAT or
	 *            TableCell.STYLE_PERCENTAGE
	 * @param value
	 *            The value to be set
	 * @return true
	 * @throws SimpleOdsException
	 *             Thrown when nRow or nCol have wrong values.
	 */
	public boolean setCell(final int nRow, final int nCol, final int valuetype,
			final String value) throws SimpleOdsException {
		TableRow tr;

		if (nRow >= Table.TABLE_MAXROWNUMBER) {
			throw new SimpleOdsException(
					"Maximum row number (65536) exception, row value:[" + nRow
							+ "]");
		}
		if (nCol >= Table.TABLE_MAXCOLUMNNUMBER) {
			throw new SimpleOdsException(
					"Maximum column number (256) exception, column value:["
							+ nCol + "]");
		}
		if (nRow < 0) {
			throw new SimpleOdsException(
					"Negative row number exception, row value:[" + nRow + "]");
		}
		if (nCol < 0) {
			throw new SimpleOdsException(
					"Negative column number exception, column value:[" + nCol
							+ "]");
		}

		if (nRow > this.nLastRow) {
			this.nLastRow = nRow; // TODO: Ersatz durch qTableRows.size()???
		}
		if (nCol > this.nLastCol) {
			this.nLastCol = nCol;
		}

		// Check if this row already exists and create a new one if not
		if (this.qTableRows.get(nRow) == null) {
			tr = new TableRow();
		} else {
			tr = (TableRow) this.qTableRows.get(nRow);
		}
		tr.setCell(nCol, valuetype, value);
		this.qTableRows.setAt(nRow, tr);
		return true;
	}

	/**
	 * Set the value of a cell.
	 * 
	 * @param nRow
	 *            The row (maximal 65536)
	 * @param nCol
	 *            The column (maximal 256)
	 * @param valuetype
	 *            The type of the value,
	 *            TableCell.STYLE_STRING,TableCell.STYLE_FLOAT or
	 *            TableCell.STYLE_PERCENTAGE
	 * @param value
	 *            The value to be set
	 * @param ts
	 *            The TableStyle to be used for this cell.
	 * @return true
	 * @throws SimpleOdsException
	 *             Thrown when nRow or nCol have wrong values.
	 */
	public boolean setCell(final int nRow, final int nCol, final int valuetype,
			final String value, final TableStyle ts) throws SimpleOdsException {

		this.setCell(nRow, nCol, valuetype, value);
		this.setCellStyle(nRow, nCol, ts);
		return true;
	}

	/**
	 * Get a TableCell, if no TableCell was present at this nRow,nCol, create a
	 * new one with a default of TableCell.STYLE_STRING and a content of "".
	 * 
	 * @param nRow
	 *            The row (maximal 65536)
	 * @param nCol
	 *            The column (maximal 256)
	 * @return The TableCell for this position, maybe a new TableCell
	 */
	public TableCell getCell(final int nRow, final int nCol) {

		// -------------------------------------------------------------
		// Check if this row already exists and create a new one if not
		// -------------------------------------------------------------
		TableRow tr = (TableRow) this.qTableRows.get(nRow);
		if (tr == null) {
			tr = new TableRow();
			this.qTableRows.setAt(nRow, tr);
		}
		return tr.getCell(nCol);
	}

	/**
	 * Set the style of a column.
	 * 
	 * @param nCol
	 *            The column number
	 * @param ts
	 *            The style to be used, make sure the style is of type
	 *            TableStyle.STYLEFAMILY_TABLECOLUMN
	 * @throws SimpleOdsException
	 *             Thrown if nCol has an invalid value.
	 */
	public void setColumnStyle(final int nCol, final TableStyle ts)
			throws SimpleOdsException {

		if (nCol >= Table.TABLE_MAXCOLUMNNUMBER) {
			throw new SimpleOdsException(
					"Maximum column number (256) exception, column value:["
							+ nCol + "]");
		}
		if (nCol < 0) {
			throw new SimpleOdsException(
					"Negative column number exception, column value:[" + nCol
							+ "]");
		}

		this.qColumnStyles.setAt(nCol, ts);

	}

	/**
	 * Set the cell style for the specified cell.
	 * 
	 * @param nRow
	 *            The row number
	 * @param nCol
	 *            The column number
	 * @param ts
	 *            The TableStyle to be used
	 * @return TRUE The cell style was set
	 * @throws SimpleOdsException
	 *             when nRow or nCol have wrong values
	 */
	public boolean setCellStyle(final int nRow, final int nCol,
			final TableStyle ts) throws SimpleOdsException {
		TableRow tr;

		if (nRow >= Table.TABLE_MAXROWNUMBER) {
			throw new SimpleOdsException(
					"Maximum row number (65536) exception, row value:[" + nRow
							+ "]");
		}
		if (nCol >= Table.TABLE_MAXCOLUMNNUMBER) {
			throw new SimpleOdsException(
					"Maximum column number (256) exception, column value:["
							+ nCol + "]");
		}
		if (nRow < 0) {
			throw new SimpleOdsException(
					"Negative row number exception, row value:[" + nRow + "]");
		}
		if (nCol < 0) {
			throw new SimpleOdsException(
					"Negative column number exception, column value:[" + nCol
							+ "]");
		}

		if (nRow > this.nLastRow) {
			this.nLastRow = nRow; // TODO: Ersatz durch qTableRows.size()???
		}
		if (nCol > this.nLastCol) {
			this.nLastCol = nCol;
		}

		// -------------------------------------------------------------
		// Check if this row already exists and create a new one if not
		// -------------------------------------------------------------
		if (this.qTableRows.get(nRow) == null) {
			tr = new TableRow();
		} else {
			tr = (TableRow) this.qTableRows.get(nRow);
		}
		tr.setCellStyle(nCol, ts);
		this.qTableRows.setAt(nRow, tr);
		return true;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	public String toXML() {
		return ("<table:table table:name=\"" + this.getName()
				+ "\" table:style-name=\"" + this.getStyle()
				+ "\" table:print=\"false\">");
	}

}
