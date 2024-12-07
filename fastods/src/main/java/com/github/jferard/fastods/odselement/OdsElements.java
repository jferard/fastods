/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.FinalizeFlusher;
import com.github.jferard.fastods.ImmutableElementsFlusher;
import com.github.jferard.fastods.NamedOdsFileWriter;
import com.github.jferard.fastods.PrepareContentFlusher;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.attribute.CellType;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.odselement.config.ConfigElement;
import com.github.jferard.fastods.odselement.config.ConfigItem;
import com.github.jferard.fastods.odselement.config.ConfigItemMapEntry;
import com.github.jferard.fastods.ref.PositionUtil;
import com.github.jferard.fastods.style.FontFaceContainerStyle;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.ObjectStyle;
import com.github.jferard.fastods.style.PageLayoutStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The OdsElements class is simply a facade in front of various OdsElement classes
 * (ContentElement, StylesElement, ...).
 * See GOF Facade pattern.
 * <p>
 * Contains method for flush and async flush:
 * <ul>
 * <li>all add...Style methods are used to declare the styles before flushing data</li>
 * </ul>
 *
 * @author Julien Férard
 */
public class OdsElements implements StylesContainer {

    /**
     * LO only: do not freeze cells
     */
    public static final String SC_SPLIT_NORMAL = "0";

    /**
     * LO only: freeze cells
     */
    public static final String SC_SPLIT_FIX = "2";
    public static final Map<String, String> BASE_NAMESPACE_BY_PREFIX =
            new HashMap<>();

    private static final OdsElement[] EMPTY_ELEMENTS = {
            new ManifestEntryElement(
                    new StandardOdsEntry("/", MimetypeElement.DOCUMENT_MIMETYPE, "1.2")),
            new ManifestEntryElement(new StandardOdsEntry("Configurations2/",
                    "application/vnd.sun.xml.ui.configuration", null)),
            new EmptyElement(
                    new StandardOdsEntry("Configurations2/statusbar/", "", null)),
            new ManifestEntryElement(
                    new StandardOdsEntry("Configurations2/accelerator/", "", null)),
            new EmptyElement(
                    new StandardOdsEntry("Configurations2/accelerator/current.xml", "", null)),
            new EmptyElement(
                    new StandardOdsEntry("Configurations2/floater/", "", null)),
            new EmptyElement(
                    new StandardOdsEntry("Configurations2/popupmenu/", "", null)),
            new EmptyElement(
                    new StandardOdsEntry("Configurations2/progressbar/", "", null)),
            new EmptyElement(
                    new StandardOdsEntry("Configurations2/menubar/", "", null)),
            new EmptyElement(
                    new StandardOdsEntry("Configurations2/toolbar/", "", null)),
            new ManifestEntryElement(
                    new StandardOdsEntry("Configurations2/images/", "", null)),
            new EmptyElement(
                    new StandardOdsEntry("Configurations2/images/Bitmaps/", "", null)),
            new EmptyElement(new StandardOdsEntry("Thumbnails/", "", null)),
//            new StandardManifestEntry("Thumbnails/thumbnail.png", "", null)
    };

    static {
        BASE_NAMESPACE_BY_PREFIX.put("xmlns:office",
                "urn:oasis:names:tc:opendocument:xmlns:office:1.0");
        BASE_NAMESPACE_BY_PREFIX.put("xmlns:xlink", "http://www.w3.org/1999/xlink");
        BASE_NAMESPACE_BY_PREFIX.put("xmlns:ooo", "http://openoffice.org/2004/office");
    }

    /**
     * @param positionUtil                an util for cell addresses (e.g. "A1")
     * @param xmlUtil                     an XML util
     * @param cache                       an util for write
     * @param format                      the data styles
     * @param libreOfficeMode             try to get full compatibility with LO if true
     * @param metaElement                 meta.xml representation
     * @param additionalNamespaceByPrefix a map: prefix to NS
     * @return a new OdsElements, with newly build elements.
     */
    public static OdsElements create(final PositionUtil positionUtil, final XMLUtil xmlUtil,
                                     final IntegerRepresentationCache cache,
                                     final DataStyles format,
                                     final boolean libreOfficeMode, final MetaElement metaElement,
                                     final Map<String, String> additionalNamespaceByPrefix) {
        final Logger logger = Logger.getLogger(OdsElements.class.getName());
        final MimetypeElement mimetypeElement = new MimetypeElement();
        final ManifestElement manifestElement = ManifestElement.create();
        final SettingsElement settingsElement = SettingsElement.create();
        final StylesContainerImpl stylesContainer = new StylesContainerImpl(logger);
        final StylesElement stylesElement = new StylesElement(stylesContainer);
        final ContentElement contentElement =
                new ContentElement(positionUtil, xmlUtil, cache, format, libreOfficeMode,
                        stylesContainer, additionalNamespaceByPrefix);
        return new OdsElements(logger, stylesContainer, mimetypeElement, manifestElement,
                settingsElement, metaElement, contentElement, stylesElement);
    }

    private final ContentElement contentElement;
    private final Logger logger;
    private final ManifestElement manifestElement;
    private final MetaElement metaElement;
    private final MimetypeElement mimeTypeElement;
    private final SettingsElement settingsElement;
    private final StylesContainerImpl stylesContainer;
    private final StylesElement stylesElement;
    private final Set<OdsElement> extraElements;
    private NamedOdsFileWriter observer;

    /**
     * Create a new instance from elements
     *
     * @param logger          the logger
     * @param stylesContainer the styles container (before dispatch to styles.xml and content.xml)
     * @param mimeTypeElement the mime type element
     * @param manifestElement the manifest element
     * @param settingsElement the settings.xml element
     * @param metaElement     the meta element
     * @param contentElement  the content.xml element
     * @param stylesElement   the styles.xml element
     */
    OdsElements(final Logger logger, final StylesContainerImpl stylesContainer,
                final MimetypeElement mimeTypeElement, final ManifestElement manifestElement,
                final SettingsElement settingsElement, final MetaElement metaElement,
                final ContentElement contentElement, final StylesElement stylesElement) {
        this.logger = logger;
        this.mimeTypeElement = mimeTypeElement;
        this.manifestElement = manifestElement;
        this.settingsElement = settingsElement;
        this.metaElement = metaElement;
        this.contentElement = contentElement;
        this.stylesElement = stylesElement;
        this.stylesContainer = stylesContainer;
        this.extraElements = new HashSet<>();
    }

    /**
     * The OdsElements is observable by a writer.
     *
     * @param o the file writer
     */
    public void addObserver(final NamedOdsFileWriter o) {
        this.observer = o;
    }


    /**
     * Add a cell style for a given data type. Use only if you want to flush data before the end
     * of the document
     * construction.
     * Do not produce any effect if the type is Type.STRING or Type.VOID
     *
     * @param style the style
     * @param types the types
     */
    public void addCellStyle(final TableCellStyle style, final CellType... types) {
        this.stylesContainer.addContentStyle(style);
        for (final CellType type : types) {
            this.contentElement.addChildCellStyle(style, type);
        }
    }

    @Override
    public boolean addDataStyle(final DataStyle dataStyle) {
        return this.stylesContainer.addDataStyle(dataStyle);
    }

    @Override
    public void setDataStylesMode(final Container.Mode mode) {
        this.stylesContainer.setDataStylesMode(mode);
    }

    @Override
    public boolean addMasterPageStyle(final MasterPageStyle masterPageStyle) {
        return this.stylesContainer.addMasterPageStyle(masterPageStyle);
    }

    @Override
    public void setMasterPageStyleMode(final Container.Mode mode) {
        this.stylesContainer.setMasterPageStyleMode(mode);
    }

    @Override
    public boolean addNewDataStyleFromCellStyle(final TableCellStyle style) {
        return this.stylesContainer.addNewDataStyleFromCellStyle(style);
    }

    @Override
    public void setPageLayoutStyleMode(final Container.Mode mode) {
        this.stylesContainer.setPageLayoutStyleMode(mode);
    }

    @Override
    public boolean addPageLayoutStyle(final PageLayoutStyle pageLayoutStyle) {
        return this.stylesContainer.addPageLayoutStyle(pageLayoutStyle);
    }

    @Override
    public void setPageStyleMode(final Container.Mode mode) {
        this.stylesContainer.setPageStyleMode(mode);
    }

    @Override
    public boolean addPageStyle(final PageStyle ps) {
        return this.stylesContainer.addPageStyle(ps);
    }

    @Override
    public void setObjectStyleMode(final Container.Mode mode) {
        this.stylesContainer.setObjectStyleMode(mode);
    }

    @Override
    public boolean addContentStyle(final ObjectStyle objectStyle) {
        return this.stylesContainer.addContentStyle(objectStyle);
    }

    @Override
    public boolean addStylesStyle(final ObjectStyle objectStyle) {
        return this.stylesContainer.addStylesStyle(objectStyle);
    }

    @Override
    public TableCellStyle addChildCellStyle(final TableCellStyle style, final DataStyle dataStyle) {
        return this.stylesContainer.addChildCellStyle(style, dataStyle);
    }

    @Override
    public boolean addContentFontFaceContainerStyle(final FontFaceContainerStyle objectStyle) {
        return this.stylesContainer.addContentFontFaceContainerStyle(objectStyle);
    }

    @Override
    public boolean addStylesFontFaceContainerStyle(final FontFaceContainerStyle objectStyle) {
        return this.stylesContainer.addStylesFontFaceContainerStyle(objectStyle);
    }

    /**
     * Create empty elements for package. Used on save or by the ImmutableElementsFlusher.
     *
     * @param util   an xml util
     * @param writer destination
     * @throws IOException if the elements were not created.
     */
    public void createEmptyElements(final XMLUtil util, final ZipUTF8Writer writer)
            throws IOException {
        this.logger.log(Level.FINER, "Writing empty ods elements to zip file");
        for (final OdsElement element : EMPTY_ELEMENTS) {
            this.logger.log(Level.FINEST, "Writing ods element: {0} to zip file", element);
            element.write(util, writer);
        }
    }

    /**
     * Activate style debugging (for flushers)
     */
    public void debugStyles() {
        this.stylesContainer.debug();
    }

    /**
     * Freeze the styles: adding a new style to the container will generate an IllegalStateException
     */
    public void freezeStyles() {
        this.stylesContainer.freeze();
    }

    /**
     * Add a new table to content. The config for this table is added to the settings.
     * If the OdsElements is observed the previous table is async flushed. If there
     * is no previous table, meta and styles are async flushed.
     * If there is no previous table, meta.xml, styles.xml and the preamble of content.xml
     * are written to destination.
     *
     * @param name           name of the table
     * @param rowCapacity    estimated rows
     * @param columnCapacity estimated columns
     * @return the table or null
     * @throws IOException if the OdsElements is observed and there is a write exception
     */
    @Deprecated
    public Table addTableToContent(final String name, final int rowCapacity,
                                   final int columnCapacity) throws IOException {
        final Table table = this.contentElement.createTable(name, rowCapacity, columnCapacity);
        if (this.addTableToContent(table)) {
            return table;
        } else {
            return null;
        }
    }

    /**
     * Add a new table to content. The config for this table is added to the settings.
     * If the OdsElements is observed the previous table is async flushed. If there
     * is no previous table, meta and styles are async flushed.
     * If there is no previous table, meta.xml, styles.xml and the preamble of content.xml
     * are written to destination.
     *
     * @param table the table
     * @return true if the table was added
     * @throws IOException if the OdsElements is observed and there is a write exception
     */
    public boolean addTableToContent(final Table table) throws IOException {
        final Table previousTable = this.contentElement.getLastTable();
        final boolean add = this.contentElement.addTable(table);
        if (add) {
            this.settingsElement.addTableConfig(table.getConfigEntry());
            if (this.observer != null) {
                this.asyncFlushPreviousTable(previousTable, table);
            }
        }
        return add;
    }

    public Table createTable(final String name, final int rowCapacity, final int columnCapacity) {
        return this.contentElement.createTable(name, rowCapacity, columnCapacity);
    }


    /**
     * flush everything up to the new table excluded: the previous table is async flushed. If there
     * is no previous table, meta and styles are async flushed.
     *
     * @param previousTable the previous table
     * @param table         the table
     */
    private void asyncFlushPreviousTable(final Table previousTable, final Table table)
            throws IOException {
        table.addObserver(this.observer);
        if (previousTable == null) {
            this.observer.update(new PrepareContentFlusher(this, this.contentElement));
        } else {
            previousTable.asyncFlushEndTable();
        }
    }

    /**
     * Prepare the elements for writing.
     * Performs an async flush.
     *
     * @throws IOException if the preparation fails
     */
    public void prepareAsync() throws IOException {
        this.observer.update(new ImmutableElementsFlusher(this));
    }

    /**
     * Save the elements, the file is already open. (launches async flushes)
     *
     * @throws IOException if the write fails
     */
    public void saveAsync() throws IOException {
        final Table previousTable = this.contentElement.getLastTable();
        if (previousTable == null) {
            this.observer.update(new PrepareContentFlusher(this, this.contentElement));
        } else {
            previousTable.asyncFlushEndTable();
        }

        this.observer.update(new FinalizeFlusher(this, this.contentElement));
    }

    /**
     * Write the content element to a writer.
     *
     * @param xmlUtil the xml util
     * @param writer  the writer
     * @throws IOException if write fails
     */
    public void writeContent(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
        this.logger.log(Level.FINER, "Writing ods element: contentElement to zip file");
        this.contentElement.write(xmlUtil, writer);
    }

    /**
     * Write the meta element to a writer.
     *
     * @param xmlUtil the xml util
     * @param writer  the writer
     * @throws IOException if write fails
     */
    public void writeMeta(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
        this.logger.log(Level.FINER, "Writing ods element: metaElement to zip file");
        this.metaElement.write(xmlUtil, writer);
    }

    /**
     * Write the settings element to a writer.
     *
     * @param xmlUtil the xml util
     * @param writer  the writer
     * @throws IOException if write fails
     */
    public void writeSettings(final XMLUtil xmlUtil, final ZipUTF8Writer writer)
            throws IOException {
        this.settingsElement.setTables(this.getTables());
        this.logger.log(Level.FINER, "Writing ods element: settingsElement to zip file");
        this.settingsElement.write(xmlUtil, writer);
    }

    /**
     * Write the styles element to a writer.
     *
     * @param xmlUtil the xml util
     * @param writer  the writer
     * @throws IOException if write fails
     */
    public void writeStyles(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
        this.logger.log(Level.FINER, "Writing ods element: stylesElement to zip file");
        this.stylesElement.write(xmlUtil, writer);
    }


    /**
     * Freeze cells. See https://help.libreoffice.org/Calc/Freezing_Rows_or_Columns_as_Headers
     *
     * @param table    the table to freeze
     * @param rowCount the number of rows to freeze (e.g. 1 to freeze the first row)
     * @param colCount the number of cols to freeze.
     */
    public void freezeCells(final Table table, final int rowCount, final int colCount) {
        final ConfigItemMapEntry tableConfig = table.getConfigEntry();
        tableConfig.put(ConfigItem.create(ConfigElement.HORIZONTAL_SPLIT_MODE, SC_SPLIT_FIX));
        tableConfig.put(ConfigItem.create(ConfigElement.VERTICAL_SPLIT_MODE, SC_SPLIT_FIX));
        final String colCountStr = String.valueOf(colCount);
        tableConfig.put(ConfigItem.create(ConfigElement.HORIZONTAL_SPLIT_POSITION, colCountStr));
        tableConfig.put(ConfigItem.create(ConfigElement.POSITION_RIGHT, colCountStr));
        final String rowCountStr = String.valueOf(rowCount);
        tableConfig.put(ConfigItem.create(ConfigElement.VERTICAL_SPLIT_POSITION, rowCountStr));
        tableConfig.put(ConfigItem.create(ConfigElement.POSITION_BOTTOM, rowCountStr));
    }

    /**
     * Return a table from an index
     *
     * @param tableIndex the index
     * @return the table
     */
    public Table getTable(final int tableIndex) {
        return this.contentElement.getTable(tableIndex);
    }

    /**
     * Return a table from a name
     *
     * @param name the name
     * @return the table
     */
    public Table getTable(final String name) {
        return this.contentElement.getTable(name);
    }

    /**
     * @return the table count
     */
    public int getTableCount() {
        return this.contentElement.getTableCount();
    }

    /**
     * @return the list of tables
     */
    public List<Table> getTables() {
        return this.contentElement.getTables();
    }

    /**
     * Set a new active table
     *
     * @param table the table
     */
    public void setActiveTable(final Table table) {
        this.settingsElement.setActiveTable(table);
    }

    /**
     * Set a view setting
     *
     * @param viewId the id of the view
     * @param item   the item name
     * @param value  the item value
     */
    public void setViewSetting(final String viewId, final String item, final String value) {
        this.settingsElement.setViewSetting(viewId, item, value);
    }

    /**
     * Add an AutoFilter to a table
     *
     * @param autoFilter the filter
     */
    public void addAutoFilter(final AutoFilter autoFilter) {
        this.contentElement.addAutoFilter(autoFilter);
    }

    /**
     * Add a new named range to the document
     *
     * @param namedRange the named range
     */
    public void addNamedRange(final NamedRange namedRange) {
        this.contentElement.addNamedRange(namedRange);
    }

    /**
     * Add an extra file
     *
     * @param fullPath  the name of the file in the sequence
     * @param mediaType the MIME type
     * @param data      the content
     */
    public void addExtraFile(final String fullPath, final String mediaType, final byte[] data) {
        final OdsElement element =
                new ExtraElement(new StandardOdsEntry(fullPath, mediaType, null), data);
        this.extraElements.add(element);
    }

    /**
     * Add an extra dir. If the directoy does not end with a slash,
     * add it.
     *
     * @param fullDirectoryPath the path of the dir
     */
    public void addExtraDir(final String fullDirectoryPath) {
        final String fullPath = this.pathWithSlash(fullDirectoryPath);
        final ManifestEntryElement element = new ManifestEntryElement(
                new StandardOdsEntry(fullPath, "", null));
        this.extraElements.add(element);
    }

    private String pathWithSlash(final String fullDirectoryPath) {
        if (fullDirectoryPath.endsWith("/")) {
            return fullDirectoryPath;
        } else {
            return fullDirectoryPath + "/";
        }
    }

    /**
     * Add an extra object (eg. a spreadsheet)
     *
     * @param fullDirectoryPath the path of the dir
     * @param mediaType         the type of the object
     * @param version           the version
     */
    public void addExtraObjectReference(final String fullDirectoryPath, final String mediaType,
                                        final String version) {
        final String fullPath = this.pathWithSlash(fullDirectoryPath);
        final ManifestEntryElement element = new ManifestEntryElement(
                new StandardOdsEntry(fullPath, mediaType, version));
        this.extraElements.add(element);
    }

    /**
     * @param xmlUtil the XML util instance.
     * @param writer  write the extra files to the archive
     * @throws IOException if something can"t be written
     */
    public void writeExtras(final XMLUtil xmlUtil,
                            final ZipUTF8Writer writer) throws IOException {
        this.logger.log(Level.FINER, "Writing extra elements to zip file");
        for (final OdsElement element : this.extraElements) {
            this.logger.log(Level.FINEST, "Writing ods element: {0} to zip file", element);
            element.write(xmlUtil, writer);
        }
    }

    /**
     * Write the mimetype element to a writer.
     *
     * @param xmlUtil the xml util
     * @param writer  the writer
     * @throws IOException if write fails
     */
    public void writeMimeType(final XMLUtil xmlUtil, final ZipUTF8Writer writer)
            throws IOException {
        this.logger.log(Level.FINER, "Writing ods element: mimeTypeEntry to zip file");
        this.mimeTypeElement.write(xmlUtil, writer);
    }

    /**
     * Add some events to the document
     *
     * @param events the events to add
     */
    public void addEvents(final ScriptEventListener... events) {
        this.contentElement.addEvents(events);
    }

    /**
     * Add a new pilot table
     *
     * @param pilotTable the pilot table
     */
    public void addPilotTable(final PilotTable pilotTable) {
        this.contentElement.addPilotTable(pilotTable);
    }
}