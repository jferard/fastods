/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.fastods;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.zip.ZipOutputStream;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.style.FHTextStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.util.Util;
import com.github.jferard.fastods.util.XMLUtil;

public class OdsEntries {
	public static OdsEntries create(final Util util,
			final XMLUtil xmlUtil, final DataStyles format) {
		final MimetypeEntry mimetypeEntry = new MimetypeEntry();
		final ManifestEntry manifestEntry = new ManifestEntry();
		final SettingsEntry settingsEntry = new SettingsEntry();
		final MetaEntry metaEntry = new MetaEntry();
		final StylesEntry stylesEntry = new StylesEntry();
		final ContentEntry contentEntry = new ContentEntry(stylesEntry, xmlUtil, util,
				format);
		return new OdsEntries(mimetypeEntry, manifestEntry, settingsEntry,
				metaEntry, contentEntry, stylesEntry);
	}

	private final ContentEntry contentEntry;
	private final ManifestEntry manifestEntry;
	private final MetaEntry metaEntry;
	private final MimetypeEntry mimetypeEntry;
	private final SettingsEntry settingsEntry;

	private final StylesEntry stylesEntry;

	private OdsEntries(final MimetypeEntry mimetypeEntry,
			final ManifestEntry manifestEntry,
			final SettingsEntry settingsEntry, final MetaEntry metaEntry,
			final ContentEntry contentEntry, final StylesEntry stylesEntry) {
		this.mimetypeEntry = mimetypeEntry;
		this.manifestEntry = manifestEntry;
		this.settingsEntry = settingsEntry;
		this.metaEntry = metaEntry;
		this.contentEntry = contentEntry;
		this.stylesEntry = stylesEntry;
	}

	public void addDataStyle(final DataStyle dataStyle) {
		this.stylesEntry.addDataStyle(dataStyle);
	}

	public void addPageStyle(final PageStyle pageStyle) {
		this.stylesEntry.addPageStyle(pageStyle);
	}

	public void addStyleTag(final StyleTag styleTag) {
		this.contentEntry.addStyleTag(styleTag);
	}

	public Table addTableToContent(final String name, final int rowCapacity,
			final int columnCapacity) {
		return this.contentEntry.addTable(name, rowCapacity, columnCapacity);
	}

	public void addTextStyle(final FHTextStyle fhTextStyle) {
		this.stylesEntry.addTextStyle(fhTextStyle);
	}

	public Table getTable(final int tableIndex) {
		return this.contentEntry.getTable(tableIndex);
	}

	public Table getTable(final String name) {
		return this.contentEntry.getTable(name);
	}

	public int getTableCount() {
		return this.contentEntry.getTableCount();
	}

	public List<Table> getTables() {
		return this.contentEntry.getTables();
	}

	public void setActiveTable(final Table table) {
		this.settingsEntry.setActiveTable(table);
	}

	public void setTables() {
		this.settingsEntry.setTables(this.getTables());
	}

	public void writeEntries(final XMLUtil xmlUtil,
			final ZipOutputStream zipOut, final Writer writer)
			throws IOException {
		this.mimetypeEntry.write(xmlUtil, zipOut, writer);
		this.manifestEntry.write(xmlUtil, zipOut, writer);
		this.metaEntry.write(xmlUtil, zipOut, writer);
		this.stylesEntry.write(xmlUtil, zipOut, writer);
		this.contentEntry.write(xmlUtil, zipOut, writer);
		this.settingsEntry.write(xmlUtil, zipOut, writer);
	}

}
