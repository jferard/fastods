/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.util;

import com.github.jferard.fastods.attribute.AttributeValue;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * An util for writing XML representation.
 *
 * @author Julien Férard
 */
public class XMLUtil {
    /**
     * A space char for append
     */
    public static final char SPACE_CHAR = ' ';

    /**
     * @return a new default xml util
     */
    public static XMLUtil create() {
        final XMLEscaper escaper = FastOdsXMLEscaper.create();
        return new XMLUtil(escaper);
    }

    private final XMLEscaper escaper;

    /**
     * Create a new util
     *
     * @param escaper the embedded escaper
     */
    XMLUtil(final XMLEscaper escaper) {
        this.escaper = escaper;
    }

    /**
     * Escape then append.
     * <p>
     * Append a space and new element to the appendable element, the name of the element is
     * attrName and the value is attrRawValue. The will be escaped if necessary
     *
     * @param appendable   The StringBuilder to which the new element should be added.
     * @param attrName     The new element name
     * @param attrRawValue The value of the element
     * @throws IOException If an I/O error occurs
     */
    public void appendEAttribute(final Appendable appendable, final CharSequence attrName,
                                 final String attrRawValue) throws IOException {
        appendable.append(' ').append(attrName).append("=\"")
                .append(this.escaper.escapeXMLAttribute(attrRawValue)).append('"');
    }

    /**
     * Append a new element to the appendable element, the name of the element is
     * attrName and the value is the boolean attrValue.
     *
     * @param appendable The StringBuilder to which the new element should be added.
     * @param attrName   The new element name
     * @param attrValue  The value of the element
     * @throws IOException If an I/O error occurs
     */
    public void appendAttribute(final Appendable appendable, final CharSequence attrName,
                                final boolean attrValue) throws IOException {
        this.appendAttribute(appendable, attrName, Boolean.toString(attrValue));
    }

    /**
     * Append a new element to the appendable element, the name of the element is
     * attrName and the value is attrValue.
     *
     * @param appendable The StringBuilder to which the new element should be added.
     * @param attrName   The new element name
     * @param attrValue  The value of the element
     * @throws IOException If an I/O error occurs
     */
    public void appendAttribute(final Appendable appendable, final CharSequence attrName,
                                final int attrValue) throws IOException {
        this.appendAttribute(appendable, attrName, Integer.toString(attrValue));
    }

    /**
     * Append a space, then a new element to the appendable element, the name of the element is
     * attrName and the value is attrValue. The value won't be escaped.
     *
     * @param appendable where to write
     * @param attrName   the name of the attribute
     * @param attrValue  escaped attribute
     * @throws IOException If an I/O error occurs
     */
    public void appendAttribute(final Appendable appendable, final CharSequence attrName,
                                final CharSequence attrValue) throws IOException {
        appendable.append(' ').append(attrName).append("=\"").append(attrValue).append('"');
    }

    /**
     * Append a space, then a new element to the appendable element, the name of the element is
     * attrName and the value is attrValue. The value won't be escaped.
     *
     * @param appendable where to write
     * @param attrName   the name of the attribute
     * @param attrValue  escaped attribute
     * @throws IOException If an I/O error occurs
     */
    public void appendAttribute(final Appendable appendable, final CharSequence attrName,
                                final AttributeValue attrValue) throws IOException {
        appendable.append(' ').append(attrName).append("=\"").append(attrValue.getValue())
                .append('"');
    }

    /**
     * Append a space, then a new element to the appendable element, the name of the element is
     * attrName and the value is a list. The value won't be escaped.
     *
     * @param appendable where to write
     * @param attrName   the name of the attribute
     * @param attrs      list of attributes
     * @param sep        the separator
     * @throws IOException If an I/O error occurs
     */
    public void appendAttribute(final Appendable appendable, final CharSequence attrName,
                                final List<?> attrs, final String sep)
            throws IOException {
        appendable.append(' ').append(attrName).append("=\"");
        if (!attrs.isEmpty()) {
            final Iterator<?> it = attrs.iterator();
            appendable.append(it.next().toString());
            while (it.hasNext()) {
                appendable.append(sep).append(it.next().toString());
            }
        }
        appendable.append('"');
    }

    /**
     * Append a content inside a tag
     *
     * @param appendable the destination
     * @param tagName    the tag name
     * @param content    the content
     * @throws IOException if an I/O error occurs
     */
    public void appendTag(final Appendable appendable, final CharSequence tagName,
                          final String content) throws IOException {
        appendable.append('<').append(tagName).append('>')
                .append(this.escaper.escapeXMLContent(content)).append("</").append(tagName)
                .append('>');
    }

    /**
     * Escape an XML attribute
     *
     * @param s the attribute
     * @return the escaped attribute
     */
    public String escapeXMLAttribute(final String s) {
        return this.escaper.escapeXMLAttribute(s);
    }

    /**
     * Escape an XML content
     *
     * @param s the content
     * @return the escaped content
     */
    public String escapeXMLContent(final String s) {
        return this.escaper.escapeXMLContent(s);
    }

    /**
     * XML Schema Part 2, 3.2.6 duration
     * "'P'yyyy'Y'MM'M'dd'DT'HH'H'mm'M'ss.SSS'S'"
     *
     * @param milliseconds the interval to format in milliseconds
     * @return the string that represents this interval
     */
    public String formatTimeInterval(final long milliseconds) {
        return "PT" + (double) milliseconds / 1000 + "S";
    }

    /**
     * XML Schema Part 2, 3.2.6 duration
     * "'P'yyyy'Y'MM'M'dd'DT'HH'H'mm'M'ss.SSS'S'"
     * <p>
     * All parameters must be positive
     *
     * @param years   number of years
     * @param months  number of months
     * @param days    number of days
     * @param hours   number of hours
     * @param minutes number of minutes
     * @param seconds number of seconds
     * @return the string that represents this interval
     */
    public String formatTimeInterval(final long years, final long months, final long days,
                                     final long hours, final long minutes, final double seconds) {
        if (years < 0 || months < 0 || days < 0 || hours < 0 || minutes < 0 || seconds < 0) {
            throw new IllegalArgumentException(
                    String.format("Wrong arguments: %d, %d, %d, %d, %d, %f can't be negative",
                            years, months, days, hours, minutes, seconds));
        }
        final boolean noDate = years == 0 && months == 0 && days == 0;
        final boolean noTime = hours == 0 && minutes == 0 && seconds == 0;

        final StringBuilder sb = new StringBuilder().append('P');
        if (years > 0 || (noDate && noTime)) {
            sb.append(years).append('Y');
        }
        if (months > 0) {
            sb.append(months).append('M');
        }
        if (days > 0) {
            sb.append(days).append('D');
        }
        if (noTime) {
            return sb.toString();
        }
        sb.append('T');
        if (hours > 0) {
            sb.append(hours).append('H');
        }
        if (minutes > 0) {
            sb.append(minutes).append('M');
        }
        if (seconds > 0) {
            sb.append(seconds).append('S');
        }
        return sb.toString();
    }

    /**
     * XML Schema Part 2, 3.2.6 duration
     * "'P'yyyy'Y'MM'M'dd'DT'HH'H'mm'M'ss.SSS'S'"
     * <p>
     * All parameters must be positive
     *
     * @param years   number of years
     * @param months  number of months
     * @param days    number of days
     * @param hours   number of hours
     * @param minutes number of minutes
     * @param seconds number of seconds
     * @return the string that represents this interval with a negative value
     */
    public String formatNegTimeInterval(final long years, final long months, final long days,
                                        final long hours, final long minutes,
                                        final double seconds) {
        return '-' + this.formatTimeInterval(years, months, days, hours, minutes, seconds);
    }
}
