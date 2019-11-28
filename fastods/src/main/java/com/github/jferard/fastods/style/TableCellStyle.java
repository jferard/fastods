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

import com.github.jferard.fastods.attribute.Angle;
import com.github.jferard.fastods.attribute.CellAlign;
import com.github.jferard.fastods.attribute.Color;
import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.attribute.VerticalAlign;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * WHERE ? content.xml/office:document-content/office:automatic-styles/style:
 * style
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableCellStyle implements FontFaceContainerStyle {
    /**
     * This is the default cell style: left and top align, no wrap.
     */
    public static final TableCellStyle DEFAULT_CELL_STYLE =
            TableCellStyle.builder("Default").verticalAlign(VerticalAlign.TOP).fontWrap(false)
                    .backgroundColor(SimpleColor.NONE).allMargins(Length.NULL_LENGTH)
                    .fontName(LOFonts.LIBERATION_SANS).parentCellStyle(null).build();

    /**
     * Create a builder
     *
     * @param name the name of the builder
     * @return the builder
     */
    public static TableCellStyleBuilder builder(final String name) {
        return new TableCellStyleBuilder(name);
    }

    private final Color backgroundColor;
    private final boolean hidden;
    private final Borders borders;
    private final Margins margins;
    private final String name;
    // true
    private final TableCellStyle parentCellStyle;
    private final CellAlign textAlign; // 'center','end','start','justify'
    private final TextProperties textProperties;
    private final VerticalAlign verticalAlign; // 'middle', 'bottom', 'top'
    private final Angle textRotating;
    private final boolean wrap; // No line wrap when false, line wrap when
    private final DataStyle dataStyle;
    private String key;

    /**
     * Create a new cell style
     *
     * @param name            A unique name for this style
     * @param hidden          true if the style is automatic
     * @param dataStyle       the style of the data
     * @param backgroundColor the background color
     * @param textProperties  the text properties
     * @param textAlign       horizontal align
     * @param verticalAlign   vertical align
     * @param wrap            true if the text is wrapped
     * @param parentCellStyle the parent style
     * @param borders         the borders of the cell
     * @param margins         the margins of the cell
     * @param textRotating    an angle for the rotation
     */
    TableCellStyle(final String name, final boolean hidden, final DataStyle dataStyle,
                   final Color backgroundColor, final TextProperties textProperties,
                   final CellAlign textAlign, final VerticalAlign verticalAlign, final boolean wrap,
                   final TableCellStyle parentCellStyle, final Borders borders,
                   final Margins margins, final Angle textRotating) {
        this.hidden = hidden;
        this.borders = borders;
        this.margins = margins;
        this.name = name;
        this.dataStyle = dataStyle;
        this.backgroundColor = backgroundColor;
        this.textProperties = textProperties;
        this.textAlign = textAlign;
        this.verticalAlign = verticalAlign;
        this.wrap = wrap;
        this.parentCellStyle = parentCellStyle;
        this.textRotating = textRotating;
    }

    @Override
    public void addToElements(final OdsElements odsElements) {
        if (this.dataStyle != null) {
            this.dataStyle.addToElements(odsElements);
        }
        odsElements.addContentStyle(this);
    }

    private void appendCellProperties(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<style:table-cell-properties");
        if (this.backgroundColor != SimpleColor.NONE) {
            util.appendAttribute(appendable, "fo:background-color", this.backgroundColor);
        }

        if (this.verticalAlign != null) {
            util.appendAttribute(appendable, "style:vertical-align", this.verticalAlign);
        }
        if (this.textRotating != null) {
            util.appendAttribute(appendable, "style:rotation-angle", this.textRotating);
        }
        this.borders.appendXMLContent(util, appendable);

        if (this.wrap) {
            util.appendAttribute(appendable, "fo:wrap-option", "wrap");
        }

        appendable.append("/>");
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<style:style");
        util.appendEAttribute(appendable, "style:name", this.name);
        util.appendAttribute(appendable, "style:family", "table-cell");
        if (this.parentCellStyle != null) {
            util.appendEAttribute(appendable, "style:parent-style-name",
                    this.parentCellStyle.getRealName());
        }
        if (this.dataStyle != null) {
            util.appendEAttribute(appendable, "style:data-style-name", this.dataStyle.getName());
        }

        if (this.hasCellProperties() || this.hasTextProperties() || this.hasParagraphProperties()) {
            appendable.append(">");
            if (this.hasCellProperties()) {
                this.appendCellProperties(util, appendable);
            }

            if (this.hasTextProperties()) {
                this.textProperties.appendXMLContent(util, appendable);
            }

            if (this.hasParagraphProperties()) {
                appendable.append("<style:paragraph-properties");
                if (this.textAlign != null) {
                    util.appendAttribute(appendable, "fo:text-align", this.textAlign);
                }

                this.margins.appendXMLContent(util, appendable);
                appendable.append("/>");
            }
            appendable.append("</style:style>");
        } else {
            appendable.append("/>");
        }
    }

    private boolean hasTextProperties() {
        return this.textProperties != null && this.textProperties.isNotEmpty();
    }

    private boolean hasParagraphProperties() {
        return this.textAlign != null || !this.margins.areVoid();
    }

    /**
     * @return the data style inside this cell style
     */
    public DataStyle getDataStyle() {
        return this.dataStyle;
    }

    @Override
    public ObjectStyleFamily getFamily() {
        return ObjectStyleFamily.TABLE_CELL;
    }

    @Override
    public String getKey() {
        if (this.key == null) {
            this.key = this.getFamily() + "@" + this.getName();
        }
        return this.key;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @return the name without a suffix for data style
     */
    public String getRealName() {
        final int index = this.name.indexOf("-_-");
        if (index > 0) {
            return this.name.substring(0, index);
        } else {
            return this.name;
        }
    }

    private boolean hasCellProperties() {
        return this.backgroundColor != SimpleColor.NONE || this.verticalAlign != null ||
                !this.borders.areVoid() || this.wrap || this.textRotating != null;
    }

    /**
     * @return true if this is a derived cell style
     */
    public boolean hasParent() {
        return this.parentCellStyle != null;
    }

    @Override
    public boolean isHidden() {
        return this.hidden;
    }

    /**
     * @return the parent cell style, never null
     */
    public TableCellStyle getParentCellStyle() {
        return this.parentCellStyle;
    }

    @Override
    public FontFace getFontFace() {
        return this.textProperties.getFontFace();
    }

    /**
     * @return true if there is an text align
     */
    public boolean hasTextAlign() {
        return this.textAlign != null;
    }

    /**
     * @return a builder with this table cell style parameters
     * @param newName the new name of the style
     */
    public TableCellStyleBuilder toBuilder(final String newName) {
        return new TableCellStyleBuilder(newName, this.hidden, this.borders, this.margins,
                this.dataStyle, this.backgroundColor, this.textProperties, this.textAlign,
                this.verticalAlign, this.wrap, this.parentCellStyle, this.textRotating);
    }
}
