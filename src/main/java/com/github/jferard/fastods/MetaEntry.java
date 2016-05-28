/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
*    SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file MetaEntry.java is part of FastODS.
 *
 *         WHERE ? meta.xml/office:document-meta
 */
public class MetaEntry implements OdsEntry {
	final static SimpleDateFormat DF_DATE = new SimpleDateFormat("yyyy-MM-dd");
	final static SimpleDateFormat DF_TIME = new SimpleDateFormat("HH:mm:ss");

	private int nCellCount = 1;
	private int nTableCount = 1;
	private String sCreator;
	private String sDateTime;

	private final String sEditingCycles;
	private final String sEditingDuration;
	private final String sGenerator;

	public MetaEntry() {
		this.setDateTimeNow();
		this.sGenerator = "FastOds 0.0.1 2016";
		this.sCreator = "FastOds 0.0.1";
		this.sEditingCycles = "1";
		this.sEditingDuration = "PT1M00S";
	}

	public void decCellCount() {
		if (this.nCellCount > 0) {
			this.nCellCount--;
		}
	}

	public void decTableCount() {
		if (this.nTableCount > 0) {
			this.nTableCount--;
		}
	}

	public String getCreator() {
		return this.sCreator;
	}

	public void incCellCount() {
		this.nCellCount++;
	}

	public void incTableCount() {
		this.nTableCount++;
	}

	public void setCreator(final String sCreator) {
		this.sCreator = sCreator;
	}

	@Override
	public void write(final Util util, final ZipOutputStream zipOut)
			throws IOException {
		zipOut.putNextEntry(new ZipEntry("meta.xml"));
		final Writer writer = util.wrapStream(zipOut);
		writer.append("<?xml");
		util.appendEAttribute(writer, "version", "1.0");
		util.appendEAttribute(writer, "encoding", "UTF-8");
		writer.append("?><office:document-meta");
		util.appendEAttribute(writer, "xmlns:office",
				"urn:oasis:names:tc:opendocument:xmlns:office:1.0");
		util.appendEAttribute(writer, "xmlns:xlink",
				"http://www.w3.org/1999/xlink");
		util.appendEAttribute(writer, "xmlns:dc",
				"http://purl.org/dc/elements/1.1/");
		util.appendEAttribute(writer, "xmlns:meta",
				"urn:oasis:names:tc:opendocument:xmlns:meta:1.0");
		util.appendEAttribute(writer, "xmlns:ooo",
				"http://openoffice.org/2004/office");
		util.appendEAttribute(writer, "office:version", "1.1");
		writer.append("><office:meta>");
		util.appendTag(writer, "meta:generator", this.sGenerator);
		util.appendTag(writer, "dc:creator", this.sCreator);
		util.appendTag(writer, "dc:date", this.sDateTime);
		util.appendTag(writer, "meta:editing-cycles", this.sEditingCycles);
		util.appendTag(writer, "meta:editing-duration", this.sEditingDuration);
		writer.append("<meta:user-defined meta:name=\"Info 1\"/>")
				.append("<meta:user-defined meta:name=\"Info 2\"/>")
				.append("<meta:user-defined meta:name=\"Info 3\"/>")
				.append("<meta:user-defined meta:name=\"Info 4\"/>")
				.append("<meta:document-statistic");
		util.appendAttribute(writer, "meta:table-count", this.nTableCount);
		util.appendAttribute(writer, "meta:cell-count", this.nCellCount);
		writer.append("/></office:meta>").append("</office:document-meta>");
		writer.flush();
		zipOut.closeEntry();
	}

	/**
	 * Store the date and time of the document creation in the MetaEntry data.
	 */
	private void setDateTimeNow() {
		final Date dt = new Date();

		this.sDateTime = new StringBuilder(MetaEntry.DF_DATE.format(dt))
				.append("T").append(MetaEntry.DF_TIME.format(dt)).toString();
	}
}