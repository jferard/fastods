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
package com.github.jferard.fastods.style;

import com.github.jferard.fastods.SimpleColor;
import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.util.SimpleLength;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TableCellStyleTest {
    private XMLUtil util;

    @Before
    public final void setUp() {
        this.util = XMLUtil.create();
    }

    @Test
    public final void testAllBorders() throws IOException {
        final TableCellStyle tcs = TableCellStyle.builder("test")
                .borderAll(SimpleLength.pt(1.0), SimpleColor.AQUA, BorderAttribute.Style.DOUBLE)
                .build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\">" +
                "<style:table-cell-properties fo:border=\"1pt double #00ffff\"/>" +
                "</style:style>", tcs);
    }

    @Test
    public final void testAllMargins() throws IOException {
        final TableCellStyle tcs = TableCellStyle.builder("tcs").allMargins(SimpleLength.pt(10.0))
                .build();
        TestHelper.assertXMLEquals("<style:style style:name=\"tcs\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\">" +
                "<style:paragraph-properties fo:margin=\"10pt\"/>" + "</style:style>", tcs);
    }

    @Test
    public final void testBorders() throws IOException {
        final TableCellStyle tcs = TableCellStyle.builder("test")
                .borderTop(SimpleLength.pt(1.0), SimpleColor.AQUA, BorderAttribute.Style.DOUBLE)
                .borderRight(SimpleLength.pt(2.0), SimpleColor.BEIGE, BorderAttribute.Style.SOLID)
                .borderBottom(SimpleLength.pt(3.0), SimpleColor.CADETBLUE,
                        BorderAttribute.Style.DOUBLE)
                .borderLeft(SimpleLength.pt(4.0), SimpleColor.DARKBLUE,
                        BorderAttribute.Style.DOUBLE).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\">" +
                "<style:table-cell-properties fo:border-bottom=\"3pt double #5f9ea0\" " +
                "fo:border-left=\"4pt " +
                "double #00008b\" fo:border-right=\"2pt solid #f5f5dc\" " +
                "fo:border-top=\"1pt double " + "#00ffff\"/>" + "</style:style>", tcs);
    }

    @Test
    public final void testMargins() throws IOException {
        final TableCellStyle tcs = TableCellStyle.builder("tcs").marginTop(SimpleLength.pt(10.0))
                .marginRight(SimpleLength.pt(11.0)).marginBottom(SimpleLength.pt(12.0))
                .marginLeft(SimpleLength.pt(13.0)).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"tcs\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\">" +
                "<style:paragraph-properties fo:margin-bottom=\"12pt\" " +
                "fo:margin-left=\"13pt\" " + "fo:margin-right=\"11pt\" fo:margin-top=\"10pt\"/>" +
                "</style:style>", tcs);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testNullName() {
        TableCellStyle.builder(null);
    }

    @Test
    public void testDefaultCellStyle() throws IOException {
        TestHelper.assertXMLEquals(
                "<style:style style:name=\"Default\" style:family=\"table-cell\">" +
                        "<style:table-cell-properties style:vertical-align=\"top\"/>" +
                        "<style:text-properties style:font-name=\"Liberation Sans\"/>" +
                        "<style:paragraph-properties fo:text-align=\"start\" fo:margin=\"0mm\"/>" +
                        "</style:style>", TableCellStyle.DEFAULT_CELL_STYLE);
    }

    @Test
    public final void testGetters() {
        StyleTestHelper.testGetters(TableCellStyle.builder("test"));
    }

    @Test
    public final void testFontName() throws IOException {
        final TableCellStyle symbolStyle = TableCellStyle.builder("symbol-cell")
                .fontName(LOFonts.OPENSYMBOL).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"symbol-cell\" " +
                "style:family=\"table-cell\" style:parent-style-name=\"Default\"><style:text" +
                "-properties style:font-name=\"OpenSymbol\"/></style:style>", symbolStyle);
    }

    @Test
    public final void testFontNormal() throws IOException {
        final TableCellStyle s = TableCellStyle.builder("test").fontStyleNormal().fontWeightNormal()
                .build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\"><style:text-properties " +
                "fo:font-weight=\"normal\" style:font-weight-asian=\"normal\" " +
                "style:font-weight-complex=\"normal\" fo:font-style=\"normal\" " +
                "style:font-style-asian=\"normal\" " +
                "style:font-style-complex=\"normal\"/></style:style>", s);
    }

    @Test
    public final void testFontUnderline() throws IOException {
        final TableCellStyle s = TableCellStyle.builder("test")
                .fontUnderlineStyle(TextProperties.Underline.DASH)
                .fontUnderlineColor(SimpleColor.RED).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\"><style:text-properties " +
                "style:text-underline-style=\"dash\" style:text-underline-width=\"auto\" " +
                "style:text-underline-color=\"#ff0000\"/></style:style>", s);
    }

    @Test
    public final void testFontSizePercentage() throws IOException {
        final TableCellStyle s = TableCellStyle.builder("test").fontSizePercentage(125.9).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\"><style:text-properties " +
                "fo:font-size=\"125.9%\" style:font-size-asian=\"125.9%\" " +
                "style:font-size-complex=\"125.9%\"/></style:style>", s);
    }

    @Test
    public final void testFontColor() throws IOException {
        final TableCellStyle s = TableCellStyle.builder("test").fontColor(SimpleColor.GRAY).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\"><style:text-properties " +
                "fo:color=\"#808080\"/></style:style>", s);
    }

    @Test
    public final void testFontSize() throws IOException {
        final TableCellStyle s = TableCellStyle.builder("test").fontSize(SimpleLength.pt(12.5))
                .build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\"><style:text-properties " +
                "fo:font-size=\"12.5pt\" style:font-size-asian=\"12.5pt\" " +
                "style:font-size-complex=\"12.5pt\"/></style:style>", s);
    }
}
