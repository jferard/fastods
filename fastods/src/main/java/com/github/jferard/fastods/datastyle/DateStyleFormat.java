/*
 * FastODS - a Martin Schulz's SimpleODS fork
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

package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.XMLConvertible;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
public class DateStyleFormat implements XMLConvertible {
	/**
	 * The default date format Format.DDMMYY.
	 */
	public static final String DASH = "<number:text>-</number:text>";
    public static final String DAY = "<number:day/>";
    public static final String DOT = "<number:text>.</number:text>";
    public static final String DOT_SPACE = "<number:text>. </number:text>";
    public static final String HASH = "<number:text>/</number:text>";
    public static final String LONG_DAY = "<number:day number:style=\"long\"/>";
    public static final String LONG_MONTH = "<number:month number:style=\"long\"/>";
    public static final String LONG_TEXTUAL_MONTH = "<number:month number:style=\"long\" number:textual=\"true\"/>";
    public static final String LONG_YEAR = "<number:year number:style=\"long\"/>";
    public static final String SPACE = "<number:text> </number:text>";

	static final String WEEK = "<number:week-of-year/>";

	static final String YEAR = "<number:year/>";
    private final String[] strings;

    public DateStyleFormat(final String... strings) {
        this.strings = strings;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable) throws IOException {
        for (final String string : this.strings)
            appendable.append(string);
    }
}
