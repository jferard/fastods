/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.attribute.CellType;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.ref.PositionUtil;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.AutoFilter;
import com.github.jferard.fastods.util.PilotTable;
import com.github.jferard.fastods.util.UniqueList;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.jferard.fastods.odselement.MetaElement.OFFICE_VERSION;

/**
 * See 3.1.3.2 <office:document-content>.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class ContentElement implements OdsElement {
    private static final Map<String, String> CONTENT_NAMESPACE_BY_PREFIX = new HashMap<String, String>();

    static {
        CONTENT_NAMESPACE_BY_PREFIX.putAll(StylesElement.STYLES_NAMESPACE_BY_PREFIX);

        CONTENT_NAMESPACE_BY_PREFIX.put("xmlns:xforms", "http://www.w3.org/2002/xforms");
        CONTENT_NAMESPACE_BY_PREFIX.put("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
        CONTENT_NAMESPACE_BY_PREFIX.put("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        CONTENT_NAMESPACE_BY_PREFIX.put("xmlns:of", "urn:oasis:names:tc:opendocument:xmlns:of:1.2");
    }

    private final FlushPosition flushPosition;
    private final DataStyles format;
    private final PositionUtil positionUtil;
    private final StylesContainerImpl stylesContainer;
    private final UniqueList<Table> tables;
    private final WriteUtil writeUtil;
    private final XMLUtil xmlUtil;
    private final boolean libreOfficeMode;
    private final List<ScriptEventListener> scriptEvents;
    private List<AutoFilter> autoFilters;
    private List<PilotTable> pilotTables;
    private final Map<String, String> additionalNamespaceByPrefix;

    /**
     * @param positionUtil    an util object for positions (e.g. "A1")
     * @param xmlUtil         an util object to write xml
     * @param writeUtil       an util to compute some data
     * @param format          the format for data styles
     * @param libreOfficeMode try to get full compatibility with LO if true
     * @param stylesContainer a styles container.
     * @param additionalNamespaceByPrefix a map prefix -> namespace
     */
    ContentElement(final PositionUtil positionUtil, final XMLUtil xmlUtil,
                   final WriteUtil writeUtil, final DataStyles format,
                   final boolean libreOfficeMode, final StylesContainerImpl stylesContainer,
                   final Map<String, String> additionalNamespaceByPrefix) {
        this.writeUtil = writeUtil;
        this.xmlUtil = xmlUtil;
        this.positionUtil = positionUtil;
        this.format = format;
        this.libreOfficeMode = libreOfficeMode;
        this.stylesContainer = stylesContainer;
        this.additionalNamespaceByPrefix = additionalNamespaceByPrefix;
        this.tables = new UniqueList<Table>();
        this.flushPosition = new FlushPosition();
        this.scriptEvents = new ArrayList<ScriptEventListener>();
    }

    /**
     * Create an automatic style for this TableCellStyle and this type of cell.
     * Do not produce any effect if the type is Type.STRING or Type.VOID.
     *
     * @param style the style of the cell (color, data style, etc.)
     * @param type  the type of the cell
     * @return the created style, or style if the type is Type.STRING or Type.VOID
     */
    public TableCellStyle addChildCellStyle(final TableCellStyle style, final CellType type) {
        final TableCellStyle newStyle;
        final DataStyle dataStyle = this.format.getDataStyle(type);
        if (dataStyle == null) {
            newStyle = style;
        } else {
            newStyle = this.stylesContainer.addChildCellStyle(style, dataStyle);
        }
        return newStyle;
    }

    /**
     * @param name           the name of the table to create
     * @param columnCapacity the initial capacity in columns: this will be allocated at table
     *                       creation
     * @param rowCapacity    the initial capacity in rows: this will be allocated at table creation
     * @return the table (whether it existed before call or not). Never null
     * @deprecated use `addTable(table)`
     */
    @Deprecated
    public Table addTable(final String name, final int rowCapacity, final int columnCapacity) {
        Table table = this.tables.getByName(name);
        if (table == null) {
            table = Table.create(this, this.positionUtil, this.writeUtil, this.xmlUtil, name,
                    rowCapacity, columnCapacity, this.stylesContainer, this.format,
                    this.libreOfficeMode);
            this.tables.add(table);
        }
        return table;
    }

    /**
     * @param table the table
     * @return true if the table was added
     */
    public boolean addTable(final Table table) {
        final Table t = this.tables.getByName(table.getName());
        final boolean add = t == null;
        if (add) {
            this.tables.add(table);
        }
        return add;
    }

    /**
     * Create a new table
     *
     * @param name           the name of the new table
     * @param rowCapacity    the row capacity
     * @param columnCapacity the column capacity
     * @return the newly created table
     */
    public Table createTable(final String name, final int rowCapacity, final int columnCapacity) {
        return Table
                .create(this, this.positionUtil, this.writeUtil, this.xmlUtil, name, rowCapacity,
                        columnCapacity, this.stylesContainer, this.format, this.libreOfficeMode);
    }

    /**
     * @return the last table in the document or null
     */
    public Table getLastTable() {
        final int size = this.tables.size();
        return size <= 0 ? null : this.tables.get(size - 1);
    }

    /**
     * @return the styles container
     */
    public StylesContainer getStyleTagsContainer() {
        return this.stylesContainer;
    }

    /**
     * @param tableIndex an index of the table
     * @return the table at that index
     */
    public Table getTable(final int tableIndex) {
        return this.tables.get(tableIndex);
    }

    /**
     * @param name the name of the table to find
     * @return the table, or null if none present
     */
    public Table getTable(final String name) {
        return this.tables.getByName(name);
    }

    /**
     * @return the number of tables in the document
     */
    public int getTableCount() {
        return this.tables.size();
    }

    /**
     * @return the list of tables
     */
    public List<Table> getTables() {
        return this.tables;
    }

    @Override
    public void write(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
        this.writePreamble(util, writer);
        for (final Table table : this.tables) {
            table.appendXMLContent(util, writer);
        }
        this.writePostamble(util, writer);
    }

    /**
     * Write the preamble into the given writer. Used by the MetaAndStylesElementsFlusher and by
     * standard write method
     *
     * @param util   an XML util
     * @param writer the destination
     * @throws IOException if the preamble was not written
     */
    public void writePreamble(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
        writer.putAndRegisterNextEntry(new StandardManifestEntry("content.xml", "text/xml", null));
        writer.append(XMLUtil.XML_PROLOG);
        writer.append("<office:document-content");
        for (final Map.Entry<String, String> entry: CONTENT_NAMESPACE_BY_PREFIX.entrySet()) {
            util.appendAttribute(writer, entry.getKey(), entry.getValue());
        }
        for (final Map.Entry<String, String> entry: this.additionalNamespaceByPrefix.entrySet()) {
            util.appendAttribute(writer, entry.getKey(), entry.getValue());
        }
        util.appendAttribute(writer, "office:version", OFFICE_VERSION);
        writer.append(">");
        this.writeEvents(util, writer);
        this.stylesContainer.writeFontFaceDecls(util, writer);
        writer.append("<office:automatic-styles>");
        this.stylesContainer.writeHiddenDataStyles(util, writer);
        this.stylesContainer.writeContentAutomaticStyles(util, writer);
        writer.append("</office:automatic-styles>");
        writer.append("<office:body>");
        writer.append("<office:spreadsheet>");
        // don't close here
    }

    /**
     * Write the postamble into the given writer. Used by the FinalizeFlusher and by standard
     * write method
     *
     * @param util   an XML util
     * @param writer the destination
     * @throws IOException if the postamble could not be written
     */
    public void writePostamble(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
        if (this.autoFilters != null) {
            this.appendAutoFilters(util, writer);
        }
        if (this.pilotTables != null) {
            this.appendPilotTables(util, writer);
        }
        writer.append("</office:spreadsheet>");
        writer.append("</office:body>");
        writer.append("</office:document-content>");
        writer.closeEntry();
    }

    public void writeEvents(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
        if (this.scriptEvents.isEmpty()) {
            return;
        }

        writer.append("<office:scripts><office:event-listeners>");
        for (final ScriptEventListener event : this.scriptEvents) {
            event.appendXMLContent(util, writer);
        }
        writer.append("</office:event-listeners></office:scripts>");
    }

    private void appendAutoFilters(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<table:database-ranges>");
        for (final AutoFilter autoFilter : this.autoFilters) {
            autoFilter.appendXMLContent(util, appendable);
        }
        appendable.append("</table:database-ranges>");
    }

    private void appendPilotTables(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<table:data-pilot-tables>");
        for (final PilotTable pilotTable : this.pilotTables) {
            pilotTable.appendXMLContent(util, appendable);
        }
        appendable.append("</table:data-pilot-tables>");
    }


    /**
     * Add an autoFilter to a table
     *
     * @param autoFilter the auto filter
     */
    public void addAutoFilter(final AutoFilter autoFilter) {
        if (this.autoFilters == null) {
            this.autoFilters = new ArrayList<AutoFilter>();
        }
        this.autoFilters.add(autoFilter);
    }

    /**
     * Add some events to the document
     *
     * @param events the events to add
     */
    public void addEvents(final ScriptEventListener... events) {
        this.scriptEvents.addAll(Arrays.asList(events));
    }

    /**
     * Add a new pilot table
     *
     * @param pilotTable the filter
     */
    public void addPilotTable(final PilotTable pilotTable) {
        if (this.pilotTables == null) {
            this.pilotTables = new ArrayList<PilotTable>();
        }
        this.pilotTables.add(pilotTable);
    }
}
