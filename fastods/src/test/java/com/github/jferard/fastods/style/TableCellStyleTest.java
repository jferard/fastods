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
package com.github.jferard.fastods.style;

import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.attribute.Angle;
import com.github.jferard.fastods.attribute.BorderAttribute;
import com.github.jferard.fastods.attribute.BorderStyle;
import com.github.jferard.fastods.attribute.CellAlign;
import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.datastyle.FloatStyle;
import com.github.jferard.fastods.datastyle.FloatStyleBuilder;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Locale;

public class TableCellStyleTest {
    private XMLUtil util;

    @Before
    public final void setUp() {
        this.util = XMLUtil.create();
    }

    @Test
    public final void testAllBorders() throws IOException {
        final TableCellStyle tcs = TableCellStyle.builder("test")
                .borderAll(SimpleLength.pt(1.0), SimpleColor.AQUA, BorderStyle.DOUBLE).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\">" +
                "<style:table-cell-properties fo:border=\"1pt double #00ffff\"/>" +
                "</style:style>", tcs);
    }

    @Test
    public final void testAllMargins() throws IOException {
        final TableCellStyle tcs =
                TableCellStyle.builder("tcs").allMargins(SimpleLength.pt(10.0)).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"tcs\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\">" +
                "<style:paragraph-properties fo:margin=\"10pt\"/>" + "</style:style>", tcs);
    }

    @Test
    public final void testBorders() throws IOException {
        final TableCellStyle tcs = TableCellStyle.builder("test")
                .borderTop(SimpleLength.pt(1.0), SimpleColor.AQUA, BorderStyle.DOUBLE)
                .borderRight(SimpleLength.pt(2.0), SimpleColor.BEIGE, BorderStyle.SOLID)
                .borderBottom(SimpleLength.pt(3.0), SimpleColor.CADETBLUE, BorderStyle.DOUBLE)
                .borderLeft(SimpleLength.pt(4.0), SimpleColor.DARKBLUE, BorderStyle.DOUBLE).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\">" +
                "<style:table-cell-properties fo:border-bottom=\"3pt double #5f9ea0\" " +
                "fo:border-left=\"4pt " +
                "double #00008b\" fo:border-right=\"2pt solid #f5f5dc\" " +
                "fo:border-top=\"1pt double " + "#00ffff\"/>" + "</style:style>", tcs);
    }

    @Test
    public final void testSimpleBorders() throws IOException {
        final BorderAttribute borderAttribute =
                new BorderAttribute(SimpleLength.pt(1.0), SimpleColor.AQUA, BorderStyle.DOUBLE);
        final TableCellStyle tcs = TableCellStyle.builder("test").borderTop(borderAttribute)
                .borderBottom(borderAttribute).borderLeft(borderAttribute)
                .borderRight(borderAttribute).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\"><style:table-cell-properties fo:border=\"1pt" +
                " double #00ffff\"/></style:style>", tcs);
    }

    @Test
    public final void testSimpleBordersAll() throws IOException {
        final BorderAttribute borderAttributeTop =
                new BorderAttribute(SimpleLength.pt(1.0), SimpleColor.AQUA, BorderStyle.DOUBLE);
        final BorderAttribute borderAttribute =
                new BorderAttribute(SimpleLength.pt(2.0), SimpleColor.BLACK, BorderStyle.DOUBLE);
        final TableCellStyle tcs = TableCellStyle.builder("test").borderTop(borderAttributeTop)
                .borderAll(borderAttribute).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\"><style:table-cell-properties fo:border=\"2pt" +
                " double #000000\" fo:border-top=\"1pt double #00ffff\"/></style:style>", tcs);
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

    @Test
    public final void testNullName() {
        Assert.assertThrows(IllegalArgumentException.class, () -> TableCellStyle.builder(null));
    }

    @Test
    public void testDefaultCellStyle() throws IOException {
        TestHelper.assertXMLEquals(
                "<style:style style:name=\"Default\" style:family=\"table-cell\">" +
                        "<style:table-cell-properties style:vertical-align=\"top\"/>" +
                        "<style:paragraph-properties fo:margin=\"0cm\"/>" +
                        "<style:text-properties style:font-name=\"Liberation Sans\"/>" +
                        "</style:style>",
                TableCellStyle.DEFAULT_CELL_STYLE);
    }

    @Test
    public final void testGetters() {
        StyleTestHelper.testGetters(TableCellStyle.builder("test"));
    }

    @Test
    public final void testFontName() throws IOException {
        final TableCellStyle symbolStyle =
                TableCellStyle.builder("symbol-cell").fontName(LOFonts.OPENSYMBOL).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"symbol-cell\" " +
                "style:family=\"table-cell\" style:parent-style-name=\"Default\"><style:text" +
                "-properties style:font-name=\"OpenSymbol\"/></style:style>", symbolStyle);
    }

    @Test
    public final void testFontNormal() throws IOException {
        final TableCellStyle s =
                TableCellStyle.builder("test").fontStyleNormal().fontWeightNormal().build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\"><style:text-properties " +
                "fo:font-weight=\"normal\" style:font-weight-asian=\"normal\" " +
                "style:font-weight-complex=\"normal\" fo:font-style=\"normal\" " +
                "style:font-style-asian=\"normal\" " +
                "style:font-style-complex=\"normal\"/></style:style>", s);
    }

    @Test
    public final void testFontUnderline() throws IOException {
        final TableCellStyle s =
                TableCellStyle.builder("test").fontUnderlineStyle(TextProperties.Underline.DASH)
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
        final TableCellStyle s =
                TableCellStyle.builder("test").fontSize(SimpleLength.pt(12.5)).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\"><style:text-properties " +
                "fo:font-size=\"12.5pt\" style:font-size-asian=\"12.5pt\" " +
                "style:font-size-complex=\"12.5pt\"/></style:style>", s);
    }

    @Test
    public final void testToBuilder() throws IOException {
        final TableCellStyle style =
                TableCellStyle.DEFAULT_CELL_STYLE.toBuilder("Default with red bg")
                        .backgroundColor(SimpleColor.RED).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"Default with red bg\" " +
                "style:family=\"table-cell\">" +
                "<style:table-cell-properties fo:background-color=\"#ff0000\" " +
                "style:vertical-align=\"top\"/>" +
                "<style:paragraph-properties fo:margin=\"0cm\"/>" +
                "<style:text-properties style:font-name=\"Liberation Sans\"/>" +
                "</style:style>", style);
    }

    @Test
    public final void testRotating() throws IOException {
        final TableCellStyle style =
                TableCellStyle.builder("test").textRotating(Angle.deg(35)).build();
        TestHelper.assertXMLEquals(
                "<style:style style:name=\"test\" style:family=\"table-cell\" " +
                        "style:parent-style-name=\"Default\"><style:table-cell-properties " +
                        "style:rotation-angle=\"35\"/>" +
                        "</style:style>",
                style);
    }

    @Test
    public final void testRealName() {
        final TableCellStyle style = TableCellStyle.builder("foo-_-bar").build();
        Assert.assertEquals("foo", style.getRealName());
        final TableCellStyle style2 = TableCellStyle.builder("foo_bar").build();
        Assert.assertEquals("foo_bar", style2.getRealName());
    }

    @Test
    public final void testDataStyle() throws IOException {
        final FloatStyle fs = new FloatStyleBuilder("fs", Locale.US).build();
        final TableCellStyle style =
                TableCellStyle.builder("ts").dataStyle(fs).textAlign(CellAlign.LEFT).build();
        TestHelper.assertXMLEquals(
                "<style:style style:name=\"ts\" style:family=\"table-cell\" " +
                        "style:parent-style-name=\"Default\" style:data-style-name=\"fs\">" +
                        "<style:paragraph-properties fo:text-align=\"start\"/></style:style>",
                style);
    }

    @Test
    public final void testWrap() throws IOException {
        final TableCellStyle tcs = TableCellStyle.builder("test")
                .fontWrap(true).build();
        TestHelper.assertXMLEquals("<style:style style:name=\"test\" style:family=\"table-cell\" " +
                "style:parent-style-name=\"Default\">" +
                "<style:table-cell-properties fo:wrap-option=\"wrap\"/>" +
                "</style:style>", tcs);
    }

    @Test
    public final void testAddToElements() throws IOException {
        final FloatStyle fs = new FloatStyleBuilder("fs", Locale.US).build();
        final TableCellStyle style =
                TableCellStyle.builder("ts").dataStyle(fs).textAlign(CellAlign.LEFT).build();
        final OdsElements elements = PowerMock.createMock(OdsElements.class);

        PowerMock.resetAll();
        EasyMock.expect(elements.addDataStyle(fs)).andReturn(true);
        EasyMock.expect(elements.addContentStyle(style)).andReturn(true);

        PowerMock.replayAll();
        style.addToElements(elements);

        PowerMock.verifyAll();
    }

    @Test
    public final void testEmptyName() {
        Assert.assertThrows(IllegalArgumentException.class, () -> TableCellStyle.builder("").build());
    }

    @Test
    public final void testNameWithDash() {
        Assert.assertThrows(IllegalArgumentException.class, () -> TableCellStyle.builder("-").build());
    }

    @Test
    public final void testNameWithUnderscode() {
        final TableCellStyle style = TableCellStyle.builder("_").build();

        PowerMock.resetAll();
        PowerMock.replayAll();

        Assert.assertEquals("_", style.getName());
        PowerMock.replayAll();
    }

    @Test
    public final void testNameWithColon() {
        Assert.assertThrows(IllegalArgumentException.class, () -> TableCellStyle.builder("a:b").build());
    }

}
