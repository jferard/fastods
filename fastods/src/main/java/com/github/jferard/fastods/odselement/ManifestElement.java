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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.odselement.config.ManifestEntry;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;

/**
 * Part 3, 3.2 Manifest:
 * > All OpenDocument packages shall contain a file named “META-INF/manifest.xml”. This file is
 * the OpenDocument package manifest. The manifest provides :
 * •A list of all of the files in the package (except those specifically excluded from the
 * manifest).
 * •The MIME media type of each file in the package.
 * •If a file is stored in the file data in encrypted form, the manifest provides information
 * required to decrypt the file correctly when the encryption key is also supplied.
 * <p>
 * Part 3, 4 Manifest File
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class ManifestElement implements OdsElement {
    private static final List<ManifestEntry> ENTRIES = Arrays.asList(
            new ManifestEntry("/", "application/vnd.oasis.opendocument.spreadsheet", null),
            new ManifestEntry("content.xml", "text/xml", null),
            new ManifestEntry("styles.xml", "text/xml", null),
            new ManifestEntry("meta.xml", "text/xml", null),
            new ManifestEntry("settings.xml", "text/xml", null),
            new ManifestEntry("Configurations2/", "application/vnd.sun.xml.ui.configuration", null),
            new ManifestEntry("Configurations2/statusbar/", "", null),
            new ManifestEntry("Configurations2/accelerator/", "", null),
            new ManifestEntry("Configurations2/accelerator/current.xml", "", null),
            new ManifestEntry("Configurations2/floater/", "", null),
            new ManifestEntry("Configurations2/popupmenu/", "", null),
            new ManifestEntry("Configurations2/progressbar/", "", null),
            new ManifestEntry("Configurations2/menubar/", "", null),
            new ManifestEntry("Configurations2/toolbar/", "", null),
            new ManifestEntry("Configurations2/images/", "", null),
            new ManifestEntry("Configurations2/images/Bitmaps/", "", null),
            new ManifestEntry("Thumbnails/", "", null),
            new ManifestEntry("Thumbnails/thumbnail.png", "", null));

    /**
     * @return a new ManifestElement
     */
    public static ManifestElement create() {
        return new ManifestElement(ManifestElement.ENTRIES);
    }

    private final List<ManifestEntry> manifestEntries;

    /**
     * @param initialEntries the first entries
     */
    ManifestElement(final List<ManifestEntry> initialEntries) {
        this.manifestEntries = new ArrayList<ManifestEntry>(initialEntries);
    }

    @Override
    public void write(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
        writer.putNextEntry(new ZipEntry("META-INF/manifest.xml"));
        writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                "<manifest:manifest xmlns:manifest=\"urn:oasis:names:tc:opendocument:xmlns" +
                ":manifest:1.0\">");
        for (final ManifestEntry entry : this.manifestEntries) {
            entry.appendXMLContent(util, writer);
        }
        writer.append("</manifest:manifest>");
        writer.flush();
        writer.closeEntry();
    }

    /**
     * @param manifestEntry the new entry to add
     */
    public void add(final ManifestEntry manifestEntry) {
        this.manifestEntries.add(manifestEntry);
    }
}
