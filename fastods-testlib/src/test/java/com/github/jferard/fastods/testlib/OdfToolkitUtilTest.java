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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.Null;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStyle;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Table;
import org.w3c.dom.Node;

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
    public void testDocumentStyle() throws Exception {
        final OdfStyle style = this.document.getDocumentStyles().newStyle("gcs", OdfStyleFamily.TableCell);
        Assert.assertEquals(style, OdfToolkitUtil.getDocumentStyle(this.document, "gcs", OdfStyleFamily.TableCell));
    }
}