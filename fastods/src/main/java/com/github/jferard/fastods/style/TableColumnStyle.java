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

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableColumnStyle implements ObjectStyle {
    /**
     * The default style, see LO.
     */
    public static final TableColumnStyle DEFAULT_TABLE_COLUMN_STYLE =
            TableColumnStyle.builder("co1").build();

    /**
     * @param name the name of the style
     * @return a new column style builder
     */
    public static TableColumnStyleBuilder builder(final String name) {
        return new TableColumnStyleBuilder(name);
    }

    private final boolean hidden;
    private final Length columnWidth;
    private final String name;
    private final boolean optimalWidth;
    private String key;

    /**
     * Create a new column style
     *
     * @param name             A unique name for this style
     * @param hidden           true if the style is automatic
     * @param columnWidth      the width of the column
     * @param optimalWidth     true if the optimal width is set
     */
    TableColumnStyle(final String name, final boolean hidden, final Length columnWidth,
                     final boolean optimalWidth) {
        this.name = name;
        this.hidden = hidden;
        this.columnWidth = columnWidth;
        this.optimalWidth = optimalWidth;
    }

    @Override
    public void addToElements(final OdsElements odsElements) {
        odsElements.addContentStyle(this);
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<style:style");
        util.appendEAttribute(appendable, "style:name", this.name);
        util.appendAttribute(appendable, "style:family", "table-column");
        appendable.append("><style:table-column-properties");
        util.appendAttribute(appendable, "fo:break-before", "auto");
        if (this.optimalWidth) {
            util.appendAttribute(appendable, "style:use-optimal-column-width", this.optimalWidth);
        } else if (this.columnWidth.isNotNull()) {
            util.appendAttribute(appendable, "style:column-width", this.columnWidth);
        }
        appendable.append("/></style:style>");
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof TableColumnStyle) {
            final TableColumnStyle other = (TableColumnStyle) obj;
            return this.name.equals(other.name);
        } else {
            return false;
        }
    }

    /**
     * @return the column width
     * @deprecated should not be used.
     */
    @Deprecated
    public Length getColumnWidth() {
        return this.columnWidth;
    }

    @Override
    public ObjectStyleFamily getFamily() {
        return ObjectStyleFamily.TABLE_COLUMN;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.name == null ? 0 : this.name.hashCode());
        return result;
    }

    @Override
    public String getKey() {
        if (this.key == null) {
            this.key = this.getFamily() + "@" + this.getName();
        }
        return this.key;
    }

    @Override
    public boolean isHidden() {
        return this.hidden;
    }

    /**
     * Add this style to a styles container
     *
     * @param stylesContainer the styles container
     */
    public void addToContentStyles(final StylesContainer stylesContainer) {
        stylesContainer.addContentStyle(this);
    }
}
