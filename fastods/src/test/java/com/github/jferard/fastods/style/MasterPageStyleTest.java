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

import com.github.jferard.fastods.Footer;
import com.github.jferard.fastods.Header;
import com.github.jferard.fastods.PageSection;
import com.github.jferard.fastods.odselement.OdsElements;
import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class MasterPageStyleTest {
    @Test
    public void testAddToElements() {
        final TextStyle noneStyle = TextStyle.builder("none").build();
        final Header header =
                PageSection.simpleHeader("", noneStyle);
        final Footer footer = PageSection.simpleFooter("", noneStyle);
        final MasterPageStyle ms = new MasterPageStyle("test", "layoutName", header, footer);
        final OdsElements elements = PowerMock.createMock(OdsElements.class);

        PowerMock.resetAll();
        EasyMock.expect(elements.addMasterPageStyle(ms)).andReturn(true);

        PowerMock.replayAll();
        ms.addToElements(elements);

        PowerMock.verifyAll();
    }

}