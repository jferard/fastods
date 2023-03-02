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
package com.github.jferard.fastods;

import com.github.jferard.fastods.attribute.CellType;
import com.github.jferard.fastods.datastyle.BooleanStyle;
import com.github.jferard.fastods.datastyle.BooleanStyleBuilder;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.PageLayoutStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

public class NamedOdsDocumentTest extends OdsDocumentTest<NamedOdsDocument> {
    private Logger logger;
    private XMLUtil xmlUtil;
    private TableCellStyle aStyle;
    private BooleanStyle aDataStyle;

    @Before
    public final void setUp() {
        this.logger = PowerMock.createNiceMock(Logger.class);
        this.xmlUtil = XMLUtil.create();
        this.odsElements = PowerMock.createMock(OdsElements.class);
        this.aDataStyle = new BooleanStyleBuilder("o", Locale.US).build();
        this.aStyle = TableCellStyle.builder("a").dataStyle(this.aDataStyle).build();
    }

    @Test
    public final void testAddBooleanStyle() {
        final DataStyle ds = new BooleanStyleBuilder("b", Locale.US).build();

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.addDataStyle(ds)).andReturn(true);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addDataStyle(ds);

        PowerMock.verifyAll();
    }


    @Test
    public final void testAddPageStyle() {
        final PageStyle ps = PageStyle.builder("p").build();

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.addPageStyle(ps)).andReturn(true);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addPageStyle(ps);

        PowerMock.verifyAll();
    }

    @Override
    NamedOdsDocument getDocument() {
        return NamedOdsDocument.create(this.logger, this.xmlUtil, this.odsElements);
    }

    @Test
    public final void testSaveTo() throws IOException {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.saveAsync();

        PowerMock.replayAll();
        final NamedOdsDocument d = this.getDocument();
        d.save();

        PowerMock.verifyAll();
    }

    @Test
    public final void testSaveToCloseException() throws IOException {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.saveAsync();
        EasyMock.expectLastCall().andThrow(new IOException("@"));

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        Assert.assertThrows("@", IOException.class, () -> document.save());

        PowerMock.verifyAll();
    }


    @Test
    public final void testSaveWriterException() throws IOException {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.saveAsync();
        EasyMock.expectLastCall().andThrow(new RuntimeException("@"));

        PowerMock.replayAll();
        final NamedOdsDocument d = this.getDocument();
        Assert.assertThrows("@", RuntimeException.class, () -> d.save());

        PowerMock.verifyAll();
    }

    @Test
    public void testFreezeStyles() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.freezeStyles();
        this.odsElements.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.freezeStyles();
        Assert.assertThrows(IllegalStateException.class,
                () -> document.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE));

        PowerMock.verifyAll();
    }

    @Test
    public void testDebugStyles() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.debugStyles();

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.debugStyles();

        PowerMock.verifyAll();
    }

    @Test
    public void testAddStyleToContentAutomaticStyles() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE))
                .andReturn(true);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddChildCellStyle() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.addCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, CellType.STRING);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, CellType.STRING);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddPageStyle2() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.addPageStyle(PageStyle.DEFAULT_PAGE_STYLE))
                .andReturn(true);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addPageStyle(PageStyle.DEFAULT_PAGE_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddTableToContent() throws IOException {
        final Table t = PowerMock.createMock(Table.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.createTable("table1", 10, 15)).andReturn(t);
        EasyMock.expect(this.odsElements.addTableToContent(t)).andReturn(true);
        this.odsElements.setActiveTable(t);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addTable("table1", 10, 15);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddContentStyle() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE))
                .andReturn(true);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addContentStyle(TableCellStyle.DEFAULT_CELL_STYLE);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddPageLayoutStyle() {
        final PageLayoutStyle pls = PowerMock.createMock(PageLayoutStyle.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.addPageLayoutStyle(pls)).andReturn(true);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addPageLayoutStyle(pls);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddMasterPageStyle() {
        final MasterPageStyle mps = PowerMock.createMock(MasterPageStyle.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.addMasterPageStyle(mps)).andReturn(true);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addMasterPageStyle(mps);

        PowerMock.verifyAll();
    }

    @Test
    public void testChildCellStyle() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.addCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, CellType.STRING);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, CellType.STRING);

        PowerMock.verifyAll();
    }

    @Test
    public void testChildCellVoidStyle() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.addCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, CellType.STRING);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, CellType.STRING);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddNewDataStyleFromCellStyle() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.addNewDataStyleFromCellStyle(this.aStyle)).andReturn(true);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addNewDataStyleFromCellStyle(this.aStyle);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddStylesStyle() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.addStylesStyle(this.aStyle)).andReturn(true);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addStylesStyle(this.aStyle);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddChildCellStyleDS() {
        final TableCellStyle style = PowerMock.createMock(TableCellStyle.class);

        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements
                        .addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, this.aDataStyle))
                .andReturn(style);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, this.aDataStyle);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddContentFontFaceContainerStyle() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.addContentFontFaceContainerStyle(this.aStyle))
                .andReturn(true);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addContentFontFaceContainerStyle(this.aStyle);

        PowerMock.verifyAll();
    }

    @Test
    public void testAddStylesFontFaceContainerStyle() {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        EasyMock.expect(this.odsElements.addStylesFontFaceContainerStyle(this.aStyle))
                .andReturn(true);

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.addStylesFontFaceContainerStyle(this.aStyle);

        PowerMock.verifyAll();
    }

    @Test
    public void testSave() throws IOException {
        PowerMock.resetAll();
        TestHelper.initMockDocument(this.odsElements);
        this.odsElements.saveAsync();

        PowerMock.replayAll();
        final NamedOdsDocument document = this.getDocument();
        document.save();

        PowerMock.verifyAll();
    }
}
