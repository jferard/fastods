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

import com.github.jferard.fastods.attribute.FormatSource;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * content.xml/office:document-content/office:automatic-styles/number:
 * date-style styles.xml/office:document-styles/office:styles/number:date-style
 * <p>
 * To create a custom `DateStyle`, first create a new `DateStyleFormat` and pass it to
 * a `DateStyleBuilder`:
 *
 * <pre>{@code
 *     DateStyleFormat f = new DateStyleFormat(DateStyleFormat.text("Month of "),
 *                                                          DateStyleFormat.LONG_TEXTUAL_MONTH);
 *     DateStyle s = DateStyleBuilder("my style", Locale.US).dateFormat(f).build();
 *     ... // use the style
 * }</pre>
 *
 * Note that the bricks from `TimeStyleFormat` are allowed too:
 *
 * <pre>{@code
 *     DateStyleFormat f = new DateStyleFormat(TimeStyleFormat.MINUTES);
 *     DateStyle s = DateStyleBuilder("my style", Locale.US).dateFormat(f).build();
 *     ... // use the style
 * }</pre>
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class DateStyle implements DataStyle {
    private final FormatSource formatSource;

    /**
     * The format of the date
     */
    public static class Format {
        /** Do not instantiate */
        private Format() {}

        /**
         * Set the date format like '10.07.2012'.
         */
        public static final DateTimeStyleFormat DDMMYYYY =
                new DateTimeStyleFormat(DateTimeStyleFormat.LONG_DAY, DateTimeStyleFormat.DOT,
                        DateTimeStyleFormat.LONG_MONTH, DateTimeStyleFormat.DOT,
                        DateTimeStyleFormat.LONG_YEAR);

        /**
         * Set the date format like '10.07.12'.
         */
        public static final DateTimeStyleFormat DDMMYY =
                new DateTimeStyleFormat(DateTimeStyleFormat.LONG_DAY, DateTimeStyleFormat.DOT,
                        DateTimeStyleFormat.LONG_MONTH, DateTimeStyleFormat.DOT,
                        DateTimeStyleFormat.YEAR);

        /**
         * Set the date format like 'July'.
         */
        public static final DateTimeStyleFormat MMMM =
                new DateTimeStyleFormat(DateTimeStyleFormat.LONG_TEXTUAL_MONTH);

        /**
         * Set the date format like '07.12'.<br>
         * Month.Year
         */
        public static final DateTimeStyleFormat MMYY =
                new DateTimeStyleFormat(DateTimeStyleFormat.LONG_MONTH, DateTimeStyleFormat.DOT,
                        DateTimeStyleFormat.YEAR);

        /**
         * Set the date format like '10.July 2012'.
         */
        public static final DateTimeStyleFormat TMMMMYYYY =
                new DateTimeStyleFormat(DateTimeStyleFormat.DAY, DateTimeStyleFormat.DOT_SPACE,
                        DateTimeStyleFormat.LONG_TEXTUAL_MONTH, DateTimeStyleFormat.SPACE,
                        DateTimeStyleFormat.LONG_YEAR);

        /**
         * Set the date format to the weeknumber like '28'.<br>
         * Week number
         */
        public static final DateTimeStyleFormat WW =
                new DateTimeStyleFormat(DateTimeStyleFormat.WEEK);

        /**
         * Set the date format like '2012-07-10'.<br>
         */
        public static final DateTimeStyleFormat YYYYMMDD =
                new DateTimeStyleFormat(DateTimeStyleFormat.LONG_YEAR, DateTimeStyleFormat.DASH,
                        DateTimeStyleFormat.LONG_MONTH, DateTimeStyleFormat.DASH,
                        DateTimeStyleFormat.LONG_DAY);
    }

    /**
     * 19.340 number:automatic-order: "specifies whether data is ordered to match the default
     * order for the language and country of a data style"
     */
    private final boolean automaticOrder;
    private final CoreDataStyle dataStyle;
    private final DateTimeStyleFormat dateFormat;

    /**
     * Create a new date style with the name name.
     *
     * @param dataStyle      the core data style
     * @param dateFormat     the format for the date
     * @param automaticOrder true if the order comes from the current locale
     */
    DateStyle(final CoreDataStyle dataStyle, final DateTimeStyleFormat dateFormat,
              final boolean automaticOrder, final FormatSource formatSource) {
        this.dataStyle = dataStyle;
        this.dateFormat = dateFormat;
        this.automaticOrder = automaticOrder;
        this.formatSource = formatSource;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<number:date-style");
        util.appendEAttribute(appendable, "style:name", this.dataStyle.getName());
        this.dataStyle.appendLVAttributes(util, appendable);
        util.appendAttribute(appendable, "number:automatic-order", this.automaticOrder);
        if (this.formatSource != FormatSource.FIXED) {
            util.appendAttribute(appendable, "number:format-source", this.formatSource);
        }
        appendable.append(">");
        this.dateFormat.appendXMLContent(util, appendable);
        appendable.append("</number:date-style>");
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
