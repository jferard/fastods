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

import com.github.jferard.fastods.DateValue;
import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.StringValue;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.TimeValue;
import com.github.jferard.fastods.ToCellValueConverter;
import com.mockrunner.mock.jdbc.MockBlob;
import com.mockrunner.mock.jdbc.MockClob;
import com.mockrunner.mock.jdbc.MockSQLXML;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.nio.charset.Charset;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class SQLToCellValueConverterTest {
    public static final Charset CHARSET = Charset.forName("US-ASCII");
    public static final String XML_FASTODS =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<fastods />";
    public static final String XML_FASTODS_RAW = "<fastods/>";
    public static final String FASTODS = "FastODS";
    public static final Date SQL_DATE = new Date(100);
    public static final java.util.Date UTIL_DATE = new java.util.Date(100);
    public static final TimeValue TIME_VALUE = new TimeValue(false, 0, 0, 0, 0, 0, 0);
    public static final StringValue EMPTY_STRING_VALUE = new StringValue("");
    private static final Time SQL_TIME = new Time(100);
    private static final Timestamp SQL_TS = new Timestamp(100);
    private SQLToCellValueConverter.IntervalConverter intervalConverter;
    private ToCellValueConverter wrappedConverter;
    private SQLToCellValueConverter converter;

    @Before
    public void setUp() {
        this.intervalConverter = PowerMock
                .createMock(SQLToCellValueConverter.IntervalConverter.class);
        this.wrappedConverter = PowerMock.createMock(ToCellValueConverter.class);
        this.converter = new SQLToCellValueConverter(this.wrappedConverter, this.intervalConverter,
                CHARSET);
    }

    @Test
    public void testFromBase() throws FastOdsException {
        PowerMock.resetAll();
        EasyMock.expect(this.wrappedConverter.from(TableCell.Type.FLOAT, FASTODS)).andReturn(EMPTY_STRING_VALUE);

        PowerMock.replayAll();
        Assert.assertEquals(EMPTY_STRING_VALUE, this.converter.from(TableCell.Type.FLOAT, FASTODS));

        PowerMock.verifyAll();
    }

    @Test
    public void testFromBlob() {
        final MockBlob blob = new MockBlob(FASTODS.getBytes(CHARSET));

        PowerMock.resetAll();
        EasyMock.expect(this.intervalConverter.castToInterval(blob)).andReturn(null);
        EasyMock.expect(this.wrappedConverter.from(blob)).andReturn(EMPTY_STRING_VALUE);

        PowerMock.replayAll();
        Assert.assertEquals(EMPTY_STRING_VALUE, this.converter.from(blob));

        PowerMock.verifyAll();
    }

    @Test
    public void testFromHintBlob() throws FastOdsException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertEquals(new StringValue(FASTODS), this.converter
                .from(TableCell.Type.STRING, new MockBlob(FASTODS.getBytes(CHARSET))));

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailBlob() throws FastOdsException {
        final MockBlob blob = new MockBlob(FASTODS.getBytes(CHARSET));
        EasyMock.expect(this.wrappedConverter.from(TableCell.Type.VOID, blob))
                .andThrow(new FastOdsException(""));

        PowerMock.replayAll();
        this.converter.from(TableCell.Type.VOID, blob);

        PowerMock.verifyAll();
    }

    @Test
    public void testFromClob() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertEquals(new StringValue(FASTODS), this.converter.from(new MockClob(FASTODS)));

        PowerMock.verifyAll();
    }

    @Test
    public void testFromHintClob() throws FastOdsException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertEquals(new StringValue(FASTODS),
                this.converter.from(TableCell.Type.STRING, new MockClob(FASTODS)));

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailClob() throws FastOdsException {
        final MockClob clob = new MockClob(FASTODS);
        EasyMock.expect(this.wrappedConverter.from(TableCell.Type.VOID, clob))
                .andThrow(new FastOdsException(""));

        PowerMock.replayAll();
        this.converter.from(TableCell.Type.VOID, clob);

        PowerMock.verifyAll();
    }

    @Test
    public void testFromSQLXML() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertEquals(new StringValue(XML_FASTODS),
                this.converter.from(new MockSQLXML(XML_FASTODS_RAW)));

        PowerMock.verifyAll();
    }

    @Test
    public void testFromHintSQLXML() throws FastOdsException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertEquals(new StringValue(XML_FASTODS),
                this.converter.from(TableCell.Type.STRING, new MockSQLXML(XML_FASTODS_RAW)));

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailSQLXML() throws FastOdsException {
        PowerMock.resetAll();
        final MockSQLXML sqlxml = new MockSQLXML(FASTODS);
        EasyMock.expect(this.wrappedConverter.from(TableCell.Type.VOID, sqlxml))
                .andThrow(new FastOdsException(""));

        PowerMock.replayAll();
        this.converter.from(TableCell.Type.VOID, sqlxml);

        PowerMock.verifyAll();
    }

    @Test
    public void testFromDate() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertEquals(new DateValue(UTIL_DATE), this.converter.from(SQL_DATE));

        PowerMock.verifyAll();
    }

    @Test
    public void testFromHintDate() throws FastOdsException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertEquals(new DateValue(UTIL_DATE),
                this.converter.from(TableCell.Type.DATE, SQL_DATE));

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailDate() throws FastOdsException {
        PowerMock.resetAll();
        EasyMock.expect(this.wrappedConverter.from(TableCell.Type.VOID, SQL_DATE))
                .andThrow(new FastOdsException(""));

        PowerMock.replayAll();
        this.converter.from(TableCell.Type.VOID, SQL_DATE);

        PowerMock.verifyAll();
    }

    @Test
    public void testFromTime() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertEquals(new DateValue(UTIL_DATE), this.converter.from(SQL_TIME));

        PowerMock.verifyAll();
    }

    @Test
    public void testFromHintTime() throws FastOdsException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertEquals(new DateValue(UTIL_DATE),
                this.converter.from(TableCell.Type.DATE, SQL_TIME));

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailTime() throws FastOdsException {
        PowerMock.resetAll();
        EasyMock.expect(this.wrappedConverter.from(TableCell.Type.VOID, SQL_TIME))
                .andThrow(new FastOdsException(""));

        PowerMock.replayAll();
        this.converter.from(TableCell.Type.VOID, SQL_TIME);
        PowerMock.verifyAll();
    }

    @Test
    public void testFromTimestamp() {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertEquals(new DateValue(UTIL_DATE), this.converter.from(SQL_TS));

        PowerMock.verifyAll();
    }

    @Test
    public void testFromHintTimestamp() throws FastOdsException {
        PowerMock.resetAll();
        PowerMock.replayAll();
        Assert.assertEquals(new DateValue(UTIL_DATE),
                this.converter.from(TableCell.Type.DATE, SQL_TS));

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailTimestamp() throws FastOdsException {
        PowerMock.resetAll();
        EasyMock.expect(this.wrappedConverter.from(TableCell.Type.VOID, SQL_TS))
                .andThrow(new FastOdsException(""));

        PowerMock.replayAll();
        this.converter.from(TableCell.Type.VOID, SQL_TS);

        PowerMock.verifyAll();
    }

    @Test
    public void testFromIntervalYes() {
        PowerMock.resetAll();
        final Object interval = new Object();
        EasyMock.expect(this.intervalConverter.castToInterval(interval)).andReturn(TIME_VALUE);

        PowerMock.replayAll();
        Assert.assertEquals(TIME_VALUE, this.converter.from(interval));

        PowerMock.verifyAll();
    }

    @Test
    public void testFromIntervalNo() {
        PowerMock.resetAll();
        final Object interval = new Object();
        EasyMock.expect(this.intervalConverter.castToInterval(interval)).andReturn(null);
        EasyMock.expect(this.wrappedConverter.from(interval)).andReturn(EMPTY_STRING_VALUE);

        PowerMock.replayAll();
        Assert.assertEquals(EMPTY_STRING_VALUE, this.converter.from(interval));

        PowerMock.verifyAll();
    }

    @Test
    public void testFromHintIntervalYes() throws FastOdsException {
        PowerMock.resetAll();
        final Object interval = new Object();
        EasyMock.expect(this.intervalConverter.castToInterval(interval)).andReturn(TIME_VALUE);

        PowerMock.replayAll();
        Assert.assertEquals(TIME_VALUE, this.converter.from(TableCell.Type.TIME, interval));

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintIntervalNo() throws FastOdsException {
        PowerMock.resetAll();
        final Object interval = new Object();
        EasyMock.expect(this.intervalConverter.castToInterval(interval)).andReturn(null);

        PowerMock.replayAll();
        this.converter.from(TableCell.Type.TIME, interval);

        PowerMock.verifyAll();
    }

    @Test(expected = FastOdsException.class)
    public void testFromHintFailInterval() throws FastOdsException {
        PowerMock.resetAll();
        final Object interval = new Object();
        EasyMock.expect(this.wrappedConverter.from(TableCell.Type.VOID, interval))
                .andThrow(new FastOdsException(""));

        PowerMock.replayAll();
        this.converter.from(TableCell.Type.VOID, interval);

        PowerMock.verifyAll();
    }
}