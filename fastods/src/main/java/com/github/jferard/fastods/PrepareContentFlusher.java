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

import com.github.jferard.fastods.odselement.ContentElement;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;

/**
 * When the first table is created.
 * Flush everything before the tables.
 * <p>
 * Sent by the first table creation.
 *
 * @author Julien Férard
 */
public class PrepareContentFlusher implements OdsAsyncFlusher {
    private final OdsElements odsElements;
    private final ContentElement contentElement;

    /**
     * Create a new meta and styles elements
     *
     * @param odsElements    ods elements (content.xml, styles.xml, ...)
     * @param contentElement content.xml element
     */
    public PrepareContentFlusher(final OdsElements odsElements,
                                 final ContentElement contentElement) {
        this.odsElements = odsElements;
        this.contentElement = contentElement;
    }

    @Override
    public void flushInto(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
        this.odsElements.writeMeta(xmlUtil, writer);
        this.odsElements.writeStyles(xmlUtil, writer);
        this.contentElement.writePreamble(xmlUtil, writer);
    }

    @Override
    public boolean isEnd() {
        return false;
    }
}
