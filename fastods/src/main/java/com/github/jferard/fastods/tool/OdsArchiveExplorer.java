/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.odselement.ManifestElement;
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.FileUtil;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class OdsArchiveExplorer {
    private final FileUtil fileUtil;
    private final InputStream sourceStream;
    private final Map<String, OdsFile> fileByName;
    /**
     * @param fileUtil     an util
     * @param sourceStream the source of the image
     */
    public OdsArchiveExplorer(final FileUtil fileUtil, final InputStream sourceStream) {
        this.fileUtil = fileUtil;
        this.sourceStream = sourceStream;
        this.fileByName = new HashMap<String, OdsFile>();
    }

    public Map<String, OdsFile> explore() throws IOException {
        final ZipInputStream zipStream = new ZipInputStream(this.sourceStream);
        ZipEntry zipEntry = zipStream.getNextEntry();
        while (zipEntry != null) {
            final String name = zipEntry.getName();
            final byte[] bytes = this.fileUtil.readStream(zipStream);
            if (name.equals(ManifestElement.META_INF_MANIFEST_XML)) {
                this.extractMediaTypeByName(bytes);
            }
            this.putBytes(name, bytes);
            zipEntry = zipStream.getNextEntry();
        }
        return this.fileByName;
    }

    private void extractMediaTypeByName(final byte[] bytes) throws IOException {
        try {
            final Document manifest = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new ByteArrayInputStream(bytes));
            this.extractMediaTypeByName(manifest);
        } catch (final SAXException e) {
            // pass
        } catch (final ParserConfigurationException e) {
            // pass
        }
    }

    private void extractMediaTypeByName(final Document manifest) {
        final NodeList mEntries = manifest.getElementsByTagName("manifest:file-entry");
        for (int i = 0; i < mEntries.getLength(); i++) {
            final Node mEntry = mEntries.item(i);
            final NamedNodeMap attributes = mEntry.getAttributes();
            this.putMediaType(attributes.getNamedItem("manifest:full-path").getNodeValue(),
                    attributes.getNamedItem("manifest:media-type").getNodeValue());
        }
    }

    private void putBytes(final String name, final byte[] bytes) {
        final OdsFile odsFile = this.getOrCreateOdsFile(name);
        odsFile.setBytes(bytes);
    }

    private void putMediaType(final String name, final String mediaType) {
        final OdsFile odsFile = this.getOrCreateOdsFile(name);
        odsFile.setMediaType(mediaType);
    }

    private OdsFile getOrCreateOdsFile(final String name) {
        OdsFile odsFile = this.fileByName.get(name);
        if (odsFile == null) {
            odsFile = new OdsFile(name);
            this.fileByName.put(name, odsFile);
        }
        return odsFile;
    }

    static class OdsFile {
        private final String name;
        private byte[] bytes;
        private String mediaType;

        OdsFile(final String name) {
            this.name = name;
        }

        public void setBytes(final byte[] bytes) {
            this.bytes = bytes;
        }

        public void setMediaType(final String mediaType) {
            this.mediaType = mediaType;
        }

        /**
         * Add this file to an existing document
         * @param document the destination document
         * @param prefix the prefix in ths manifest
         */
        public void addToDocument(final OdsDocument document, final String prefix) {
            if (this.bytes == null) {
                document.addExtraObjectReference(prefix + this.name, this.mediaType, null);
            } else {
                document.addExtraFile(prefix + this.name, this.mediaType, this.bytes);
            }
        }

        @Override
        public int hashCode() {
            return EqualityUtil.hashObjects(this.name, this.mediaType);
        }

        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof OdsArchiveExplorer.OdsFile)) {
                return false;
            }
            final OdsArchiveExplorer.OdsFile other = (OdsArchiveExplorer.OdsFile) o;
            return this.name.equals(other.name) && EqualityUtil.equal(this.mediaType, other.mediaType) &&
                    Arrays.equals(this.bytes, other.bytes);
        }
    }
}