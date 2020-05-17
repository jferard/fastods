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

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableRowStyle;

public interface TableRow {
    /**
     * Add a format to this TableRow
     *
     * @param format the format
     */
    void setRowFormat(DataStyles format);

    /**
     * Set the row style
     *
     * @param rowStyle the style
     */
    void setRowStyle(TableRowStyle rowStyle);

    /**
     * @return the current column count
     */
    int getColumnCount();

    /**
     * Set the cell style for the cell of this row to ts.
     *
     * @param ts The table rowStyle to be used
     */
    void setRowDefaultCellStyle(TableCellStyle ts);

    /**
     * @return the index of this row in the table
     */
    int rowIndex();

    /**
     * Remove the default row style
     */
    void removeRowStyle();

    /**
     * Add an attribute to a row
     * @param attribute the attribute name
     * @param value the value
     */
    void setRowAttribute(String attribute, CharSequence value);
}
