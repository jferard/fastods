
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

package com.github.jferard.fastods.extra;

import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.CharsetUtil;
import com.github.jferard.javamcsv.MetaCSVDataException;
import com.github.jferard.javamcsv.MetaCSVParseException;
import com.github.jferard.javamcsv.MetaCSVReadException;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CSVDataWrapperTest {
    private TableCellWalker walker;

    @Before
    public void setUp() {
        this.walker = PowerMock.createMock(TableCellWalker.class);
    }

    @Test
    public void testBoolean()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setBooleanValue(true);
        this.finalizeWalker();

        PowerMock.replayAll();
        final CSVDataWrapper dataWrapper = this.getWrapper("T", "boolean/T/F");
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testCurrencyDecimal()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setCurrencyValue(new BigDecimal("7.4"), "€");
        this.finalizeWalker();

        PowerMock.replayAll();
        final CSVDataWrapper dataWrapper = this.getWrapper("7.4 €", "currency/post/€/decimal//.");
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testCurrencyInteger()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setCurrencyValue(74, "€");
        this.finalizeWalker();

        PowerMock.replayAll();
        final CSVDataWrapper dataWrapper = this.getWrapper("74 €", "currency/post/€/integer");
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testDate()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        final Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2021, Calendar.FEBRUARY, 8);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        final Date d = cal.getTime();

        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setDateValue(d);
        this.finalizeWalker();

        PowerMock.replayAll();
        final CSVDataWrapper dataWrapper = this.getWrapper("2021-02-08Z", "date/yyyy-MM-ddX");
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testDatetime()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        final Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2021, Calendar.FEBRUARY, 8);
        cal.set(Calendar.HOUR, 10);
        cal.set(Calendar.MINUTE, 11);
        cal.set(Calendar.SECOND, 12);
        cal.set(Calendar.MILLISECOND, 0);
        final Date d = cal.getTime();

        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setDateValue(d);
        this.finalizeWalker();

        PowerMock.replayAll();
        final CSVDataWrapper dataWrapper = this.getWrapper("2021-02-08T10:11:12Z", "datetime/yyyy-MM-dd'T'HH:mm:ssX");
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testFloat()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setFloatValue(10.5d);
        this.finalizeWalker();

        PowerMock.replayAll();
        final CSVDataWrapper dataWrapper = this.getWrapper("\"10,5\"", "\"float//,\"");
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testDecimal()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setFloatValue(new BigDecimal("10.5"));
        this.finalizeWalker();

        PowerMock.replayAll();
        final CSVDataWrapper dataWrapper = this.getWrapper("\"10,5\"", "\"decimal//,\"");
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testInteger()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setFloatValue(10L);
        this.finalizeWalker();

        PowerMock.replayAll();
        final CSVDataWrapper dataWrapper = this.getWrapper("10", "integer");
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testObject()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setStringValue("foo");
        this.finalizeWalker();

        PowerMock.replayAll();
        final CSVDataWrapper dataWrapper = this.getWrapper("foo", "object");
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testPercentageDecimal()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setPercentageValue(new BigDecimal("0.074"));
        this.finalizeWalker();

        PowerMock.replayAll();
        final CSVDataWrapper dataWrapper = this.getWrapper("7.4 %", "percentage/post/%/decimal//.");
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testPercentageFloat()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setPercentageValue(0.073d);
        this.finalizeWalker();

        PowerMock.replayAll();
        final CSVDataWrapper dataWrapper = this.getWrapper("7.3 %", "percentage/post/%/float//.");
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    @Test
    public void testText()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        PowerMock.resetAll();
        this.prepareWalker();
        this.walker.setStringValue("foo");
        this.finalizeWalker();

        PowerMock.replayAll();
        final CSVDataWrapper dataWrapper = this.getWrapper("foo", "text");
        dataWrapper.addToTable(this.walker);

        PowerMock.verifyAll();
    }

    private TableCellWalker prepareWalker() throws IOException {
        EasyMock.expect(this.walker.rowIndex()).andReturn(0);
        EasyMock.expect(this.walker.colIndex()).andReturn(0);
        this.walker.to(0);
        this.walker.setStringValue("VALUE");
        this.walker.setStyle(EasyMock.isA(TableCellStyle.class));
        this.walker.next();
        this.walker.nextRow();
        this.walker.to(0);
        return this.walker;
    }

    private void finalizeWalker() throws IOException {
        this.walker.next();
        this.walker.nextRow();
        this.walker.nextRow();
    }

    private CSVDataWrapper getWrapper(final String value, final String type)
            throws MetaCSVReadException, MetaCSVDataException, MetaCSVParseException, IOException {
        final byte[] bytes = ("VALUE\r\n" + value).getBytes(CharsetUtil.UTF_8);
        final CSVDataWrapper dataWrapper =
                new CSVDataWrapperBuilder(new ByteArrayInputStream(bytes))
                        .metaCSVDirectives("data,col/0/type," + type).build();
        return dataWrapper;
    }
}
