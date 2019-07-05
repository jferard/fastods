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

package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.CellValue;
import com.github.jferard.fastods.DateValue;
import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.ObjectToCellValueConverter;
import com.github.jferard.fastods.StringValue;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.TimeValue;
import com.github.jferard.fastods.ToCellValueConverter;

import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * A converter SQL -> OpenDocument cell value
 *
 * @author J. Férard
 */
public class SQLToCellValueConverter implements ToCellValueConverter {
    /**
     * Create a new converter
     *
     * @param intervalConverter a custom converter for SQL intervals. May be null
     * @param currency          the currency
     * @param charset           the charset for SQL byte object conversion
     * @return a new converter
     */
    public static SQLToCellValueConverter create(final IntervalConverter intervalConverter,
                                                 final String currency, final Charset charset) {
        return new SQLToCellValueConverter(new ObjectToCellValueConverter(currency),
                intervalConverter, charset);
    }

    private final ToCellValueConverter converter;
    private final SQLToCellValueConverter.IntervalConverter intervalConverter;
    private final Charset charset;

    /**
     * @param converter         the wrapped POJO -> Cell value converter
     * @param intervalConverter a custom converter for SQL intervals. May be null
     * @param charset           the charset for SQL byte object conversion
     */
    SQLToCellValueConverter(final ToCellValueConverter converter,
                            final IntervalConverter intervalConverter, final Charset charset) {
        this.converter = converter;
        this.intervalConverter = intervalConverter;
        this.charset = charset;
    }

    @Override
    public CellValue from(final Object o) {
        try {
            if (o instanceof Clob) { // NClob extends Clob
                final Clob clob = (Clob) o;
                return new StringValue(clob.getSubString(1, (int) clob.length()));
            } else if (o instanceof SQLXML) {
                final SQLXML sqlxml = (SQLXML) o;
                final String string = sqlxml.getString().trim();
                return new StringValue(string);
            } else if (o instanceof Date) {
                final long time = ((Date) o).getTime();
                return new DateValue(new Date(time));
            } else if (o instanceof Time) {
                final long time = ((Time) o).getTime();
                return new DateValue(new Date(time));
            } else if (o instanceof Timestamp) {
                final long time = ((Timestamp) o).getTime();
                return new DateValue(new Date(time));
            } else {
                final TimeValue timeValue = this.intervalConverter.castToInterval(o);
                if (timeValue == null) {
                    return this.converter.from(o);
                } else {
                    return timeValue;
                }
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CellValue from(final TableCell.Type type, final Object o) throws FastOdsException {
        try {
            switch (type) {
                case STRING:
                    if (o instanceof Blob) {
                        final Blob blob = (Blob) o;
                        return new StringValue(
                                new String(blob.getBytes(1, (int) blob.length()), this.charset));
                    } else if (o instanceof Clob) {
                        final Clob clob = (Clob) o;
                        return new StringValue(clob.getSubString(1, (int) clob.length()));
                    } else if (o instanceof SQLXML) {
                        final SQLXML sqlxml = (SQLXML) o;
                        final String string = sqlxml.getString().trim();
                        return new StringValue(string);
                    }
                    break;
                case DATE:
                    if (o instanceof Date) {
                        final long time = ((Date) o).getTime();
                        return new DateValue(new Date(time));
                    } else if (o instanceof Time) {
                        final long time = ((Time) o).getTime();
                        return new DateValue(new Date(time));
                    } else if (o instanceof Timestamp) {
                        final long time = ((Timestamp) o).getTime();
                        return new DateValue(new Date(time));
                    }
                    break;
                case TIME:
                    final TimeValue timeValue = this.intervalConverter.castToInterval(o);
                    if (timeValue != null) {
                        return timeValue;
                    }
                    break;
                default: // other cases
                    return this.converter.from(type, o);
            }
        } catch (final SQLException e) {
            throw new FastOdsException(e);
        }
        throw new FastOdsException("Can't cast " + o + " to " + type);
    }

    /**
     * JDBC does not provide a way to identify SQL Intervals. If the result set
     * contains intervals, we need to try a cast from the specific driver object.
     */
    public interface IntervalConverter {
        /**
         * @param o the object to cast
         * @return the time value or null if the object was not casted
         */
        TimeValue castToInterval(Object o);
    }


}
