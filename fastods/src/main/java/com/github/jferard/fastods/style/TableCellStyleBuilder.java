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

import com.github.jferard.fastods.Color;
import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.SimpleColor;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.StyleBuilder;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableCellStyleBuilder implements StyleBuilder<TableCellStyle> {
    private final BordersBuilder bordersBuilder;
    private final MarginsBuilder marginsBuilder;
    private final String name;
    private final TextPropertiesBuilder tpBuilder;
    private Color backgroundColor;
    private DataStyle dataStyle;
    // true
    private TableCellStyle.Align textAlign; // 'center','end','start','justify'
    private TableCellStyle.VerticalAlign verticalAlign; // 'middle', 'bottom',
    // 'top'
    private boolean wrap; // No line wrap when false, line wrap when
    private boolean hidden;
    private TableCellStyle parentCellStyle;

    /**
     * @param name A unique name for this style
     */
    TableCellStyleBuilder(final String name) {
        this.name = TableStyleBuilder.checker.checkStyleName(name);
        this.parentCellStyle = TableCellStyle.DEFAULT_CELL_STYLE;
        this.tpBuilder = TextProperties.builder();
        this.bordersBuilder = new BordersBuilder();
        this.marginsBuilder = new MarginsBuilder();
        this.backgroundColor = SimpleColor.NONE;
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
     * Set the cell background color to color.<br>
     * The TableFamilyStyle must be of a format of
     * TableFamilyStyle.STYLE_TABLECELL
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
     * @param style       the style of the border line, either BorderAttribute.BORDER_SOLID or BorderAttribute.BORDER_DOUBLE
     * @return this for fluent style
     */
    public TableCellStyleBuilder borderAll(final Length size, final Color borderColor,
                                           final BorderAttribute.Style style) {
        final BorderAttribute bs = new BorderAttribute(size, borderColor, style);
        this.bordersBuilder.all(bs);
        return this;
    }

    /**
     * Add a border style for the bottom border of this cell.
     *
     * @param size        the size of the line
     * @param borderColor the color to be used in format #rrggbb e.g. '#ff0000' for a red border
     * @param style       the style of the border line, either BorderAttribute.BORDER_SOLID or BorderAttribute.BORDER_DOUBLE
     * @return this for fluent style
     */
    public TableCellStyleBuilder borderBottom(final Length size, final Color borderColor,
                                              final BorderAttribute.Style style) {
        final BorderAttribute bs = new BorderAttribute(size, borderColor, style);
        this.bordersBuilder.bottom(bs);
        return this;
    }

    /**
     * Add a border style for the left border of this cell.
     *
     * @param size        the size of the line
     * @param borderColor the color to be used in format #rrggbb e.g. '#ff0000' for a red border
     * @param style       the style of the border line, either BorderAttribute.BORDER_SOLID or BorderAttribute.BORDER_DOUBLE
     * @return this for fluent style
     */
    public TableCellStyleBuilder borderLeft(final Length size, final Color borderColor,
                                            final BorderAttribute.Style style) {
        final BorderAttribute bs = new BorderAttribute(size, borderColor, style);
        this.bordersBuilder.left(bs);
        return this;
    }

    /**
     * Add a border style for the right border of this cell.
     *
     * @param size        the size of the line
     * @param borderColor the color to be used in format #rrggbb e.g. '#ff0000' for a red border
     * @param style       the style of the border line, either BorderAttribute.BORDER_SOLID or BorderAttribute.BORDER_DOUBLE
     * @return this for fluent style
     */
    public TableCellStyleBuilder borderRight(final Length size, final Color borderColor,
                                             final BorderAttribute.Style style) {
        final BorderAttribute bs = new BorderAttribute(size, borderColor, style);
        this.bordersBuilder.right(bs);
        return this;
    }

    /**
     * Add a border style for the top border of this cell.
     *
     * @param size        the size of the line
     * @param borderColor the color to be used in format #rrggbb e.g. '#ff0000' for a red border
     * @param style       the style of the border line, either BorderAttribute.BORDER_SOLID or BorderAttribute.BORDER_DOUBLE
     * @return this for fluent style
     */
    public TableCellStyleBuilder borderTop(final Length size, final Color borderColor,
                                           final BorderAttribute.Style style) {
        final BorderAttribute bs = new BorderAttribute(size, borderColor, style);
        this.bordersBuilder.top(bs);
        return this;
    }

    @Override
    public TableCellStyle build() {
        return new TableCellStyle(this.name, this.hidden, this.dataStyle, this.backgroundColor, this.tpBuilder.build(),
                this.textAlign, this.verticalAlign, this.wrap, this.parentCellStyle, this.bordersBuilder.build(),
                this.marginsBuilder.build());
    }

    @Override
    public TableCellStyle buildHidden() {
        this.hidden = true;
        return this.build();
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
     * Set the font weight to bold.<br>
     * The TableFamilyStyle must be of a format of
     * TableFamilyStyle.STYLE_TABLECELL
     *
     * @return true - the value was set,<br>
     * false - This object is no table cell, you can not set it to bold
     */
    public TableCellStyleBuilder fontWeightBold() {
        this.tpBuilder.fontWeightBold();
        return this;
    }

    /**
     * Set the font weight to normal.<br>
     * The TableFamilyStyle must be of a format of
     * TableFamilyStyle.STYLE_TABLECELL
     *
     * @return this for fluent style
     */
    public TableCellStyleBuilder fontWeightNormal() {
        this.tpBuilder.fontWeightNormal();
        return this;
    }

    // /**
    // * Set the data style for this TableFamilyStyle to ns.<br>
    // * If the StyleType of this TableFamilyStyle is not STYLE_TABLECELL, an
    // * exception is thrown
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
    public TableCellStyleBuilder textAlign(final TableCellStyle.Align align) {
        this.textAlign = align;
        return this;
    }

    /**
     * Set the vertical alignment of text.
     *
     * @param align - The vertical alignment flag,<br>
     *              either: VERTICALALIGN_TOP,VERTICALALIGN_MIDDLE or
     *              VERTICALALIGN_BOTTOM
     * @return this for fluent style
     */
    public TableCellStyleBuilder verticalAlign(final TableCellStyle.VerticalAlign align) {
        this.verticalAlign = align;
        return this;
    }
}
