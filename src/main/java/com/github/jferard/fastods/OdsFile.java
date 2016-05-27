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
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.common.base.Optional;

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
	private ContentEntry contentEntry;

	private final ManifestEntry manifestEntry;
	private final MetaEntry metaEntry;
	private final MimetypeEntry mimetypeEntry;
	private final SettingsEntry settingsEntry;
	private String sFilename;
	private StylesEntry stylesEntry;
	private final Util util = Util.getInstance();

	/**
	 * Create a new ODS file.
	 *
	 * @param sName
	 *            - The filename for this file, if this file exists it is
	 *            overwritten
	 */
	public OdsFile(final String sName) {
		this.newFile(sName);
		this.mimetypeEntry = new MimetypeEntry();
		this.manifestEntry = new ManifestEntry();
		this.settingsEntry = new SettingsEntry();
		this.metaEntry = new MetaEntry();

		// Add four default stylesEntry to contentEntry
		TableStyle.DEFAULT_TABLE_STYLE.addToFile(this);
		TableRowStyle.builder("ro1").build().addToFile(this);
		TableColumnStyle.DEFAULT_TABLE_COLUMN_STYLE.addToFile(this);
		TableCellStyle.DEFAULT_CELL_STYLE.addToFile(this);
		PageStyle.builder("Mpm1").build().addToFile(this);
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
	 * @return true - The table was added,<br>
	 *         false - The table already exist, it was added again
	 * @throws FastOdsException
	 */
	public Optional<Table> addTable(final String sName)
			throws FastOdsException {
		final Optional<Table> optTable = this.getContent().addTable(sName);
		if (optTable.isPresent())
			this.settingsEntry.setActiveTable(optTable.get());

		return optTable;
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
	 * @throws FastOdsException
	 */
	public TableCell getCell(final int nTab, final int nRow, final int nCol)
			throws FastOdsException {
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
	 * @throws FastOdsException
	 */
	public TableCell getCell(final int nTab, final String sPos)
			throws FastOdsException {
		final int nRow = this.util.positionToRow(sPos);
		final int nCol = this.util.positionToColumn(sPos);

		return this.getContent().getCell(nTab, nRow, nCol);
	}

	/**
	 * @return The contentEntry object for this OdsFile
	 */
	public ContentEntry getContent() {
		if (this.contentEntry == null) {
			this.contentEntry = new ContentEntry(this);
		}
		return this.contentEntry;
	}

	/*
	public PageStyle getDefaultPageStyle() {
		return this.getContent().getDefaultPageStyle();
	}*/

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

	public Optional<Table> getTable(final String sName) {
		final Optional<Table> optTable = this.getContent().getTable(sName);
		if (optTable.isPresent())
			this.settingsEntry.setActiveTable(optTable.get());

		return optTable;
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
		final ListIterator<Table> iterator = this.getContent().getTables()
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

	/**
	 * Gets the number of the last table.
	 *
	 * @return The number of the last table
	 */
	public int lastTableNumber() {
		return this.getContent().getTables().size();
	}

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
		// this.save();

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
		this.settingsEntry.setTables(this.getContent().getTables());

		try {
			final ZipOutputStream out = new ZipOutputStream(output);
			this.mimetypeEntry.write(this.util, out);
			this.manifestEntry.write(this.util, out);
			this.metaEntry.write(this.util, out);
			this.getStyles().write(this.util, out);
			this.getContent().write(this.util, out);
			this.createConfigurations(out);
			this.settingsEntry.write(this.util, out);
			out.putNextEntry(new ZipEntry("Thumbnails/"));
			out.closeEntry();

			out.close();
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
		if (nTab < 0 || nTab >= this.getContent().getTables().size()) {
			return false;
		}

		final Table tab = this.getContent().getTables().get(nTab);
		this.settingsEntry.setActiveTable(tab);

		return true;
	}

	// ----------------------------------------------------------------------
	// All methods for setCell with TableCell.Type.STRING
	// ----------------------------------------------------------------------

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
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final Calendar cal) throws FastOdsException {
		final TableCell tc = this.getContent().getCell(nTab, nRow, nCol);
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
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final Calendar cal, final TableCellStyle ts)
			throws FastOdsException {
		this.setCell(nTab, nRow, nCol, cal);
		this.getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.Type.FLOAT.
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
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final double dValue) throws FastOdsException {
		this.getContent().setCell(nTab, nRow, nCol, TableCell.Type.FLOAT,
				Double.toString(dValue));
	}

	// -----------------------------------------------------------------------
	// All methods for setCell with TableCell.Type.FLOAT and integer nValue
	// -----------------------------------------------------------------------

	/**
	 * Sets the cell value in table nTab to the given value, the value type is
	 * TableCell.Type.FLOAT.
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
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final double dValue, final TableCellStyle ts)
			throws FastOdsException {
		this.setCell(nTab, nRow, nCol, dValue);
		this.getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.Type.FLOAT.
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
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final int nValue) throws FastOdsException {
		this.getContent().setCell(nTab, nRow, nCol, TableCell.Type.FLOAT,
				Integer.toString(nValue));
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.Type.FLOAT.
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
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final int nValue, final TableCellStyle ts) throws FastOdsException {
		this.getContent().setCell(nTab, nRow, nCol, TableCell.Type.FLOAT,
				Integer.toString(nValue));
		this.getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.Type.STRING.
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
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final String sValue) throws FastOdsException {
		this.getContent().setCell(nTab, nRow, nCol, TableCell.Type.STRING,
				sValue);
	}

	// -----------------------------------------------------------------------
	// All methods for setCell with TableCell.Type.FLOAT and double dValue
	// -----------------------------------------------------------------------

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.Type.STRING.
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
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final String sValue, final TableCellStyle ts)
			throws FastOdsException {
		this.getContent().setCell(nTab, nRow, nCol, TableCell.Type.STRING,
				sValue);
		this.getContent().setCellStyle(nTab, nRow, nCol, ts);
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
	 *            TableCell.Type.STRING,TableCell.Type.FLOAT or
	 *            TableCell.Type.PERCENTAGE
	 * @param sValue
	 *            The value to set the cell to
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final TableCell.Type nValuetype, final String sValue)
			throws FastOdsException {
		this.getContent().setCell(nTab, nRow, nCol, nValuetype, sValue);
	}

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
	 *            TableCell.Type.STRING,TableCell.Type.FLOAT or
	 *            TableCell.Type.PERCENTAGE.
	 * @param sValue
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final int nRow, final int nCol,
			final TableCell.Type nValuetype, final String sValue,
			final TableCellStyle ts) throws FastOdsException {
		this.getContent().setCell(nTab, nRow, nCol, nValuetype, sValue);
		this.getContent().setCellStyle(nTab, nRow, nCol, ts);
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
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final String sPos, final Calendar cal)
			throws FastOdsException {
		this.setCell(nTab, this.util.positionToRow(sPos),
				this.util.positionToColumn(sPos), cal);
	}

	// -----------------------------------------------------------------------
	// All methods for setCell with TableCell.Type.DATE
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
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final String sPos, final Calendar cal,
			final TableCellStyle ts) throws FastOdsException {
		final int nRow = this.util.positionToRow(sPos);
		final int nCol = this.util.positionToColumn(sPos);

		this.setCell(nTab, nRow, nCol, cal);
		this.getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.Type.FLOAT.
	 *
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param dValue
	 *            The value to set the cell to
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final String sPos, final double dValue)
			throws FastOdsException {
		this.getContent().setCell(nTab, this.util.positionToRow(sPos),
				this.util.positionToColumn(sPos), TableCell.Type.FLOAT,
				Double.toString(dValue));
	}

	/**
	 * Sets the cell value in table nTab to the given value, the value type is
	 * TableCell.Type.FLOAT.
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
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final String sPos, final double dValue,
			final TableCellStyle ts) throws FastOdsException {
		final int nRow = this.util.positionToRow(sPos);
		final int nCol = this.util.positionToColumn(sPos);

		this.setCell(nTab, nRow, nCol, dValue);
		this.getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.Type.FLOAT.
	 *
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param nValue
	 *            The value to set the cell to
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final String sPos, final int nValue)
			throws FastOdsException {
		this.getContent().setCell(nTab, this.util.positionToRow(sPos),
				this.util.positionToColumn(sPos), TableCell.Type.FLOAT,
				Integer.toString(nValue));
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.Type.FLOAT.
	 *
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param nValue
	 *            The value to set the cell to
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final String sPos, final int nValue,
			final TableCellStyle ts) throws FastOdsException {
		final int nRow = this.util.positionToRow(sPos);
		final int nCol = this.util.positionToColumn(sPos);
		this.getContent().setCell(nTab, nRow, nCol, TableCell.Type.FLOAT,
				Integer.toString(nValue));
		this.getContent().setCellStyle(nTab, nRow, nCol, ts);
	}

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.Type.STRING.
	 *
	 * @param nTab
	 *            The table number, this table must already exist, 0 is the
	 *            first table
	 * @param sPos
	 *            The cell position e.g. 'A1'
	 * @param sValue
	 *            The value to set the cell to
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final String sPos, final String sValue)
			throws FastOdsException {
		this.getContent().setCell(nTab, this.util.positionToRow(sPos),
				this.util.positionToColumn(sPos), TableCell.Type.STRING,
				sValue);
	}

	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------
	// -----------------------------------------------------------------------

	/**
	 * Sets the cell value in Table nTab to the given value, the value type is
	 * TableCell.Type.STRING.
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
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final String sPos, final String sValue,
			final TableCellStyle ts) throws FastOdsException {
		final int nRow = this.util.positionToRow(sPos);
		final int nCol = this.util.positionToColumn(sPos);

		this.setCell(nTab, nRow, nCol, sValue, ts);

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
	 *            TableCell.Type.STRING,TableCell.Type.FLOAT or
	 *            TableCell.Type.PERCENTAGE
	 * @param sValue
	 *            The value to set the cell to
	 */
	public void setCell(final int nTab, final String sPos,
			final TableCell.Type nValuetype, final String sValue)
			throws FastOdsException {
		this.getContent().setCell(nTab, this.util.positionToRow(sPos),
				this.util.positionToColumn(sPos), nValuetype, sValue);
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
	 *            TableCell.Type.STRING,TableCell.Type.FLOAT or
	 *            TableCell.Type.PERCENTAGE.
	 * @param sValue
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCell(final int nTab, final String sPos,
			final TableCell.Type nValuetype, final String sValue,
			final TableCellStyle ts) throws FastOdsException {
		final int nRow = this.util.positionToRow(sPos);
		final int nCol = this.util.positionToColumn(sPos);

		this.getContent().setCell(nTab, nRow, nCol, nValuetype, sValue);
		this.getContent().setCellStyle(nTab, nRow, nCol, ts);
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
	 *            TableCell.Type.STRING,TableCell.Type.FLOAT or
	 *            TableCell.Type.PERCENTAGE
	 * @param sValue
	 *            The value to set the cell to
	 * @throws FastOdsException
	 *
	 * @deprecated As of release 0.2.1, use {@link #getTableNumber(String)}<br>
	 *             instead setCell("Tab2",1,1,"stringvalue") use
	 *             setCell(getTableNumber("Tab2"),1,1,"stringvalue")
	 *
	 */
	@Deprecated
	public void setCell(final String sTab, final int nRow, final int nCol,
			final TableCell.Type nValuetype, final String sValue)
			throws FastOdsException {

		final ListIterator<Table> iterator = this.getContent().getTables()
				.listIterator();
		while (iterator.hasNext()) {
			final int n = iterator.nextIndex();
			final Table tab = iterator.next();
			if (tab.getName().equals(sTab)) {
				this.getContent().setCell(n, nRow, nCol, nValuetype, sValue);
				return;
			}
		}

		throw FastOdsException.unkownTableName(sTab);
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
	 *            TableCell.Type.STRING,TableCell.Type.FLOAT or
	 *            TableCell.Type.PERCENTAGE.
	 * @param sValue
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 *
	 * @deprecated As of release 0.2.1, use {@link #getTableNumber(String)}<br>
	 *             instead e.g.setCell("Tab2",1,1,"stringvalue") use
	 *             setCell(getTableNumber("Tab2"),1,1,"stringvalue")
	 *
	 */
	@Deprecated
	public void setCell(final String sTab, final int nRow, final int nCol,
			final TableCell.Type nValuetype, final String sValue,
			final TableCellStyle ts) throws FastOdsException {

		final ContentEntry contentEntry = this.getContent();
		final ListIterator<Table> iterator = contentEntry.getTables()
				.listIterator();
		while (iterator.hasNext()) {
			final int n = iterator.nextIndex();
			final Table tab = iterator.next();
			if (tab.getName().equals(sTab)) {
				contentEntry.setCell(n, nRow, nCol, nValuetype, sValue);
				contentEntry.setCellStyle(n, nRow, nCol, ts);
				return;
			}
		}

		throw FastOdsException.unkownTableName(sTab);
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

		final ContentEntry contentEntry = this.getContent();
		final int size = contentEntry.getTables().size();
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
	 *            TableCell.Type.STRING,TableCell.Type.FLOAT or
	 *            TableCell.Type.PERCENTAGE.
	 * @param sValue
	 *            The value to set the cell to
	 * @param ts
	 *            The table style for this cell, must be of type
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCellInAllTables(final int nRow, final int nCol,
			final TableCell.Type nValuetype, final String sValue,
			final TableCellStyle ts) throws FastOdsException {
		for (final Table tab : this.getContent().getTables()) {
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
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 * @throws FastOdsException
	 */
	public void setCellInAllTables(final String sPos, final Calendar cal,
			final TableCellStyle ts) throws FastOdsException {
		final int nRow = this.util.positionToRow(sPos);
		final int nCol = this.util.positionToColumn(sPos);

		final ContentEntry contentEntry = this.getContent();
		final int size = contentEntry.getTables().size();
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
		final int nRow = this.util.positionToRow(sPos);
		final int nCol = this.util.positionToColumn(sPos);

		for (final Table tab : this.getContent().getTables()) {
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
	 * @throws FastOdsException
	 */
	public void setCellMerge(final int nTab, final int nRow, final int nCol,
			final int nRowMerge, final int nColumnMerge)
			throws FastOdsException {
		final TableCell tc = this.getContent().getCell(nTab, nRow, nCol);
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
	 * @throws FastOdsException
	 */
	public void setCellMerge(final int nTab, final String sPos,
			final int nRowMerge, final int nColumnMerge)
			throws FastOdsException {
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
	 * @throws FastOdsException
	 */
	public void setCellMergeInAllTables(final int nRow, final int nCol,
			final int nRowMerge, final int nColumnMerge)
			throws FastOdsException {
		TableCell tc;
		final ContentEntry contentEntry = this.getContent();
		final int size = contentEntry.getTables().size();
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
	 * @throws FastOdsException
	 */
	public void setCellMergeInAllTables(final String sPos, final int nRowMerge,
			final int nColumnMerge) throws FastOdsException {

		final int size = this.getContent().getTables().size();
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
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 */
	public void setCellStyle(final int nTab, final int nRow, final int nCol,
			final TableCellStyle ts) throws FastOdsException {
		this.getContent().setCellStyle(nTab, nRow, nCol, ts);
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
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 */
	public void setCellStyle(final int nTab, final String sPos,
			final TableCellStyle ts) throws FastOdsException {
		final int nRow = this.util.positionToRow(sPos);
		final int nCol = this.util.positionToColumn(sPos);

		this.getContent().setCellStyle(nTab, nRow, nCol, ts);
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
	 *            TableCellStyle.STYLEFAMILY_TABLECELL
	 *
	 * @deprecated As of release 0.2.1, use {@link #getTableNumber(String)}<br>
	 *             instead setCellStyle("Tab2",1,1,"stringvalue") use
	 *             setCellStyle(getTableNumber("Tab2"),1,1,"stringvalue")
	 *
	 */
	@Deprecated
	public void setCellStyle(final String sTab, final int nRow, final int nCol,
			final TableCellStyle ts) throws FastOdsException {
		final ListIterator<Table> iterator = this.getContent().getTables()
				.listIterator();
		while (iterator.hasNext()) {
			final int n = iterator.nextIndex();
			final Table tab = iterator.next();
			if (tab.getName().equals(sTab)) {
				this.getContent().setCellStyle(n, nRow, nCol, ts);
				return;
			}
		}

		throw FastOdsException.unkownTableName(sTab);
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
	 *            TableCellStyle.STYLEFAMILY_TABLECOLUMN
	 */
	public void setColumnStyle(final int nTab, final int nCol,
			final TableColumnStyle ts) throws FastOdsException {
		this.getContent().setColumnStyle(nTab, nCol, ts);
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
	 * Set the stylesEntry for this ODsFile to stylesEntry.
	 *
	 * @param s
	 *            - The new stylesEntry object to be used.
	 */
	public final void setStyles(final StylesEntry s) {
		this.stylesEntry = s;
	}

	private void createConfigurations(final ZipOutputStream o)
			throws IOException {
		for (final String entry : new String[] {
				"Configurations2/accelerator/current.xml",
				"Configurations2/floater/", "Configurations2/images/Bitmaps/",
				"Configurations2/menubar/", "Configurations2/popupmenu/",
				"Configurations2/progressbar/", "Configurations2/statusbar/",
				"Configurations2/toolbar/" }) {
			o.putNextEntry(new ZipEntry(entry));
			o.closeEntry();
		}
	}

	private Table getTable(final int n) throws FastOdsException {
		final List<Table> tableQueue = this.getContent().getTables();
		if (n < 0 || tableQueue.size() <= n) {
			throw new FastOdsException(new StringBuilder("Wrong table number [")
					.append(n).append("]").toString());
		}

		final Table t = tableQueue.get(n);
		return t;
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
	 *            TableCellStyle.STYLEFAMILY_TABLECOLUMN
	 *
	 * @deprecated As of release 0.2.1, use {@link #getTableNumber(final String
	 *             sName)} instead setColumnStyle("Tab2",1,1,"stringvalue") use
	 *             setColumnCell(getTableNumber("Tab2"),1,1,"stringvalue")
	 *
	 */
	@Deprecated
	private void setColumnStyle(final String sTab, final int nCol,
			final TableColumnStyle ts) throws FastOdsException {

		final ContentEntry contentEntry = this.getContent();
		final Optional<Table> optTab = contentEntry.getTable(sTab);
		if (optTab.isPresent())
			contentEntry.setColumnStyle(optTab.get(), nCol, ts);
		else
			throw FastOdsException.unkownTableName(sTab);
	}

}
