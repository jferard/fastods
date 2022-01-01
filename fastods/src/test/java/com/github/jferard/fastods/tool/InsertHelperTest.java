/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.DrawFrame;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.odselement.ManifestElement;
import com.github.jferard.fastods.style.GraphicStyle;
import com.github.jferard.fastods.util.CharsetUtil;
import com.github.jferard.fastods.util.SVGRectangle;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class InsertHelperTest {
    private InsertHelper ih;
    private OdsDocument document;
    private Table table;

    @Before
    public void setUp() {
        this.ih = InsertHelper.create();
        this.document = PowerMock.createMock(OdsDocument.class);
        this.table = PowerMock.createMock(Table.class);
    }

    @Test
    public void testInsertImageFile() throws IOException {
        final File source = File.createTempFile("image1", ".jpg");
        final SVGRectangle rectangle = SVGRectangle.cm(0, 1, 2, 3);
        final Capture<DrawFrame> df = EasyMock.newCapture();

        PowerMock.resetAll();
        this.document.addExtraFile("Pictures/dest1.foo", "image/jpeg", new byte[0]);
        this.table.addShape(EasyMock.capture(df));

        PowerMock.replayAll();
        this.ih.insertImage(this.document, this.table, "frame",
                source, "dest1.foo", rectangle);

        PowerMock.verifyAll();
        TestHelper.assertXMLEquals(
                "<draw:frame draw:name=\"frame\" draw:z-index=\"0\" svg:x=\"0cm\" svg:y=\"1cm\" svg:width=\"2cm\" svg:height=\"3cm\">" +
                        "<draw:image xlink:href=\"Pictures/dest1.foo\" xlink:type=\"simple\" xlink:show=\"embed\" xlink:actuate=\"onLoad\"/>" +
                        "</draw:frame>",
                df.getValue());
    }

    @Test
    public void testInsertImageStream() throws IOException {
        final SVGRectangle rectangle = SVGRectangle.cm(0, 1, 2, 3);
        final Capture<DrawFrame> df = EasyMock.newCapture();

        PowerMock.resetAll();
        this.document.addExtraFile("Pictures/dest1.foo", "image/foo", new byte[0]);
        this.table.addShape(EasyMock.capture(df));

        PowerMock.replayAll();
        this.ih.insertImage(this.document, this.table, "frame",
                new ByteArrayInputStream(new byte[0]), "dest1.foo", rectangle);

        PowerMock.verifyAll();
        TestHelper.assertXMLEquals(
                "<draw:frame draw:name=\"frame\" draw:z-index=\"0\" svg:x=\"0cm\" svg:y=\"1cm\" svg:width=\"2cm\" svg:height=\"3cm\">" +
                        "<draw:image xlink:href=\"Pictures/dest1.foo\" xlink:type=\"simple\" xlink:show=\"embed\" xlink:actuate=\"onLoad\"/>" +
                        "</draw:frame>",
                df.getValue());
    }

    @Test
    public void testCreateDrawFillImage() throws IOException {
        PowerMock.resetAll();
        this.document.addExtraFile("ref", "image/ref", new byte[0]);

        PowerMock.replayAll();
        this.ih.createDrawFillImage(this.document, new ByteArrayInputStream(new byte[0]), "name",
                "ref");

        PowerMock.verifyAll();
    }

    @Test
    public void testInsertObject() throws IOException {
        final byte[] content = (XMLUtil.XML_PROLOG + "\n<root/>").getBytes(CharsetUtil.UTF_8);
        final byte[] bs = this.createAlmostEmptyZipFile(content);

        final SVGRectangle rectangle = SVGRectangle.cm(0, 1, 2, 3);
        final Capture<DrawFrame> df = EasyMock.newCapture();

        PowerMock.resetAll();
        this.document.addExtraFile("object/content.xml", null, content);
        this.document.addExtraObject("object", "media/type", "1.0");
        this.table.addShape(EasyMock.capture(df));

        PowerMock.replayAll();
        this.ih.insertObject(this.document, this.table, "frame", "object", "media/type", "1.0",
                new ByteArrayInputStream(bs), rectangle,
                GraphicStyle.builder("gs").build());

        PowerMock.verifyAll();
        TestHelper.assertXMLEquals(
                "<draw:frame draw:name=\"frame\" draw:z-index=\"0\" draw:style-name=\"gs\" svg:x=\"0cm\" svg:y=\"1cm\" svg:width=\"2cm\" svg:height=\"3cm\">" +
                        "<draw:object xlink:href=\"./object\" draw:notify-on-update-of-ranges=\"\" xlink:type=\"simple\" xlink:show=\"embed\" xlink:actuate=\"onLoad\"/>" +
                        "</draw:frame>",
                df.getValue());
    }

    private byte[] createAlmostEmptyZipFile(final byte[] content) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        final ZipOutputStream zos = new ZipOutputStream(bos);
        zos.setMethod(ZipOutputStream.DEFLATED);
        zos.setLevel(0);
        for (final String name : Arrays
                .asList(ManifestElement.META_INF_MANIFEST_XML, "mimetype", "Thumbnails",
                        "content.xml")) {
            final ZipEntry e = new ZipEntry(name);
            zos.putNextEntry(e);
            zos.write(content);
        }
        zos.finish();
        zos.close();
        return bos.toByteArray();
    }
}