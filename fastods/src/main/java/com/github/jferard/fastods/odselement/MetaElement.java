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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;

/**
 * WHERE ? meta.xml/office:document-meta
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class MetaElement implements OdsElement {
    final static SimpleDateFormat DF_DATE = new SimpleDateFormat("yyyy-MM-dd");
    final static SimpleDateFormat DF_TIME = new SimpleDateFormat("HH:mm:ss");
    private final String editingCycles;
    private final String editingDuration;
    private final String generator;
    private String creator;
    private String dateTime;
    // private int tableCount = 1;
    // private int cellCount = 1;

    public MetaElement() {
        this.setDateTimeNow();
        this.generator = "FastOds 0.0.1 2016";
        this.creator = "FastOds 0.0.1";
        this.editingCycles = "1";
        this.editingDuration = "PT1M00S";
    }

    public void setCreator(final String creator) {
        this.creator = creator;
    }

    @Override
    public void write(final XMLUtil util, final ZipUTF8Writer writer)
            throws IOException {
        writer.putNextEntry(new ZipEntry("meta.xml"));
        writer.append("<?xml");
        util.appendEAttribute(writer, "version", "1.0");
        util.appendEAttribute(writer, "encoding", "UTF-8");
        writer.append("?><office:document-meta");
        util.appendEAttribute(writer, "xmlns:office",
                "urn:oasis:names:tc:opendocument:xmlns:office:1.0");
        util.appendEAttribute(writer, "xmlns:xlink",
                "http://www.w3.org/1999/xlink");
        util.appendEAttribute(writer, "xmlns:dc",
                "http://purl.org/dc/elements/1.1/");
        util.appendEAttribute(writer, "xmlns:meta",
                "urn:oasis:names:tc:opendocument:xmlns:meta:1.0");
        util.appendEAttribute(writer, "xmlns:ooo",
                "http://openoffice.org/2004/office");
        util.appendEAttribute(writer, "office:version", "1.1");
        writer.append("><office:meta>");
        util.appendTag(writer, "meta:generator", this.generator);
        util.appendTag(writer, "dc:creator", this.creator);
        util.appendTag(writer, "dc:date", this.dateTime);
        util.appendTag(writer, "meta:editing-cycles", this.editingCycles);
        util.appendTag(writer, "meta:editing-duration", this.editingDuration);
        writer.append("<meta:user-defined meta:name=\"Info 1\"/>")
                .append("<meta:user-defined meta:name=\"Info 2\"/>")
                .append("<meta:user-defined meta:name=\"Info 3\"/>")
                .append("<meta:user-defined meta:name=\"Info 4\"/>");
        // .append("<meta:document-statistic");
        // util.appendEAttribute(writer, "meta:table-count", this.tableCount);
        // util.appendEAttribute(writer, "meta:cell-count", this.cellCount);
        // writer.append("/>")
        writer.append("</office:meta>").append("</office:document-meta>");
        writer.flush();
        writer.closeEntry();
    }

    /**
     * Store the date and time of the document creation in the MetaElement data.
     */
    private void setDateTimeNow() {
        final Date dt = new Date();

        this.dateTime = MetaElement.DF_DATE.format(dt) +
                "T" + MetaElement.DF_TIME.format(dt);
    }
}
