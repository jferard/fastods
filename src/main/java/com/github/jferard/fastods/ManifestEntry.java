/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
*    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.jferard.fastods;

import java.io.IOException;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file ManifestEntry.java is part of FastODS.
 * 
 * WHERE ?
 * META-INF/manifest.xml/manifest:manifest
 */
public class ManifestEntry implements OdsEntry {
	String[] sText = { "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>",
			"<manifest:manifest xmlns:manifest=\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0\">",
			"<manifest:file-entry manifest:media-type=\"application/vnd.oasis.opendocument.spreadsheet\" manifest:full-path=\"/\" />",

			"<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/statusbar/\" />",
			"<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/accelerator/current.xml\" />",
			"<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/accelerator/\" /> ",
			"<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/floater/\" /> ",
			"<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/popupmenu/\" />",
			"<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/progressbar/\" />",
			"<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/menubar/\" /> ",
			"<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/toolbar/\" /> ",
			"<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/images/Bitmaps/\" />",
			"<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/images/\" /> ",
			"<manifest:file-entry manifest:media-type=\"application/vnd.sun.xml.ui.configuration\" manifest:full-path=\"Configurations2/\" />",
			"<manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"content.xml\" /> ",
			"<manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"styles.xml\" /> ",
			"<manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"meta.xml\" /> ",
			"<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Thumbnails/thumbnail.png\" />",
			"<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Thumbnails/\" /> ",
			"<manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"settings.xml\" />",
			"</manifest:manifest>"

	};

	public String[] getManifest() {
		return this.sText;
	}

	@Override
	public void write(Util util, final ZipOutputStream zipOut)
			throws IOException {
		zipOut.putNextEntry(new ZipEntry("META-INF/manifest.xml"));
		Writer writer = util.wrapStream(zipOut);
		for (String item : this.getManifest())
			writer.write(item);
		writer.flush();
		zipOut.closeEntry();
	}
}
