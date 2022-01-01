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

package com.github.jferard.fastods.util;

import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.testlib.DomTester;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Collections;

public class MacroLibraryTest {
    private MacroModule module;
    private XMLUtil util;

    @Before
    public void setUp() {
        this.module = PowerMock.createMock(MacroModule.class);
        this.util = XMLUtil.create();
    }

    @Test
    public void testIndexLine() throws IOException {
        final MacroLibrary lib =
                MacroLibrary.builder().name("ml").modules(this.module).readOnly().build();
        final StringBuilder sb = new StringBuilder();
        lib.appendIndexLine(XMLUtil.create(), sb);
        DomTester.assertEquals("<library:library library:name=\"ml\" library:link=\"false\"/>",
                sb.toString());
    }

    @Test
    public void testAdd() throws IOException {
        final OdsDocument document = PowerMock.createMock(OdsDocument.class);
        final MacroLibrary lib = new MacroLibrary("ml", false,
                Collections.singletonList(this.module));
        final Capture<StringBuilder> sb1 = Capture.newInstance();
        final Capture<byte[]> bs = Capture.newInstance();

        PowerMock.resetAll();
        document.addExtraDir("Basic/ml/");
        this.module.appendIndexLine(EasyMock.eq(this.util), EasyMock.capture(sb1));
        document.addExtraFile(EasyMock.eq("Basic/ml/script-lb.xml"), EasyMock.eq("text/xml"),
                EasyMock.capture(bs));
        this.module.add(this.util, document, "Basic/ml/");

        PowerMock.replayAll();
        lib.add(this.util, document);

        PowerMock.verifyAll();
        Assert.assertEquals(XMLUtil.XML_PROLOG + "<!DOCTYPE library:library " +
                "PUBLIC \"-//OpenOffice.org//DTD OfficeDocument 1.0//EN\" \"library" +
                ".dtd\"><library:library xmlns:library=\"http://openoffice.org/2000/library\" " +
                "library:name=\"ml\" library:readonly=\"false\" " +
                "library:passwordprotected=\"false\"></library:library>", sb1.toString());
        Assert.assertEquals(XMLUtil.XML_PROLOG + "<!DOCTYPE library:library " +
                        "PUBLIC \"-//OpenOffice.org//DTD OfficeDocument 1.0//EN\" \"library" +
                        ".dtd\"><library:library xmlns:library=\"http://openoffice" +
                        ".org/2000/library\" " +
                        "library:name=\"ml\" library:readonly=\"false\" " +
                        "library:passwordprotected=\"false\"></library:library>",
                new String(bs.getValue(), CharsetUtil.UTF_8));
    }
}