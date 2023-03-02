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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.testlib.ZipUTF8WriterMockHandler;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class StylesElementTest {
    @Test
    public void testWrite() throws IOException {
        final ZipUTF8WriterMockHandler handler = ZipUTF8WriterMockHandler.create();
        final ZipUTF8Writer writer = handler.getInstance(ZipUTF8Writer.class);
        PowerMock.resetAll();

        PowerMock.replayAll();

        final StylesElement stylesElement =
                new StylesElement(new StylesContainerImpl(Logger.getLogger("")));
        stylesElement.write(XMLUtil.create(), writer);

        PowerMock.verifyAll();
        Assert.assertEquals(Collections.singletonList("OdsEntry[path=styles.xml]"),
                new ArrayList<>(handler.getEntryNames()));
        DomTester.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<office:document-styles " +
                        "xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\" " +
                        "xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" " +
                        "xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" " +
                        "xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" " +
                        "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" " +
                        "xmlns:ooo=\"http://openoffice.org/2004/office\" " +
                        "xmlns:oooc=\"http://openoffice.org/2004/calc\" " +
                        "xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\" " +
                        "xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\" " +
                        "xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
                        "xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\" " +
                        "xmlns:math=\"http://www.w3.org/1998/Math/MathML\" " +
                        "xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\" " +
                        "xmlns:ooow=\"http://openoffice.org/2004/writer\" " +
                        "xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\" " +
                        "xmlns:presentation=\"urn:oasis:names:tc:opendocument:xmlns:presentation:1.0\" " +
                        "xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" " +
                        "xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" " +
                        "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" " +
                        "xmlns:dom=\"http://www.w3.org/2001/xml-events\" " +
                        "xmlns:dc=\"http://purl.org/dc/elements/1.1/\" " +
                        "office:version=\"1.2\">" +
                        "<office:font-face-decls>" +
                        "<style:font-face style:name=\"Liberation Sans\" " +
                        "svg:font-family=\"Liberation Sans\"/>" +
                        "</office:font-face-decls>" +
                        "<office:styles></office:styles>" +
                        "<office:automatic-styles>" +
                        "</office:automatic-styles>" +
                        "<office:master-styles>" +
                        "</office:master-styles>" +
                        "</office:document-styles>",
                handler.getEntryAsString("OdsEntry[path=styles.xml]"));
    }

    @Test
    public void testWriteFooterHeader() throws IOException {
        final ZipUTF8WriterMockHandler handler = ZipUTF8WriterMockHandler.create();
        final ZipUTF8Writer writer = handler.getInstance(ZipUTF8Writer.class);
        final MasterPageStyle ms1 = PowerMock.createMock(MasterPageStyle.class);
        final StylesContainerImpl stylesContainer = new StylesContainerImpl(Logger.getLogger(""));
        XMLUtil util = XMLUtil.create();

        PowerMock.resetAll();

        EasyMock.expect(ms1.getName()).andReturn("foo");
        EasyMock.expect(ms1.hasHeader()).andReturn(true);
        ms1.addEmbeddedStyles(stylesContainer);
        ms1.appendXMLToMasterStyle(util, writer);
        EasyMock.expect(ms1.hasFooter()).andReturn(true);
        PowerMock.replayAll();

        stylesContainer.addMasterPageStyle(ms1);
        final StylesElement stylesElement =
                new StylesElement(stylesContainer);
        stylesElement.write(util, writer);

        PowerMock.verifyAll();
        Assert.assertEquals(Collections.singletonList("OdsEntry[path=styles.xml]"),
                new ArrayList<>(handler.getEntryNames()));
        DomTester.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<office:document-styles " +
                        "xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\" " +
                        "xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" " +
                        "xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" " +
                        "xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" " +
                        "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" " +
                        "xmlns:ooo=\"http://openoffice.org/2004/office\" " +
                        "xmlns:oooc=\"http://openoffice.org/2004/calc\" " +
                        "xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\" " +
                        "xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\" " +
                        "xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
                        "xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\" " +
                        "xmlns:math=\"http://www.w3.org/1998/Math/MathML\" " +
                        "xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\" " +
                        "xmlns:ooow=\"http://openoffice.org/2004/writer\" " +
                        "xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\" " +
                        "xmlns:presentation=\"urn:oasis:names:tc:opendocument:xmlns:presentation:1.0\" " +
                        "xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" " +
                        "xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" " +
                        "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" " +
                        "xmlns:dom=\"http://www.w3.org/2001/xml-events\" " +
                        "xmlns:dc=\"http://purl.org/dc/elements/1.1/\" " +
                        "office:version=\"1.2\">" +
                        "<office:font-face-decls>" +
                        "<style:font-face style:name=\"Liberation Sans\" " +
                        "svg:font-family=\"Liberation Sans\"/>" +
                        "</office:font-face-decls>" +
                        "<office:styles>" +
                        "<style:style style:name=\"Header\" style:family=\"paragraph\" " +
                        "style:parent-style-name=\"Standard\" style:class=\"extra\">" +
                        "<style:paragraph-properties text:number-lines=\"false\" " +
                        "text:line-number=\"0\"/></style:style>" +
                        "<style:style style:name=\"Footer\" style:family=\"paragraph\" " +
                        "style:parent-style-name=\"Standard\" style:class=\"extra\">" +
                        "<style:paragraph-properties text:number-lines=\"false\" " +
                        "text:line-number=\"0\"/>" +
                        "</style:style>" +
                        "</office:styles>" +
                        "<office:automatic-styles>" +
                        "</office:automatic-styles>" +
                        "<office:master-styles>" +
                        "</office:master-styles>" +
                        "</office:document-styles>",
                handler.getEntryAsString("OdsEntry[path=styles.xml]"));
    }

    @Test
    public void testContainer() {
        final StylesContainerImpl stylesContainer = new StylesContainerImpl(Logger.getLogger(""));
        final StylesElement stylesElement =
                new StylesElement(stylesContainer);
        Assert.assertEquals(stylesContainer, stylesElement.getStyleTagsContainer());
    }
}