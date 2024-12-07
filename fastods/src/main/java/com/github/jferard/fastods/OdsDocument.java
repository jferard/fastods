/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods;

import com.github.jferard.fastods.odselement.ScriptEventListener;
import com.github.jferard.fastods.odselement.StylesModeSetter;
import com.github.jferard.fastods.util.AutoFilter;
import com.github.jferard.fastods.util.PilotTable;

import java.io.IOException;
import java.util.List;

/**
 * An ods document. It is part of a writer that is responsible for the recording.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public interface OdsDocument extends StylesModeSetter {
    /**
     * Add a new table to the document, the new table is set to the active table.<br>
     * Use setActiveTable to override the current active table, this has no
     * influence to the program, the active table is the first table that is shown in
     * OpenOffice.
     *
     * @param name - The name of the table to add
     * @return the table
     * @throws IOException if the table can't be added to document
     */
    Table addTable(String name) throws IOException;

    /**
     * Add a new table to the document, the new table is set to the active table.<br>
     * Use setActiveTable to override the current active table, this has no
     * influence to the program, the active table is the first table that is shown in
     * OpenOffice.
     *
     * @param name           the name of the table to add
     * @param rowCapacity    the initial row capacity
     * @param columnCapacity the initial column capacity
     * @return the table or nll if the name already exists
     * @throws IOException if the table can't be added to document
     */
    Table addTable(String name, int rowCapacity, int columnCapacity) throws IOException;

    /**
     * Add a new table to the document, the new table is set to the active table.<br>
     * Use setActiveTable to override the current active table, this has no
     * influence to the program, the active table is the first table that is shown in
     * OpenOffice.
     *
     * @param table the table to add
     * @return true if the table was added
     * @throws IOException if the table can't be added to document
     */
    boolean addTable(Table table) throws IOException;

    /**
     * *You probably need {@code addTable}*
     * <p>
     * Create a new table, **but do not add it to the document**.
     *
     * @param name - The name of the table to add
     * @return the table
     * @throws IOException if the table can't be added to document
     */
    Table createTable(String name) throws IOException;

    /**
     * *You probably need {@code addTable}*
     * <p>
     * Create a new table, **but do not add it to the document**.
     *
     * @param name           - The name of the table to add
     * @param rowCapacity    the initial row capacity
     * @param columnCapacity the initial column capacity
     * @return the table
     * @throws IOException if the table can't be added to document
     */
    Table createTable(String name, int rowCapacity, int columnCapacity) throws IOException;

    /**
     * Get a table by index
     *
     * @param n the index
     * @return the table
     * @throws FastOdsException if the table index is negative or &ge; number of tables
     */
    Table getTable(int n) throws FastOdsException;

    /**
     * Get a table by name
     *
     * @param name the name of the table
     * @return the table
     * @throws FastOdsException if the table does not exist.
     */
    Table getTable(String name) throws FastOdsException;

    /**
     * @param name the name of the table
     * @return the table
     * @throws IOException if the table does not exist.
     */
    Table getOrAddTable(String name) throws IOException;

    /**
     * Returns the name of the table.
     *
     * @param n The number of the table
     * @return The name of the table
     * @throws FastOdsException if n is negative
     */
    String getTableName(int n) throws FastOdsException;

    /**
     * Search a table by name and return its number.
     *
     * @param name The name of the table
     * @return The number of the table or -1 if name was not found
     */
    int getTableNumber(String name);

    /**
     * @return the list of tables
     */
    List<Table> getTables();

    /**
     * Set the active table, this is the table that is shown if you open the
     * file.
     *
     * @param tableIndex The table number, this table should already exist, otherwise
     *                   the first table is shown
     * @return true - The active table was set, false - tab has an illegal value
     */
    boolean setActiveTable(int tableIndex);

    /**
     * Set a view setting
     *
     * @param viewId the view id
     * @param item   the item name
     * @param value  the value
     */
    void setViewSetting(String viewId, String item, String value);

    /**
     * Gets the number of the last table.
     *
     * @return The number of the last table
     */
    int tableCount();

    /**
     * Add an autoFilter to a range address
     *
     * @param rangeName the name of the range
     * @param table     the table
     * @param r1        the top row
     * @param c1        the left column
     * @param r2        the bottom row
     * @param c2        the right column
     * @deprecated use {@code table.addAutoFilter(r1, c1, r2, c2)}
     */
    @Deprecated
    void addAutoFilter(String rangeName, Table table, int r1, int c1, int r2, int c2);

    /**
     * @param table    the table
     * @param rowCount the number of rows to freeze
     * @param colCount the number of cols to freeze
     */
    void freezeCells(Table table, int rowCount, int colCount);

    /**
     * Add an extra file to the document
     *
     * @param fullPath  the path of the file in the sequence
     * @param mediaType the MIME type of the file
     * @param bytes     the content
     */
    void addExtraFile(final String fullPath, final String mediaType, final byte[] bytes);

    /**
     * Add an extra directory to the manifest.
     *
     * @param fullPath the path of the dir
     */
    void addExtraDir(final String fullPath);

    /**
     * Add an extra object (eg. a spreadsheet) to the manifest. Files have to be added
     * with `AddExtraFile`.
     *
     * @param fullPath  the path of the dir
     * @param mediaType the type of the object
     * @param version   the version
     */
    void addExtraObjectReference(String fullPath, String mediaType, String version);

    /**
     * Add some events to the document
     *
     * @param events the events to add
     */
    void addEvents(final ScriptEventListener... events);

    /**
     * Add a new pilot table
     *
     * @param pilot the pilot table
     */
    void addPilotTable(PilotTable pilot);

    /**
     * Add a new auto filter
     *
     * @param autoFilter the filter
     */
    void addAutoFilter(final AutoFilter autoFilter);

    /**
     * Add a new named range to the document
     *
     * @param name the name of the range
     * @param rangeAddress the range address.
     */
    void addNamedRange(final String name, final String rangeAddress);
}
