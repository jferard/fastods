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

package com.github.jferard.fastods.util;

import com.github.jferard.fastods.OdsDocument;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * A library container.
 */
public class MacroLibraryContainer {
    private static final String CONTAINER_NAME = "Basic";
    /**
     * Directory of scripts
     */
    public static final String CONTAINER_NAME_SLASH = CONTAINER_NAME + "/";
    private final XMLUtil util;
    private final List<MacroLibrary> libraries;
    /**
     * @param util      an util to write XML
     * @param libraries the libraries of the module
     */
    public MacroLibraryContainer(final XMLUtil util, final List<MacroLibrary> libraries) {
        this.util = util;
        this.libraries = libraries;
    }

    /**
     * Variadic version
     *
     * @param libraries the libraries
     * @return a container
     */
    public static MacroLibraryContainer create(final MacroLibrary... libraries) {
        final XMLUtil xmlUtil = XMLUtil.create();
        return new MacroLibraryContainer(xmlUtil, Arrays.asList(libraries));
    }

    /**
     * Add this container to a document
     *
     * @param document the document
     * @throws IOException if can't write
     */
    public void add(final OdsDocument document) throws IOException {
        document.addExtraDir(CONTAINER_NAME_SLASH);
        document.addExtraFile(CONTAINER_NAME_SLASH + "script-lc.xml", "text/xml",
                this.container(this.util));
        for (final MacroLibrary library : this.libraries) {
            library.add(this.util, document);
        }
    }

    private byte[] container(final XMLUtil util) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append(XMLUtil.XML_PROLOG + "\n" +
                "<!DOCTYPE library:libraries PUBLIC \"-//OpenOffice.org//DTD " +
                "OfficeDocument 1.0//EN\" \"libraries.dtd\">\n" +
                "<library:libraries xmlns:library=\"http://openoffice.org/2000/library\" " +
                "xmlns:xlink=\"http://www.w3.org/1999/xlink\">");
        for (final MacroLibrary library : this.libraries) {
            library.appendIndexLine(util, sb);
        }
        sb.append("</library:libraries>");
        return sb.toString().getBytes(CharsetUtil.UTF_8);
    }
}
