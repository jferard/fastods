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
import java.util.List;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.github.jferard.fastods.style.DataStyle;
import com.github.jferard.fastods.style.DataStyles;
import com.github.jferard.fastods.style.FHTextStyle;
import com.github.jferard.fastods.style.LocaleDataStyles;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.Util;
import com.github.jferard.fastods.util.Util.Position;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file OdsFile.java is part of FastODS.
 *
 *         This file OdsFile.java is part of FastODS.<br>
 *
 *         SimpleOds 0.5.1 Changed all 'throw Exception' to 'throw
 *         FastOdsException'. <br>
 *         SimpleOds 0.5.3 Added getMeta().<br>
 *         SimpleOds Added getCell(final int nTab, final String sPos).<br>
 *         SimpleOds Added getCell(final int nTab, final int nRow, final int
 *         nCol)<br>
 *
 *         WHERE ? root !
 */
public class OdsFile {
	/**
	 * 512 k of buffer before sending data to OutputStreamWriter.
	 */
	private static final int DEFAULT_BUFFER_SIZE = 512 * 1024;

	public static OdsFile create(final String sName) {
		final FastOdsXMLEscaper escaper = new FastOdsXMLEscaper();
		final XMLUtil xmlUtil = new XMLUtil(escaper);
		return new OdsFile(sName, new Util(), xmlUtil,
				new LocaleDataStyles(xmlUtil), OdsFile.DEFAULT_BUFFER_SIZE);
	}

	private final int bufferSize;
	private final ContentEntry contentEntry;
	private final ManifestEntry manifestEntry;
	private final MetaEntry metaEntry;
	private final MimetypeEntry mimetypeEntry;
	private final SettingsEntry settingsEntry;
	private String sFilename;
	private final StylesEntry stylesEntry;

	private final Util util;

	private final XMLUtil xmlUtil;

	/**
	 * Create a new ODS file.
	 *
	 * @param sName
	 *            - The filename for this file, if this file exists it is
	 *            overwritten
	 * @param util
	 * @param xmlUtil
	 */
	public OdsFile(final String sName, final Util util, final XMLUtil xmlUtil,
			final DataStyles format, final int bufferSize) {
		this.util = util;
		this.xmlUtil = xmlUtil;
		this.newFile(sName);
		this.bufferSize = bufferSize;
		this.mimetypeEntry = new MimetypeEntry();
		this.manifestEntry = new ManifestEntry();
		this.settingsEntry = new SettingsEntry();
		this.metaEntry = new MetaEntry();
		this.contentEntry = new ContentEntry(this, xmlUtil, util, format);
		this.stylesEntry = new StylesEntry(this);

		// Add four default stylesEntry to contentEntry
		TableStyle.DEFAULT_TABLE_STYLE.addToFile(this);
		TableRowStyle.DEFAULT_TABLE_ROW_STYLE.addToFile(this);
		TableColumnStyle.getDefaultColumnStyle(xmlUtil).addToFile(this);
		TableCellStyle.getDefaultCellStyle(xmlUtil).addToFile(this);
		PageStyle.DEFAULT_PAGE_STYLE.addToFile(this);
	}

	public void addDataStyle(final DataStyle dataStyle) {
		this.stylesEntry.addDataStyle(dataStyle);
	}

	public void addPageStyle(final PageStyle pageStyle) {
		this.stylesEntry.addPageStyle(pageStyle);
	}

	public void addStyleTag(final StyleTag styleTag) {
		this.contentEntry.addStyleTag(styleTag);
	}

	/**
	 * Add a new table to the file, the new table is set to the active table.
	 * <br>
	 * Use setActiveTable to override the current active table, this has no
	 * influence to<br>
	 * the program, the active table is the first table that is shown in
	 * OpenOffice.
	 *
	 * @param sName
	 *            - The name of the table to add
	 * @return the table
	 * @throws FastOdsException
	 */
	public Table addTable(final String sName)
			throws FastOdsException {
		final Table table = this.contentEntry.addTable(sName);
		this.settingsEntry.setActiveTable(table);
		return table;
	}

	public void addTextStyle(final FHTextStyle fhTextStyle) {
		this.stylesEntry.addTextStyle(fhTextStyle);
	}

	/**
	 * The filename of the spreadsheet file.
	 *
	 * @return The filename of the spreadsheet file
	 */
	public String getName() {
		return this.sFilename;
	}

	public Table getTable(final int n) throws FastOdsException {
		final List<Table> tableQueue = this.contentEntry.getTables();
		if (n < 0 || tableQueue.size() <= n) {
			throw new FastOdsException(new StringBuilder("Wrong table number [")
					.append(n).append("]").toString());
		}

		final Table t = tableQueue.get(n);
		return t;
	}

	/**
	 * @param sName the name of the table
	 * @return the table, or null if not exists
	 */
	public Table getTable(final String sName) {
		final Table table = this.contentEntry.getTable(sName);
		if (table != null)
			this.settingsEntry.setActiveTable(table);
		return table;
	}

	/**
	 * Returns the name of the table.
	 *
	 * @param n
	 *            The number of the table
	 * @return The name of the table
	 */
	public String getTableName(final int n) throws FastOdsException {
		final Table t = this.getTable(n);
		return t.getName();
	}

	/**
	 * Search a table by name and return its number.
	 *
	 * @param sName
	 *            The name of the table
	 * @return The number of the table or -1 if sName was not found
	 */
	public int getTableNumber(final String sName) {
		final ListIterator<Table> iterator = this.contentEntry.getTables()
				.listIterator();
		while (iterator.hasNext()) {
			final int n = iterator.nextIndex();
			final Table tab = iterator.next();
			if (tab.getName().equalsIgnoreCase(sName)) {
				return n;
			}
		}

		return -1;
	}

	// ----------------------------------------------------------------------
	// All methods for setCell with TableCell.Type.STRING
	// ----------------------------------------------------------------------

	/**
	 * Create a new,empty file, use addTable to add tables.
	 *
	 * @param sName
	 *            - The filename of the new spreadsheet file, if this file
	 *            exists it is overwritten
	 * @return False, if filename is a directory
	 */
	public final boolean newFile(final String sName) {
		final File f = new File(sName);
		// Check if sName is a directory and abort if YES
		if (f.isDirectory()) {
			return false;
		}
		this.sFilename = sName;
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
		} catch (final FileNotFoundException e) {
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
		this.settingsEntry.setTables(this.contentEntry.getTables());
		final ZipOutputStream zipOut = new ZipOutputStream(output);
		final Writer writer = this.util.wrapStream(zipOut, this.bufferSize);

		try {
			this.mimetypeEntry.write(this.xmlUtil, zipOut, writer);
			this.manifestEntry.write(this.xmlUtil, zipOut, writer);
			this.metaEntry.write(this.xmlUtil, zipOut, writer);
			this.stylesEntry.write(this.xmlUtil, zipOut, writer);
			this.contentEntry.write(this.xmlUtil, zipOut, writer);
			this.settingsEntry.write(this.xmlUtil, zipOut, writer);
			this.createEmptyEntries(zipOut);

			zipOut.close();
		} catch (final IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

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
		if (nTab < 0 || nTab >= this.contentEntry.getTableCount())
			return false;

		final Table tab = this.contentEntry.getTable(nTab);
		this.settingsEntry.setActiveTable(tab);
		return true;
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
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCellInAllTables(final int nRow, final int nCol,
			final Calendar cal, final TableCellStyle ts)
			throws FastOdsException {

		for (final Table table : this.contentEntry.getTables()) {
			final TableRow row = table.getRow(nRow);
			final TableCell cell = row.getCell(nCol);
			cell.setDateValue(cal);
			cell.setStyle(ts);
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
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCellInAllTables(final String sPos, final Calendar cal,
			final TableCellStyle ts) throws FastOdsException {
		final Position position = this.util.getPosition(sPos);
		final int nRow = position.getRow();
		final int nCol = position.getColumn();
		this.setCellInAllTables(nRow, nCol, cal, ts);
	}

	/**
	 * Sets the cell value in all tables to the given values.
	 *
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param nValuetype
	 *            The value type of sValue,
	 *            TableCell.Type.STRING,TableCell.Type.FLOAT or
	 *            TableCell.Type.PERCENTAGE.
	 * @param sValue
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCellInAllTables(final String sPos,
			final TableCell.Type nValuetype, final String sValue,
			final TableCellStyle ts) throws FastOdsException {
		final Position position = this.util.getPosition(sPos);
		final int nRow = position.getRow();
		final int nCol = position.getColumn();
		// this.setCellInAllTables(nRow, nCol, nValuetype, sValue,
		// ts);
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
	 * @throws FastOdsException
	 */
	public void setCellMergeInAllTables(final int nRow, final int nCol,
			final int nRowMerge, final int nColumnMerge)
			throws FastOdsException {
		for (final Table table : this.contentEntry.getTables()) {
			final TableRow row = table.getRow(nRow);
			final TableCell cell = row.getCell(nCol);
			cell.setRowsSpanned(nRowMerge);
			cell.setColumnsSpanned(nColumnMerge);
		}
	}

	/**
	 * Set the merging of multiple cells to one cell in all existing tables.
	 *
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param nRowMerge
	 * @param nColumnMerge
	 * @throws FastOdsException
	 */
	public void setCellMergeInAllTables(final String sPos, final int nRowMerge,
			final int nColumnMerge) throws FastOdsException {
		final Position position = this.util.getPosition(sPos);
		final int nRow = position.getRow();
		final int nCol = position.getColumn();
		this.setCellMergeInAllTables(nRow, nCol, nRowMerge, nColumnMerge);
	}

	/**
	 * Gets the number of the last table.
	 *
	 * @return The number of the last table
	 */
	public int tableCount() {
		return this.contentEntry.getTableCount();
	}

	private void createEmptyEntries(final ZipOutputStream o)
			throws IOException {
		for (final String entry : new String[] { "Thumbnails/",
				"Configurations2/accelerator/current.xml",
				"Configurations2/floater/", "Configurations2/images/Bitmaps/",
				"Configurations2/menubar/", "Configurations2/popupmenu/",
				"Configurations2/progressbar/", "Configurations2/statusbar/",
				"Configurations2/toolbar/" }) {
			o.putNextEntry(new ZipEntry(entry));
			o.closeEntry();
		}
	}

}
