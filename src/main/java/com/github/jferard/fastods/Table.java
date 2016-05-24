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

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.github.jferard.fastods.TableCell.Type;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file Table.java is part of FastODS.
 * 
 *         SimpleOds 0.5.1 Changed all 'throw Exception' to 'throw
 *         FastOdsException'
 *
 *         WHERE ?
 *         content.xml/office:document-content/office:body/office:spreadsheet/
 *         table:table
 */
public class Table implements NamedObject, XMLAppendable {
	final static int TABLE_MAXROWNUMBER = 65536;
	final static int TABLE_MAXCOLUMNNUMBER = 256;
	
	private String sName;
	private String styleName;
	private int nLastCol; // The highest column in the table TODO: Check if
								// this can be removed

	private final ConfigItem cursorPositionX;
	private final ConfigItem cursorPositionY;
	private final ConfigItem horizontalSplitMode;
	private final ConfigItem verticalSplitMode;
	private final ConfigItem horizontalSplitPosition;
	private final ConfigItem verticalSplitPosition;
	private final ConfigItem activeSplitRange;
	private final ConfigItem positionLeft;
	private final ConfigItem positionRight;
	private final ConfigItem positionTop;
	private final ConfigItem positionBottom;
	private final ConfigItem zoomType;
	private final ConfigItem zoomValue;
	private final ConfigItem pageViewZoomValue;

	private final List<TableColumnStyle> qColumnStyles;
	private final List<TableRow> qTableRows;
	private final OdsFile odsFile;

	Table(OdsFile odsFile, String sName) {
		this.odsFile = odsFile;
		this.sName = sName;
		this.styleName = "ta1";
		this.nLastCol = 0; // The highest column in the table TODO: Check if
									// this can be removed

		this.cursorPositionX = new ConfigItem("CursorPositionX",
				"int", "0");
		this.cursorPositionY = new ConfigItem("cursorPositionY",
				"int", "0");
		this.horizontalSplitMode = new ConfigItem(
				"horizontalSplitMode", "short", "0");
		this.verticalSplitMode = new ConfigItem("verticalSplitMode",
				"short", "0");
		this.horizontalSplitPosition = new ConfigItem(
				"horizontalSplitPosition", "int", "0");
		this.verticalSplitPosition = new ConfigItem(
				"verticalSplitPosition", "int", "0");
		this.activeSplitRange = new ConfigItem("activeSplitRange",
				"short", "2");
		this.positionLeft = new ConfigItem("positionLeft", "int",
				"0");
		this.positionRight = new ConfigItem("PositionRight", "int",
				"0");
		this.positionTop = new ConfigItem("PositionTop", "int", "0");
		this.positionBottom = new ConfigItem("positionBottom", "int",
				"0");
		this.zoomType = new ConfigItem("zoomType", "short", "0");
		this.zoomValue = new ConfigItem("zoomValue", "int", "100");
		this.pageViewZoomValue = new ConfigItem("pageViewZoomValue",
				"int", "60");

		this.qColumnStyles = FullList.newList();
		this.qTableRows = FullList.newList();
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
		TableRow tr = this.qTableRows.get(nRow);
		if (tr == null) {
			tr = new TableRow(this.odsFile, nRow);
			this.qTableRows.set(nRow, tr);
		}
		return tr.getCell(nCol);
	}

	public List<TableColumnStyle> getColumnStyles() {
		return this.qColumnStyles;
	}

	public void appendXMLConfig(Util util, Appendable appendable)
			throws IOException {
		appendable.append("<config:config-item-map-entry");
		util.appendAttribute(appendable, "config:name", this.sName);
		appendable.append(">");
		this.cursorPositionX.appendXML(util, appendable);
		this.cursorPositionY.appendXML(util, appendable);
		this.horizontalSplitMode.appendXML(util, appendable);
		this.verticalSplitMode.appendXML(util, appendable);
		this.horizontalSplitMode.appendXML(util, appendable);
		this.verticalSplitMode.appendXML(util, appendable);
		this.horizontalSplitPosition.appendXML(util, appendable);
		this.verticalSplitPosition.appendXML(util, appendable);
		this.activeSplitRange.appendXML(util, appendable);
		this.positionLeft.appendXML(util, appendable);
		this.positionRight.appendXML(util, appendable);
		this.positionTop.appendXML(util, appendable);
		this.positionBottom.appendXML(util, appendable);
		this.zoomType.appendXML(util, appendable);
		this.zoomValue.appendXML(util, appendable);
		this.pageViewZoomValue.appendXML(util, appendable);
		appendable.append("</config:config-item-map-entry>");
	}

	public int getLastCol() {
		return this.nLastCol;
	}

	public int getLastRow() {
		return this.qTableRows.size() - 1;
	}

	/**
	 * Get the name of this table.
	 * 
	 * @return The name of this table.
	 */
	@Override
	public String getName() {
		return this.sName;
	}

	public List<TableRow> getRows() {
		return this.qTableRows;
	}

	public TableRow getRow(int nRow) throws FastOdsException {
		this.checkRow(nRow);

		TableRow tr;
		if (nRow >= this.qTableRows.size()) {
			tr = new TableRow(this.odsFile, nRow);
			this.qTableRows.set(nRow, tr);
		} else {
			tr = this.qTableRows.get(nRow);
		}
		return tr;
	}

	public TableRow nextRow() throws FastOdsException {
		final int nRow = this.qTableRows.size();
		this.checkRow(nRow);

		TableRow tr = new TableRow(this.odsFile, nRow);
		this.qTableRows.add(tr);
		return tr;
	}

	/**
	 * Get the current TableFamilyStyle
	 * 
	 * @return The current TableStlye
	 */
	public String getStyleName() {
		return this.styleName;
	}

	/**
	 * Set the value of a cell.
	 * 
	 * @param nRow
	 *            The row (maximal 65536)
	 * @param nCol
	 *            The column (maximal 256)
	 * @param type
	 *            The type of the value,
	 *            TableCell.STYLE_STRING,TableCell.STYLE_FLOAT or
	 *            TableCell.STYLE_PERCENTAGE
	 * @param value
	 *            The value to be set
	 * @return true
	 * @throws FastOdsException
	 *             Thrown when nRow or nCol have wrong values.
	 */
	public boolean setCell(final int nRow, final int nCol, final Type type,
			final String value) throws FastOdsException {
		this.checkRow(nRow);
		this.checkCol(nCol);

		if (nCol > this.nLastCol) {
			this.nLastCol = nCol;
		}
		TableRow tr = this.getRow(nRow);
		tr.setCell(nCol, type, value);
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
	 *            The TableFamilyStyle to be used for this cell.
	 * @return true
	 * @throws FastOdsException
	 *             Thrown when nRow or nCol have wrong values.
	 */
	public boolean setCell(final int nRow, final int nCol, final Type valuetype,
			final String value, final TableCellStyle ts)
			throws FastOdsException {

		this.setCell(nRow, nCol, valuetype, value);
		this.setCellStyle(nRow, nCol, ts);
		return true;
	}

	/**
	 * Set the cell style for the specified cell.
	 * 
	 * @param nRow
	 *            The row number
	 * @param nCol
	 *            The column number
	 * @param ts
	 *            The TableFamilyStyle to be used
	 * @return TRUE The cell style was set
	 * @throws FastOdsException
	 *             when nRow or nCol have wrong values
	 */
	public boolean setCellStyle(final int nRow, final int nCol,
			final TableCellStyle ts) throws FastOdsException {

		this.checkCol(nCol);
		if (nCol > this.nLastCol) {
			this.nLastCol = nCol;
		}
		TableRow tr = this.getRow(nRow);
		tr.setCellStyle(nCol, ts);
		return true;
	}

	/**
	 * Set the style of a column.
	 * 
	 * @param nCol
	 *            The column number
	 * @param ts
	 *            The style to be used, make sure the style is of type
	 *            TableFamilyStyle.STYLEFAMILY_TABLECOLUMN
	 * @throws FastOdsException
	 *             Thrown if nCol has an invalid value.
	 */
	public void setColumnStyle(final int nCol, final TableColumnStyle ts)
			throws FastOdsException {
		this.checkCol(nCol);
		this.qColumnStyles.set(nCol, ts);
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
	 * Set a new TableFamilyStyle
	 * 
	 * @param style
	 *            The new TableStlye to be used
	 */
	public void setStyle(String style) {
		this.styleName = style;
	}

	private void appendRows(Appendable appendable, Util util)
			throws IOException {
		// Loop through all rows
		for (TableRow tr : this.getRows()) {
			if (tr != null) {
				tr.appendXML(util, appendable);
			} else {
				// Empty TableRow
				appendable.append("<table:table-row table:style-name=\"ro1\">");
				// no toString() here
				appendable.append("<table:table-cell ");
				util.appendAttribute(appendable,
						"table:number-columns-repeated", this.getLastCol());
				appendable.append("/>");
				appendable.append("</table:table-row>");
			}
		}
	}

	private void appendColumnStyles(Appendable appendable, Util util)
			throws IOException {
		TableColumnStyle ts0 = null;
		TableColumnStyle ts1 = null;
		String sDefaultCellSytle0 = "Default";
		String sDefaultCellSytle1 = "Default";

		// Loop through all table column styles and write the informations
		String sSytle0 = "co1";
		String sSytle1 = "co1";

		// If there is only one column style in column one, just write this
		// info to OutputStream o
		if (this.getColumnStyles().size() == 1) {
			ts0 = this.getColumnStyles().get(0);
			Table.appendColumnStyle(appendable, util, ts0.getName(),
					ts0.getDefaultCellStyle(), 1);

		}

		// If there is more than one column with a style, loop through all
		// styles and
		// write the info to OutputStream o
		if (this.getColumnStyles().size() > 1) {
			int nCount = 1;

			Iterator<TableColumnStyle> iterator = this.getColumnStyles()
					.iterator();
			ts1 = iterator.next();
			while (iterator.hasNext()) {
				ts0 = ts1;
				ts1 = iterator.next();

				if (ts0 == null) {
					sSytle0 = "co1";
					sDefaultCellSytle0 = "Default";
				} else {
					sSytle0 = ts0.getName();
					sDefaultCellSytle0 = ts0.getDefaultCellStyle();
				}
				if (ts1 == null) {
					sSytle1 = "co1";
					sDefaultCellSytle1 = "Default";
				} else {
					sSytle1 = ts1.getName();
					sDefaultCellSytle1 = ts1.getDefaultCellStyle();
				}

				if (sSytle0.equalsIgnoreCase(sSytle1)) {
					nCount++;
				} else {
					Table.appendColumnStyle(appendable, util, sSytle0,
							sDefaultCellSytle0, nCount);

					nCount = 1;
				}

			}
			Table.appendColumnStyle(appendable, util, sSytle1,
					sDefaultCellSytle1, nCount);

		}
	}

	private void checkCol(final int nCol) throws FastOdsException {
		if (nCol >= Table.TABLE_MAXCOLUMNNUMBER) {
			throw new FastOdsException(new StringBuilder(
					"Maximum column number (256) exception, column value:[")
							.append(nCol).append("]").toString());
		}
		if (nCol < 0) {
			throw new FastOdsException(new StringBuilder(
					"Negative column number exception, column value:[")
							.append(nCol).append("]").toString());
		}
	}

	private void checkRow(final int nRow) throws FastOdsException {
		if (nRow >= Table.TABLE_MAXROWNUMBER) {
			throw new FastOdsException(new StringBuilder(
					"Maximum row number (65536) exception, row value:[")
							.append(nRow).append("]").toString());
		}
		if (nRow < 0) {
			throw new FastOdsException(new StringBuilder(
					"Negative row number exception, row value:[").append(nRow)
							.append("]").toString());
		}
	}

	private static void appendColumnStyle(Appendable appendable, Util util,
			String sSytle, String sDefaultCellSytle, int nCount)
			throws IOException {
		appendable.append("<table:table-column ");
		util.appendAttribute(appendable, "table:style-name", sSytle);
		util.appendAttribute(appendable, "table:number-columns-repeated",
				nCount);
		util.appendAttribute(appendable, "table:default-cell-style-name",
				sDefaultCellSytle);
		appendable.append("/>");
	}

	@Override
	public void appendXML(Util util, Appendable appendable) throws IOException {
		appendable.append("<table:table table:name=\"").append(this.getName())
				.append("\" table:style-name=\"").append(this.getStyleName())
				.append("\" table:print=\"false\">");
		appendable.append(
				"<office:forms form:automatic-focus=\"false\" form:apply-design-mode=\"false\"/>");
		this.appendColumnStyles(appendable, util);
		this.appendRows(appendable, util);
		appendable.append("</table:table>");
	}

}
