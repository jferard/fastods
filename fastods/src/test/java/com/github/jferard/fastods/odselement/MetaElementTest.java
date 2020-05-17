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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.testlib.ZipUTF8WriterMockHandler;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

public class MetaElementTest {
    @Test
    public void testEmpty() throws IOException {
        final MetaElement element = MetaElement.builder().creator("creator").date("date")
                .language(Locale.US.getLanguage()).build();
        final String string = this.getString(element);
        DomTester.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<office:document-meta " +
                        "xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" " +
                        "xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
                        "xmlns:dc=\"http://purl.org/dc/elements/1.1/\" " +
                        "xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" " +
                        "xmlns:ooo=\"http://openoffice.org/2004/office\" " +
                        "office:version=\"1.2\"><office:meta>" +
                        "<dc:creator>creator</dc:creator>" + "<dc:date>date</dc:date>" +
                        "<dc:language>en</dc:language>" +
                        "<meta:generator>FastOds/0.7.3</meta:generator>" +
                        "<meta:editing-cycles>1</meta:editing-cycles>" +
                        "<meta:editing-duration>PT1M00S</meta:editing-duration>" +
                        "</office:meta></office:document-meta>", string);
    }

    @Test
    public void testFull() throws IOException {
        final MetaElement element =
                MetaElement.builder().creator("creator").date("date").description("description")
                        .language("l").editingCycles("10").editingDuration("duration")
                        .initialCreator("init").keyWord("kw1", "kw3").keyWord("kw2")
                        .subject("subject").title("title").userDefinedBoolean("b", false)
                        .userDefinedDate("d", new Date(0)).userDefinedFloat("f", 10)
                        .userDefinedString("s1", "s1").userDefinedString("s2", "s2")
                        .userDefinedTime("t", new Date(0)).build();
        final String string = this.getString(element);
        DomTester.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<office:document-meta " +
                        "xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" " +
                        "xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
                        "xmlns:dc=\"http://purl.org/dc/elements/1.1/\" " +
                        "xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" " +
                        "xmlns:ooo=\"http://openoffice.org/2004/office\" " +
                        "office:version=\"1.2\">" + "<office:meta>" +
                        "<dc:creator>creator</dc:creator><dc:date>date</dc:date>" +
                        "<dc:description>description</dc:description>" +
                        "<dc:language>l</dc:language><dc:subject>subject</dc:subject>" +
                        "<dc:title>title</dc:title>" +
                        "<meta:generator>FastOds/0.7.3</meta:generator>" +
                        "<meta:editing-cycles>10</meta:editing-cycles>" +
                        "<meta:editing-duration>duration</meta:editing-duration>" +
                        "<meta:initial-creator>init</meta:initial-creator>" +
                        "<meta:keyword>kw1</meta:keyword><meta:keyword>kw3</meta:keyword>" +
                        "<meta:keyword>kw2</meta:keyword>" +
                        "<meta:user-defined meta:name=\"b\" meta:type=\"boolean\">" +
                        "false</meta:user-defined>" +
                        "<meta:user-defined meta:name=\"d\" meta:type=\"date\">" +
                        "1970-01-01</meta:user-defined>" +
                        "<meta:user-defined meta:name=\"f\" meta:type=\"float\">" +
                        "10</meta:user-defined>" +
                        "<meta:user-defined meta:name=\"s1\" meta:type=\"string\">" +
                        "s1</meta:user-defined>" +
                        "<meta:user-defined meta:name=\"s2\" meta:type=\"string\">" +
                        "s2</meta:user-defined>" +
                        "<meta:user-defined meta:name=\"t\" meta:type=\"time\">" +
                        "00:00:00</meta:user-defined>" + "</office:meta></office:document-meta>",
                string);
    }

    private String getString(final MetaElement element) throws IOException {
        final ZipUTF8WriterMockHandler handler = ZipUTF8WriterMockHandler.create();
        final ZipUTF8Writer instance = handler.getInstance(ZipUTF8Writer.class);
        element.write(XMLUtil.create(), instance);
        return handler.getEntryAsString("meta.xml");
    }
}