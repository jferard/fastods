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

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.odselement.config.ConfigItem;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntry;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntrySet;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.FullList;
import com.github.jferard.fastods.util.NamedObject;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.PositionUtil.Position;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * OpenDocument 9.1.2 table:table
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class Table implements NamedObject {
	private static void checkCol(final int col) throws FastOdsException {
		if (col < 0) {
			throw new FastOdsException(new StringBuilder(
					"Negative column number exception, column value:[")
					.append(col).append("]").toString());
		}
	}

	private static void checkRow(final int row) throws FastOdsException {
		if (row < 0) {
			throw new FastOdsException(new StringBuilder(
					"Negative row number exception, row value:[").append(row)
					.append("]").toString());
		}
	}

	private final int columnCapacity;
	private final List<TableColumnStyle> columnStyles;
	private final ConfigItemMapEntrySet configEntry;
	private final DataStyles format;
	private final PositionUtil positionUtil;
	private final StylesContainer stylesContainer;
	private final List<HeavyTableRow> tableRows;
	private final WriteUtil writeUtil;
	private final XMLUtil xmlUtil;
	private int curRowIndex;
	private int lastRowIndex;
	private String name;
	private int nullFieldCounter;
	private TableStyle style;

	public Table(final PositionUtil positionUtil, final WriteUtil writeUtil,
				 final XMLUtil xmlUtil, final StylesContainer stylesContainer,
				 final DataStyles format, final String name, final int rowCapacity,
				 final int columnCapacity) {
		this.xmlUtil = xmlUtil;
		this.writeUtil = writeUtil;
		this.positionUtil = positionUtil;
		this.stylesContainer = stylesContainer;
		this.format = format;
		this.name = name;
		this.columnCapacity = columnCapacity;
		this.style = TableStyle.DEFAULT_TABLE_STYLE;

		this.configEntry = ConfigItemMapEntrySet.createSet(this.name);
		this.configEntry.add(new ConfigItem("CursorPositionX", "int", "0"));
		this.configEntry.add(new ConfigItem("CursorPositionY", "int", "0"));
		this.configEntry.add(new ConfigItem("HorizontalSplitMode", "short", "0"));
		this.configEntry.add(new ConfigItem("VerticalSplitMode", "short", "0"));
		this.configEntry.add(new ConfigItem("HorizontalSplitPosition", "int", "0"));
		this.configEntry.add(new ConfigItem("VerticalSplitPosition", "int", "0"));
		this.configEntry.add(new ConfigItem("ActiveSplitRange", "short", "2"));
		this.configEntry.add(new ConfigItem("PositionLeft", "int", "0"));
		this.configEntry.add(new ConfigItem("PositionRight", "int", "0"));
		this.configEntry.add(new ConfigItem("PositionTop", "int", "0"));
		this.configEntry.add(new ConfigItem("PositionBottom", "int", "0"));
		this.configEntry.add(new ConfigItem("ZoomType", "short", "0"));
		this.configEntry.add(new ConfigItem("ZoomValue", "int", "100"));
		this.configEntry.add(new ConfigItem("PageViewZoomValue", "int", "60"));

		this.columnStyles = FullList.<TableColumnStyle>builder()
				.blankElement(TableColumnStyle.getDefaultColumnStyle(xmlUtil))
				.capacity(this.columnCapacity).build();
		this.tableRows = FullList.newListWithCapacity(rowCapacity);
		this.curRowIndex = -1;
		this.lastRowIndex = -1;
	}

	public void addData(final DataWrapper data) {
		data.addToTable(this);
	}

	private void appendColumnStyles(final Appendable appendable,
									final XMLUtil xmlUtil) throws IOException {
		final Iterator<TableColumnStyle> iterator = this.getColumnStyles()
				.iterator();
		if (!iterator.hasNext())
			return;

		TableColumnStyle ts0 = TableColumnStyle.getDefaultColumnStyle(xmlUtil);
		int count = 1;
		TableColumnStyle ts1 = iterator.next();
		while (iterator.hasNext()) {
			ts0 = ts1;
			ts1 = iterator.next();

			if (ts0.equals(ts1)) {
				count++;
			} else {
				ts0.appendXMLToTable(xmlUtil, appendable, count);
				count = 1;
			}

		}
		ts1.appendXMLToTable(xmlUtil, appendable, count);
		TableColumnStyle.getDefaultColumnStyle(xmlUtil)
				.appendXMLToTable(xmlUtil, appendable, 1);
	}

	private void appendPostamble(final Appendable appendable) throws IOException {
		appendable.append("</table:table>");
	}

	public void appendPreamble(final XMLUtil util, final Appendable appendable) throws IOException {
		appendable.append("<table:table");
		util.appendAttribute(appendable, "table:name", this.name);
		util.appendAttribute(appendable, "table:style-name",
				this.style.getName());
		util.appendEAttribute(appendable, "table:print", false);
		appendable.append("><office:forms");
		util.appendEAttribute(appendable, "form:automatic-focus", false);
		util.appendEAttribute(appendable, "form:apply-design-mode", false);
		appendable.append("/>");
		this.appendColumnStyles(appendable, util);
	}

	private void appendRows(final XMLUtil util, final Appendable appendable)
			throws IOException {
		this.appendRows(util, appendable, 0);
	}

	private void appendRows(final XMLUtil util, final Appendable appendable, final int firstRowIndex)
			throws IOException {
		if (firstRowIndex == 0)
			this.nullFieldCounter = 0;

		final int size = this.tableRows.size();
		for (int r = firstRowIndex; r < size; r++) {
			final HeavyTableRow tr = this.tableRows.get(r);
			if (tr == null) {
				this.nullFieldCounter++;
			} else {
				if (this.nullFieldCounter > 0) {
					appendable.append("<table:table-row");
					if (this.nullFieldCounter > 1) {
						util.appendEAttribute(appendable,
								"table:number-rows-repeated", this.nullFieldCounter);
					}
					util.appendEAttribute(appendable, "table:style-name",
							"ro1");
					appendable.append("><table:table-cell/></table:table-row>");
				}
				tr.appendXMLToTable(util, appendable);
			}
		}
	}

	public void appendXMLToContentEntry(final XMLUtil util,
										final Appendable appendable) throws IOException {
		this.appendPreamble(util, appendable);
		this.appendRows(util, appendable);
		this.appendPostamble(appendable);
	}

	@Deprecated
	public void appendXMLToSettingsElement(final XMLUtil util,
										   final Appendable appendable) throws IOException {
		this.configEntry.appendXML(util, appendable);
	}

	/**
	 * Open the table, flush all rows from start, but do not close the table
	 *
	 * @param util       a XMLUtil instance for writing XML
	 * @param appendable where to write
	 */
	public void flushAllAvailableRows(final XMLUtil util, final Appendable appendable) throws IOException {
		this.appendPreamble(util, appendable);
		this.appendRows(util, appendable, 0);
	}

	/**
	 * Flush all rows from a given position, and do close the table
	 *
	 * @param util       a XMLUtil instance for writing XML
	 * @param appendable where to write
	 * @param rowIndex   the first index to use.
	 */
	public void flushRemainingRowsFrom(final XMLUtil util, final Appendable appendable, final int rowIndex)
			throws IOException {
		if (rowIndex == 0)
			this.appendPreamble(util, appendable);
		this.appendRows(util, appendable, rowIndex);
		this.appendPostamble(appendable);
	}

	/**
	 * Flush all rows from a given position, but do not close the table
	 *
	 * @param util       a XMLUtil instance for writing XML
	 * @param appendable where to write
	 */
	public void flushSomeAvailableRowsFrom(final XMLUtil util, final Appendable appendable, final int rowIndex)
			throws IOException {
		if (rowIndex == 0)
			this.appendPreamble(util, appendable);
		this.appendRows(util, appendable, rowIndex);
	}

	public List<TableColumnStyle> getColumnStyles() {
		return this.columnStyles;
	}

	public ConfigItemMapEntry getConfigEntry() {
		return this.configEntry;
	}

	public int getLastRowNumber() {
		return this.tableRows.size() - 1;
	}

	/**
	 * Get the name of this table.
	 *
	 * @return The name of this table.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	public HeavyTableRow getRow(final int rowIndex) throws FastOdsException {
		Table.checkRow(rowIndex);
		return this.getRowSecure(rowIndex);
	}

	public HeavyTableRow getRow(final String pos) throws FastOdsException {
		final int row = this.positionUtil.getPosition(pos).getRow();
		return this.getRow(row);
	}

	public HeavyTableRow getRowSecure(final int rowIndex) {
		HeavyTableRow tr = this.tableRows.get(rowIndex);
		if (tr == null) {
			tr = new HeavyTableRow(this.writeUtil, this.xmlUtil,
					this.stylesContainer, this.format, this, rowIndex,
					this.columnCapacity);
			this.tableRows.set(rowIndex, tr);
			if (rowIndex > this.lastRowIndex)
				this.lastRowIndex = rowIndex;
		}
		return tr;
	}

	/**
	 * Get the current TableFamilyStyle
	 *
	 * @return The current TableStlye
	 */
	public String getStyleName() {
		return this.style.getName();
	}

	public HeavyTableRow nextRow() {
		this.curRowIndex++;
		return this.getRowSecure(this.curRowIndex);
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 *
	 * @param pos         The cell position e.g. 'A1'
	 * @param rowMerge    the number of rows to merge
	 * @param columnMerge the number of cells to merge
	 * @throws FastOdsException if the row index or the col index is negative
	 */
	@Deprecated
	public void setCellMerge(final String pos, final int rowMerge,
							 final int columnMerge) throws FastOdsException {
		final Position position = this.positionUtil.getPosition(pos);
		final HeavyTableRow row = this.getRow(position.getRow());
		row.setCellMerge(position.getColumn(), rowMerge, columnMerge);
	}

	/**
	 * Set the style of a column.
	 *
	 * @param col The column number
	 * @param ts  The style to be used, make sure the style is of type
	 *            TableFamilyStyle.STYLEFAMILY_TABLECOLUMN
	 * @throws FastOdsException Thrown if col has an invalid value.
	 */
	public void setColumnStyle(final int col, final TableColumnStyle ts)
			throws FastOdsException {
		Table.checkCol(col);
		this.stylesContainer.addStyleToContentAutomaticStyles(ts);
		this.columnStyles.set(col, ts);
	}

	public void setConfigItem(final String name, final String type,
							  final String value) {
		this.configEntry.add(new ConfigItem("PageViewZoomValue", "int", "60"));
	}

	/**
	 * Set the name of this table.
	 *
	 * @param name The name of this table.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	public void setSettings(final String viewId, final String item, final String value) {
		this.configEntry.set(item, value);
	}

	/**
	 * Set a new TableFamilyStyle
	 *
	 * @param style The new TableStyle to be used
	 */
	public void setStyle(final TableStyle style) {
		this.stylesContainer.addPageStyle(style.getPageStyle());
		this.stylesContainer.addStyleToContentAutomaticStyles(style);
		this.style = style;
	}
}
