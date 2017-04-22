/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.FinalizeFlusher;
import com.github.jferard.fastods.ImmutableElementsFlusher;
import com.github.jferard.fastods.MetaAndStylesElementsFlusher;
import com.github.jferard.fastods.OdsFileWriter;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.PageLayoutStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

/**
 * The OdsElements class is simply a facade in front of OdsElement classes. See GOF
 * Facade pattern.
 *
 * @author Julien Férard
 */
public class OdsElements {
	private static final String[] EMPTY_ELEMENT_NAMES = {"Thumbnails/",
            "Configurations2/accelerator/current.xml",
            "Configurations2/floater/", "Configurations2/images/Bitmaps/",
            "Configurations2/menubar/", "Configurations2/popupmenu/",
            "Configurations2/progressbar/", "Configurations2/statusbar/",
            "Configurations2/toolbar/"};

	/**
	 * @param positionUtil an util for cell addresses (e.g. "A1")
	 * @param xmlUtil an XML util
	 * @param writeUtil an util for write
	 * @param format the data styles
	 * @return a new OdsElements, with newly build elements.
	 */
	public static OdsElements create(final PositionUtil positionUtil,
									 final XMLUtil xmlUtil, final WriteUtil writeUtil,
									 final DataStyles format) {
		final MimetypeElement mimetypeElement = new MimetypeElement();
		final ManifestElement manifestElement = new ManifestElement();
		final SettingsElement settingsElement = SettingsElement.create();
		final MetaElement metaElement = new MetaElement();
		final StylesContainer stylesContainer = new StylesContainer();
		final StylesElement stylesElement = new StylesElement(stylesContainer);
		final ContentElement contentElement = new ContentElement(positionUtil,
				xmlUtil, writeUtil, format, stylesContainer);
		return new OdsElements(Logger.getLogger(OdsElements.class.getName()),
				stylesContainer, mimetypeElement, manifestElement, settingsElement, metaElement,
				contentElement, stylesElement);
	}

	private final ContentElement contentElement;
	private final Logger logger;
	private final ManifestElement manifestElement;
	private final MetaElement metaElement;
	private final MimetypeElement mimetypeElement;
	private final SettingsElement settingsElement;
	private final StylesContainer stylesContainer;
	private final StylesElement stylesElement;
	private OdsFileWriter observer;

	/**
	 * Create a new instance from elements
	 * @param logger
	 * @param stylesContainer
	 * @param mimetypeElement
	 * @param manifestElement
	 * @param settingsElement
	 * @param metaElement
	 * @param contentElement
	 * @param stylesElement
	 */
	private OdsElements(final Logger logger, final StylesContainer stylesContainer, final MimetypeElement mimetypeElement,
						final ManifestElement manifestElement,
						final SettingsElement settingsElement, final MetaElement metaElement,
						final ContentElement contentElement, final StylesElement stylesElement) {
		this.logger = logger;
		this.mimetypeElement = mimetypeElement;
		this.manifestElement = manifestElement;
		this.settingsElement = settingsElement;
		this.metaElement = metaElement;
		this.contentElement = contentElement;
		this.stylesElement = stylesElement;
		this.stylesContainer = stylesContainer;
	}

	/**
	 * Create an automatic style for this TableCellStyle and this type of cell.
	 * @param style the style of the cell (color, data style, etc.)
	 * @param type the type of the cell
	 */
	public void addChildCellStyle(final TableCellStyle style, final TableCell.Type type) {
		this.contentElement.addChildCellStyle(style, type);
	}

	/**
	 * Create a new data style into styles container. No duplicate style name is allowed.
	 * @param dataStyle the data style to add
	 */
	public void addDataStyle(final DataStyle dataStyle) {
		this.stylesContainer.addDataStyle(dataStyle);
	}

	/**
	 * Create a new master page style into styles container. No duplicate style name is allowed.
	 * @param masterPageStyle the data style to add
	 * @return true if the style was created
	 */
	public void addMasterPageStyle(final MasterPageStyle masterPageStyle) {
		this.stylesContainer.addMasterPageStyle(masterPageStyle);
	}

	/**
	 * The OdsElements is observable by a writer.
	 * @param o the file writer
	 */
	public void addObserver(final OdsFileWriter o) {
		this.observer = o;
	}

	public void addPageLayoutStyle(final PageLayoutStyle pageLayoutStyle) {
		this.stylesContainer.addPageLayoutStyle(pageLayoutStyle);
	}

	public void addPageStyle(final PageStyle ps) {
		this.stylesContainer.addPageStyle(ps);
	}

	public void addStyleTag(final StyleTag styleTag) {
		final String family = styleTag.getFamily();
		if ("table-cell".equals(family))
			this.stylesContainer.addStyleToStylesCommonStyles(styleTag);
		else if ("text".equals(family))
			this.stylesContainer.addStyleToStylesAutomaticStyles(styleTag);
		else
			this.stylesContainer.addStyleToContentAutomaticStyles(styleTag);
	}

	public void addStyleToContentAutomaticStyles(final StyleTag styleTag) {
		this.stylesContainer.addStyleToContentAutomaticStyles(styleTag);
	}

	/**
	 * Add a new table to content. The config for this table is added to the settings.
	 * If the OdsElements is observed, the previous table is flushed. If there is no previous table,
	 * meta.xml, styles.xml and the preamble of content.xml are written to destination.
	 * @param name name of the table
	 * @param rowCapacity estimated rows
	 * @param columnCapacity estimated columns
	 * @return the table
	 * @throws IOException if the OdsElements is observed and there is a write exception
	 */
	public Table addTableToContent(final String name, final int rowCapacity,
								   final int columnCapacity) throws IOException {
		final Table table = this.contentElement.addTable(name, rowCapacity, columnCapacity);
		this.settingsElement.addTableConfig(table.getConfigEntry());
		if (this.observer != null) {
			final Table previousTable = this.contentElement.getLastTable();
			if (previousTable == null)
				this.observer.update(new MetaAndStylesElementsFlusher(this, this.contentElement));
			else
				previousTable.flush();
			table.addObserver(this.observer);
		}
		return table;
	}

	/**
	 * Create empty elements for package. Used on save or by the ImmutableElementsFlusher.
	 * @param writer destination
	 * @throws IOException if the elements were not created.
	 */
	public void createEmptyElements(final ZipUTF8Writer writer)
			throws IOException {
		this.logger.log(Level.FINER, "Writing empty ods elements to zip file");
		for (final String elementName : EMPTY_ELEMENT_NAMES) {
			this.logger.log(Level.FINEST, "Writing odselement: {0} to zip file",
					elementName);
			writer.putNextEntry(new ZipEntry(elementName));
			writer.closeEntry();
		}
	}

	public void debugStyles() {
		this.stylesContainer.debug();
	}

	public void finalizeContent(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
		this.contentElement.flushTables(xmlUtil, writer);
		this.contentElement.writePostamble(xmlUtil, writer);
	}

	public void flushRows(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
		this.contentElement.flushRows(util, writer, this.settingsElement);
	}

	public void flushTables(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
		this.contentElement.flushTables(util, writer);
	}

	public void freezeStyles() {
		this.stylesContainer.freeze();
	}

	public Table getTable(final int tableIndex) {
		return this.contentElement.getTable(tableIndex);
	}

	public Table getTable(final String name) {
		return this.contentElement.getTable(name);
	}

	public int getTableCount() {
		return this.contentElement.getTableCount();
	}

	/**
	 * @return the list of tables
	 */
	public List<Table> getTables() {
		return this.contentElement.getTables();
	}

	public void prepare() throws IOException {
		this.observer.update(new ImmutableElementsFlusher(this));
	}

	public void save() throws IOException {
		final Table previousTable = this.contentElement.getLastTable();
		if (previousTable != null)
			previousTable.flush();

		this.observer.update(new FinalizeFlusher(this.contentElement, this.settingsElement));
	}

	public void setActiveTable(final Table table) {
		this.settingsElement.setActiveTable(table);
	}

	public void setViewSettings(final String viewId, final String item, final String value) {
		this.settingsElement.setViewSettings(viewId, item, value);
	}

	public void writeContent(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
		this.logger.log(Level.FINER, "Writing odselement: contentElement to zip file");
		this.contentElement.write(xmlUtil, writer);
	}

	public void writeImmutableElements(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
		this.logger.log(Level.FINER,
				"Writing odselement: mimeTypeEntry to zip file");
		this.mimetypeElement.write(xmlUtil, writer);
		this.logger.log(Level.FINER,
				"Writing odselement: manifestElement to zip file");
		this.manifestElement.write(xmlUtil, writer);
	}

	public void writeMeta(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
		this.logger.log(Level.FINER, "Writing odselement: metaElement to zip file");
		this.metaElement.write(xmlUtil, writer);
	}

	public void writeSettings(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
		this.settingsElement.setTables(this.getTables());
		this.logger.log(Level.FINER,
				"Writing odselement: settingsElement to zip file");
		this.settingsElement.write(xmlUtil, writer);
	}

	public void writeStyles(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
		this.logger.log(Level.FINER, "Writing odselement: stylesElement to zip file");
		this.stylesElement.write(xmlUtil, writer);
	}

	public void addAutofilter(final Table table, final int r1, final int c1, final int r2, final int c2) {
		this.contentElement.addAutofilter(table, r1, c1, r2, c2);
	}
}
