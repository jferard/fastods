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
package com.github.jferard.fastods.entry;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

/**
 * The OdsEntries class is simply a facade in front of OdsEntry classes. See GOF
 * Facade pattern.
 *
 * @author Julien Férard
 *
 */
public class OdsEntries {
	public static OdsEntries create(final PositionUtil positionUtil,
			final XMLUtil xmlUtil, final DataStyles format) {
		final MimetypeEntry mimetypeEntry = new MimetypeEntry();
		final ManifestEntry manifestEntry = new ManifestEntry();
		final SettingsEntry settingsEntry = new SettingsEntry();
		final MetaEntry metaEntry = new MetaEntry();
		final StylesEntry stylesEntry = new StylesEntry();
		final ContentEntry contentEntry = new ContentEntry(stylesEntry,
				positionUtil, xmlUtil, format);
		return new OdsEntries(Logger.getLogger(OdsEntries.class.getName()),
				mimetypeEntry, manifestEntry, settingsEntry, metaEntry,
				contentEntry, stylesEntry);
	}

	private final ContentEntry contentEntry;
	private final Logger logger;
	private final ManifestEntry manifestEntry;
	private final MetaEntry metaEntry;
	private final MimetypeEntry mimetypeEntry;

	private final SettingsEntry settingsEntry;
	private final StylesEntry stylesEntry;

	protected OdsEntries(final Logger logger, final MimetypeEntry mimetypeEntry,
			final ManifestEntry manifestEntry,
			final SettingsEntry settingsEntry, final MetaEntry metaEntry,
			final ContentEntry contentEntry, final StylesEntry stylesEntry) {
		this.logger = logger;
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

	public void addTextStyle(final TextStyle fhTextStyle) {
		this.stylesEntry.addTextStyle(fhTextStyle);
	}

	public void createEmptyEntries(final ZipUTF8Writer writer)
			throws IOException {
		this.logger.log(Level.FINER, "Writing empty entries to zip file");
		for (final String entryName : new String[] { "Thumbnails/",
				"Configurations2/accelerator/current.xml",
				"Configurations2/floater/", "Configurations2/images/Bitmaps/",
				"Configurations2/menubar/", "Configurations2/popupmenu/",
				"Configurations2/progressbar/", "Configurations2/statusbar/",
				"Configurations2/toolbar/" }) {
			this.logger.log(Level.FINEST, "Writing entry: {0} to zip file",
					entryName);
			writer.putNextEntry(new ZipEntry(entryName));
			writer.closeEntry();
		}
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

	/**
	 * @return the list of tables
	 */
	public List<Table> getTables() {
		return this.contentEntry.getTables();
	}

	public void setActiveTable(final Table table) {
		this.settingsEntry.setActiveTable(table);
	}

	public void setTables() {
		this.settingsEntry.setTables(this.getTables());
	}

	public void writeEntries(final XMLUtil xmlUtil, final ZipUTF8Writer writer)
			throws IOException {
		this.logger.log(Level.FINER,
				"Writing entry: mimeTypeEntry to zip file");
		this.mimetypeEntry.write(xmlUtil, writer);
		this.logger.log(Level.FINER,
				"Writing entry: manifestEntry to zip file");
		this.manifestEntry.write(xmlUtil, writer);
		this.logger.log(Level.FINER, "Writing entry: metaEntry to zip file");
		this.metaEntry.write(xmlUtil, writer);
		this.logger.log(Level.FINER, "Writing entry: stylesEntry to zip file");
		this.stylesEntry.write(xmlUtil, writer);
		this.logger.log(Level.FINER, "Writing entry: contentEntry to zip file");
		this.contentEntry.write(xmlUtil, writer);
		this.logger.log(Level.FINER,
				"Writing entry: settingsEntry to zip file");
		this.settingsEntry.write(xmlUtil, writer);
	}
}
