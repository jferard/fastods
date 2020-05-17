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

package com.github.jferard.fastods.util;

import com.github.jferard.fastods.OdsDocument;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class MacroLibraryContainerTest {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    @Test
    public void test() throws IOException {
        final OdsDocument document = PowerMock.createMock(OdsDocument.class);
        final MacroLibrary library = PowerMock.createMock(MacroLibrary.class);
        final MacroLibraryContainer container =
                new MacroLibraryContainer(XMLUtil.create(), Arrays.asList(library));
        final Capture<StringBuilder> sb1 = Capture.newInstance();
        final Capture<byte[]> bs = Capture.newInstance();

        PowerMock.resetAll();
        document.addExtraDir("Basic/");
        library.appendIndexLine(EasyMock.isA(XMLUtil.class), EasyMock.capture(sb1));
        document.addExtraFile(EasyMock.eq("Basic/script-lc.xml"), EasyMock.eq("text/xml"),
                EasyMock.capture(bs));
        library.add(EasyMock.isA(XMLUtil.class), EasyMock.eq(document));

        PowerMock.replayAll();
        container.add(document);

        PowerMock.verifyAll();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<!DOCTYPE library:libraries PUBLIC \"-//OpenOffice.org//DTD " +
                        "OfficeDocument 1" +
                        ".0//EN\" \"libraries.dtd\">\n" +
                        "<library:libraries xmlns:library=\"http://openoffice.org/2000/library\" " +
                        "xmlns:xlink=\"http://www.w3.org/1999/xlink\"></library:libraries>",
                sb1.toString());
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<!DOCTYPE library:libraries PUBLIC \"-//OpenOffice.org//DTD " +
                        "OfficeDocument 1" +
                        ".0//EN\" \"libraries.dtd\">\n" +
                        "<library:libraries xmlns:library=\"http://openoffice.org/2000/library\" " +
                        "xmlns:xlink=\"http://www.w3.org/1999/xlink\"></library:libraries>",
                new String(bs.getValue(), UTF_8));
    }
}