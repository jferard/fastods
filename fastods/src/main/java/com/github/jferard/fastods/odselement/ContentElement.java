/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.UniqueList;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;

/**
 * See 3.1.3.2 <office:document-content>.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class ContentElement implements OdsElement {
    private final FlushPosition flushPosition;
    private final DataStyles format;
    private final PositionUtil positionUtil;
    private final StylesContainer stylesContainer;
    private final UniqueList<Table> tables;
    private final WriteUtil writeUtil;
    private final XMLUtil xmlUtil;
    private final boolean libreOfficeMode;
    private List<String> autoFilters;

    /**
     * @param positionUtil    an util object for positions (e.g. "A1")
     * @param xmlUtil         an util object to write xml
     * @param writeUtil       an util to compute some data
     * @param format          the format for data styles
     * @param libreOfficeMode try to get full compatibility with LO if true
     * @param stylesContainer a styles container.
     */
    ContentElement(final PositionUtil positionUtil, final XMLUtil xmlUtil,
                   final WriteUtil writeUtil, final DataStyles format,
                   final boolean libreOfficeMode, final StylesContainer stylesContainer) {
        this.writeUtil = writeUtil;
        this.xmlUtil = xmlUtil;
        this.positionUtil = positionUtil;
        this.format = format;
        this.libreOfficeMode = libreOfficeMode;
        this.stylesContainer = stylesContainer;
        this.tables = new UniqueList<Table>();
        this.flushPosition = new FlushPosition();
    }

    /**
     * Create an automatic style for this TableCellStyle and this type of cell.
     * Do not produce any effect if the type is Type.STRING or Type.VOID.
     *
     * @param style the style of the cell (color, data style, etc.)
     * @param type  the type of the cell
     * @return the created style, or style if the type is Type.STRING or Type.VOID
     */
    public TableCellStyle addChildCellStyle(final TableCellStyle style, final TableCell.Type type) {
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
     */
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

    private void ensureContentBegin(final XMLUtil util, final ZipUTF8Writer writer)
            throws IOException {
        if (this.flushPosition.isUndefined()) {
            this.writePreamble(util, writer);
            this.flushPosition.set(0, -1);
        }
    }

    /**
     * Flush the rows from the last position to the current position
     *
     * @param util            the XML util
     * @param writer          the destination
     * @param settingsElement the settings.xml representation
     * @throws IOException if the rows were not flushed
     */
    public void flushRows(final XMLUtil util, final ZipUTF8Writer writer,
                          final SettingsElement settingsElement) throws IOException {
        this.ensureContentBegin(util, writer);
        final int lastTableIndex = this.tables.size() - 1;
        if (lastTableIndex == -1) { // no table yet
            return;
        }

        final int curTableIndex = this.flushPosition.getTableIndex();

        if (curTableIndex < lastTableIndex) {
            this.flushPendingTablesAndLastTableAvailableRows(util, writer, settingsElement,
                    lastTableIndex, curTableIndex);
        } else {
            final Table lastTable = this.tables.get(lastTableIndex);
            final int firstRowIndex = this.flushPosition.getLastRowIndex() + 1;
            lastTable.flushSomeAvailableRowsFrom(util, writer, firstRowIndex);
        }

        final Table lastTable = this.tables.get(lastTableIndex);
        this.flushPosition.set(lastTableIndex, lastTable.getLastRowNumber());
    }

    private void flushPendingTablesAndLastTableAvailableRows(final XMLUtil util,
                                                             final ZipUTF8Writer writer,
                                                             final SettingsElement settingsElement,
                                                             final int lastTableIndex,
                                                             final int curTableIndex)
            throws IOException {
        final Table lastTable = this.tables.get(lastTableIndex);
        this.flushRemainingRowsFromCurTable(util, writer, settingsElement, curTableIndex);
        this.flushCompleteTables(util, writer, settingsElement, lastTableIndex, curTableIndex);
        lastTable.flushAllAvailableRows(util, writer);
    }

    private void flushRemainingRowsFromCurTable(final XMLUtil util, final ZipUTF8Writer writer,
                                                final SettingsElement settingsElement,
                                                final int curTableIndex) throws IOException {
        final int firstRowIndex = this.flushPosition.getLastRowIndex() + 1;
        final Table curTable = this.tables.get(curTableIndex);
        curTable.flushRemainingRowsFrom(util, writer, firstRowIndex);
        settingsElement.addTableConfig(curTable.getConfigEntry());
    }

    private void flushCompleteTables(final XMLUtil util, final ZipUTF8Writer writer,
                                     final SettingsElement settingsElement, final int from,
                                     final int to) throws IOException {
        for (int index = to + 1; index < from; index++) {
            final Table table = this.tables.get(index);
            table.appendXMLToContentEntry(util, writer);
            settingsElement.addTableConfig(table.getConfigEntry());
        }
    }

    /**
     * Flush the tables.
     *
     * @param util            an XML util
     * @param writer          destination
     * @param settingsElement the settings.xml representation
     * @throws IOException if the tables were not flushed
     */
    public void flushTables(final XMLUtil util, final ZipUTF8Writer writer,
                            final SettingsElement settingsElement) throws IOException {
        this.ensureContentBegin(util, writer);
        final int lastTableIndex = this.tables.size() - 1;
        if (lastTableIndex < 0) {
            return;
        }

        final int curTableIndex = this.flushPosition.getTableIndex();
        this.flushCompleteTables(util, writer, settingsElement, curTableIndex, lastTableIndex + 1);
    }

    /**
     * @return the last table in the document
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
            table.appendXMLToContentEntry(util, writer);
        }
        this.writePostamble(util, writer);
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
            this.appendAutoFilters(writer, util);
        }
        writer.write("</office:spreadsheet>");
        writer.write("</office:body>");
        writer.write("</office:document-content>");
        writer.flush();
        writer.closeEntry();
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
        writer.putNextEntry(new ZipEntry("content.xml"));
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.write(
                "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns" +
                        ":office:1.0\" " +
                        "xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\" " +
                        "xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" " +
                        "xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" " +
                        "xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\" " +
                        "xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\"" +
                        " " +
                        "xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl" +
                        ".org/dc/elements/1.1/\" " +
                        "xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" " +
                        "xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\" " +
                        "xmlns:presentation=\"urn:oasis:names:tc:opendocument:xmlns:presentation" +
                        ":1.0\" " +
                        "xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" " +
                        "xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\" " +
                        "xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" " +
                        "xmlns:math=\"http://www" +
                        ".w3.org/1998/Math/MathML\" xmlns:form=\"urn:oasis:names:tc:opendocument" +
                        ":xmlns:form:1.0\" " +
                        "xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\" " +
                        "xmlns:ooo=\"http://openoffice.org/2004/office\" " +
                        "xmlns:ooow=\"http://openoffice" +
                        ".org/2004/writer\" xmlns:oooc=\"http://openoffice.org/2004/calc\" " +
                        "xmlns:dom=\"http://www" +
                        ".w3.org/2001/xml-events\" xmlns:xforms=\"http://www.w3.org/2002/xforms\"" +
                        " " +
                        "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www" +
                        ".w3.org/2001/XMLSchema-instance\" office:version=\"1.2\">");
        writer.write("<office:scripts/>");
        this.stylesContainer.writeFontFaceDecls(util, writer);
        writer.write("<office:automatic-styles>");
        this.stylesContainer.writeHiddenDataStyles(util, writer);
        this.stylesContainer.writeContentAutomaticStyles(util, writer);
        writer.write("</office:automatic-styles>");
        writer.write("<office:body>");
        writer.write("<office:spreadsheet>");
    }

    private void appendAutoFilters(final Appendable appendable, final XMLUtil util)
            throws IOException {
        appendable.append("<table:database-ranges>");
        for (final String autoFilter : this.autoFilters) {
            appendable.append("<table:database-range");
            util.appendAttribute(appendable, "table:display-filter-buttons", "true");
            util.appendAttribute(appendable, "table:target-range-address", autoFilter);
            appendable.append("/>");
        }
        appendable.append("</table:database-ranges>");
    }

    /**
     * Add an autoFilter to a table
     *
     * @param table the table where the filter goes
     * @param r1    first row index (0..n-1)
     * @param c1    first col index
     * @param r2    last row index
     * @param c2    last col index
     */
    public void addAutoFilter(final Table table, final int r1, final int c1, final int r2,
                              final int c2) {
        if (this.autoFilters == null) {
            this.autoFilters = new ArrayList<String>();
        }

        this.autoFilters.add(this.positionUtil.toRangeAddress(table, r1, c1, r2, c2));
    }

}
