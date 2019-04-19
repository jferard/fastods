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

package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.Color;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * Embbeded in FloatStyle and FractionStyle.
 * Built by a NumberStyleHelperBuilder.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
class NumberStyleHelper implements NumberStyle {
    private final CoreDataStyle dataStyle;

    private final boolean grouping;
    private final int minIntegerDigits;
    private final Color negativeValueColor;

    /**
     * Create a new number style
     *
     * @param dataStyle the inner data style
     * @param grouping true if
     * @param minIntegerDigits The minimum integer digits to be shown.
     * @param negativeValueColor the color if the numbrer is negative
     */
    NumberStyleHelper(final CoreDataStyle dataStyle, final boolean grouping, final int minIntegerDigits,
                      final Color negativeValueColor) {
        this.dataStyle = dataStyle;
        this.grouping = grouping;
        this.negativeValueColor = negativeValueColor;
        this.minIntegerDigits = minIntegerDigits;
    }

    /**
     * A helper to append XML content
     * @param util an util
     * @param appendable destination
     * @param numberStyleName the name of the number syle
     * @param number the number itself
     * @throws IOException if an I/O error occurs
     */
    void appendXMLHelper(final XMLUtil util, final Appendable appendable, final String numberStyleName,
                         final CharSequence number) throws IOException {
        this.appendOpenTag(util, appendable, numberStyleName, this.dataStyle.getName());
        appendable.append(number);
        this.appendCloseTag(appendable, numberStyleName);

        if (this.negativeValueColor != null) {
            this.appendOpenTag(util, appendable, numberStyleName, this.dataStyle.getName() + "-neg");
            this.appendStyleColor(util, appendable);
            appendable.append("<number:text>-</number:text>");
            appendable.append(number);
            this.appendStyleMap(util, appendable);
            this.appendCloseTag(appendable, numberStyleName);
        }
    }

    private void appendCloseTag(final Appendable appendable, final CharSequence numberStyleName) throws IOException {
        appendable.append("</number:").append(numberStyleName).append(">");
    }

    private void appendOpenTag(final XMLUtil util, final Appendable appendable, final CharSequence numberStyleName,
                              final String name) throws IOException {
        appendable.append("<number:").append(numberStyleName);
        util.appendEAttribute(appendable, "style:name", name);
        this.dataStyle.appendLVAttributes(util, appendable);
        appendable.append(">");
    }

    /**
     * Append the 19.348 number:grouping attribute. Default = false.
     *
     * @param util       a util for XML writing
     * @param appendable where to write
     * @throws IOException If an I/O error occurs
     */
    private void appendGroupingAttribute(final XMLUtil util, final Appendable appendable) throws IOException {
        if (this.grouping) util.appendAttribute(appendable, "number:grouping", "true");
    }

    private void appendMinIntegerDigitsAttribute(final XMLUtil util, final Appendable appendable) throws IOException {
        if (this.minIntegerDigits > 0)
            util.appendAttribute(appendable, "number:min-integer-digits", this.minIntegerDigits);
    }

    /**
     * Append the attributes of the number
     * @param util an util
     * @param appendable the destination
     * @throws IOException if an I/O error occurs
     */
    public void appendNumberAttribute(final XMLUtil util, final Appendable appendable) throws IOException {
        this.appendMinIntegerDigitsAttribute(util, appendable);
        this.appendGroupingAttribute(util, appendable);
    }

    /**
     * Appends the style color.
     *
     * @param util       XML util
     * @param appendable where to write
     * @throws IOException If an I/O error occurs
     */
    private void appendStyleColor(final XMLUtil util, final Appendable appendable) throws IOException {
        appendable.append("<style:text-properties");
        util.appendAttribute(appendable, "fo:color", this.negativeValueColor.hexValue());
        appendable.append("/>");
    }

    /**
     * Appends 16.3 style:map tag.
     *
     * @param util       XML util for escaping
     * @param appendable where to write
     * @throws IOException If an I/O error occurs
     */
    private void appendStyleMap(final XMLUtil util, final Appendable appendable) throws IOException {
        appendable.append("<style:map");
        util.appendEAttribute(appendable, "style:condition", "value()>=0");
        util.appendEAttribute(appendable, "style:apply-style-name", this.dataStyle.getName());
        appendable.append("/>");
    }

    @Override
    public String getName() {
        return this.dataStyle.getName();
    }

    @Override
    public boolean isHidden() {
        return this.dataStyle.isHidden();
    }
}
