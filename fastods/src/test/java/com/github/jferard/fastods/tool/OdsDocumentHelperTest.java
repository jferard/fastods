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
package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.BooleanValue;
import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.NamedOdsDocument;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.TableNameUtil;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class OdsDocumentHelperTest {
    private OdsFileHelper helper;
    private List<Table> l;
    private NamedOdsDocument odsDocument;
    private PositionUtil positionUtil;
    private Table t1;
    private Table t2;
    private Table t3;
    private TableHelper tableHelper;

    @Before
    public void setUp() {
        this.positionUtil = new PositionUtil(new EqualityUtil(), new TableNameUtil());
        this.odsDocument = PowerMock.createMock(NamedOdsDocument.class);
        this.tableHelper = PowerMock.createMock(TableHelper.class);
        this.t1 = PowerMock.createMock(Table.class);
        this.t2 = PowerMock.createMock(Table.class);
        this.t3 = PowerMock.createMock(Table.class);
        this.l = Arrays.asList(this.t1, this.t2, this.t3);
        this.helper = new OdsFileHelper(this.odsDocument, this.tableHelper, this.positionUtil);
        PowerMock.resetAll();
    }

    @After
    public void tearDown() {
        PowerMock.verifyAll();
    }


    @Test
    public final void testCellMergeInAllTables() throws FastOdsException, IOException {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(1234567891011L);

        // play
        EasyMock.expect(this.odsDocument.getTables()).andReturn(this.l);
        this.tableHelper.setCellMerge(this.t1, 10, 5, 3, 2);
        this.tableHelper.setCellMerge(this.t2, 10, 5, 3, 2);
        this.tableHelper.setCellMerge(this.t3, 10, 5, 3, 2);

        PowerMock.replayAll();
        this.helper.setCellMergeInAllTables("F11", 3, 2);

    }

    @Test
    public final void testCellValueInAllTables() throws FastOdsException, IOException {
        final TableCellStyle ts = TableCellStyle.builder("a").build();
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(1234567891011L);
        final BooleanValue v = new BooleanValue(true);

        // play
        EasyMock.expect(this.odsDocument.getTables()).andReturn(this.l);
        this.tableHelper.setCellValue(this.t1, 6, 2, v, ts);
        this.tableHelper.setCellValue(this.t2, 6, 2, v, ts);
        this.tableHelper.setCellValue(this.t3, 6, 2, v, ts);

        PowerMock.replayAll();
        this.helper.setCellValueInAllTables("C7", v, ts);
    }
}
