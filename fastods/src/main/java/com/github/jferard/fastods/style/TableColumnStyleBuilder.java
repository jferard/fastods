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

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.annotation.NoLibreOffice;
import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.util.StyleBuilder;

/**
 * @author Julien Férard
 */
public class TableColumnStyleBuilder
        implements StyleBuilder<TableColumnStyle>, ShowableBuilder<TableColumnStyleBuilder> {
    private static final Length DEFAULT_COLUMN_WIDTH = SimpleLength.cm(2.5);
    private final String name;
    private Length columnWidth;
    private boolean hidden;
    private boolean optimalWidth;

    /**
     * The style will be hidden by default
     *
     * @param name A unique name for this style
     */
    TableColumnStyleBuilder(final String name) {
        this.name = TableStyleBuilder.checker.checkStyleName(name);
        this.columnWidth = DEFAULT_COLUMN_WIDTH;
        this.optimalWidth = false;
        this.hidden = true;
    }

    @Override
    public TableColumnStyle build() {
        return new TableColumnStyle(this.name, this.hidden, this.columnWidth, this.optimalWidth);
    }

    /**
     * Set the column width of a table column.<br>
     * width is a length value.
     *
     * @param width - The width of a column as a length
     * @return true - The width was set, false - this object is no table column,
     * you can not set the default cell to it
     */
    public TableColumnStyleBuilder columnWidth(final Length width) {
        this.columnWidth = width;
        this.optimalWidth = false;
        return this;
    }

    /**
     * Set the width to the optimal value *permanently*: the consumer should adapt the width
     * when the text changes. BUT LIBREOFFICE HAS NO SUCH FEATURE.
     *
     * @return this for fluent style
     */
    @NoLibreOffice(until = "now")
    public TableColumnStyleBuilder optimalWidth() {
        this.optimalWidth = true;
        this.columnWidth = Length.NULL_LENGTH;
        return this;
    }

    @Override
    public TableColumnStyleBuilder visible() {
        this.hidden = false;
        return this;
    }
}
