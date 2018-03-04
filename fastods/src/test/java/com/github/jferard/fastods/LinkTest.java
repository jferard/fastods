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

import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 */
public class LinkTest {
    private TextStyle ts;

    @Before
    public void setUp() {
        this.ts = TextProperties.builder().buildStyle("test");
    }

    @Test
    public final void testTable() throws IOException {
        final Table table = PowerMock.createMock(Table.class);

        // PLAY
        PowerMock.resetAll();
        EasyMock.expect(table.getName()).andReturn("t");

        PowerMock.replayAll();
        final Link link = Link.create("table", this.ts, table);
        TestHelper.assertXMLEquals(
                "<text:a text:style-name=\"test\" xlink:href=\"#t\" xlink:type=\"simple\">table</text:a>", link);

        PowerMock.verifyAll();
    }

    @Test
    public final void testURL() throws IOException {
        final Link link = Link.create("url", this.ts, new URL("https://www.github.com/jferard/fastods"));
        TestHelper.assertXMLEquals(
                "<text:a text:style-name=\"test\" xlink:href=\"https://www.github.com/jferard/fastods\" " + "xlink" +
                        ":type=\"simple\">url</text:a>",
                link);
    }

    @Test
    public final void testFile() throws IOException {
        final File f = new File("generated_files", "fastods_50_5.ods");
        final Link link = Link.create("file", this.ts, f);
        TestHelper.assertXMLEquals("<text:a text:style-name=\"test\" xlink:href=\"" + f.toURI()
                .toString() + "\" xlink:type=\"simple\">file</text:a>", link);
    }
}