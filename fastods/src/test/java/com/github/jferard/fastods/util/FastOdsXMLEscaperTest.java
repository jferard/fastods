/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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
package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FastOdsXMLEscaperTest {
    private XMLEscaper escaper;

    @Before
    public void setUp() {
        this.escaper = FastOdsXMLEscaper.create();
    }

    @Test
    public final void testAttrAmp() {
        this.assertEqualsToAttrEscaped("&amp;0", "&0");
        this.assertEqualsToAttrEscaped("&amp; 0", "& 0");
    }

    @Test
    public final void testAttrApos() {
        this.assertEqualsToAttrEscaped("&apos; 0", "' 0");
    }

    @Test
    public final void testAttrBuffer() {
        final XMLEscaper escaper = new FastOdsXMLEscaper(124);
        final StringBuilder sb1 = new StringBuilder(8 * (2 << 5));
        final StringBuilder sb2 = new StringBuilder(8 * (2 << 5));
        sb1.append("'ae< >");
        sb2.append("&apos;ae&lt; &gt;");
        for (int i = 0; i < 5; i++) {
            sb1.append(sb1.toString()).append(sb1.toString());
            sb2.append(sb2.toString()).append(sb2.toString());
        }

        Assert.assertEquals(sb2.toString(), escaper.escapeXMLAttribute(sb1.toString()));
    }

    @Test
    public final void testAttrExpression() {
        this.assertEqualsToAttrEscaped("w&lt;&amp; &apos; d&quot;gfgh &gt;", "w<& ' d\"gfgh >");
    }

    @Test
    public final void testAttrLt() {
        this.assertEqualsToAttrEscaped("&lt; 0", "< 0");
    }

    @Test
    public final void testAttrNullString() {
        Assert.assertNull(this.escaper.escapeXMLAttribute(null));
    }

    @Test
    public final void testAttrOther() {
        this.assertEqualsToAttrEscaped("&#x9;&#xA;&#xD;\\uFFFD", "\t\n\r\b");
    }

    @Test
    public final void testBasicChars() {
        this.assertEqualsToAttrEscaped("abcde", "abcde");
        this.assertEqualsToContentEscaped("abcde", "abcde");
    }

    @Test
    public final void testContentAmp() {
        this.assertEqualsToContentEscaped("&amp; 0", "& 0");
    }

    @Test
    public final void testContentBuffer() {
        final XMLEscaper escaper = new FastOdsXMLEscaper(124);
        final StringBuilder sb1 = new StringBuilder(8 * (2 << 5));
        final StringBuilder sb2 = new StringBuilder(8 * (2 << 5));
        sb1.append("'ae< >");
        sb2.append("'ae&lt; &gt;");
        for (int i = 0; i < 5; i++) {
            sb1.append(sb1.toString()).append(sb1.toString());
            sb2.append(sb2.toString()).append(sb2.toString());
        }

        Assert.assertEquals(sb2.toString(), escaper.escapeXMLContent(sb1.toString()));
    }

    @Test
    public final void testContentBuffer2() {
        final XMLEscaper escaper = new FastOdsXMLEscaper(5);
        final String expected = "<ae";
        final String actual = "&lt;ae";
        Assert.assertEquals(actual, escaper.escapeXMLContent(expected));
    }

    @Test
    public final void testAttrBuffer2() {
        final XMLEscaper escaper = new FastOdsXMLEscaper(5);
        final String actual = "<ae";
        final String expected = "&lt;ae";
        Assert.assertEquals(expected, escaper.escapeXMLAttribute(actual));
    }

    @Test
    public final void testContentExpression() {
        this.assertEqualsToContentEscaped("w&lt;&amp; ' d\"gfgh &gt;", "w<& ' d\"gfgh >");
    }

    @Test
    public final void testContentLt() {
        this.assertEqualsToContentEscaped("&lt;", "<");
    }

    @Test
    public final void testContentNullString() {
        Assert.assertNull(this.escaper.escapeXMLContent(null));
    }

    @Test
    public final void testContentOther() {
        this.assertEqualsToContentEscaped("\t\n\r\\uFFFD", "\t\n\r\b");
    }

    @Test
    public final void testDifferentCaches() {
        final String actualToEscape = "\t\n\r\b";
        final String expectedAttr = "&#x9;&#xA;&#xD;\\uFFFD";
        final String expectedContent = "\t\n\r\\uFFFD";

        this.assertEqualsToAttrEscaped(expectedAttr, actualToEscape);
        this.assertEqualsToAttrEscaped(expectedAttr, actualToEscape);
        this.assertEqualsToContentEscaped(expectedContent, actualToEscape);
        this.assertEqualsToContentEscaped(expectedContent, actualToEscape);
    }

    @Test
    public final void testEmptyString() {
        this.assertEqualsToAttrEscaped("", "");
        this.assertEqualsToContentEscaped("", "");
    }

    @Test
    public final void testFinalChars() {
        this.assertEqualsToAttrEscaped("&apos;abcde", "'abcde");
        this.assertEqualsToContentEscaped("'abcde", "'abcde");
    }

    private void assertEqualsToAttrEscaped(final String expected, final String actualToEscape) {
        Assert.assertEquals(expected, this.escaper.escapeXMLAttribute(actualToEscape));
    }

    private void assertEqualsToContentEscaped(final String expected, final String actualToEscape) {
        Assert.assertEquals(expected, this.escaper.escapeXMLContent(actualToEscape));
    }
}
