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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * TODO : clean code
 * @author Martin Schulz<br>
 * 
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *         <br>
 * 
 *         This file Meta.java is part of SimpleODS.
 *
 */
public class Meta {
	private String sGenerator = "SimpleOds 0.5.3 20120722";
	private String sCreator = "SimpleOds 0.5.3";
	private String sDateTime = "";
	private String sEditingCycles = "1";
	private String sEditingDuration = "PT1M00S";
	private int nTableCount = 1;
	private int nCellCount = 1;

	public Meta() {
		this.setDateTimeNow();
	}

	/**
	 * Store the date and time of the document creation in the Meta data.
	 */
	private void setDateTimeNow() {
		Date dt = new Date();
		SimpleDateFormat df_date = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df_time = new SimpleDateFormat("HH:mm:ss");

		this.sDateTime = new StringBuilder(df_date.format(dt)).append("T")
				.append(df_time.format(dt)).toString();
	}

	public String[] getMeta() {
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

	public boolean createMeta(Util util, final ZipOutputStream o) {

		try {
			o.putNextEntry(new ZipEntry("meta.xml"));
			util.writeStringArray(o, this.getMeta());
			o.closeEntry();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
