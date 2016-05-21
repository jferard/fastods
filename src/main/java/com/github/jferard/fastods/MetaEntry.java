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
 * WHERE ?
 * meta.xml/office:document-meta
 */
public class MetaEntry implements OdsEntry {
	final static SimpleDateFormat DF_DATE = new SimpleDateFormat("yyyy-MM-dd");
	final static SimpleDateFormat DF_TIME = new SimpleDateFormat("HH:mm:ss");

	private String sDateTime;
	private int nTableCount = 1;
	private int nCellCount = 1;
	private String sCreator;
	
	private final String sGenerator;
	private final String sEditingCycles;
	private final String sEditingDuration;

	public MetaEntry() {
		this.setDateTimeNow();
		this.sGenerator = "FastOds 0.0.1 2016";
		this.sCreator = "FastOds 0.0.1";
		this.sEditingCycles = "1";
		this.sEditingDuration = "PT1M00S";
	}

	/**
	 * Store the date and time of the document creation in the MetaEntry data.
	 */
	private void setDateTimeNow() {
		Date dt = new Date();

		this.sDateTime = new StringBuilder(MetaEntry.DF_DATE.format(dt))
				.append("T").append(MetaEntry.DF_TIME.format(dt)).toString();
	}

	private String[] getMeta() {
		String[] sReturn = { "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
				"<office:document-meta xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\" office:version=\"1.1\">",
				"<office:meta>", "<meta:generator>", this.sGenerator,
				"</meta:generator>", "<dc:creator>", this.sCreator,
				"</dc:creator>", "<dc:date>", this.sDateTime, "</dc:date>",
				"<meta:editing-cycles>", this.sEditingCycles,
				"</meta:editing-cycles>", "<meta:editing-duration>",
				this.sEditingDuration, "</meta:editing-duration>",
				"<meta:user-defined meta:name=\"Info 1\"/>",
				"<meta:user-defined meta:name=\"Info 2\"/>",
				"<meta:user-defined meta:name=\"Info 3\"/>",
				"<meta:user-defined meta:name=\"Info 4\"/>",
				new StringBuilder(
						"<meta:document-statistic meta:table-count=\"")
								.append(this.nTableCount)
								.append("\" meta:cell-count=\"")
								.append(this.nCellCount).append("\"/>")
								.toString(),
				"</office:meta>", "</office:document-meta>"
		};
		return sReturn;
	}

	public void incTableCount() {
		this.nTableCount++;
	}

	public void incCellCount() {
		this.nCellCount++;
	}

	public void decTableCount() {
		if (this.nTableCount > 0) {
			this.nTableCount--;
		}
	}

	public void decCellCount() {
		if (this.nCellCount > 0) {
			this.nCellCount--;
		}
	}

	public String getCreator() {
		return this.sCreator;
	}

	public void setCreator(final String sCreator) {
		this.sCreator = sCreator;
	}

	@Override
	public void write(Util util, final ZipOutputStream zipOut) throws IOException {
		zipOut.putNextEntry(new ZipEntry("meta.xml"));
		Writer writer = util.wrapStream(zipOut);
		for (String item :this.getMeta())
			writer.write(item);
		writer.flush();
		zipOut.closeEntry();
	}
}