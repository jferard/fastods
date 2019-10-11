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
import java.util.List;

/**
 * A macro library, e.g. standard
 */
public class MacroLibrary {
    public static MacroLibraryBuilder builder() {
        return new MacroLibraryBuilder();
    }

    private final String name;
    private final boolean readOnly;
    private final List<MacroModule> modules;

    /**
     * @param name name of the library e.g. Standard
     * @param readOnly if this library is readonly
     * @param modules the modules
     */
    public MacroLibrary(final String name, final boolean readOnly,
                        final List<MacroModule> modules) {
        this.name = name;
        this.readOnly = readOnly;
        this.modules = modules;
    }

    /**
     * Add this library to a document
     * @param util an util to write XML
     * @param document the document
     * @throws IOException if can't write
     */
    public void add(final XMLUtil util, final OdsDocument document) throws IOException {
        final String nameSlash = MacroLibraryContainer.CONTAINER_NAME_SLASH + this.name + "/";
        document.addExtraDir(nameSlash);
        document.addExtraFile(nameSlash + "script-lb.xml", "text/xml", this.index(util));
        for (final MacroModule module : this.modules) {
            module.add(util, document, nameSlash);
        }
    }

    private byte[] index(final XMLUtil util) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE library:library PUBLIC \"-//OpenOffice.org//DTD OfficeDocument" +
                " 1.0//EN\" \"library.dtd\">");
        sb.append("<library:library xmlns:library=\"http://openoffice.org/2000/library\"");
        util.appendAttribute(sb, "library:name", this.name);
        util.appendAttribute(sb, "library:readonly", this.readOnly);
        util.appendAttribute(sb, "library:passwordprotected", false);
        sb.append(">");
        for (final MacroModule module : this.modules) {
            module.appendIndexLine(util, sb);
        }
        sb.append("</library:library>");
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
        appendable.append("<library:library");
        util.appendAttribute(appendable, "library:name", this.name);
        util.appendAttribute(appendable, "library:link", false);
        appendable.append("/>");
    }
}
