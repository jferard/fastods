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

import com.github.jferard.fastods.PageSectionContent.Region;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.testlib.DomTester;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.XMLUtil;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class RegionPageSectionTest {
    private XMLUtil util;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
        PowerMock.resetAll();
    }

    @After
    public void tearDown() {
        PowerMock.verifyAll();
    }

    @Test
    public final void testNullOrEmptyRegions() throws IOException {
        PowerMock.replayAll();
        final PageSection headerSection = PageSection.regionBuilder().region(Region.LEFT).content("l")
                .region(Region.CENTER).text(Text.builder().par().build()).build();
        this.assertMasterXMLEquals(
                "<style:region-left>" + "<text:p>l</text:p>" + "</style:region-left>" + "<style:region-center>" +
                        "<text:p>" + "</text:p>" + "</style:region-center>",
                headerSection);
    }

    @Test
    public final void testRegionsToMasterStyle() throws IOException {
        PowerMock.replayAll();
        final TextStyle ts1 = TextProperties.builder().fontStyleItalic().buildStyle("style1");
        final TextStyle ts2 = TextProperties.builder().fontStyleNormal().fontWeightNormal().buildStyle("style2");
        final TextStyle ts3 = TextProperties.builder().fontWeightBold().buildStyle("style3");
        final PageSection headerSection = PageSection.regionBuilder().region(Region.LEFT)
                .styledContent("left-text", ts1).region(Region.CENTER).styledContent("center-text", ts2)
                .region(Region.RIGHT).styledContent("right-text", ts3).build();
        this.assertMasterXMLEquals(
                "<style:region-left>" + "<text:p>" + "<text:span text:style-name=\"style1\">left-text</text:span>" +
                        "</text:p>" + "</style:region-left>" + "<style:region-center>" + "<text:p>" + "<text:span " +
                        "text:style-name=\"style2\">center-text</text:span>" + "</text:p>" + "</style:region-center>"
                        + "<style:region-right>" + "<text:p>" + "<text:span " +
                        "text:style-name=\"style3\">right-text</text:span>" + "</text:p>" + "</style:region-right>",
                headerSection);
    }

    @Test
    public final void testRegionToAutomaticStyle() throws IOException {
        PowerMock.replayAll();
        final TextStyle ts = TextProperties.builder().fontWeightBold().buildStyle("style");
        final PageSection footerSection = PageSection.regionBuilder().region(Region.CENTER)
                .styledContent(Text.TEXT_PAGE_NUMBER, ts).build();
        final StringBuilder sb = new StringBuilder();
        footerSection.appendPageSectionStyleXMLToAutomaticStyle(this.util, sb, PageSection.Type.FOOTER);
        DomTester.assertEquals(
                "<style:footer-style>" + "<style:header-footer-properties fo:min-height=\"0cm\" fo:margin=\"0cm\"/>"
                        + "</style:footer-style>",
                sb.toString());
    }

    @Test
    public final void testRegionToMasterStyle() throws IOException {
        PowerMock.replayAll();
        final TextStyle ts = TextProperties.builder().fontWeightBold().buildStyle("style");
        final PageSection footerSection = PageSection.regionBuilder().region(Region.CENTER)
                .styledContent(Text.TEXT_PAGE_NUMBER, ts).build();
        this.assertMasterXMLEquals(
                "<style:region-center>" + "<text:p>" + "<text:span text:style-name=\"style\">" +
                        "<text:page-number>1</text:page-number>" + "</text:span>" + "</text:p>" +
                        "</style:region-center>",
                footerSection);
    }

    @Test
    public final void testEmbedded() throws IOException {
        final StylesContainer sc = PowerMock.createMock(StylesContainer.class);
        final TextStyle ts1 = TextProperties.builder().fontStyleItalic().buildStyle("style1");
        final TextStyle ts2 = TextProperties.builder().fontStyleNormal().fontWeightNormal().buildStyle("style2");
        final TextStyle ts3 = TextProperties.builder().fontWeightBold().buildStyle("style3");
        final PageSection headerSection = PageSection.regionBuilder().region(Region.LEFT)
                .styledContent("left-text", ts1).region(Region.CENTER).styledContent("center-text", ts2)
                .region(Region.RIGHT).styledContent("right-text", ts3).build();

        // play
        EasyMock.expect(sc.addStyleToStylesAutomaticStyles(ts1, Mode.CREATE)).andReturn(true);
        EasyMock.expect(sc.addStyleToStylesAutomaticStyles(ts2, Mode.CREATE)).andReturn(true);
        EasyMock.expect(sc.addStyleToStylesAutomaticStyles(ts3, Mode.CREATE)).andReturn(true);
        PowerMock.replayAll();
        headerSection.addEmbeddedStyles(sc);
    }

    @Test
    public final void testEmbeddedNull() throws IOException {
        final StylesContainer sc = PowerMock.createMock(StylesContainer.class);
        final PageSection headerSecton = PageSection.regionBuilder().build();

        // play
        PowerMock.replayAll();
        headerSecton.addEmbeddedStyles(sc);
        PowerMock.verifyAll();
    }

    @Test
    public final void testEmbeddedMode() throws IOException {
        final StylesContainer sc = PowerMock.createMock(StylesContainer.class);
        final TextStyle ts1 = TextProperties.builder().fontStyleItalic().buildStyle("style1");
        final TextStyle ts2 = TextProperties.builder().fontStyleNormal().fontWeightNormal().buildStyle("style2");
        final TextStyle ts3 = TextProperties.builder().fontWeightBold().buildStyle("style3");
        final PageSection headerSection = PageSection.regionBuilder().region(Region.LEFT)
                .styledContent("left-text", ts1).region(Region.CENTER).styledContent("center-text", ts2)
                .region(Region.RIGHT).styledContent("right-text", ts3).build();

        // play
        EasyMock.expect(sc.addStyleToStylesAutomaticStyles(ts1, Mode.CREATE_OR_UPDATE)).andReturn(true);
        EasyMock.expect(sc.addStyleToStylesAutomaticStyles(ts2, Mode.CREATE_OR_UPDATE)).andReturn(true);
        EasyMock.expect(sc.addStyleToStylesAutomaticStyles(ts3, Mode.CREATE_OR_UPDATE)).andReturn(true);
        PowerMock.replayAll();
        headerSection.addEmbeddedStyles(sc, Mode.CREATE_OR_UPDATE);
        PowerMock.verifyAll();
    }

    @Test
    public final void testEmbeddedNullMode() throws IOException {
        final StylesContainer sc = PowerMock.createMock(StylesContainer.class);
        final PageSection headerSection = PageSection.regionBuilder().build();

        // play
        PowerMock.replayAll();
        headerSection.addEmbeddedStyles(sc, Mode.CREATE_OR_UPDATE);
        PowerMock.verifyAll();
    }

    private void assertMasterXMLEquals(final String xml, final PageSection section) throws IOException {
        final StringBuilder sb = new StringBuilder();
        section.appendXMLToMasterStyle(this.util, sb);
        DomTester.assertEquals(xml, sb.toString());
    }
}