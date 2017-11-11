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
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by jferard on 20/05/17.
 */
public class TextBuilderTest {
    private XMLUtil util;
    private TextStyle ts;
    private String tsXML;

    @Before
    public void setUp() throws IOException {
        this.util = XMLUtil.create();
        this.ts = TextProperties.builder().fontName("fn").fontColor("fc").buildStyle("ts");
        final StringBuilder sb = new StringBuilder();
        this.ts.appendXML(this.util, sb);
        this.tsXML = sb.toString();
    }

    @Test
    public void parContent() throws Exception {
        final Text t = TextBuilder.create().parContent("a").build();
        final String c = this.getXMLContent(t);
        DomTester.assertEquals("<text:p>a</text:p>", c);
    }

    private String getXMLContent(final Text t) throws IOException {
        final StringBuilder sb = new StringBuilder();
        t.appendXMLContent(this.util, sb);
        return sb.toString();
    }

    @Test
    public void parStyledContent() throws Exception {
        final Text t = TextBuilder.create().parStyledContent("a", this.ts).build();
        final String c = this.getXMLContent(t);
        DomTester.assertEquals("<text:p><text:span text:style-name=\"ts\">a</text:span></text:p>", c);
    }

    @Test
    public void linkRef() throws Exception {
        final Text t = TextBuilder.create().par().link("a", "ref").build();
        final String c = this.getXMLContent(t);
        DomTester.assertEquals("<text:p><text:a xlink:href=\"#ref\" xlink:type=\"simple\">a</text:a></text:p>", c);
    }

    @Test
    public void styledLinkRef() throws Exception {
        final Text t = TextBuilder.create().par().styledLink("a", "ref", this.ts).build();
        final String c = this.getXMLContent(t);
        DomTester.assertEquals("<text:p><text:a text:style-name=\"ts\" xlink:href=\"#ref\" xlink:type=\"simple\">a</text:a></text:p>", c);
    }

    @Test
    public void linkURL() throws Exception {
        final Text t = TextBuilder.create().par().link("a", new URL("http://url")).build();
        final String c = this.getXMLContent(t);
        DomTester.assertEquals("<text:p><text:a xlink:href=\"http://url\" xlink:type=\"simple\">a</text:a></text:p>", c);
    }

    @Test
    public void styledLinkURL() throws Exception {
        final Text t = TextBuilder.create().par().styledLink("a", new URL("http://url"), this.ts).build();
        final String c = this.getXMLContent(t);
        DomTester.assertEquals("<text:p><text:a text:style-name=\"ts\" xlink:href=\"http://url\" xlink:type=\"simple\">a</text:a></text:p>", c);
    }

    @Test
    public void linkFile() throws Exception {
        final File f = new File("f");
        final Text t = TextBuilder.create().par().link("a", f).build();
        final String c = this.getXMLContent(t);
        DomTester.assertEquals("<text:p><text:a xlink:href=\"" + f.toURI().toString() + "\" xlink:type=\"simple\">a</text:a></text:p>", c);
    }

    @Test
    public void styledLinkFile() throws Exception {
        final File f = new File("f");
        final Text t = TextBuilder.create().par().styledLink("a", f, this.ts).build();
        final String c = this.getXMLContent(t);
        DomTester.assertEquals("<text:p><text:a text:style-name=\"ts\" xlink:href=\"" + f.toURI().toString() + "\" xlink:type=\"simple\">a</text:a></text:p>", c);
    }

    @Test
    public void linkTable() throws Exception {
        final Table table = Table.create(null, null,null,null,null,"n", 0, 0);
        final Text t = TextBuilder.create().par().link("a", table).build();
        final String c = this.getXMLContent(t);
        Assert.assertEquals("n", table.getName());
        DomTester.assertEquals("<text:p><text:a xlink:href=\"#n\" xlink:type=\"simple\">a</text:a></text:p>", c);
    }

    @Test
    public void styledLinkTable() throws Exception {
        final Table table = Table.create(null, null,null,null,null,"n", 0, 0);
        final Text t = TextBuilder.create().par().styledLink("a", table, this.ts).build();
        final String c = this.getXMLContent(t);
        DomTester.assertEquals("<text:p><text:a text:style-name=\"ts\" xlink:href=\"#n\" xlink:type=\"simple\">a</text:a></text:p>", c);
    }


}