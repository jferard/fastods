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
import java.util.List;

import com.github.jferard.fastods.style.DataStyles;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.Util;
import com.github.jferard.fastods.util.XMLUtil;

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
public class Table implements NamedObject {
	private static void checkCol(final int nCol) throws FastOdsException {
		if (nCol < 0) {
			throw new FastOdsException(new StringBuilder(
					"Negative column number exception, column value:[")
							.append(nCol).append("]").toString());
		}
	}

	private static void checkRow(final int nRow) throws FastOdsException {
		if (nRow < 0) {
			throw new FastOdsException(new StringBuilder(
					"Negative row number exception, row value:[").append(nRow)
							.append("]").toString());
		}
	}

	private final ConfigItem activeSplitRange;
	private final int columnCapacity;
	private final ConfigItem cursorPositionX;
	private final ConfigItem cursorPositionY;
	private final DataStyles format;
	private final ConfigItem horizontalSplitMode;
	private final ConfigItem horizontalSplitPosition;
	private final OdsFile odsFile;
	private final ConfigItem pageViewZoomValue;
	private final ConfigItem positionBottom;
	private final ConfigItem positionLeft;
	private final ConfigItem positionRight;
	private final ConfigItem positionTop;

	private final List<TableColumnStyle> qColumnStyles;
	private final List<HeavyTableRow> qTableRows;
	private String sName;

	private TableStyle style;
	private final Util util;
	private final ConfigItem verticalSplitMode;

	private final ConfigItem verticalSplitPosition;

	private final ConfigItem zoomType;
	private final ConfigItem zoomValue;

	Table(final OdsFile odsFile, final XMLUtil xmlUtil, final Util util,
			final DataStyles format, final String sName, final int rowCapacity,
			final int columnCapacity) {
		this.odsFile = odsFile;
		this.util = util;
		this.format = format;
		this.sName = sName;
		this.columnCapacity = columnCapacity;
		this.style = TableStyle.DEFAULT_TABLE_STYLE;
		this.cursorPositionX = new ConfigItem("CursorPositionX", "int", "0");
		this.cursorPositionY = new ConfigItem("cursorPositionY", "int", "0");
		this.horizontalSplitMode = new ConfigItem("horizontalSplitMode",
				"short", "0");
		this.verticalSplitMode = new ConfigItem("verticalSplitMode", "short",
				"0");
		this.horizontalSplitPosition = new ConfigItem("horizontalSplitPosition",
				"int", "0");
		this.verticalSplitPosition = new ConfigItem("verticalSplitPosition",
				"int", "0");
		this.activeSplitRange = new ConfigItem("activeSplitRange", "short",
				"2");
		this.positionLeft = new ConfigItem("positionLeft", "int", "0");
		this.positionRight = new ConfigItem("PositionRight", "int", "0");
		this.positionTop = new ConfigItem("PositionTop", "int", "0");
		this.positionBottom = new ConfigItem("positionBottom", "int", "0");
		this.zoomType = new ConfigItem("zoomType", "short", "0");
		this.zoomValue = new ConfigItem("zoomValue", "int", "100");
		this.pageViewZoomValue = new ConfigItem("pageViewZoomValue", "int",
				"60");

		this.qColumnStyles = FullList.<TableColumnStyle> builder()
				.blankElement(TableColumnStyle.getDefaultColumnStyle(xmlUtil))
				.capacity(this.columnCapacity).build();
		this.qTableRows = FullList.newListWithCapacity(rowCapacity);
	}

	public void addData(final DataWrapper data) {
		data.addToTable(this);
	}

	// /**
	// * Get a HeavyTableCell, if no HeavyTableCell was present at this
	// nRow,nCol, create a
	// * new one with a default of HeavyTableCell.STYLE_STRING and a content of
	// "".
	// *
	// * @param nRow
	// * The row
	// * @param nCol
	// * The column
	// * @return The HeavyTableCell for this position, maybe a new
	// HeavyTableCell
	// * @throws FastOdsException
	// */
	// public HeavyTableCell getCell(final int nRow, final int nCol)
	// throws FastOdsException {
	// final HeavyTableRow tr = this.getRow(nRow);
	// return tr.getCell(nCol);
	// }

	public List<TableColumnStyle> getColumnStyles() {
		return this.qColumnStyles;
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

	public HeavyTableRow getRow(final int nRow) throws FastOdsException {
		Table.checkRow(nRow);
		HeavyTableRow tr = this.qTableRows.get(nRow);
		if (tr == null) {
			tr = new HeavyTableRow(this.odsFile, this.util, this.format, nRow,
					this.columnCapacity);
			this.qTableRows.set(nRow, tr);
		}
		return tr;
	}

	public HeavyTableRow getRow(final String sPos) throws FastOdsException {
		final int nRow = this.util.getPosition(sPos).getRow();
		return this.getRow(nRow);
	}

	// public List<LightTableRow> getRows() {
	// return this.qTableRows;
	// }

	/**
	 * Get the current TableFamilyStyle
	 *
	 * @return The current TableStlye
	 */
	public String getStyleName() {
		return this.style.getName();
	}

	public HeavyTableRow nextRow() {
		final int nRow = this.qTableRows.size();
		final HeavyTableRow tr = new HeavyTableRow(this.odsFile, this.util,
				this.format, nRow, this.columnCapacity);
		this.qTableRows.add(tr);
		return tr;
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
		Table.checkCol(nCol);
		ts.addToFile(this.odsFile);
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
	 *            The new TableStyle to be used
	 */
	public void setStyle(final TableStyle style) {
		this.style = style;
	}

	private void appendColumnStyles(final Appendable appendable,
			final XMLUtil xmlUtil) throws IOException {
		final Iterator<TableColumnStyle> iterator = this.getColumnStyles()
				.iterator();
		if (!iterator.hasNext())
			return;

		TableColumnStyle ts0 = TableColumnStyle.getDefaultColumnStyle(xmlUtil);
		int nCount = 1;
		TableColumnStyle ts1 = iterator.next();
		while (iterator.hasNext()) {
			ts0 = ts1;
			ts1 = iterator.next();

			if (ts0.equals(ts1)) {
				nCount++;
			} else {
				ts0.appendXMLToTable(xmlUtil, appendable, nCount);
				nCount = 1;
			}

		}
		ts1.appendXMLToTable(xmlUtil, appendable, nCount);
		TableColumnStyle.getDefaultColumnStyle(xmlUtil)
				.appendXMLToTable(xmlUtil, appendable, 1);
	}

	private void appendRows(final Appendable appendable, final XMLUtil util)
			throws IOException {
		// Loop through all rows
		for (final HeavyTableRow tr : this.qTableRows) {
			if (tr == null) {
				appendable.append("<table:table-row");
				util.appendEAttribute(appendable, "table:style-name", "ro1");
				appendable.append("><table:table-cell/></table:table-row>");
			} else
				tr.appendXMLToTable(util, appendable);
		}
	}

	void appendXMLToContentEntry(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<table:table");
		util.appendAttribute(appendable, "table:name", this.sName);
		util.appendAttribute(appendable, "table:style-name",
				this.style.getName());
		util.appendEAttribute(appendable, "table:print", false);
		appendable.append("><office:forms");
		util.appendEAttribute(appendable, "form:automatic-focus", false);
		util.appendEAttribute(appendable, "form:apply-design-mode", false);
		appendable.append("/>");
		this.appendColumnStyles(appendable, util);
		this.appendRows(appendable, util);
		appendable.append("</table:table>");
	}

	void appendXMLToSettingsEntry(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<config:config-item-map-entry");
		util.appendAttribute(appendable, "config:name", this.sName);
		appendable.append(">");
		this.cursorPositionX.appendXMLToObject(util, appendable);
		this.cursorPositionY.appendXMLToObject(util, appendable);
		this.horizontalSplitMode.appendXMLToObject(util, appendable);
		this.verticalSplitMode.appendXMLToObject(util, appendable);
		this.horizontalSplitMode.appendXMLToObject(util, appendable);
		this.verticalSplitMode.appendXMLToObject(util, appendable);
		this.horizontalSplitPosition.appendXMLToObject(util, appendable);
		this.verticalSplitPosition.appendXMLToObject(util, appendable);
		this.activeSplitRange.appendXMLToObject(util, appendable);
		this.positionLeft.appendXMLToObject(util, appendable);
		this.positionRight.appendXMLToObject(util, appendable);
		this.positionTop.appendXMLToObject(util, appendable);
		this.positionBottom.appendXMLToObject(util, appendable);
		this.zoomType.appendXMLToObject(util, appendable);
		this.zoomValue.appendXMLToObject(util, appendable);
		this.pageViewZoomValue.appendXMLToObject(util, appendable);
		appendable.append("</config:config-item-map-entry>");
	}
}
