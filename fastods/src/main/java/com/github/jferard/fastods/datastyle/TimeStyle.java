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

package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * content.xml/office:document-content/office:automatic-styles/number:
 * time-style styles.xml/office:document-styles/office:styles/number:time-style
 *
 * @author Julien Férard
 */
public class TimeStyle implements DataStyle {
    private final CoreDataStyle dataStyle;
    private final DateTimeStyleFormat timeFormat;

    /**
     * Create a new date style
     *
     * @param dataStyle  the embedded core data style
     * @param timeFormat the format
     */
    TimeStyle(final CoreDataStyle dataStyle, final DateTimeStyleFormat timeFormat) {
        this.dataStyle = dataStyle;
        this.timeFormat = timeFormat;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<number:time-style");
        util.appendEAttribute(appendable, "style:name", this.dataStyle.getName());
        this.dataStyle.appendLVAttributes(util, appendable);
        if (this.timeFormat == null) {
            util.appendAttribute(appendable, "number:format-source", "language");
            appendable.append("/>");
        } else {
            util.appendAttribute(appendable, "number:format-source", "fixed");
            appendable.append(">");
            this.timeFormat.appendXMLContent(util, appendable);
            appendable.append("</number:time-style>");
        }
    }

    /**
     * A time format
     */
    public static class Format {
        /**
         * Set the time format like '01:02:03'.
         */
        public static final DateTimeStyleFormat HHMMSS =
                new DateTimeStyleFormat(DateTimeStyleFormat.LONG_HOURS, DateTimeStyleFormat.COLON,
                        DateTimeStyleFormat.LONG_MINUTES, DateTimeStyleFormat.COLON,
                        DateTimeStyleFormat.LONG_SECONDS);
        /**
         * Set the time format like '01:02:03.45'.
         */
        public static final DateTimeStyleFormat HHMMSS00 =
                new DateTimeStyleFormat(DateTimeStyleFormat.LONG_HOURS, DateTimeStyleFormat.COLON,
                        DateTimeStyleFormat.LONG_MINUTES, DateTimeStyleFormat.COLON,
                        DateTimeStyleFormat.longSeconds(2));
    }

    @Override
    public String getName() {
        return this.dataStyle.getName();
    }

    @Override
    public boolean isHidden() {
        return this.dataStyle.isHidden();
    }

    @Override
    public void addToElements(final OdsElements odsElements) {
        odsElements.addDataStyle(this);
    }
}
