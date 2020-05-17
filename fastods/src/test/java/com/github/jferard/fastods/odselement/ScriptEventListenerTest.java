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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.TestHelper;
import com.github.jferard.fastods.attribute.ScriptEvent;
import org.junit.Test;

import java.io.IOException;

public class ScriptEventListenerTest {
    @Test
    public void testCreate() throws IOException {
        final ScriptEventListener sel = ScriptEventListener.create(ScriptEvent.ON_LOAD, "func");
        TestHelper.assertXMLEquals("<script:event-listener script:language=\"ooo:script\" " +
                "script:event-name=\"dom:load\" xlink:href=\"vnd.sun.star" +
                ".script:func?language=Basic&amp;location=document\" " + "xlink:type" +
                "=\"simple\"/>", sel);
    }

    @Test
    public void testBuilder() throws IOException {
        final ScriptEventListener sel =
                ScriptEventListener.builder(ScriptEvent.ON_LOAD, "func").language("l")
                        .genericLanguage("gl").build();
        TestHelper.assertXMLEquals("<script:event-listener script:language=\"gl\" " +
                "script:event-name=\"dom:load\" xlink:href=\"vnd.sun.star" +
                ".script:func?language=l&amp;location=document\" xlink:type=\"simple\"/>", sel);
    }
}