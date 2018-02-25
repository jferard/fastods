/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * WHERE ? content.xml/office:document-content/office:automatic-styles/style:
 * style content.xml/office:document-content/office:body/office:spreadsheet/
 * table:table/table:table-column
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableColumnStyle implements ObjectStyle {
    /**
     * The default style, see LO.
     */
    public static final TableColumnStyle DEFAULT_TABLE_COLUMN_STYLE = TableColumnStyle.builder("co1").setOptimalWidth()
            .buildHidden();

    private static TableColumnStyle defaultColumnStyle;

    /**
     * @param name the name of the style
     * @return a new column style builder
     */
    public static TableColumnStyleBuilder builder(final String name) {
        return new TableColumnStyleBuilder(name);
    }

    private final boolean hidden;
    private final Length columnWidth;
    private final TableCellStyle defaultCellStyle;
    private final String name;
    private final boolean optimalWidth;
    private String key;

    /**
     * Create a new column style
     *
     * @param name             A unique name for this style
     * @param hidden           ture if the style is automatic
     * @param columnWidth      the width of the column
     * @param defaultCellStyle the default style for cells
     * @param optimalWidth
     */
    TableColumnStyle(final String name, final boolean hidden, final Length columnWidth,
                     final TableCellStyle defaultCellStyle, final boolean optimalWidth) {
        this.name = name;
        this.hidden = hidden;
        this.columnWidth = columnWidth;
        this.defaultCellStyle = defaultCellStyle;
        this.optimalWidth = optimalWidth;
    }

    @Override
    public void addToElements(final OdsElements odsElements) {
        odsElements.addContentStyle(this);
        odsElements.addContentStyle(this.defaultCellStyle);
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable) throws IOException {
        appendable.append("<style:style");
        util.appendEAttribute(appendable, "style:name", this.name);
        util.appendAttribute(appendable, "style:family", "table-column");
        appendable.append("><style:table-column-properties");
        util.appendAttribute(appendable, "fo:break-before", "auto");
        if (this.optimalWidth) util.appendAttribute(appendable, "style:use-optimal-column-width", this.optimalWidth);
        if (!this.columnWidth.isNull()) {
            util.appendAttribute(appendable, "style:column-width", this.columnWidth.toString());
        }
        appendable.append("/></style:style>");
    }

    /**
     * Append the XML to the table representation
     *
     * @param util       an util
     * @param appendable the destination
     * @param count      the number of columns concerned
     * @throws IOException if an I/O error occurs
     */
    public void appendXMLToTable(final XMLUtil util, final Appendable appendable, final int count) throws IOException {
        appendable.append("<table:table-column");
        util.appendEAttribute(appendable, "table:style-name", this.name);
        if (count > 1) util.appendAttribute(appendable, "table:number-columns-repeated", count);
        if (this.defaultCellStyle != null)
            util.appendEAttribute(appendable, "table:default-cell-style-name", this.defaultCellStyle.getName());
        appendable.append("/>");
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;

        if (obj instanceof TableColumnStyle) {
            final TableColumnStyle other = (TableColumnStyle) obj;
            return this.name.equals(other.name);
        } else return false;
    }

    /**
     * @return the column width
     */
    @Deprecated
    public Length getColumnWidth() {
        return this.columnWidth;
    }

    /**
     * @return the default style
     */
    public TableCellStyle getDefaultCellStyle() {
        return this.defaultCellStyle;
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
        if (this.key == null) this.key = this.getFamily() + "@" + this.getName();
        return this.key;
    }

    @Override
    public boolean isHidden() {
        return this.hidden;
    }

    public void addToContentStyles(final StylesContainer stylesContainer) {
        stylesContainer.addContentStyle(this);
        if (this.defaultCellStyle != null) {
            stylesContainer.addContentStyle(this.defaultCellStyle);
        }
    }
}
