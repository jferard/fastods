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

import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.zip.ZipEntry;

/**
 * 2.2.4 OpenDocument Spreadsheet Document
 *
 * > An OpenDocument Spreadsheet document shall meet all requirements of a Conforming OpenDocument
 * > Document, as well as the following additional requirements:
 * >     ...
 * >
 * >    B)If the document is an OpenDocument package then it shall contain a file named mimetype
 * >    containing one of these strings: "application/vnd.oasis.opendocument.spreadsheet" or
 * >    "application/vnd.oasis.opendocument.spreadsheet-template".
 * >
 * >     ...
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class MimetypeElement implements OdsElement {
    @Override
    public void write(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
        writer.putNextEntry(new ZipEntry("mimetype"));
        writer.write("application/vnd.oasis.opendocument.spreadsheet");
        writer.flush();
        writer.closeEntry();
    }

}
