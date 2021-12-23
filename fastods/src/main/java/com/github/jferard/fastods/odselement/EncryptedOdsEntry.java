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

import java.io.IOException;
import java.util.zip.ZipEntry;

/**
 * 4.3 manifest:file-entry
 * An entry in the manifest for encrypted files. External modules should provide the ZipUTF8Writer
 * to effectively encrypt the file using `encryptParameters`.
 */
public class EncryptedOdsEntry implements OdsEntry {
    private final String fullPath;
    private final CharSequence mediaType;
    private final CharSequence version;
    private final EncryptParameters encryptParameters;

    /**
     * @param fullPath          the path
     * @param mediaType         the media MIME type
     * @param version           the version
     * @param encryptParameters the parameters for encryption
     */
    public EncryptedOdsEntry(final String fullPath, final CharSequence mediaType,
                             final CharSequence version,
                             final EncryptParameters encryptParameters) {
        this.fullPath = fullPath;
        this.mediaType = mediaType;
        this.version = version;
        this.encryptParameters = encryptParameters;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<manifest:file-entry");
        util.appendAttribute(appendable, "manifest:full-path", this.fullPath);
        if (this.mediaType != null) {
            util.appendAttribute(appendable, "manifest:media-type", this.mediaType);
        }
        if (this.version != null) {
            util.appendAttribute(appendable, "manifest:version", this.version);
        }
        util.appendAttribute(appendable, "manifest:size",
                this.encryptParameters.getUncompressedSize());
        appendable.append(">");
        this.encryptParameters.appendXMLContent(util, appendable);
        appendable.append("</manifest:file-entry>");
    }

    @Override
    public OdsEntry encryptParameters(final EncryptParameters encryptParameters) {
        throw new IllegalArgumentException();
    }

    @Override
    public ZipEntry asZipEntry() {
        final ZipEntry zipEntry = new ZipEntry(this.fullPath);
        zipEntry.setSize(this.encryptParameters.getCompressedSize());
        zipEntry.setCompressedSize(this.encryptParameters.getCompressedSize());
        zipEntry.setCrc(this.encryptParameters.getCrc32());
        zipEntry.setMethod(ZipEntry.STORED);
        return zipEntry;
    }

    @Override
    public boolean neverEncrypt() {
        return true;
    }
}
