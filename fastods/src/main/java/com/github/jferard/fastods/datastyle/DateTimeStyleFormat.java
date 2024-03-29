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

package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.XMLConvertible;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 16.27.10 number:date-style
 * The class `DateStyleFormat` represents a style for date and time values.
 * A `DateStyleFormat` may be constructed using bricks (the static `String`s),
 * free text or any string. This is flexible enough to create any style.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class DateTimeStyleFormat implements XMLConvertible {
    /**
     * 16.27.11 number:day
     * the day
     */
    public static final String DAY = "<number:day/>";
    /**
     * 16.27.11 number:day
     * A day (long)
     */
    public static final String LONG_DAY = "<number:day number:style=\"long\"/>";
    /**
     * 16.27.11 number:day
     * the day, textual version
     */
    public static final String TEXTUAL_DAY = "<number:day number:textual=\"true\"/>";
    /**
     * 16.27.11 number:day
     * A day (long)
     */
    public static final String LONG_TEXTUAL_DAY =
            "<number:day number:style=\"long\" " + "number:textual=\"true\"/>";


    /**
     * 16.27.12 number:month
     * A month
     */
    public static final String MONTH = "<number:month/>";
    /**
     * 16.27.12 number:month
     * A month (long)
     */
    public static final String LONG_MONTH = "<number:month number:style=\"long\"/>";
    /**
     * 16.27.12 number:month
     * A month name
     */
    public static final String TEXTUAL_MONTH = "<number:month number:textual=\"true\"/>";
    /**
     * 16.27.12 number:month
     * A month name (long)
     */
    public static final String LONG_TEXTUAL_MONTH =
            "<number:month number:style=\"long\" " + "number:textual=\"true\"/>";

    /**
     * 16.27.13 number:year
     * A year YY
     */
    public static final String YEAR = "<number:year/>";
    /**
     * 16.27.13 number:year
     * A year YYYY
     */
    public static final String LONG_YEAR = "<number:year number:style=\"long\"/>";

    /**
     * A dash
     */
    public static final String DASH = "<number:text>-</number:text>";
    /**
     * A colon
     */
    public static final String COLON = "<number:text>:</number:text>";
    /**
     * A dot
     */
    public static final String DOT = "<number:text>.</number:text>";
    /**
     * A dot and a space
     */
    public static final String DOT_SPACE = "<number:text>. </number:text>";
    /**
     * A slash
     */
    public static final String SLASH = "<number:text>/</number:text>";
    /**
     * A space
     */
    public static final String SPACE = "<number:text> </number:text>";
    /**
     * 16.27.15 number:day-of-week
     * Day of week
     */
    public static final String DAY_OF_WEEK = "<number:day-of-week/>";
    /**
     * 16.27.16 number:week-of-year
     * A week number in the year
     */
    public static final String WEEK = "<number:week-of-year/>";
    /**
     * 16.27.19 number:hours
     * Hours
     */
    public static final String HOURS = "<number:hours/>";
    /**
     * 16.27.19 number:hours
     * Hours (long)
     */
    public static final String LONG_HOURS = "<number:hours number:style=\"long\"/>";
    /**
     * 16.27.20 number:minutes
     * Minutes
     */
    public static final String MINUTES = "<number:minutes/>";
    /**
     * 16.27.20 number:minutes
     * Minutes (long)
     */
    public static final String LONG_MINUTES = "<number:minutes number:style=\"long\"/>";
    /**
     * 16.27.21 number:seconds
     * Seconds
     */
    public static final String SECONDS = "<number:seconds/>";
    /**
     * 16.27.21 number:seconds
     * Seconds (long)
     */
    public static final String LONG_SECONDS = "<number:seconds number:style=\"long\"/>";

    /**
     * A free text
     *
     * @param s the text
     * @return the XML representation of the text
     */
    public static String text(final String s) {
        return "<number:text>" + s + "</number:text>";
    }

    /**
     * 16.27.21 number:seconds
     * Seconds (long)
     *
     * @param n number of digits
     * @return the XML representation of the data time with n digits
     */
    public static String longSeconds(final int n) {
        return "<number:seconds number:style=\"long\" number:decimal-places=\"" + n + "\"/>";
    }


    private final String[] strings;

    /**
     * The constructor
     *
     * @param strings the string that compose the format
     */
    public DateTimeStyleFormat(final String... strings) {
        this.strings = strings;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        for (final String string : this.strings) {
            appendable.append(string);
        }
    }
}
