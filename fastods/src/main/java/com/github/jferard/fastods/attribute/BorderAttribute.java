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

package com.github.jferard.fastods.attribute;

import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * The BorderAttribute class represents an xml attribute in style:style tag.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class BorderAttribute implements AttributeValue {
    /**
     * The border color default is #000000 (black).
     */
    public static final Color DEFAULT_BORDER_COLOR = SimpleColor.BLACK;
    /**
     * The border size default is 0.1cm
     */
    public static final Length DEFAULT_BORDER_SIZE = SimpleLength.mm(1);
    /**
     * The default position for border
     */
    public static final Position DEFAULT_POSITION = Position.ALL;
    /**
     * The default style
     */
    public static final BorderStyle DEFAULT_STYLE = BorderStyle.SOLID;

    /**
     * @return a builder for BorderAttribute
     */
    public static BorderAttributeBuilder builder() {
        return new BorderAttributeBuilder();
    }

    /**
     * The border color
     */
    private final Color borderColor;
    /**
     * The border size.
     */
    private final Length borderSize;
    /**
     * The border style. Either BorderAttribute.BORDER_SOLID or
     * BorderAttribute.BORDER_DOUBLE.<br>
     * Default is BorderAttribute.BORDER_SOLID.
     */
    private final BorderStyle style;

    /**
     * size is a length value
     *
     * @param size  The size of the border
     * @param color The color of the border in format '#rrggbb'
     * @param style The style of the border, BorderAttribute.BORDER_SOLID or
     *              BorderAttribute.BORDER_DOUBLE
     */
    public BorderAttribute(final Length size, final Color color, final BorderStyle style) {
        this.borderSize = size;
        this.borderColor = color;
        this.style = style;
    }

    @Override
    public CharSequence getValue() {
        final StringBuilder sb = new StringBuilder();

        if (this.borderSize == null) {
            if (this.borderColor != SimpleColor.NONE) {
                sb.append(this.style.getValue()).append(XMLUtil.SPACE_CHAR)
                        .append(this.borderColor.getValue());
            }
        } else if (this.borderColor == SimpleColor.NONE) {
            sb.append(this.borderSize);
        } else {
            sb.append(this.borderSize).append(XMLUtil.SPACE_CHAR).append(this.style.getValue())
                    .append(XMLUtil.SPACE_CHAR).append(this.borderColor.getValue());
        }
        return sb;
    }

    @Override
    public String toString() {
        return "BorderAttribute[" + this.getValue() + "]";
    }

    /**
     * @param util       an xml util
     * @param appendable where to write
     * @param attrName   the attribute name
     * @throws IOException if an I/O error occurs
     */
    public void appendXMLAttribute(final XMLUtil util, final Appendable appendable,
                                   final String attrName) throws IOException {
        util.appendAttribute(appendable, attrName, this);
    }

    /**
     * The position of the border.
     */
    public enum Position {
        /**
         * All borders
         */
        ALL("fo:border"),
        /**
         * The bottom border
         */
        BOTTOM("fo:border-bottom"),
        /**
         * The left border
         */
        LEFT("fo:border-left"),
        /**
         * the right border
         */
        RIGHT("fo:border-right"),
        /**
         * The top border
         */
        TOP("fo:border-top");

        private final String attrName;

        Position(final String attrName) {
            this.attrName = attrName;
        }

        /**
         * @return the name of the attribute for XML use
         */
        String getAttrName() {
            return this.attrName;
        }
    }

}
