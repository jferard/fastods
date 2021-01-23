/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    /**
     * the name of the manifest file in the archive
     */
    public static final String META_INF_MANIFEST_XML = "META-INF/manifest.xml";

    /**
     * @return a new ManifestElement
     */
    public static ManifestElement create() {
        return new ManifestElement(new HashSet<ManifestEntry>()); //ManifestElement.ENTRIES);
    }

    private final Set<ManifestEntry> manifestEntries;

    /**
     * @param initialEntries the first entries
     */
    ManifestElement(final Set<ManifestEntry> initialEntries) {
        this.manifestEntries = new HashSet<ManifestEntry>(initialEntries);
    }

    @Override
    public void write(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
        writer.putNextEntry(new UnregisteredEntry(ManifestElement.META_INF_MANIFEST_XML));
        writer.append(XMLUtil.XML_PROLOG);
        writer.append("<manifest:manifest");
        util.appendAttribute(writer, "xmlns:manifest",
                "urn:oasis:names:tc:opendocument:xmlns:manifest:1.0");
        util.appendAttribute(writer, "manifest:version", "1.2");
        writer.append(">");
        final List<ManifestEntry> entries = this.getManifestEntries();
        for (final ManifestEntry entry : entries) {
            entry.appendXMLContent(util, writer);
        }
        writer.append("</manifest:manifest>");
        writer.closeEntry();
    }

    private List<ManifestEntry> getManifestEntries() {
        final List<ManifestEntry> entries = new ArrayList<ManifestEntry>(this.manifestEntries);
        Collections.sort(entries, new Comparator<ManifestEntry>() {
            @Override
            public int compare(final ManifestEntry o1, final ManifestEntry o2) {
                return o1.asZipEntry().getName().compareTo(o2.asZipEntry().getName());
            }
        });
        return entries;
    }

    /**
     * @param manifestEntry the new entry to add
     */
    public void add(final ManifestEntry manifestEntry) {
        this.manifestEntries.add(manifestEntry);
    }
}
