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

package com.github.jferard.fastods.testlib;

import org.apache.html.dom.HTMLBodyElementImpl;
import org.apache.html.dom.HTMLDivElementImpl;
import org.apache.html.dom.HTMLDocumentImpl;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.odftoolkit.odfdom.dom.element.OdfStyleBase;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStyle;
import org.odftoolkit.odfdom.pkg.OdfFileDom;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Table;
import org.powermock.api.easymock.PowerMock;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLDivElement;

public class OdfToolkitUtilTest {
    private SpreadsheetDocument document;
    private Table sheet;
    private Cell cell;

    @Before
    public void setUp() throws Exception {
        this.document = SpreadsheetDocument.newSpreadsheetDocument();
        this.sheet = this.document.getSheetByIndex(0);
        this.cell = this.sheet.getCellByPosition(0, 0);
    }

    @Test
    public void test() {
        this.cell.setStringValue("str");
        Assert.assertEquals("str", OdfToolkitUtil.getStringValue(this.cell));
    }

    @Test(expected = NullPointerException.class)
    public void testNullParentStyleName() {
        this.cell.setStringValue("str");
        OdfToolkitUtil.getParentStyleName(this.cell);
    }

    @Test
    public void testStyleName() {
        this.cell.setCellStyleName("default");
        Assert.assertEquals("default", OdfToolkitUtil.getStyleName(this.cell));
    }

    @Test
    public void testStyleFamilyName() {
        this.cell.setCellStyleName("default");
        Assert.assertEquals(OdfStyleFamily.TableCell.getName(),
                OdfToolkitUtil.getStyleFamilyName(this.cell));
    }

    @Test
    public void testParentStyleFamilyName() throws Exception {
        final OdfFileDom ownerDoc = PowerMock.createMock(OdfFileDom.class);
        // PLAY
        EasyMock.expect(ownerDoc.getDocument()).andReturn(null).anyTimes();
        EasyMock.expect(ownerDoc.getDoctype()).andReturn(null).anyTimes();
        PowerMock.expectPrivate(ownerDoc, "changed").anyTimes();

        PowerMock.replayAll();
        final OdfStyle style = new OdfStyle(ownerDoc) {
            @Override
            public OdfStyleBase getParentStyle() {
                return null;
            }

            @Override
            public String getStyleNameAttribute() {
                return "ok";
            }
        };

        this.cell.getOdfElement().getOrCreateUnqiueAutomaticStyle();
        this.cell.getOdfElement().setDocumentStyle(style);
        Assert.assertEquals("ok", OdfToolkitUtil.getParentStyleName(this.cell));
        PowerMock.verifyAll();
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeNull() {
        final Node n = new HTMLBodyElementImpl(new HTMLDocumentImpl(), "name");
        OdfToolkitUtil.getAttribute(n, "attr");
    }

    @Test
    public void testAttribute() {
        final HTMLBodyElement n = new HTMLBodyElementImpl(new HTMLDocumentImpl(), "body");
        n.setBgColor("color");
        Assert.assertEquals("color", OdfToolkitUtil.getAttribute(n, "bgcolor"));
    }

    @Test
    public void testFirstElement() {
        final HTMLDocumentImpl owner = new HTMLDocumentImpl();
        final HTMLBodyElement n = new HTMLBodyElementImpl(owner, "body");
        final HTMLDivElement d = new HTMLDivElementImpl(owner, "div");
        n.appendChild(d);
        Assert.assertNull(OdfToolkitUtil.getFirstElement(n, "ddd"));
        Assert.assertEquals(d, OdfToolkitUtil.getFirstElement(n, "div"));
    }

    @Test
    public void testDocumentStyle() throws Exception {
        final OdfStyle style = this.document.getDocumentStyles()
                .newStyle("gcs", OdfStyleFamily.TableCell);
        Assert.assertEquals(style,
                OdfToolkitUtil.getDocumentStyle(this.document, "gcs", OdfStyleFamily.TableCell));
    }
}