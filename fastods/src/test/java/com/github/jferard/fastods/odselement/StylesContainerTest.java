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
package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.datastyle.BooleanStyleBuilder;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.SimpleLength;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.Locale;

public class StylesContainerTest {
    private DataStyle ds1;
    private DataStyle ds2;
    private Locale locale;
    private PageStyle ps1;
    private PageStyle ps2;
    private TableCellStyle st1;
    private TableCellStyle st2;
    private StylesContainer stylesContainer;
    private XMLUtil util;

    @Before
    public void setUp() {
        this.stylesContainer = new StylesContainer();
        this.util = XMLUtil.create();
        this.locale = Locale.US;

        this.st1 = TableCellStyle.builder("a").fontStyleItalic().build();
        this.st2 = TableCellStyle.builder("a").fontWeightBold().build();

        this.ds1 = new BooleanStyleBuilder("a", this.locale).country("a").buildHidden();
        this.ds2 = new BooleanStyleBuilder("a", this.locale).country("b").buildHidden();

        this.ps1 = PageStyle.builder("a").allMargins(SimpleLength.pt(1.0)).build();
        this.ps2 = PageStyle.builder("a").allMargins(SimpleLength.pt(2.0)).build();
    }

    // CONTENT
    @Test
    public final void testAddDataStyle() {
        final DataStyle dataStyle = new BooleanStyleBuilder("test", this.locale).buildHidden();

        this.stylesContainer.addDataStyle(dataStyle);
        PowerMock.replayAll();
        // this.oe.addDataStyle(dataStyle);
        PowerMock.verifyAll();
    }

    @Test
    public final void testDataStyleCreate() throws IOException {
        final Appendable sb = new StringBuilder();

        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds1, Mode.CREATE));
        this.stylesContainer.writeDataStyles(this.util, sb);
        DomTester.assertEquals(
                "<number:boolean-style style:name=\"a\" number:language=\"en\" number:country=\"A\" style:volatile=\"true\"/>",
                sb.toString());
    }

    @Test
    public final void testDataStyleCreateThenUpdate() throws IOException {
        final Appendable sb = new StringBuilder();

        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds1, Mode.CREATE));
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds2, Mode.UPDATE)); // country: a -> b
        this.stylesContainer.writeDataStyles(this.util, sb);
        DomTester.assertEquals(
                "<number:boolean-style style:name=\"a\" number:language=\"en\" number:country=\"B\" style:volatile=\"true\"/>",
                sb.toString());
    }

    @Test
    public final void testDataStyleCreateThenUpdateIfExists() throws IOException {
        final Appendable sb = new StringBuilder();

        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds1, Mode.CREATE));
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds2, Mode.CREATE_OR_UPDATE));
        this.stylesContainer.writeDataStyles(this.util, sb);
        DomTester.assertEquals(
                "<number:boolean-style style:name=\"a\" number:language=\"en\" number:country=\"B\" style:volatile=\"true\"/>",
                sb.toString());
    }

    @Test
    public final void testDataStyleCreateTwice() throws IOException {
        final Appendable sb = new StringBuilder();

        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds1, Mode.CREATE));
        Assert.assertFalse(this.stylesContainer.addDataStyle(this.ds2, Mode.CREATE));
        this.stylesContainer.writeDataStyles(this.util, sb);
        DomTester.assertEquals(
                "<number:boolean-style style:name=\"a\" number:language=\"en\" number:country=\"A\" style:volatile=\"true\"/>",
                sb.toString());
    }

    @Test
    public final void testDataStyleUpdate() {
        final Appendable sb = new StringBuilder();
        Assert.assertFalse(this.stylesContainer.addDataStyle(this.ds2, Mode.UPDATE));
        DomTester.assertEquals("", sb.toString());
    }

    @Test
    public final void testDataStyleUpdateIfExists() throws IOException {
        final Appendable sb = new StringBuilder();
        Assert.assertTrue(this.stylesContainer.addDataStyle(this.ds2, Mode.CREATE_OR_UPDATE));
        this.stylesContainer.writeDataStyles(this.util, sb);
        DomTester.assertEquals(
                "<number:boolean-style style:name=\"a\" number:language=\"en\" number:country=\"B\" style:volatile=\"true\"/>",
                sb.toString());
    }

    @Test
    public final void testPageStyleCreate() throws IOException {
        final Appendable sb = new StringBuilder();
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps1, Mode.CREATE));
        this.stylesContainer.writeMasterPageStylesToMasterStyles(this.util, sb);
        this.stylesContainer.writePageLayoutStylesToAutomaticStyles(this.util, sb);
        DomTester.assertEquals(
                "<style:master-page style:name=\"a\" style:page-layout-name=\"a\">" +
                        "<style:header>" +
                        "<text:p><text:span text:style-name=\"none\"></text:span></text:p>" +
                        "</style:header>" +
                        "<style:header-left style:display=\"false\"/>" +
                        "<style:footer><text:p><text:span text:style-name=\"none\"></text:span></text:p>" +
                        "</style:footer><style:footer-left style:display=\"false\"/>" +
                        "</style:master-page>" +
                        "<style:page-layout style:name=\"a\">" +
                        "<style:page-layout-properties fo:page-width=\"21cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"1pt\"/>" +
                        "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:header-style>" +
                        "<style:footer-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:footer-style>" +
                        "</style:page-layout>",
                sb.toString());
    }


    @Test
    public final void testPageStyleCreateThenUpdate() throws IOException {
        final Appendable sb = new StringBuilder();
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps1, Mode.CREATE));
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps2, Mode.UPDATE));
        this.stylesContainer.writePageLayoutStylesToAutomaticStyles(this.util, sb);
        DomTester.assertEquals(
                "<style:page-layout style:name=\"a\">" +
                        "<style:page-layout-properties fo:page-width=\"21cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"2pt\"/>" +
                        "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:header-style>" +
                        "<style:footer-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:footer-style>" +
                        "</style:page-layout>",
                sb.toString());
    }

    @Test
    public final void testPageStyleCreateThenUpdateIfExists() throws IOException {
        final Appendable sb = new StringBuilder();
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps1, Mode.CREATE));
        Assert.assertTrue(
                this.stylesContainer.addPageStyle(this.ps2, Mode.CREATE_OR_UPDATE));
        this.stylesContainer.writePageLayoutStylesToAutomaticStyles(this.util, sb);
        DomTester.assertEquals(
                "<style:page-layout style:name=\"a\">" +
                        "<style:page-layout-properties fo:page-width=\"21cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"2pt\"/>" +
                        "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:header-style>" +
                        "<style:footer-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:footer-style>" +
                        "</style:page-layout>",
                sb.toString());
    }

    @Test
    public final void testPageStyleCreateTwice() throws IOException {
        final Appendable sb = new StringBuilder();
        Assert.assertTrue(this.stylesContainer.addPageStyle(this.ps1, Mode.CREATE));
        Assert.assertFalse(this.stylesContainer.addPageStyle(this.ps2, Mode.CREATE));
        this.stylesContainer.writePageLayoutStylesToAutomaticStyles(this.util, sb);
        DomTester.assertEquals(
                "<style:page-layout style:name=\"a\">" +
                        "<style:page-layout-properties fo:page-width=\"21cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"1pt\"/>" +
                        "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:header-style>" +
                        "<style:footer-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:footer-style>" +
                        "</style:page-layout>",
                sb.toString());
    }

    @Test
    public final void testPageStyleUpdate() throws IOException {
        final Appendable sb = new StringBuilder();
        Assert.assertFalse(this.stylesContainer.addPageStyle(this.ps2, Mode.UPDATE));
        this.stylesContainer.writePageLayoutStylesToAutomaticStyles(this.util, sb);
        DomTester.assertEquals(
                "",
                sb.toString());
    }

    @Test
    public final void testPageStyleUpdateIfExists() throws IOException {
        final Appendable sb = new StringBuilder();
        Assert.assertTrue(
                this.stylesContainer.addPageStyle(this.ps2, Mode.CREATE_OR_UPDATE));
        this.stylesContainer.writePageLayoutStylesToAutomaticStyles(this.util, sb);
        DomTester.assertEquals(
                "<style:page-layout style:name=\"a\">" +
                        "<style:page-layout-properties fo:page-width=\"21cm\" fo:page-height=\"29.7cm\" style:num-format=\"1\" style:writing-mode=\"lr-tb\" style:print-orientation=\"portrait\" fo:margin=\"2pt\"/>" +
                        "<style:header-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:header-style>" +
                        "<style:footer-style>" +
                        "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>" +
                        "</style:footer-style>" +
                        "</style:page-layout>",
                sb.toString());
    }

    @Test
    public void testAddChildCellStyle() throws Exception {
        final TableCellStyle tcs = TableCellStyle.builder("tcs").build();
        final DataStyle ds = new BooleanStyleBuilder("bs", this.locale).buildHidden();

        TableCellStyle childCellStyle = this.stylesContainer.addChildCellStyle(tcs, ds);
        Assert.assertNotNull(childCellStyle);
        Assert.assertEquals("tcs@@bs", childCellStyle.getName());
        Assert.assertEquals(ds, childCellStyle.getDataStyle());

        childCellStyle = this.stylesContainer.addChildCellStyle(tcs, ds);
        Assert.assertNotNull(childCellStyle);
        Assert.assertEquals("tcs@@bs", childCellStyle.getName());
        Assert.assertEquals(ds, childCellStyle.getDataStyle());
    }
}
