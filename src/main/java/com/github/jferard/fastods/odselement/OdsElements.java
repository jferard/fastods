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

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.PageLayoutStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TextStyle;
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
				mimetypeElement, manifestElement, settingsElement, metaElement,
				contentElement, stylesElement, stylesContainer);
	}
	private final ContentElement contentElement;
	private final Logger logger;
	private final ManifestElement manifestElement;
	private final MetaElement metaElement;
	private final MimetypeElement mimetypeElement;
	private final SettingsElement settingsElement;
	private final StylesContainer stylesContainer;
	private final StylesElement stylesElement;

	protected OdsElements(final Logger logger, final MimetypeElement mimetypeElement,
						  final ManifestElement manifestElement,
						  final SettingsElement settingsElement, final MetaElement metaElement,
						  final ContentElement contentElement, final StylesElement stylesElement,
						  final StylesContainer stylesContainer) {
		this.logger = logger;
		this.mimetypeElement = mimetypeElement;
		this.manifestElement = manifestElement;
		this.settingsElement = settingsElement;
		this.metaElement = metaElement;
		this.contentElement = contentElement;
		this.stylesElement = stylesElement;
		this.stylesContainer = stylesContainer;
	}

	public void addChildCellStyle(final TableCellStyle style, final TableCell.Type type) {
		this.contentElement.addChildCellStyle(style, type);
	}

	public void addDataStyle(final DataStyle dataStyle) {
		this.stylesContainer.addDataStyle(dataStyle);
	}

	public void addMasterPageStyle(final MasterPageStyle masterPageStyle) {
		this.stylesContainer.addMasterPageStyle(masterPageStyle);
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

	public void addStyleToContentAutomaticStyles(StyleTag styleTag) {
		this.stylesContainer.addStyleToContentAutomaticStyles(styleTag);
	}

	public Table addTableToContent(final String name, final int rowCapacity,
								   final int columnCapacity) {
		return this.contentElement.addTable(name, rowCapacity, columnCapacity);
	}

	public void createEmptyElements(final ZipUTF8Writer writer)
			throws IOException {
		this.logger.log(Level.FINER, "Writing empty ods elements to zip file");
		for (final String elementName : new String[]{"Thumbnails/",
				"Configurations2/accelerator/current.xml",
				"Configurations2/floater/", "Configurations2/images/Bitmaps/",
				"Configurations2/menubar/", "Configurations2/popupmenu/",
				"Configurations2/progressbar/", "Configurations2/statusbar/",
				"Configurations2/toolbar/"}) {
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
		this.contentElement.writePostamble(xmlUtil, writer);
	}

	public void flushRows(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
		this.contentElement.flushRows(util, writer);
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

	public void writeEditableElements(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
		this.logger.log(Level.FINER, "Writing odselement: metaElement to zip file");
		this.metaElement.write(xmlUtil, writer);
		this.logger.log(Level.FINER, "Writing odselement: stylesElement to zip file");
		this.stylesElement.write(xmlUtil, writer);
	}

	@Deprecated
	public void writeElements(final XMLUtil xmlUtil, final ZipUTF8Writer writer)
			throws IOException {
		this.writeImmutableElements(xmlUtil, writer);
		this.writeEditableElements(xmlUtil, writer);
		this.writeContent(xmlUtil, writer);
		this.writeSettings(xmlUtil, writer);
	}

	public void writeImmutableElements(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
		this.logger.log(Level.FINER,
				"Writing odselement: mimeTypeEntry to zip file");
		this.mimetypeElement.write(xmlUtil, writer);
		this.logger.log(Level.FINER,
				"Writing odselement: manifestElement to zip file");
		this.manifestElement.write(xmlUtil, writer);
	}

	public void writeSettings(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
		this.settingsElement.setTables(this.getTables());
		this.logger.log(Level.FINER,
				"Writing odselement: settingsElement to zip file");
		this.settingsElement.write(xmlUtil, writer);
	}
}
