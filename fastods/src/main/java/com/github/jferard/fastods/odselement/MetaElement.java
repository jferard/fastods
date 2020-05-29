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

import com.github.jferard.fastods.odselement.config.StandardManifestEntry;
import com.github.jferard.fastods.util.CharsetUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.ZipEntry;

/**
 * 3.1.3.4 office:document-meta
 * and 4.3 Non-RDF Metadata
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class MetaElement implements OdsElement {
    public static final String GENERATOR = "FastOds/0.7.3";
    public static final String OFFICE_VERSION = "1.2";

    /**
     * the date format: 2017-12-31
     */
    final static SimpleDateFormat DF_DATE;

    static {
        DF_DATE = new SimpleDateFormat("yyyy-MM-dd");
        DF_DATE.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * the time format: 18:12:59
     */
    final static SimpleDateFormat DF_TIME;

    static {
        DF_TIME = new SimpleDateFormat("HH:mm:ss");
        DF_TIME.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static MetaElementBuilder builder() {
        return new MetaElementBuilder();
    }

    public static MetaElement create() {
        return new MetaElementBuilder().build();
    }

    public static final Map<String, String> META_NAMESPACE_BY_PREFIX = new HashMap<String, String>();

    static {
        META_NAMESPACE_BY_PREFIX.putAll(OdsElements.BASE_NAMESPACE_BY_PREFIX);
        META_NAMESPACE_BY_PREFIX.put("xmlns:dc", "http://purl.org/dc/elements/1.1/");
        META_NAMESPACE_BY_PREFIX.put("xmlns:meta",
                "urn:oasis:names:tc:opendocument:xmlns:meta:1.0");
    }

    private final String description;
    private final String language;
    private final String subject;
    private final String title;
    private final String editingCycles;
    private final String editingDuration;
    private final String initialCreator;
    private final List<String> keyWords;
    private final List<UserDefined> userDefineds;
    private final String creator;
    private final String dateTime;

    /**
     * Create a new meta element
     */
    public MetaElement(final String creator, final String dateTime, final String description,
                       final String language, final String subject, final String title,
                       final String editingCycles, final String editingDuration,
                       final String initialCreator, final List<String> keyWords,
                       final List<UserDefined> userDefineds) {
        this.dateTime = dateTime;
        this.creator = creator;
        this.description = description;
        this.language = language;
        this.subject = subject;
        this.title = title;
        this.editingCycles = editingCycles;
        this.editingDuration = editingDuration;
        this.initialCreator = initialCreator;
        this.keyWords = keyWords;
        this.userDefineds = userDefineds;
    }

    @Override
    public void write(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
        writer.putNextEntry(new StandardManifestEntry("meta.xml", "text/xml", null));
        writer.append("<?xml");
        util.appendAttribute(writer, "version", "1.0");
        util.appendAttribute(writer, "encoding", CharsetUtil.UTF_8_NAME);
        writer.append("?><office:document-meta");
        for (final Map.Entry<String, String> entry: META_NAMESPACE_BY_PREFIX.entrySet()) {
            util.appendAttribute(writer, entry.getKey(), entry.getValue());
        }
        util.appendAttribute(writer, "office:version", OFFICE_VERSION);
        writer.append("><office:meta>");
        util.appendTag(writer, "dc:creator", this.creator);
        util.appendTag(writer, "dc:date", this.dateTime);
        if (this.description != null) {
            util.appendTag(writer, "dc:description", this.description);
        }
        if (this.language != null) {
            util.appendTag(writer, "dc:language", this.language);
        }
        if (this.subject != null) {
            util.appendTag(writer, "dc:subject", this.subject);
        }
        if (this.title != null) {
            util.appendTag(writer, "dc:title", this.title);
        }
        util.appendTag(writer, "meta:generator", GENERATOR);
        util.appendTag(writer, "meta:editing-cycles", this.editingCycles);
        util.appendTag(writer, "meta:editing-duration", this.editingDuration);
        if (this.initialCreator != null) {
            util.appendTag(writer, "meta:initial-creator", this.initialCreator);
        }
        for (final String keyword : this.keyWords) {
            util.appendTag(writer, "meta:keyword", keyword);
        }
        for (final UserDefined userDefined : this.userDefineds) {
            userDefined.appendXMLContent(util, writer);
        }
        //TODO: <meta:document-statistic ...>"
        writer.append("</office:meta>").append("</office:document-meta>");
        writer.flush();
        writer.closeEntry();
    }
}
