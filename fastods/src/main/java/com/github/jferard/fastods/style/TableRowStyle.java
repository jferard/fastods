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

import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableRowStyle implements FontFaceContainerStyle {
    /**
     * The default style, see LO.
     */
    public static final TableRowStyle DEFAULT_TABLE_ROW_STYLE =
            TableRowStyle.builder("ro1").build();

    /**
     * @param name the name of the TableRowStyle to create
     * @return the builder for the TableRowStyle
     */
    public static TableRowStyleBuilder builder(final String name) {
        return new TableRowStyleBuilder(name);
    }

    private final String name;
    private final boolean hidden;
    private final Length rowHeight;
    private final TableCellStyle defaultCellStyle;
    private final boolean optimalHeight;
    private String key;

    /**
     * Create a new table row style.
     *
     * @param styleName        A unique name for this style
     * @param hidden           true if the row is hidden
     * @param rowHeight        The height of the row
     * @param defaultCellStyle The default cell style
     * @param optimalHeight    True if we use optimal height
     */
    TableRowStyle(final String styleName, final boolean hidden, final Length rowHeight,
                  final TableCellStyle defaultCellStyle, final boolean optimalHeight) {
        this.name = styleName;
        this.hidden = hidden;
        this.rowHeight = rowHeight;
        this.defaultCellStyle = defaultCellStyle;
        this.optimalHeight = optimalHeight;
    }

    @Override
    public void addToElements(final OdsElements odsElements) {
        odsElements.addContentStyle(this);
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<style:style");
        util.appendAttribute(appendable, "style:name", this.name);
        util.appendAttribute(appendable, "style:family", "table-row");
        appendable.append("><style:table-row-properties");
        if (this.optimalHeight) {
            util.appendAttribute(appendable, "style:use-optimal-row-height", this.optimalHeight);
        } else if (this.rowHeight.isNotNull()) {
            util.appendAttribute(appendable, "style:row-height", this.rowHeight);
        }
        util.appendAttribute(appendable, "fo:break-before", "auto");
        appendable.append("/></style:style>");
    }

    @Override
    public ObjectStyleFamily getFamily() {
        return ObjectStyleFamily.TABLE_ROW;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Set the row height to a table row.<br>
     * height is a length value.
     *
     * @return height The table row height to be used
     */
    public Length getRowHeight() {
        return this.rowHeight;
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

    @Override
    public FontFace getFontFace() {
        if (this.defaultCellStyle == null) {
            return null;
        } else {
            return this.defaultCellStyle.getFontFace();
        }
    }


    /**
     * @return the table cell style. May be null.
     */
    public TableCellStyle getDefaultCellStyle() {
        return this.defaultCellStyle;
    }

    /**
     * Add this style to a styles container
     *
     * @param stylesContainer the styles container
     */
    public void addToContentStyles(final StylesContainer stylesContainer) {
        stylesContainer.addContentFontFaceContainerStyle(this);
        stylesContainer.addContentStyle(this);
        final TableCellStyle defaultCellStyle = this.getDefaultCellStyle();
        if (defaultCellStyle != null) {
            stylesContainer.addStylesFontFaceContainerStyle(defaultCellStyle);
            stylesContainer.addContentStyle(defaultCellStyle);
        }
    }
}
