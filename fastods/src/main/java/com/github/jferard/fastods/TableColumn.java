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

import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;

/**
 * 9.1.6 table:table-column
 * A column.
 *
 * @author J. Férard
 */
public interface TableColumn {
    /**
     * Set a style for this column
     * @param columnStyle the style
     */
    void setColumnStyle(TableColumnStyle columnStyle);

    /**
     * Set a custom attribute for this column
     * @param attribute the attribute
     * @param value the value
     */
    void setColumnAttribute(String attribute, CharSequence value);

    /**
     * Set the cell style for the cell of this column to ts.
     *
     * @param ts The table rowStyle to be used
     */
    void setColumnDefaultCellStyle(TableCellStyle ts);
}