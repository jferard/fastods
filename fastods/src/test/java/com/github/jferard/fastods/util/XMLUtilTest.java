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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class XMLUtilTest {
    private XMLUtil xu;
    private StringBuilder sb;

    @Before
    public void setUp() {
        this.xu = XMLUtil.create();
        this.sb = new StringBuilder();
    }

    @Test
    public void testBooleanAttr() throws IOException {
        this.xu.appendAttribute(this.sb, "attr", true);
        Assert.assertEquals(" attr=\"true\"", this.sb.toString());
    }

    @Test
    public void testIntAttr() throws IOException {
        this.xu.appendAttribute(this.sb, "attr", 7);
        Assert.assertEquals(" attr=\"7\"", this.sb.toString());
    }

    @Test
    public void testStringAttr() throws IOException {
        this.xu.appendAttribute(this.sb, "attr", "value&");
        Assert.assertEquals(" attr=\"value&\"", this.sb.toString());
    }

    @Test
    public void testEscStringAttr() throws IOException {
        this.xu.appendEAttribute(this.sb, "attr", "&");
        Assert.assertEquals(" attr=\"&amp;\"", this.sb.toString());
    }

    @Test
    public void testStringAttrs() throws IOException {
        this.xu.appendAttribute(this.sb, "attr", Arrays.asList("value1", "value2", "value2"), " ");
        Assert.assertEquals(" attr=\"value1 value2 value2\"", this.sb.toString());
    }

    @Test
    public void testEscapeAttr() {
        Assert.assertEquals("j&amp;v", this.xu.escapeXMLAttribute("j&v"));
        Assert.assertEquals("m&#xA;l&#xA;j&#xA;t", this.xu.escapeXMLAttribute("m\nl\nj\nt"));
    }

    @Test
    public void testAppendTag() throws IOException {
        this.xu.appendTag(this.sb, "tag", "content");
        Assert.assertEquals("<tag>content</tag>", this.sb.toString());
    }

    @Test
    public void testEscapeContent() {
        Assert.assertEquals("j&amp;v", this.xu.escapeXMLContent("j&v"));
        Assert.assertEquals("m\nl\nj\nt", this.xu.escapeXMLContent("m\nl\nj\nt"));
    }

    @Test
    public void testFormatIntervalNegValue() {
        Assert.assertThrows(IllegalArgumentException.class,
                () -> this.xu.formatTimeInterval(-1, 0, 0, 0, 0, 0));
    }

    // XML Schema 3.2.6.1 Lexical representation examples
    @Test
    public void testFormatInterval() {
        Assert.assertEquals("P1Y2M3DT10H30M", this.xu.formatTimeInterval(1, 2, 3, 10, 30, 0));
        Assert.assertEquals("-P120D", this.xu.formatNegTimeInterval(0, 0, 120, 0, 0, 0));
        Assert.assertEquals("P1347Y", this.xu.formatTimeInterval(1347, 0, 0, 0, 0, 0));
        Assert.assertEquals("P1347M", this.xu.formatTimeInterval(0, 1347, 0, 0, 0, 0));
        Assert.assertEquals("P1Y2MT2H", this.xu.formatTimeInterval(1, 2, 0, 2, 0, 0));
        Assert.assertEquals("-P1Y2M", this.xu.formatNegTimeInterval(1, 2, 0, 0, 0, 0));
    }

    // Other examples
    @Test
    public void testFormatIntervalOther() {
        Assert.assertEquals("P0Y", this.xu.formatTimeInterval(0, 0, 0, 0, 0, 0));
        Assert.assertEquals("P1Y2M3D", this.xu.formatTimeInterval(1, 2, 3, 0, 0, 0));
        Assert.assertEquals("PT4H5M6.0S", this.xu.formatTimeInterval(0, 0, 0, 4, 5, 6.0));
    }

    @Test
    public void testFormatIntervalMillis() {
        Assert.assertEquals("PT123456.789S", this.xu.formatTimeInterval(123456789));
    }
}