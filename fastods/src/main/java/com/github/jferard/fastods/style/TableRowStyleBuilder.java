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

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.SimpleLength;
import com.github.jferard.fastods.util.StyleBuilder;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableRowStyleBuilder implements StyleBuilder<TableRowStyle>, HidableBuilder<TableRowStyleBuilder> {
    private static final Length DEFAULT_ROW_HEIGHT = SimpleLength.cm(0.45);
    private final String name;
    private Length rowHeight;
    private boolean hidden;

    /**
     * The style will be visible by default
     *
     * @param name A unique name for this style
     */
    TableRowStyleBuilder(final String name) {
        this.name = TableStyleBuilder.checker.checkStyleName(name);
        this.rowHeight = DEFAULT_ROW_HEIGHT;
        this.hidden = false;
    }

    @Override
    public TableRowStyle build() {
        return new TableRowStyle(this.name, this.hidden, this.rowHeight);
    }

    @Override
    public TableRowStyleBuilder hidden() {
        this.hidden = true;
        return this;
    }

    /**
     * Set the row height to a table row.<br>
     * height is a length value
     *
     * @param height The table row height to be used.
     * @return this for fluent style
     */
    public TableRowStyleBuilder rowHeight(final Length height) {
        this.rowHeight = height;
        return this;
    }
}
