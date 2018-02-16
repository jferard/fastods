/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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
package com.github.jferard.fastods;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Locale;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TableColdCell.class)
public class TableRowTest {
    public static final long TIME_IN_MILLIS = 1234567891011L;
    private DataStyles ds;
    private TableRow row;
    private StylesContainer stc;
    private Table table;
    private TableCellStyle tcs;
    private XMLUtil xmlUtil;

    @Before
    public void setUp() {
        this.stc = PowerMock.createMock(StylesContainer.class);
        this.table = PowerMock.createMock(Table.class);
        final WriteUtil writeUtil = WriteUtil.create();
        this.xmlUtil = XMLUtil.create();
        this.ds = DataStylesBuilder.create(Locale.US).build();
        this.row = new TableRow(writeUtil, this.xmlUtil, this.stc, this.ds, this.table, 10, 100);
        this.tcs = TableCellStyle.builder("---").build();
        PowerMock.mockStatic(TableColdCell.class);
    }


    @Test
    public final void testRows() throws IOException {
        final DataStyle booleanDataStyle = this.ds.getBooleanDataStyle();
        this.stc.addDataStyle(booleanDataStyle);
        EasyMock.expect(this.stc.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, booleanDataStyle))
                .andReturn(this.tcs);

        PowerMock.replayAll();
        this.row.getOrCreateCell(5).setBooleanValue(true);
        this.row.getOrCreateCell(10).setStringValue("a");
        DomTester.assertEquals(
                "<table:table-row table:style-name=\"ro1\">" + "<table:table-cell table:number-columns-repeated=\"5\"/>" + "<table:table-cell table:style-name=\"---\" office:value-type=\"boolean\" office:boolean-value=\"true\"/>" + "<table:table-cell table:number-columns-repeated=\"4\"/>" + "<table:table-cell office:value-type=\"string\" office:string-value=\"a\"/>" + "</table:table-row>",
                this.getXML());
        PowerMock.verifyAll();
    }

    public String getXML() throws IOException {
        final StringBuilder sb = new StringBuilder();
        this.row.appendXMLToTable(this.xmlUtil, sb);
        return sb.toString();
    }
}
