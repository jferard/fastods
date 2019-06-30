/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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

import java.io.IOException;
import java.util.List;

/**
 * An ods document. It is part of a writer that is responsible for the recording.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public interface OdsDocument {
    /**
     * Add a new table to the file, the new table is set to the active table.<br>
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
     * Add a new table to the file, the new table is set to the active table.<br>
     * Use setActiveTable to override the current active table, this has no
     * influence to the program, the active table is the first table that is shown in
     * OpenOffice.
     *
     * @param name - The name of the table to add
     * @param rowCapacity the initial row capacity
     * @param columnCapacity the initial column capacity
     * @return the table
     * @throws IOException if the table can't be added to document
     */
    Table addTable(String name, int rowCapacity, int columnCapacity) throws IOException;

    /**
     * Get a table by index
     * @param n the index
     * @return the table
     * @throws FastOdsException if the table index is negative or >= number of tables
     */
    Table getTable(int n) throws FastOdsException;

    /**
     * Get a table by name
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
     * @param viewId the view id
     * @param item the item name
     * @param value the value
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
     * @param table the table
     * @param r1 the top row
     * @param c1 the left column
     * @param r2 the bottom row
     * @param c2 the right column
     * @deprecated use {@code table.addAutoFilter(r1, c1, r2, c2)}
     */
    @Deprecated
    void addAutoFilter(Table table, int r1, int c1, int r2, int c2);

    void freezeCells(Table table, int rowCount, int colCount);
}
