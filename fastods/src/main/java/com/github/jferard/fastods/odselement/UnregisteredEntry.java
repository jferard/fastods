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
 * An entry in the zip file, but not registered in the manifest.
 */
public class UnregisteredEntry implements ManifestEntry {
    private final String fullPath;

    /**
     * @param fullPath the path
     */
    public UnregisteredEntry(final String fullPath) {
        this.fullPath = fullPath;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        // do nothing
    }

    @Override
    public ManifestEntry encryptParameters(final EncryptParameters encryptParameters) {
        return this;
    }

    @Override
    public ZipEntry asZipEntry() {
        return new ZipEntry(this.fullPath);
    }

    @Override
    public boolean neverEncrypt() {
        return false;
    }
}
