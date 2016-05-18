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
*    
*    Changes:
*    20100117:	Fixed the getName() to return the filename of the ODS files
*/
package com.github.jferard.fastods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Calendar;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * TODO : split and clean code
 * 
 * @author Martin Schulz<br>
 * 
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *         <br>
 * 
 *         This file OdsFile.java is part of SimpleODS.<br>
 *         0.5.1 Changed all 'throw Exception' to 'throw SimpleOdsException'.
 *         <br>
 *         0.5.3 Added getMeta().<br>
 *         Added getCell(final int nTab, final String sPos).<br>
 *         Added getCell(final int nTab, final int nRow, final int nCol)<br>
 *
 */
public class OdsFile {
	private Util util = Util.getInstance();
	private String sFilename;
	private MimetypeEntry mimetypeEntry = new MimetypeEntry();
	private ManifestEntry manifestEntry = new ManifestEntry();
	private MetaEntry metaEntry = new MetaEntry();
	private FooterHeader header = null;
	private FooterHeader footerHeader = null;
	private ContentEntry contentEntry;
	private StylesEntry stylesEntry;

	// ViewSettings
	private ConfigItem VisibleAreaTop = new ConfigItem("VisibleAreaTop", "int",
			"0");
	private ConfigItem VisibleAreaLeft = new ConfigItem("VisibleAreaLeft",
			"int", "0");
	private ConfigItem VisibleAreaWidth = new ConfigItem("VisibleAreaWidth",
			"int", "680");
	private ConfigItem VisibleAreaHeight = new ConfigItem("VisibleAreaHeight",
			"int", "400");

	// ViewIdSettings
	private ConfigItem ViewIdActiveTable = new ConfigItem("ActiveTable",
			"string", "Tab1");
	private ConfigItem ViewIdHorizontalScrollbarWidth = new ConfigItem(
			"ViewIdHorizontalScrollbarWidth", "int", "270");
	private ConfigItem ViewIdZoomType = new ConfigItem("ViewIdZoomType",
			"short", "0");
	private ConfigItem ViewIdZoomValue = new ConfigItem("ViewIdZoomValue",
			"int", "100");
	private ConfigItem ViewIdPageViewZoomValue = new ConfigItem(
			"ViewIdPageViewZoomValue", "int", "60");
	private ConfigItem ViewIdShowPageBreakPreview = new ConfigItem(
			"ViewIdShowPageBreakPreview", "boolean", "false");
	private ConfigItem ViewIdShowZeroValues = new ConfigItem(
			"ViewIdShowZeroValues", "boolean", "true");
	private ConfigItem ViewIdShowNotes = new ConfigItem("ViewIdShowNotes",
			"boolean", "true");
	private ConfigItem ViewIdShowGrid = new ConfigItem("ViewIdShowGrid",
			"boolean", "true");
	private ConfigItem ViewIdGridColor = new ConfigItem("ViewIdGridColor",
			"long", "12632256");
	private ConfigItem ViewIdShowPageBreaks = new ConfigItem(
			"ViewIdShowPageBreaks", "boolean", "true");
	private ConfigItem ViewIdHasColumnRowHeaders = new ConfigItem(
			"ViewIdHasColumnRowHeaders", "boolean", "true");
	private ConfigItem ViewIdHasSheetTabs = new ConfigItem("ViewIdHasSheetTabs",
			"boolean", "true");
	private ConfigItem ViewIdIsOutlineSymbolsSet = new ConfigItem(
			"ViewIdIsOutlineSymbolsSet", "boolean", "true");
	private ConfigItem ViewIdIsSnapToRaster = new ConfigItem(
			"ViewIdIsSnapToRaster", "boolean", "false");
	private ConfigItem ViewIdRasterIsVisible = new ConfigItem(
			"ViewIdRasterIsVisible", "boolean", "false");
	private ConfigItem ViewIdRasterResolutionX = new ConfigItem(
			"ViewIdRasterResolutionX", "int", "1000");
	private ConfigItem ViewIdRasterResolutionY = new ConfigItem(
			"ViewIdRasterResolutionY", "int", "1000");
	private ConfigItem ViewIdRasterSubdivisionX = new ConfigItem(
			"ViewIdRasterSubdivisionX", "int", "1");
	private ConfigItem ViewIdRasterSubdivisionY = new ConfigItem(
			"ViewIdRasterSubdivisionY", "int", "1");
	private ConfigItem ViewIdIsRasterAxisSynchronized = new ConfigItem(
			"ViewIdIsRasterAxisSynchronized", "boolean", "true");

	// ConfigurationSettings
	private ConfigItem ShowZeroValues = new ConfigItem("ShowZeroValues",
			"boolean", "true");
	private ConfigItem ShowNotes = new ConfigItem("ShowNotes", "boolean",
			"true");
	private ConfigItem ShowGrid = new ConfigItem("ShowGrid", "boolean", "true");
	private ConfigItem GridColor = new ConfigItem("GridColor", "long",
			"12632256");
	private ConfigItem ShowPageBreaks = new ConfigItem("ShowPageBreaks",
			"boolean", "true");
	private ConfigItem LinkUpdateMode = new ConfigItem("LinkUpdateMode",
			"short", "3");
	private ConfigItem HasColumnRowHeaders = new ConfigItem(
			"HasColumnRowHeaders", "boolean", "true");
	private ConfigItem HasSheetTabs = new ConfigItem("HasSheetTabs", "boolean",
			"true");
	private ConfigItem IsOutlineSymbolsSet = new ConfigItem(
			"IsOutlineSymbolsSet", "boolean", "true");
	private ConfigItem IsSnapToRaster = new ConfigItem("IsSnapToRaster",
			"boolean", "false");
	private ConfigItem RasterIsVisible = new ConfigItem("RasterIsVisible",
			"boolean", "false");
	private ConfigItem RasterResolutionX = new ConfigItem("RasterResolutionX",
			"int", "1000");
	private ConfigItem RasterResolutionY = new ConfigItem("RasterResolutionY",
			"int", "1000");
	private ConfigItem RasterSubdivisionX = new ConfigItem("RasterSubdivisionX",
			"int", "1");
	private ConfigItem RasterSubdivisionY = new ConfigItem("RasterSubdivisionY",
			"int", "1");
	private ConfigItem IsRasterAxisSynchronized = new ConfigItem(
			"IsRasterAxisSynchronized", "boolean", "true");
	private ConfigItem AutoCalculate = new ConfigItem("AutoCalculate",
			"boolean", "true");
	private ConfigItem PrinterName = new ConfigItem("PrinterName", "string",
			"");
	private ConfigItem PrinterSetup = new ConfigItem("PrinterSetup",
			"base64Binary", "");
	private ConfigItem ApplyUserData = new ConfigItem("ApplyUserData",
			"boolean", "true");
	private ConfigItem CharacterCompressionType = new ConfigItem(
			"CharacterCompressionType", "short", "0");
	private ConfigItem IsKernAsianPunctuation = new ConfigItem(
			"IsKernAsianPunctuation", "boolean", "false");
	private ConfigItem SaveVersionOnClose = new ConfigItem("SaveVersionOnClose",
			"boolean", "false");
	private ConfigItem UpdateFromTemplate = new ConfigItem("UpdateFromTemplate",
			"boolean", "true");
	private ConfigItem AllowPrintJobCancel = new ConfigItem(
			"AllowPrintJobCancel", "boolean", "true");
	private ConfigItem LoadReadonly = new ConfigItem("LoadReadonly", "boolean",
			"false");

	/**
	 * Create a new ODS file.
	 * 
	 * @param sName
	 *            - The filename for this file, if this file exists it is
	 *            overwritten
	 */
	public OdsFile(final String sName) {
		this.newFile(sName);
		// Add four default stylesEntry to contentEntry
		new TableStyle(TableStyle.STYLE_TABLE, "ta1", this);
		new TableStyle(TableStyle.STYLE_TABLEROW, "ro1", this);
		new TableStyle(TableStyle.STYLE_TABLECOLUMN, "co1", this);
		new TableStyle(TableStyle.STYLE_TABLECELL, "Default", this);
		PageStyle pm1 = new PageStyle("Mpm1", this);
		this.getContent().addPageStyle(pm1);
	}

	/**
	 * Add a new table to the file, the new table is set to the active table.
	 * <br>
	 * A maximum of 256 table per file is supported.<br>
	 * Use setActiveTable to override the current active table, this has no
	 * influence to<br>
	 * the program, the active table is the first table that is shown in
	 * OpenOffice.
	 * 
	 * @param sName
	 *            - The name of the table to add
	 * @return true - The table was added,<br>
	 *         false - The table already exist, it was added again
	 * @throws SimpleOdsException
	 */
	public boolean addTable(final String sName) throws SimpleOdsException {

		if (this.getContent().addTable(sName)) {
			setActiveTable(sName);
			return true;
		}
		return false;
	}

	/**
	 * Get the TableCell object.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @return The requested TableCell object.
	 * @throws SimpleOdsException
	 */
	public TableCell getCell(final int nTab, final int nRow, final int nCol)
			throws SimpleOdsException {
		return this.getContent().getCell(nTab, nRow, nCol);
	}

	/**
	 * Get the TableCell object.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @return The requested TableCell object.
	 * @throws SimpleOdsException
	 */
	public TableCell getCell(final int nTab, final String sPos)
			throws SimpleOdsException {
		int nRow = this.util.positionToRow(sPos);
		int nCol = this.util.positionToColumn(sPos);

		return this.getContent().getCell(nTab, nRow, nCol);
	}

	/**
	 * @return The contentEntry object for this OdsFile
	 */
	public ContentEntry getContent() {
		if (this.contentEntry == null) {
			this.contentEntry = new ContentEntry();
		}
		return this.contentEntry;
	}

	public PageStyle getDefaultPageStyle() {
		return this.getContent().getDefaultPageStyle();
	}

	/**
	 * Returns the current footerHeader.
	 * 
	 * @return The footerHeader that is currently set , maybe null if no
	 *         footerHeader was set
	 */
	public FooterHeader getFooter() {
		return this.footerHeader;
	}

	/**
	 * Returns the current header.
	 * 
	 * @return The header that is currently set , maybe null if no header was
	 *         set
	 */
	public FooterHeader getHeader() {
		return this.header;
	}

	/**
	 * Get the MetaEntry data for this OdsFile.
	 * 
	 * @return The metaEntry data
	 */
	public MetaEntry getMeta() {
		return this.metaEntry;
	}

	/**
	 * The filename of the spreadsheet file.
	 * 
	 * @return The filename of the spreadsheet file
	 */
	public String getName() {
		return this.sFilename;
	}

	/**
	 * @return The stylesEntry object for this OdsFile
	 */
	public StylesEntry getStyles() {
		if (this.stylesEntry == null) {
			this.stylesEntry = new StylesEntry(this);
		}
		return this.stylesEntry;
	}

	// -----------------------------------------------------
	// All methods for setCell with the nValueType to be set by the user
	// -----------------------------------------------------

	/**
	 * Returns the name of the table.
	 * 
	 * @param n
	 *            The number of the table
	 * @return The name of the table
	 */
	public String getTableName(final int n) throws SimpleOdsException {
		final ObjectQueue<Table> tableQueue = this.getContent().getTableQueue();
		if (n < 0 || tableQueue.size() <= n) {
			throw new SimpleOdsException(
					new StringBuilder("Wrong table number [").append(n)
							.append("]").toString());
		}

		Table t = tableQueue.get(n);
		return (t.getName());
	}

	/**
	 * Search a table by name and return its number.
	 * 
	 * @param sName
	 *            The name of the table
	 * @return The number of the table or -1 if sName was not found
	 */
	public int getTableNumber(final String sName) {
		ListIterator<Table> iterator = this.getContent().getTableQueue()
				.listIterator();
		while (iterator.hasNext()) {
			int n = iterator.nextIndex();
			Table tab = iterator.next();
			if (tab.getName().equalsIgnoreCase(sName)) {
				return n;
			}
		}

		return -1;
	}

	/**
	 * Gets the number of the last table.
	 * 
	 * @return The number of the last table
	 */
	public int lastTableNumber() {
		return this.getContent().getTableQueue().size();
	}

	/**
	 * Create a new,empty file, use addTable to add tables.
	 * 
	 * @param sName
	 *            - The filename of the new spreadsheet file, if this file
	 *            exists it is overwritten
	 * @return False, if filename is a directory
	 */
	public boolean newFile(final String sName) {

		try {
			File f = new File(sName);

			// Check if sName is a directory and abort if YES
			if (f.isDirectory()) {
				return false;
			}

			this.sFilename = sName;

			this.save();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Save the new file.
	 * 
	 * @return true - the file was saved<br>
	 *         false - an exception happened
	 */
	public boolean save() {

		try {
			return this.save(new FileOutputStream(this.sFilename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Save the new file.
	 * 
	 * @param output
	 *            The OutputStream that should be used.
	 * @return true - the file was saved<br>
	 *         false - an exception happened
	 */
	public boolean save(final OutputStream output) {
		try {
			ZipOutputStream out = new ZipOutputStream(output);
			this.mimetypeEntry.write(this.util, out);
			this.manifestEntry.write(this.util, out);
			this.metaEntry.write(this.util, out);
			this.getStyles().write(this.util, out);
			this.getContent().write(this.util, out);
			this.createConfigurations2(out);
			this.createSettings(this.util, out);
			out.putNextEntry(new ZipEntry("Thumbnails/"));
			out.closeEntry();

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// ----------------------------------------------------------------------
	// All methods for setCell with TableCell.STYLE_STRING
	// ----------------------------------------------------------------------

	/**
	 * Set the active table, this is the table that is shown if you open the
	 * file.
	 * 
	 * @param nTab
	 *            The table number, this table should already exist, otherwise
	 *            the first table is shown
	 * @return true - The active table was set, false - nTab has an illegal
	 *         value
	 */
	public boolean setActiveTable(final int nTab) {
		if (nTab < 0 || nTab >= this.getContent().getTableQueue().size()) {
			return false;
		}

		Table tab = this.getContent().getTableQueue().get(nTab);

		this.ViewIdActiveTable = new ConfigItem("ActiveTable", "string",
				tab.getName());

		return true;
	}

	/**
	 * Set the active table , this is the table that is shown if you open the
	 * file.
	 * 
	 * @param sName
	 *            The table name, this table should already exist, otherwise the
	 *            first table is shown
	 */
	public void setActiveTable(final String sName) {
		this.ViewIdActiveTable = new ConfigItem("ActiveTable", "string", sName);
	}

	/**
	 * Sets the cell value in table nTab to the date from the Calendar object.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param cal
	 *            The calendar object with the date
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final Calendar cal) throws SimpleOdsException {
		TableCell tc = getContent().getCell(nTab, nRow, nCol);
		tc.setDateValue(cal);
	}

	/**
	 * Sets the cell value in table nTab to the date from the Calendar object.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param cal
	 *            The calendar object with the date
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final Calendar cal, final TableStyle ts) throws SimpleOdsException {
		this.setCell(nTab, nRow, nCol, cal);
		getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	// -----------------------------------------------------------------------
	// All methods for setCell with TableCell.STYLE_FLOAT and integer nValue
	// -----------------------------------------------------------------------

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.STYLE_FLOAT.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param dValue
	 *            The value to set the cell to
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final double dValue) throws SimpleOdsException {
		getContent().setCell(nTab, nRow, nCol, TableCell.STYLE_FLOAT,
				Double.toString(dValue));
	}

	/**
	 * Sets the cell value in table nTab to the given value, the value type is
	 * TableCell.STYLE_FLOAT.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param dValue
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final double dValue, final TableStyle ts)
			throws SimpleOdsException {
		this.setCell(nTab, nRow, nCol, dValue);
		getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.STYLE_FLOAT.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param nValue
	 *            The value to set the cell to
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final int nValue) throws SimpleOdsException {
		getContent().setCell(nTab, nRow, nCol, TableCell.STYLE_FLOAT,
				Integer.toString(nValue));
	}

	/**
	 * Sets the cell value in Table nTab to the given values.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param nValuetype
	 *            The value type of sValue,
	 *            TableCell.STYLE_STRING,TableCell.STYLE_FLOAT or
	 *            TableCell.STYLE_PERCENTAGE
	 * @param sValue
	 *            The value to set the cell to
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			int nValuetype, String sValue) throws SimpleOdsException {
		getContent().setCell(nTab, nRow, nCol, nValuetype, sValue);
	}

	// -----------------------------------------------------------------------
	// All methods for setCell with TableCell.STYLE_FLOAT and double dValue
	// -----------------------------------------------------------------------

	/**
	 * Sets the cell value in Table nTab to the given values.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table.
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param nValuetype
	 *            The value type of sValue,
	 *            TableCell.STYLE_STRING,TableCell.STYLE_FLOAT or
	 *            TableCell.STYLE_PERCENTAGE.
	 * @param sValue
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			int nValuetype, String sValue, TableStyle ts)
			throws SimpleOdsException {
		getContent().setCell(nTab, nRow, nCol, nValuetype, sValue);
		getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.STYLE_FLOAT.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param nValue
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final int nValue, final TableStyle ts) throws SimpleOdsException {
		getContent().setCell(nTab, nRow, nCol, TableCell.STYLE_FLOAT,
				Integer.toString(nValue));
		getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.STYLE_STRING.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param sValue
	 *            The value to set the cell to
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final String sValue) throws SimpleOdsException {
		getContent().setCell(nTab, nRow, nCol, TableCell.STYLE_STRING, sValue);
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.STYLE_STRING.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param sValue
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final String sValue, final TableStyle ts)
			throws SimpleOdsException {
		getContent().setCell(nTab, nRow, nCol, TableCell.STYLE_STRING, sValue);
		getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	// -----------------------------------------------------------------------
	// All methods for setCell with TableCell.STYLE_DATE
	// -----------------------------------------------------------------------

	/**
	 * Sets the cell value in table nTab to the date from the Calendar object.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param cal
	 *            The calendar object with the date
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final String sPos, final Calendar cal)
			throws SimpleOdsException {
		this.setCell(nTab, this.util.positionToRow(sPos),
				this.util.positionToColumn(sPos), cal);
	}

	/**
	 * Sets the cell value in table nTab to the date from the Calendar object.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param cal
	 *            The calendar object with the date
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final String sPos, final Calendar cal,
			final TableStyle ts) throws SimpleOdsException {
		int nRow = this.util.positionToRow(sPos);
		int nCol = this.util.positionToColumn(sPos);

		this.setCell(nTab, nRow, nCol, cal);
		getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.STYLE_FLOAT.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param dValue
	 *            The value to set the cell to
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final String sPos, final double dValue)
			throws SimpleOdsException {
		getContent().setCell(nTab, this.util.positionToRow(sPos),
				this.util.positionToColumn(sPos), TableCell.STYLE_FLOAT,
				Double.toString(dValue));
	}

	/**
	 * Sets the cell value in table nTab to the given value, the value type is
	 * TableCell.STYLE_FLOAT.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param dValue
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final String sPos, final double dValue,
			final TableStyle ts) throws SimpleOdsException {
		int nRow = this.util.positionToRow(sPos);
		int nCol = this.util.positionToColumn(sPos);

		this.setCell(nTab, nRow, nCol, dValue);
		getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.STYLE_FLOAT.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param nValue
	 *            The value to set the cell to
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final String sPos, final int nValue)
			throws SimpleOdsException {
		getContent().setCell(nTab, this.util.positionToRow(sPos),
				this.util.positionToColumn(sPos), TableCell.STYLE_FLOAT,
				Integer.toString(nValue));
	}

	/**
	 * Sets the cell value in Table nTab to the given values.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param nValuetype
	 *            The value type of sValue,
	 *            TableCell.STYLE_STRING,TableCell.STYLE_FLOAT or
	 *            TableCell.STYLE_PERCENTAGE
	 * @param sValue
	 *            The value to set the cell to
	 */
	public void setCell(final int nTab, final String sPos, final int nValuetype,
			String sValue) throws SimpleOdsException {
		getContent().setCell(nTab, this.util.positionToRow(sPos),
				this.util.positionToColumn(sPos), nValuetype, sValue);
	}

	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------

	/**
	 * Sets the cell value in Table nTab to the given values.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param nValuetype
	 *            The value type of sValue,
	 *            TableCell.STYLE_STRING,TableCell.STYLE_FLOAT or
	 *            TableCell.STYLE_PERCENTAGE.
	 * @param sValue
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final String sPos, final int nValuetype,
			final String sValue, final TableStyle ts)
			throws SimpleOdsException {
		int nRow = this.util.positionToRow(sPos);
		int nCol = this.util.positionToColumn(sPos);

		getContent().setCell(nTab, nRow, nCol, nValuetype, sValue);
		getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.STYLE_FLOAT.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param nValue
	 *            The value to set the cell to
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final String sPos, final int nValue,
			final TableStyle ts) throws SimpleOdsException {
		int nRow = this.util.positionToRow(sPos);
		int nCol = this.util.positionToColumn(sPos);
		getContent().setCell(nTab, nRow, nCol, TableCell.STYLE_FLOAT,
				Integer.toString(nValue));
		getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.STYLE_STRING.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param sValue
	 *            The value to set the cell to
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final String sPos, final String sValue)
			throws SimpleOdsException {
		getContent().setCell(nTab, this.util.positionToRow(sPos),
				this.util.positionToColumn(sPos), TableCell.STYLE_STRING,
				sValue);
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.STYLE_STRING.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param sValue
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 * @throws SimpleOdsException
	 */
	public void setCell(final int nTab, final String sPos, final String sValue,
			final TableStyle ts) throws SimpleOdsException {
		int nRow = this.util.positionToRow(sPos);
		int nCol = this.util.positionToColumn(sPos);

		this.setCell(nTab, nRow, nCol, sValue, ts);

	}

	/**
	 * Sets the cell value in Table sTab to the given values.
	 * 
	 * @param sTab
	 *            The table name, this table must already exist
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param nValuetype
	 *            The value type of sValue,
	 *            TableCell.STYLE_STRING,TableCell.STYLE_FLOAT or
	 *            TableCell.STYLE_PERCENTAGE
	 * @param sValue
	 *            The value to set the cell to
	 * @throws SimpleOdsException
	 * 
	 * @deprecated As of release 0.2.1, use {@link #getTableNumber(String)}<br>
	 *             instead setCell("Tab2",1,1,"stringvalue") use
	 *             setCell(getTableNumber("Tab2"),1,1,"stringvalue")
	 * 
	 */
	@Deprecated
	public void setCell(String sTab, int nRow, int nCol, int nValuetype,
			String sValue) throws SimpleOdsException {

		ListIterator<Table> iterator = this.getContent().getTableQueue()
				.listIterator();
		while (iterator.hasNext()) {
			int n = iterator.nextIndex();
			Table tab = iterator.next();
			if (tab.getName().equals(sTab)) {
				getContent().setCell(n, nRow, nCol, nValuetype, sValue);
				return;
			}
		}

		throw new SimpleOdsException(new StringBuilder("Unknown table name [")
				.append(sTab).append("]").toString());
	}

	/**
	 * 
	 * Sets the cell value in Table sTab to the given values.
	 * 
	 * @param sTab
	 *            The table name, this table must already exist
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param nValuetype
	 *            The value type of sValue,
	 *            TableCell.STYLE_STRING,TableCell.STYLE_FLOAT or
	 *            TableCell.STYLE_PERCENTAGE.
	 * @param sValue
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 * @throws SimpleOdsException
	 * 
	 * @deprecated As of release 0.2.1, use {@link #getTableNumber(String)}<br>
	 *             instead e.g.setCell("Tab2",1,1,"stringvalue") use
	 *             setCell(getTableNumber("Tab2"),1,1,"stringvalue")
	 * 
	 */
	@Deprecated
	public void setCell(String sTab, int nRow, int nCol, int nValuetype,
			String sValue, TableStyle ts) throws SimpleOdsException {

		final ContentEntry contentEntry = this.getContent();
		ListIterator<Table> iterator = contentEntry.getTableQueue()
				.listIterator();
		while (iterator.hasNext()) {
			int n = iterator.nextIndex();
			Table tab = iterator.next();
			if (tab.getName().equals(sTab)) {
				contentEntry.setCell(n, nRow, nCol, nValuetype, sValue);
				contentEntry.setCellStyle(n, nRow, nCol, ts);
				return;
			}
		}

		throw new SimpleOdsException(new StringBuilder("Unknown table name [")
				.append(sTab).append("]").toString());
	}

	/**
	 * Sets the cell value in all tables to the date from the Calendar object.
	 * 
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param cal
	 *            The calendar object with the date
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 * @throws SimpleOdsException
	 */
	public void setCellInAllTables(final int nRow, final int nCol,
			final Calendar cal, final TableStyle ts) throws SimpleOdsException {

		final ContentEntry contentEntry = this.getContent();
		final int size = contentEntry.getTableQueue().size();
		for (int n = 0; n < size; n++) {
			this.setCell(n, nRow, nCol, cal);
			contentEntry.setCellStyle(n, nRow, nCol, ts);
		}

	}

	/**
	 * Sets the cell value in all tables to the given values.
	 * 
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param nValuetype
	 *            The value type of sValue,
	 *            TableCell.STYLE_STRING,TableCell.STYLE_FLOAT or
	 *            TableCell.STYLE_PERCENTAGE.
	 * @param sValue
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 * @throws SimpleOdsException
	 */
	public void setCellInAllTables(final int nRow, final int nCol,
			final int nValuetype, final String sValue, final TableStyle ts)
			throws SimpleOdsException {
		for (Table tab : this.getContent().getTableQueue()) {
			tab.setCell(nRow, nCol, nValuetype, sValue, ts);
		}
	}

	/**
	 * Sets the cell value in all tables to the date from the Calendar object.
	 * 
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param cal
	 *            The calendar object with the date
	 * @param ts
	 *            The table style for this cells, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 * @throws SimpleOdsException
	 */
	public void setCellInAllTables(final String sPos, final Calendar cal,
			final TableStyle ts) throws SimpleOdsException {
		int nRow = this.util.positionToRow(sPos);
		int nCol = this.util.positionToColumn(sPos);

		final ContentEntry contentEntry = this.getContent();
		final int size = contentEntry.getTableQueue().size();
		for (int n = 0; n < size; n++) {
			this.setCell(n, nRow, nCol, cal);
			contentEntry.setCellStyle(n, nRow, nCol, ts);
		}

	}

	/**
	 * Sets the cell value in all tables to the given values.
	 * 
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param nValuetype
	 *            The value type of sValue,
	 *            TableCell.STYLE_STRING,TableCell.STYLE_FLOAT or
	 *            TableCell.STYLE_PERCENTAGE.
	 * @param sValue
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 * @throws SimpleOdsException
	 */
	public void setCellInAllTables(final String sPos, final int nValuetype,
			final String sValue, final TableStyle ts)
			throws SimpleOdsException {
		int nRow = this.util.positionToRow(sPos);
		int nCol = this.util.positionToColumn(sPos);

		for (Table tab : this.getContent().getTableQueue()) {
			tab.setCell(nRow, nCol, nValuetype, sValue, ts);
		}
	}

	// -----------------------------------------------------------------------
	// Additional methods for cells
	// -----------------------------------------------------------------------
	/**
	 * Set the merging of multiple cells to one cell.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param nRowMerge
	 * @param nColumnMerge
	 * @throws SimpleOdsException
	 */
	public void setCellMerge(final int nTab, final int nRow, final int nCol,
			final int nRowMerge, final int nColumnMerge)
			throws SimpleOdsException {
		TableCell tc = this.getContent().getCell(nTab, nRow, nCol);
		tc.setRowsSpanned(nRowMerge);
		tc.setColumnsSpanned(nColumnMerge);
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param nRowMerge
	 * @param nColumnMerge
	 * @throws SimpleOdsException
	 */
	public void setCellMerge(final int nTab, final String sPos,
			final int nRowMerge, final int nColumnMerge)
			throws SimpleOdsException {
		this.setCellMerge(nTab, this.util.positionToRow(sPos),
				this.util.positionToColumn(sPos), nRowMerge, nColumnMerge);
	}

	/**
	 * Set the merging of multiple cells to one cell.
	 * 
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param nRowMerge
	 * @param nColumnMerge
	 * @throws SimpleOdsException
	 */
	public void setCellMergeInAllTables(int nRow, int nCol, int nRowMerge,
			int nColumnMerge) throws SimpleOdsException {
		TableCell tc;
		final ContentEntry contentEntry = this.getContent();
		final int size = contentEntry.getTableQueue().size();
		for (int n = 0; n < size; n++) {
			tc = contentEntry.getCell(n, nRow, nCol);
			tc.setRowsSpanned(nRowMerge);
			tc.setColumnsSpanned(nColumnMerge);
		}
	}

	/**
	 * Set the merging of multiple cells to one cell in all existing tables.
	 * 
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param nRowMerge
	 * @param nColumnMerge
	 * @throws SimpleOdsException
	 */
	public void setCellMergeInAllTables(final String sPos, final int nRowMerge,
			final int nColumnMerge) throws SimpleOdsException {

		final int size = this.getContent().getTableQueue().size();
		for (int n = 0; n < size; n++) {
			this.setCellMerge(n, this.util.positionToRow(sPos),
					this.util.positionToColumn(sPos), nRowMerge, nColumnMerge);
		}
	}

	/**
	 * Sets the style of this table cell.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param ts
	 *            The table style to be used, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 */
	public void setCellStyle(final int nTab, final int nRow, final int nCol,
			final TableStyle ts) throws SimpleOdsException {
		ts.addStylesObject(this.getStyles());
		getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	/**
	 * Sets the style of this table cell.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param ts
	 *            The table style to be used, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 */
	public void setCellStyle(final int nTab, final String sPos,
			final TableStyle ts) throws SimpleOdsException {
		int nRow = this.util.positionToRow(sPos);
		int nCol = this.util.positionToColumn(sPos);

		ts.addStylesObject(this.getStyles());
		getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	/**
	 * Sets the style of this table cell.
	 * 
	 * @param sTab
	 *            The table name, this table must already exist
	 * @param nRow
	 *            The row, 0 is the first row
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param ts
	 *            The table style to be used, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECELL
	 * 
	 * @deprecated As of release 0.2.1, use {@link #getTableNumber(String)}<br>
	 *             instead setCellStyle("Tab2",1,1,"stringvalue") use
	 *             setCellStyle(getTableNumber("Tab2"),1,1,"stringvalue")
	 * 
	 */
	@Deprecated
	public void setCellStyle(final String sTab, final int nRow, final int nCol,
			final TableStyle ts) throws SimpleOdsException {
		ListIterator<Table> iterator = this.getContent().getTableQueue()
				.listIterator();
		while (iterator.hasNext()) {
			int n = iterator.nextIndex();
			Table tab = iterator.next();
			if (tab.getName().equals(sTab)) {
				getContent().setCellStyle(n, nRow, nCol, ts);
				return;
			}
		}

		throw new SimpleOdsException(new StringBuilder("Unknown table name [")
				.append(sTab).append("], add a table with method addTable(")
				.append(sTab).append(") first").toString());
	}

	/**
	 * Sets the style of this table column.
	 * 
	 * @param nTab
	 *            The table number, this table must already exist
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param ts
	 *            The table style to be used, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECOLUMN
	 */
	public void setColumnStyle(final int nTab, final int nCol,
			final TableStyle ts) throws SimpleOdsException {
		getContent().setColumnStyle(nTab, nCol, ts);
	}

	/**
	 * Set a new contentEntry to this OdsFile.
	 * 
	 * @param contentEntry
	 *            The new contentEntry to be used.
	 */
	public void setContent(final ContentEntry cont) {
		this.contentEntry = cont;
	}

	/**
	 * Sets a new footerHeader object, any earlier footerHeader that was set
	 * will be reset and the new footerHeader is used.
	 * 
	 * @param f
	 *            - The new footerHeader to be used.
	 */
	public void setFooter(final FooterHeader f) {
		this.footerHeader = f;
		getStyles().setFooter(f);
	}

	/**
	 * Sets a new header object, any earlier header that was set will be reset
	 * and the. new header is used.
	 * 
	 * @param h
	 *            - The new header to be used.
	 */
	public void setHeader(final FooterHeader h) {
		this.header = h;
		getStyles().setHeader(h);
	}

	/**
	 * Set the stylesEntry for this ODsFile to stylesEntry.
	 * 
	 * @param s
	 *            - The new stylesEntry object to be used.
	 */
	public final void setStyles(final StylesEntry s) {
		this.stylesEntry = s;
	}

	private void createConfigurations2(ZipOutputStream o) throws IOException {
		for (String entry : new String[] {
				"Configurations2/accelerator/current.xml",
				"Configurations2/floater/", "Configurations2/images/Bitmaps/",
				"Configurations2/menubar/", "Configurations2/popupmenu/",
				"Configurations2/progressbar/", "Configurations2/statusbar/",
				"Configurations2/toolbar/" }) {
			o.putNextEntry(new ZipEntry(entry));
			o.closeEntry();
		}
	}

	// TODO : class SettingsEntry
	private void createSettings(Util util, ZipOutputStream zipOut)
			throws IOException {
		zipOut.putNextEntry(new ZipEntry("settings.xml"));
		Writer writer = util.wrapStream(zipOut);

		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		writer.write(
				"<office:document-settings xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:config=\"urn:oasis:names:tc:opendocument:xmlns:config:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\" office:version=\"1.1\">");
		writer.write("<office:settings>");
		writer.write(
				"<config:config-item-set config:name=\"ooo:view-settings\">");
		writer.write(this.VisibleAreaTop.toXML(util));
		writer.write(this.VisibleAreaLeft.toXML(util));
		writer.write(this.VisibleAreaWidth.toXML(util));
		writer.write(this.VisibleAreaHeight.toXML(util));
		writer.write("<config:config-item-map-indexed config:name=\"Views\">");
		writer.write("<config:config-item-map-entry>");
		writer.write(
				"<config:config-item config:name=\"ViewId\" config:type=\"string\">View1</config:config-item>");
		writer.write("<config:config-item-map-named config:name=\"Tables\">");

		// Write the table informations
		for (Table t : this.getContent().getTableQueue()) {
			for (String item : t.getConfig(util))
				writer.write(item);
		}

		writer.write("</config:config-item-map-named>");
		writer.write(this.ViewIdActiveTable.toXML(util));
		writer.write(this.ViewIdHorizontalScrollbarWidth.toXML(util));
		writer.write(this.ViewIdPageViewZoomValue.toXML(util));
		writer.write(this.ViewIdZoomType.toXML(util));
		writer.write(this.ViewIdZoomValue.toXML(util));
		writer.write(this.ViewIdShowPageBreakPreview.toXML(util));
		writer.write(this.ViewIdShowZeroValues.toXML(util));
		writer.write(this.ViewIdShowNotes.toXML(util));
		writer.write(this.ViewIdShowGrid.toXML(util));
		writer.write(this.ViewIdGridColor.toXML(util));
		writer.write(this.ViewIdShowPageBreaks.toXML(util));
		writer.write(this.ViewIdHasColumnRowHeaders.toXML(util));
		writer.write(this.ViewIdIsOutlineSymbolsSet.toXML(util));
		writer.write(this.ViewIdHasSheetTabs.toXML(util));
		writer.write(this.ViewIdIsSnapToRaster.toXML(util));
		writer.write(this.ViewIdRasterIsVisible.toXML(util));
		writer.write(this.ViewIdRasterResolutionX.toXML(util));
		writer.write(this.ViewIdRasterResolutionY.toXML(util));
		writer.write(this.ViewIdRasterSubdivisionX.toXML(util));

		writer.write(this.ViewIdRasterSubdivisionY.toXML(util));
		writer.write(this.ViewIdIsRasterAxisSynchronized.toXML(util));
		writer.write("</config:config-item-map-entry>");
		writer.write("</config:config-item-map-indexed>");
		writer.write("</config:config-item-set>");
		writer.write(
				"<config:config-item-set config:name=\"ooo:configuration-settings\">");
		writer.write(this.ShowZeroValues.toXML(util));
		writer.write(this.ShowNotes.toXML(util));
		writer.write(this.ShowGrid.toXML(util));
		writer.write(this.GridColor.toXML(util));
		writer.write(this.ShowPageBreaks.toXML(util));
		writer.write(this.LinkUpdateMode.toXML(util));
		writer.write(this.HasColumnRowHeaders.toXML(util));
		writer.write(this.HasSheetTabs.toXML(util));
		writer.write(this.IsOutlineSymbolsSet.toXML(util));
		writer.write(this.IsSnapToRaster.toXML(util));
		writer.write(this.RasterIsVisible.toXML(util));
		writer.write(this.RasterResolutionX.toXML(util));
		writer.write(this.RasterResolutionY.toXML(util));
		writer.write(this.RasterSubdivisionX.toXML(util));
		writer.write(this.RasterSubdivisionY.toXML(util));
		writer.write(this.IsRasterAxisSynchronized.toXML(util));
		writer.write(this.AutoCalculate.toXML(util));
		writer.write(this.PrinterName.toXML(util));
		writer.write(this.PrinterSetup.toXML(util));
		writer.write(this.ApplyUserData.toXML(util));
		writer.write(this.CharacterCompressionType.toXML(util));
		writer.write(this.IsKernAsianPunctuation.toXML(util));
		writer.write(this.SaveVersionOnClose.toXML(util));
		writer.write(this.UpdateFromTemplate.toXML(util));
		writer.write(this.AllowPrintJobCancel.toXML(util));
		writer.write(this.LoadReadonly.toXML(util));
		writer.write("</config:config-item-set>");
		writer.write("</office:settings>");
		writer.write("</office:document-settings>");

		writer.flush();
		zipOut.closeEntry();
	}

	/**
	 * Sets the style of this table column.
	 * 
	 * @param sTab
	 *            The table name, this table must already exist
	 * @param nCol
	 *            The column, 0 is the first column
	 * @param ts
	 *            The table style to be used, must be of type
	 *            TableStyle.STYLEFAMILY_TABLECOLUMN
	 * 
	 * @deprecated As of release 0.2.1, use {@link #getTableNumber(final String
	 *             sName)} instead setColumnStyle("Tab2",1,1,"stringvalue") use
	 *             setColumnCell(getTableNumber("Tab2"),1,1,"stringvalue")
	 * 
	 */
	@Deprecated
	private void setColumnStyle(String sTab, int nCol, TableStyle ts)
			throws SimpleOdsException {

		final ContentEntry contentEntry = this.getContent();
		ListIterator<Table> iterator = contentEntry.getTableQueue()
				.listIterator();
		while (iterator.hasNext()) {
			int n = iterator.nextIndex();
			Table tab = iterator.next();
			if (tab.getName().equals(sTab)) {
				contentEntry.setColumnStyle(n, nCol, ts);
				return;
			}
		}

		throw new SimpleOdsException(new StringBuilder("Unknown table name [")
				.append(sTab).append("]").toString());
	}

}
