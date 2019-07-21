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

package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.OdsDocument;

/**
 * A helper
 */
public class MacroHelper {
    /**
     * @param document the document
     */
    // TODO add any nuùver of modules
    public void addRefreshMacro(final OdsDocument document) {
        document.addExtraDir("Basic/");
        document.addExtraFile("Basic/script-lc.xml", "text/xml",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<!DOCTYPE library:libraries PUBLIC \"-//OpenOffice.org//DTD " +
                        "OfficeDocument 1" +
                        ".0//EN\" \"libraries.dtd\">\n" +
                        "<library:libraries xmlns:library=\"http://openoffice.org/2000/library\" " +
                        "xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n" +
                        " <library:library library:name=\"Standard\" library:link=\"false\"/>\n" +
                        "</library:libraries>");
        document.addExtraDir("Basic/Standard/");
        document.addExtraFile("Basic/Standard/script-lb.xml", "text/xml",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<!DOCTYPE library:library PUBLIC \"-//OpenOffice.org//DTD OfficeDocument" +
                        " 1.0//EN\" \"library.dtd\">\n" +
                        "<library:library xmlns:library=\"http://openoffice.org/2000/library\" " +
                        "library:name=\"Standard\" library:readonly=\"false\" " +
                        "library:passwordprotected=\"false\">\n" +
                        " <library:element library:name=\"FastODS\"/>\n" + "</library:library>");
        document.addExtraFile("Basic/Standard/FastODS.xml", "text/xml",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<!DOCTYPE script:module PUBLIC \"-//OpenOffice.org//DTD OfficeDocument 1" +
                        ".0//EN\" \"module.dtd\">\n" +
                        "<script:module xmlns:script=\"http://openoffice.org/2000/script\" " +
                        "script:name=\"FastODS\" script:language=\"StarBasic\" " +
                        "script:moduleType=\"normal\">" + "REM FastODS (C) J. Férard\n" +
                        "REM Auto update macro\n\n" + "Sub Main\n" +
                        "\tfor each oElem in ThisComponent.DatabaseRanges\n" +
                        "\t\toElem.refresh\n" + "\tnext oElem\n" + "End Sub\n" +
                        "</script:module>");
    }
}
