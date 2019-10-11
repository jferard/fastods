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

import com.github.jferard.fastods.OdsDocument;

import java.io.IOException;

/**
 * A module
 *
 * @author J. Férard
 */
public class MacroModule {
    /**
     * A StarBasic module
     *
     * @param name the name of the module
     * @param code the content
     * @return a module
     */
    public static MacroModule createBasic(final String name, final CharSequence code) {
        return new MacroModule(name, "StarBasic", code);
    }

    private final String name;
    private final String language;
    private final CharSequence code;

    /**
     * @param name     name of the module
     * @param language language, e.g. StarBasic or Python
     * @param code     the content
     */
    public MacroModule(final String name, final String language, final CharSequence code) {
        this.name = name;
        this.language = language;
        this.code = code;
    }

    /**
     * Add this module to a document
     *
     * @param util             an util to write XML
     * @param document         the document
     * @param libraryNameSlash path to this module, name excepted
     * @throws IOException if can't write
     */
    public void add(final XMLUtil util, final OdsDocument document, final String libraryNameSlash)
            throws IOException {
        document.addExtraFile(libraryNameSlash + this.name + ".xml", "text/xml", this.module(util));
    }

    private byte[] module(final XMLUtil util) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE script:module PUBLIC \"-//OpenOffice.org//DTD OfficeDocument 1.0//EN\"" +
                " \"module.dtd\">");
        sb.append("<script:module xmlns:script=\"http://openoffice.org/2000/script\"");
        util.appendAttribute(sb, "script:name", this.name);
        util.appendAttribute(sb, "script:language", this.language);
        util.appendAttribute(sb, "script:moduleType", "normal");
        sb.append(">");
        sb.append(this.code);
        sb.append("</script:module>");
        return sb.toString().getBytes(ZipUTF8Writer.UTF_8);
    }

    /**
     * Append a line to script-lc.xml
     *
     * @param util       an util to write XML
     * @param appendable where to write
     * @throws IOException if can't write
     */
    public void appendIndexLine(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<library:element");
        util.appendAttribute(appendable, "library:name", this.name);
        appendable.append("/>");
    }
}
