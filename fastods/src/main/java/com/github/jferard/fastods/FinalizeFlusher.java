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
 * An async flusher flusher to finalize the file.
 * Writes postamble for contents, and the settings.
 * <p>
 * Sent by the NamedOdsDocument.save method.
 *
 * @author Julien Férard
 */
public class FinalizeFlusher implements OdsAsyncFlusher {
    private final ContentElement contentElement;
    private final OdsElements odsElements;

    /**
     * @param contentElement the content to finalize
     * @param odsElements    the elements
     */
    public FinalizeFlusher(final ContentElement contentElement, final OdsElements odsElements) {
        this.contentElement = contentElement;
        this.odsElements = odsElements;
    }

    @Override
    public void flushInto(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
        this.contentElement.writePostamble(xmlUtil, writer);
        this.odsElements.writeSettings(xmlUtil, writer);
        this.odsElements.writeManifest(xmlUtil, writer);
        this.odsElements.writeExtras(writer);
        writer.close();
    }

    @Override
    public boolean isEnd() {
        return true;
    }
}
