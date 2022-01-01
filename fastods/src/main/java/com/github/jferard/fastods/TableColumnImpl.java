/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 9.1.6 table:table-column
 * A column.
 *
 * @author J. Férard
 */
public class TableColumnImpl implements TableColumn {
    public static final TableColumnImpl DEFAULT_TABLE_COLUMN;

    static {
        DEFAULT_TABLE_COLUMN = new TableColumnImpl();
        DEFAULT_TABLE_COLUMN.setColumnDefaultCellStyle(TableCellStyle.DEFAULT_CELL_STYLE);
    }

    private TableColumnStyle columnStyle;
    private TableCellStyle defaultCellStyle;
    private Map<String, CharSequence> customValueByAttribute;

    @Override
    public void setColumnStyle(final TableColumnStyle columnStyle) {
        this.columnStyle = columnStyle;
    }

    @Override
    public void setColumnAttribute(final String attribute, final CharSequence value) {
        if (this.customValueByAttribute == null) {
            this.customValueByAttribute = new HashMap<String, CharSequence>();
        }
        this.customValueByAttribute.put(attribute, value);
    }

    public TableCellStyle getColumnDefaultCellStyle() {
        return this.defaultCellStyle;
    }

    @Override
    public void setColumnDefaultCellStyle(final TableCellStyle defaultCellStyle) {
        this.defaultCellStyle = defaultCellStyle;
    }

    /**
     * Append the XML to the table representation
     *
     * @param util       an util
     * @param appendable the destination
     * @param count      the number of columns concerned
     * @throws IOException if an I/O error occurs
     */
    public void appendXMLToTable(final XMLUtil util, final Appendable appendable, final int count)
            throws IOException {
        // append column style
        appendable.append("<table:table-column");
        util.appendEAttribute(appendable, "table:style-name", this.getColumnStyleName());
        if (count > 1) {
            util.appendAttribute(appendable, "table:number-columns-repeated", count);
        }
        util.appendEAttribute(appendable, "table:default-cell-style-name",
                this.getDefaultCellStyleName());
        if (this.customValueByAttribute != null) {
            for (final Map.Entry<String, CharSequence> entry : this.customValueByAttribute
                    .entrySet()) {
                util.appendAttribute(appendable, entry.getKey(), entry.getValue());
            }
        }
        appendable.append("/>");
    }

    private String getColumnStyleName() {
        if (this.columnStyle != null) {
            return this.columnStyle.getName();
        } else {
            return TableColumnStyle.DEFAULT_TABLE_COLUMN_STYLE.getName();
        }
    }

    private String getDefaultCellStyleName() {
        if (this.defaultCellStyle == null) {
            return TableCellStyle.DEFAULT_CELL_STYLE.getName();
        } else {
            return this.defaultCellStyle.getName();
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TableColumnImpl)) {
            return false;
        }
        final TableColumnImpl other = (TableColumnImpl) o;
        return EqualityUtil.equal(this.columnStyle, other.columnStyle) &&
                EqualityUtil.equal(this.customValueByAttribute, other.customValueByAttribute) &&
                EqualityUtil.equal(this.defaultCellStyle, other.defaultCellStyle);
    }

    @Override
    public int hashCode() {
        return EqualityUtil
                .hashObjects(this.columnStyle, this.customValueByAttribute, this.defaultCellStyle);
    }
}
