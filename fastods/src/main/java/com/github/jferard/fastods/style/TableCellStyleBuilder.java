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
import com.github.jferard.fastods.attribute.BorderAttribute;
import com.github.jferard.fastods.attribute.BorderStyle;
import com.github.jferard.fastods.attribute.CellAlign;
import com.github.jferard.fastods.attribute.Color;
import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.attribute.VerticalAlign;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.util.StyleBuilder;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableCellStyleBuilder
        implements StyleBuilder<TableCellStyle>, HidableBuilder<TableCellStyleBuilder> {
    private final BordersBuilder bordersBuilder;
    private final MarginsBuilder marginsBuilder;
    private final String name;
    private final TextPropertiesBuilder tpBuilder;
    private Color backgroundColor;
    private DataStyle dataStyle;
    // true
    private CellAlign textAlign; // 'center','end','start','justify'
    private VerticalAlign verticalAlign; // 'middle', 'bottom',
    private Angle textRotating;
    // 'top'
    private boolean wrap; // No line wrap when false, line wrap when
    private boolean hidden;
    private TableCellStyle parentCellStyle;

    /**
     * The style will be visible by default.
     *
     * @param name A unique name for this style
     */
    TableCellStyleBuilder(final String name) {
        this.name = TableStyleBuilder.checker.checkStyleName(name);
        this.parentCellStyle = TableCellStyle.DEFAULT_CELL_STYLE;
        this.tpBuilder = TextProperties.builder();
        this.bordersBuilder = new BordersBuilder();
        this.marginsBuilder = new MarginsBuilder();
        this.backgroundColor = SimpleColor.NONE;
        this.hidden = false;
    }

    /**
     * Reserved to TableCellStyle.toBuilder()
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
    public TableCellStyleBuilder(final String name, final boolean hidden, final Borders borders,
                                 final Margins margins, final DataStyle dataStyle,
                                 final Color backgroundColor, final TextProperties textProperties,
                                 final CellAlign textAlign, final VerticalAlign verticalAlign,
                                 final boolean wrap, final TableCellStyle parentCellStyle,
                                 final Angle textRotating) {
        this.name = name;
        this.hidden = hidden;
        this.dataStyle = dataStyle;
        this.backgroundColor = backgroundColor;
        this.textAlign = textAlign;
        this.verticalAlign = verticalAlign;
        this.wrap = wrap;
        this.parentCellStyle = parentCellStyle;
        this.textRotating = textRotating;
        this.tpBuilder = textProperties.toBuilder();
        this.bordersBuilder = borders.toBuilder();
        this.marginsBuilder = margins.toBuilder();
    }

    /**
     * Add a margin to this cell.
     *
     * @param size The size of the margin
     * @return this for fluent style
     */
    public TableCellStyleBuilder allMargins(final Length size) {
        this.marginsBuilder.all(size);
        return this;
    }

    /**
     * Set the cell background color to color
     *
     * @param color - The color to be used in format #rrggbb e.g. #ff0000 for a
     *              red cell background
     * @return this for fluent style
     */
    public TableCellStyleBuilder backgroundColor(final Color color) {
        this.backgroundColor = color;
        return this;
    }

    /**
     * Add a border style for all the borders of this cell.
     *
     * @param size        the size of the line
     * @param borderColor the color to be used in format #rrggbb e.g. '#ff0000' for a red border
     * @param style       the style of the border line, either BorderAttribute.BORDER_SOLID or
     *                    BorderAttribute.BORDER_DOUBLE
     * @return this for fluent style
     */
    public TableCellStyleBuilder borderAll(final Length size, final Color borderColor,
                                           final BorderStyle style) {
        final BorderAttribute borderAttribute = new BorderAttribute(size, borderColor, style);
        return this.borderAll(borderAttribute);
    }

    /**
     * Add a border style for all the borders of this cell.
     *
     * @param borderAttribute the attribute for the border
     * @return this for fluent style
     */
    public TableCellStyleBuilder borderAll(final BorderAttribute borderAttribute) {
        this.bordersBuilder.all(borderAttribute);
        return this;
    }

    /**
     * Add a border style for the bottom border of this cell.
     *
     * @param size        the size of the line
     * @param borderColor the color to be used in format #rrggbb e.g. '#ff0000' for a red border
     * @param style       the style of the border line, either BorderAttribute.BORDER_SOLID or
     *                    BorderAttribute.BORDER_DOUBLE
     * @return this for fluent style
     */
    public TableCellStyleBuilder borderBottom(final Length size, final Color borderColor,
                                              final BorderStyle style) {
        final BorderAttribute borderAttribute = new BorderAttribute(size, borderColor, style);
        return this.borderBottom(borderAttribute);
    }

    /**
     * Add a border style for the bottom border of this cell.
     *
     * @param borderAttribute the attribute for the border
     * @return this for fluent style
     */
    public TableCellStyleBuilder borderBottom(final BorderAttribute borderAttribute) {
        this.bordersBuilder.bottom(borderAttribute);
        return this;
    }

    /**
     * Add a border style for the left border of this cell.
     *
     * @param size        the size of the line
     * @param borderColor the color to be used in format #rrggbb e.g. '#ff0000' for a red border
     * @param style       the style of the border line, either BorderAttribute.BORDER_SOLID or
     *                    BorderAttribute.BORDER_DOUBLE
     * @return this for fluent style
     */
    public TableCellStyleBuilder borderLeft(final Length size, final Color borderColor,
                                            final BorderStyle style) {
        final BorderAttribute borderAttribute = new BorderAttribute(size, borderColor, style);
        return this.borderLeft(borderAttribute);
    }

    /**
     * Add a border style for the left border of this cell.
     *
     * @param borderAttribute the attribute for the border
     * @return this for fluent style
     */
    public TableCellStyleBuilder borderLeft(final BorderAttribute borderAttribute) {
        this.bordersBuilder.left(borderAttribute);
        return this;
    }

    /**
     * Add a border style for the right border of this cell.
     *
     * @param size        the size of the line
     * @param borderColor the color to be used in format #rrggbb e.g. '#ff0000' for a red border
     * @param style       the style of the border line, either BorderAttribute.BORDER_SOLID or
     *                    BorderAttribute.BORDER_DOUBLE
     * @return this for fluent style
     */
    public TableCellStyleBuilder borderRight(final Length size, final Color borderColor,
                                             final BorderStyle style) {
        final BorderAttribute borderAttribute = new BorderAttribute(size, borderColor, style);
        return this.borderRight(borderAttribute);
    }

    /**
     * Add a border style for the left border of this cell.
     *
     * @param borderAttribute the attribute for the border
     * @return this for fluent style
     */
    public TableCellStyleBuilder borderRight(final BorderAttribute borderAttribute) {
        this.bordersBuilder.right(borderAttribute);
        return this;
    }

    /**
     * Add a border style for the top border of this cell.
     *
     * @param size        the size of the line
     * @param borderColor the color to be used in format #rrggbb e.g. '#ff0000' for a red border
     * @param style       the style of the border line, either BorderAttribute.BORDER_SOLID or
     *                    BorderAttribute.BORDER_DOUBLE
     * @return this for fluent style
     */
    public TableCellStyleBuilder borderTop(final Length size, final Color borderColor,
                                           final BorderStyle style) {
        final BorderAttribute borderAttribute = new BorderAttribute(size, borderColor, style);
        return this.borderTop(borderAttribute);
    }

    /**
     * Add a border style for the top border of this cell.
     *
     * @param borderAttribute the attribute for the border
     * @return this for fluent style
     */
    public TableCellStyleBuilder borderTop(final BorderAttribute borderAttribute) {
        this.bordersBuilder.top(borderAttribute);
        return this;
    }


    @Override
    public TableCellStyle build() {
        return new TableCellStyle(this.name, this.hidden, this.dataStyle, this.backgroundColor,
                this.tpBuilder.build(), this.textAlign, this.verticalAlign, this.wrap,
                this.parentCellStyle, this.bordersBuilder.build(), this.marginsBuilder.build(),
                this.textRotating);
    }

    /**
     * Set text rotation angle
     *
     * @param angle TextRotation to be used
     * @return this for fluent style
     */
    public TableCellStyleBuilder textRotating(final Angle angle) {
        this.textRotating = angle;
        return this;
    }

    /**
     * Set the data style for this TableFamilyStyle to ds.<br>
     * If the StyleType of this TableFamilyStyle is not STYLE_TABLECELL, an
     * exception is thrown
     *
     * @param ds The date style to be used
     * @return this for fluent style
     */
    public TableCellStyleBuilder dataStyle(final DataStyle ds) {
        this.dataStyle = ds;
        return this;
    }

    /**
     * Set the font color to color.<br>
     * The TableFamilyStyle must be of a format of
     * TableFamilyStyle.STYLE_TABLECELL
     *
     * @param color The color to be used in format #rrggbb e.g. #ff0000 for a red
     *              cell background
     * @return this for fluent style
     */
    public TableCellStyleBuilder fontColor(final Color color) {
        this.tpBuilder.fontColor(color);
        return this;
    }

    /**
     * Set the font name to be used for this style.
     *
     * @param name The font name for this TextStyle
     * @return this for fluent style
     */
    public TableCellStyleBuilder fontName(final String name) {
        this.tpBuilder.fontName(name);
        return this;
    }

    /**
     * Set the font size to the given value.
     *
     * @param fontSize - The font size
     * @return this for fluent style
     */
    public TableCellStyleBuilder fontSize(final Length fontSize) {
        this.tpBuilder.fontSize(fontSize);
        return this;
    }

    /**
     * Set the font size to the given fontSizePercentage.
     * See 20.183 fo:font-size
     * "fontSizePercentage values can be used within common styles only and are based on
     * the font height of the parent style rather than to the font height
     * of the attributes neighborhood"
     *
     * @param percentage the font size as a fontSizePercentage.
     * @return this for fluent style
     */
    public TableCellStyleBuilder fontSizePercentage(final double percentage) {
        this.tpBuilder.fontSizePercentage(percentage);
        return this;
    }

    /**
     * Set the font weight to italic.
     *
     * @return this for fluent style
     */
    public TableCellStyleBuilder fontStyleItalic() {
        this.tpBuilder.fontStyleItalic();
        return this;
    }

    /**
     * Set the font weight to normal.
     *
     * @return this for fluent style
     */
    public TableCellStyleBuilder fontStyleNormal() {
        this.tpBuilder.fontStyleNormal();
        return this;
    }

    /**
     * Set the font underline color to color. Use an empty string to reset it to
     * 'auto'.
     *
     * @param color The color to be used in format #rrggbb e.g. #ff0000 for a red
     *              cell background.
     * @return this for fluent style
     */
    public TableCellStyleBuilder fontUnderlineColor(final Color color) {
        this.tpBuilder.fontUnderlineColor(color);
        return this;
    }

    /**
     * Set the style that should be used for the underline.
     *
     * @param style One of the TextStyle.STYLE_UNDERLINE
     * @return this for fluent style
     */
    public TableCellStyleBuilder fontUnderlineStyle(final TextProperties.Underline style) {
        this.tpBuilder.fontUnderlineStyle(style);
        return this;
    }

    /**
     * Set the font weight to bold.
     *
     * @return true - the value was set,<br>
     * false - This object is no table cell, you can not set it to bold
     */
    public TableCellStyleBuilder fontWeightBold() {
        this.tpBuilder.fontWeightBold();
        return this;
    }

    /**
     * Set the font weight to normal.
     *
     * @return this for fluent style
     */
    public TableCellStyleBuilder fontWeightNormal() {
        this.tpBuilder.fontWeightNormal();
        return this;
    }

    // /**
    // * Set the data style for this TableFamilyStyle to ns.
    // *
    // * @param ns
    // * The number style to be used
    // * @return this for fluent style
    // */
    // public TableCellStyleBuilder dataStyle(final NumberStyle ns) {
    // this.dataStyle = ns;
    // return this;
    // }

    /**
     * /** Set font wrap.
     *
     * @param fSetWrap <br>
     *                 true - Font will be wrapped,<br>
     *                 false - no font wrapping
     * @return this for fluent style
     */
    public TableCellStyleBuilder fontWrap(final boolean fSetWrap) {
        this.wrap = fSetWrap;
        return this;
    }

    /**
     * Add a border style to this cell.
     *
     * @param size The size of the margin as length
     * @return this for fluent style
     */
    public TableCellStyleBuilder marginBottom(final Length size) {
        this.marginsBuilder.bottom(size);
        return this;
    }

    /**
     * Add a border style to this cell.
     *
     * @param size The size of the margin as length
     * @return this for fluent style
     */
    public TableCellStyleBuilder marginLeft(final Length size) {
        this.marginsBuilder.left(size);
        return this;
    }

    /**
     * Add a border style to this cell.
     *
     * @param size The size of the margin as length
     * @return this for fluent style
     */
    public TableCellStyleBuilder marginRight(final Length size) {
        this.marginsBuilder.right(size);
        return this;
    }

    /**
     * Add a border style to this cell.
     *
     * @param size The size of the margin as length
     * @return this for fluent style
     */
    public TableCellStyleBuilder marginTop(final Length size) {
        this.marginsBuilder.top(size);
        return this;
    }

    /**
     * Sets the parent cell style
     *
     * @param tableCellStyle the parent cell style (not null)
     * @return this for fluent style.
     */
    public TableCellStyleBuilder parentCellStyle(final TableCellStyle tableCellStyle) {
        this.parentCellStyle = tableCellStyle;
        return this;
    }

    /**
     * Set the alignment of text.
     *
     * @param align - The text alignment flag,
     * @return this for fluent style
     */
    public TableCellStyleBuilder textAlign(final CellAlign align) {
        this.textAlign = align;
        return this;
    }

    /**
     * Set the vertical alignment of text.
     *
     * @param align - The vertical alignment flag
     * @return this for fluent style
     */
    public TableCellStyleBuilder verticalAlign(final VerticalAlign align) {
        this.verticalAlign = align;
        return this;
    }

    @Override
    public TableCellStyleBuilder hidden() {
        this.hidden = true;
        return this;
    }
}
