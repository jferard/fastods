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

import com.github.jferard.fastods.util.CharsetUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.zip.CRC32;

/**
 * 3.3 MIME Media Type
 * The “mimetype” file shall be the first file of the zip file. It shall not be compressed, and
 * it shall not use an 'extra field' in its header.
 * <p>
 * If the file named “META-INF/manifest.xml” contains a manifest:file-entry element whose
 * manifest:full-path attribute has the value "/", then a "mimetype" file shall exist, and
 * the content of the “mimetype” file shall be equal to the value of the manifest:media-type
 * attribute 4.8.10 of that element.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class MimetypeElement implements OdsElement {
    public static final String DOCUMENT_MIMETYPE = "application/vnd.oasis.opendocument.spreadsheet";

    @Override
    public void write(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
        final byte[] data = DOCUMENT_MIMETYPE.getBytes(CharsetUtil.UTF_8);
        final long crc32 = this.getCrc32(data);
        writer.putNextEntry(new UnregisteredStoredEntry("mimetype", data.length, crc32));
        writer.append(DOCUMENT_MIMETYPE);
        writer.closeEntry();
    }

    private long getCrc32(final byte[] data) {
        final CRC32 crc32 = new CRC32();
        crc32.update(data);
        return crc32.getValue();
    }
}
