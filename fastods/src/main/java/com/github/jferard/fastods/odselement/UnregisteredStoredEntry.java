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

import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.zip.ZipEntry;

/**
 *
 */
public class UnregisteredStoredEntry implements ManifestEntry {
    private final String fullPath;
    private final long size;
    private final long crc32;

    public UnregisteredStoredEntry(final String fullPath, final long size, final long crc32) {
        this.fullPath = fullPath;
        this.size = size;
        this.crc32 = crc32;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable) throws IOException {
        // do nothing
    }

    @Override
    public ManifestEntry encryptParameters(final EncryptParameters encryptParameters) {
        return this;
    }

    @Override
    public ZipEntry asZipEntry() {
        final ZipEntry zipEntry = new ZipEntry(this.fullPath);
        zipEntry.setSize(this.size);
        zipEntry.setCompressedSize(this.size);
        zipEntry.setCrc(this.crc32);
        zipEntry.setMethod(ZipEntry.STORED);
        return zipEntry;
    }

    @Override
    public boolean neverEncrypt() {
        return true;
    }
}
