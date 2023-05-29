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

package com.github.jferard.fastods.datastyle;

import com.github.jferard.fastods.TableCellImpl;
import com.github.jferard.fastods.TableRowImpl;
import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.IntegerRepresentationCache;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Locale;

public class FloatStyleTest {
    @Test
    public void testDsf() throws IOException {
        // see 1., https://github.com/jferard/fastods/issues/247.

        // arrange
        final Locale locale = Locale.US;
        final FloatStyle dsf = new FloatStyleBuilder("float-datastyle", locale).decimalPlaces(0)
                .groupThousands(true).build();
        final TableCellStyle tableHeadStyle = TableCellStyle.builder("tableHeadStyle")
                .fontSize(SimpleLength.pt(14))
                .fontWeightBold()
                .hidden()
                .dataStyle(dsf)
                .build();

        final StylesContainer stc = PowerMock.createMock(StylesContainer.class);
        final TableRowImpl row = PowerMock.createMock(TableRowImpl.class);
        final TableCellImpl cell = new TableCellImpl(
                IntegerRepresentationCache.create(), XMLUtil.create(), stc,
                DataStylesBuilder.create(locale).build(), false, row,
                1);
        final TableCellStyle ccs = PowerMock.createMock(TableCellStyle.class);

        PowerMock.resetAll();
        EasyMock.expect(stc.addContentFontFaceContainerStyle(tableHeadStyle)).andReturn(true);
        EasyMock.expect(stc.addDataStyle(dsf)).andReturn(true);

        // act
        PowerMock.replayAll();
        cell.setStyle(tableHeadStyle);

        // assert
        PowerMock.verifyAll();
        TestHelper.assertXMLEquals(
                "<number:number-style style:name=\"float-datastyle\" number:language=\"en\" " +
                        "number:country=\"US\" style:volatile=\"true\">" +
                        "<number:number number:decimal-places=\"0\" number:min-integer-digits=\"1\" " +
                        "number:grouping=\"true\"/>" +
                        "</number:number-style>",
                dsf);
        TestHelper.assertXMLEquals(
                "<style:style style:name=\"tableHeadStyle\" style:family=\"table-cell\" " +
                        "style:parent-style-name=\"Default\" " +
                        "style:data-style-name=\"float-datastyle\">" +
                        "<style:text-properties fo:font-weight=\"bold\" " +
                        "style:font-weight-asian=\"bold\" style:font-weight-complex=\"bold\" " +
                        "fo:font-size=\"14pt\" style:font-size-asian=\"14pt\" " +
                        "style:font-size-complex=\"14pt\"/>" +
                        "</style:style>",
                tableHeadStyle);
    }

    @Test
    public void testMultipleStyleCells() throws IOException {
        // arrange
        final Locale locale = Locale.US;
        final FloatStyle dsf2 = new FloatStyleBuilder("float-datastyle1", locale).decimalPlaces(0)
                .groupThousands(true).build();
        final FloatStyle dsf4 =
                new FloatStyleBuilder("float-datastyle2", locale).decimalPlaces(2).build();
        final TableCellStyle tcs1 = TableCellStyle.builder("cell-style1")
                .fontSize(SimpleLength.pt(14))
                .build();
        final TableCellStyle tcs2 = TableCellStyle.builder("cell-style2")
                .dataStyle(dsf2)
                .build();
        final TableCellStyle tcs3 = TableCellStyle.builder("cell-style3")
                .build();
        final TableCellStyle tcs4 = TableCellStyle.builder("cell-style4")
                .dataStyle(dsf4)
                .build();

        final StylesContainer stc = PowerMock.createMock(StylesContainer.class);
        final TableRowImpl row = PowerMock.createMock(TableRowImpl.class);
        final TableCellImpl cell = new TableCellImpl(
                IntegerRepresentationCache.create(), XMLUtil.create(), stc,
                DataStylesBuilder.create(locale).build(), false, row,
                1);
        final TableCellStyle ccs3 = PowerMock.createMock(TableCellStyle.class);

        PowerMock.resetAll();
        // take style
        EasyMock.expect(stc.addContentFontFaceContainerStyle(tcs1)).andReturn(true);

        // take style and datastyle
        EasyMock.expect(stc.addContentFontFaceContainerStyle(tcs2)).andReturn(true);
        EasyMock.expect(stc.addDataStyle(dsf2)).andReturn(true);

        // take style and previous datastyle
        EasyMock.expect(stc.addContentFontFaceContainerStyle(tcs3)).andReturn(true);
        EasyMock.expect(stc.addChildCellStyle(tcs3, dsf2)).andReturn(ccs3);

        // take style and datastyle
        EasyMock.expect(stc.addContentFontFaceContainerStyle(tcs4)).andReturn(true);
        EasyMock.expect(stc.addDataStyle(dsf4)).andReturn(true);

        // act
        PowerMock.replayAll();
        cell.setStyle(tcs1); // take style
        cell.setStyle(tcs2); // take style and datastyle
        cell.setStyle(tcs3); // take style and previous datastyle
        cell.setStyle(tcs4); // take style and datastyle

        // assert
        PowerMock.verifyAll();
    }
}