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

package com.github.jferard.fastods;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class TextValueTest {
    @Test
    public void testSet() {
        PowerMock.resetAll();
        final TableCell cell = PowerMock.createMock(TableCell.class);
        final Capture<Text> capturedValue = Capture.newInstance();
        cell.setText(EasyMock.capture(capturedValue));

        PowerMock.replayAll();
        final Text text = Text.content("text");
        final CellValue cv = new TextValue(text);
        cv.setToCell(cell);
        Assert.assertEquals(text, capturedValue.getValue());

        PowerMock.verifyAll();
    }

    @Test
    public void testEquals() {
        final Text text1 = Text.builder().parContent("text").link("url", "url")
                .parStyledContent("text2", null).span("span").build();
        final Text text2 = Text.builder().par().styledSpan("text", null).link("url", "url").par()
                .span("text2").span("span").build();
        final TextValue expected = new TextValue(text1);
        Assert.assertEquals(expected, expected);
        Assert.assertNotEquals("new TextValue(text1)", expected);
        Assert.assertNotEquals(expected, "new TextValue(text1)");
        Assert.assertEquals(expected, new TextValue(text2));
        Assert.assertEquals(new TextValue(text2), expected);
    }

    @Test
    public void testHashCode() {
        final Text text1 = Text.builder().parContent("text").link("url", "#url")
                .parStyledContent("text2", null).span("span").build();
        final Text text2 = Text.builder().par().styledSpan("text", null).link("url", "#url").par()
                .span("text2").span("span").build();
        Assert.assertEquals(-633306896, new TextValue(text1).hashCode());
        Assert.assertEquals(-633306896, new TextValue(text2).hashCode());
    }
}