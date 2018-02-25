/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.ObjectStyle;
import com.github.jferard.fastods.style.PageLayoutStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An ods document. The destination file is already set.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class NamedOdsDocument implements OdsDocument {
    /**
     * the default column capacity.
     */
    static final int DEFAULT_COLUMN_CAPACITY = 32;
    /**
     * the default row capacity.
     */
    static final int DEFAULT_ROW_CAPACITY = 1024;
    private final Logger logger;
    private final OdsElements odsElements;
    private final XMLUtil xmlUtil;

    /**
     * Create a new ODS file.
     *
     * @param logger      the logger
     * @param odsElements the ods elements (file entries in zip archive)
     * @param xmlUtil     a util for XML writing
     */
    NamedOdsDocument(final Logger logger, final OdsElements odsElements, final XMLUtil xmlUtil) {
        this.logger = logger;
        this.odsElements = odsElements;
        this.xmlUtil = xmlUtil;

        // Add five default stylesEntry to contentEntry
        TableStyle.DEFAULT_TABLE_STYLE.addToElements(this.odsElements);
        TableRowStyle.DEFAULT_TABLE_ROW_STYLE.addToElements(this.odsElements);
        TableColumnStyle.DEFAULT_TABLE_COLUMN_STYLE.addToElements(this.odsElements);
        TableCellStyle.DEFAULT_CELL_STYLE.addToElements(this.odsElements);
        PageStyle.DEFAULT_PAGE_STYLE.addToElements(this.odsElements);
    }

    /**
     * Add a cell style for a given data type. Use only if you want to flush data before the end of the document
     * construction.
     * Do not produce any effect if the type is Type.STRING or Type.VOID
     *
     * @param type the data type
     */
    public void addChildCellStyle(final TableCell.Type type) {
        this.odsElements.addChildCellStyle(TableCellStyle.DEFAULT_CELL_STYLE, type);
    }

    /**
     * Add a cell style for a given data type. Use only if you want to flush data before the end of the document
     * construction.
     * Do not produce any effect if the type is Type.STRING or Type.VOID
     *
     * @param style the style
     * @param type  the data type
     */
    public void addChildCellStyle(final TableCellStyle style, final TableCell.Type type) {
        this.odsElements.addChildCellStyle(style, type);
    }

    /**
     * Add a data style to this document. Use only if you want to flush data before the end of the document
     * construction.
     *
     * @param dataStyle the data style to add to this document
     */
    public void addDataStyle(final DataStyle dataStyle) {
        this.odsElements.addDataStyle(dataStyle);
    }

    /**
     * Add a master page style to this document. Use only if you want to flush data before the end of the document
     * construction.
     *
     * @param masterPageStyle the master page style to add to this document
     */
    public void addMasterPageStyle(final MasterPageStyle masterPageStyle) {
        this.odsElements.addMasterPageStyle(masterPageStyle);
    }

    /**
     * Add an observer (see Observer pattern).
     *
     * @param writer the writer where data will be flushed
     */
    void addObserver(final NamedOdsFileWriter writer) {
        this.odsElements.addObserver(writer);
    }

    /**
     * Add a page layout style to this document. Use only if you want to flush data before the end of the document
     * construction.
     *
     * @param pageLayoutStyle the page layout to add to this document
     */
    public void addPageLayoutStyle(final PageLayoutStyle pageLayoutStyle) {
        this.odsElements.addPageLayoutStyle(pageLayoutStyle);
    }

    /**
     * Add a page style to this document
     *
     * @param ps the page style
     */
    public void addPageStyle(final PageStyle ps) {
        this.odsElements.addPageStyle(ps);
    }

    /**
     * Add an object style to this document. Use only if you want to flush data before the end of the document
     * construction.
     *
     * @param objectStyle the object style to add to this document
     */
    public void addObjectStyle(final ObjectStyle objectStyle) {
        this.odsElements.addContentStyle(objectStyle);
    }

    /**
     * Add a style to content.xml > automatic-styles
     *
     * @param objectStyle the style
     */
    public void addStyleToContentAutomaticStyles(final ObjectStyle objectStyle) {
        this.odsElements.addStyleToContentAutomaticStyles(objectStyle);
    }

    @Override
    public Table addTable(final String name) throws IOException {
        return this.addTable(name, NamedOdsDocument.DEFAULT_ROW_CAPACITY, NamedOdsDocument.DEFAULT_COLUMN_CAPACITY);
    }

    @Override
    public Table addTable(final String name, final int rowCapacity, final int columnCapacity) throws IOException {
        final Table table = this.odsElements.addTableToContent(name, rowCapacity, columnCapacity);
        this.odsElements.setActiveTable(table);
        return table;
    }

    /**
     * Enable styles debugging
     */
    public void debugStyles() {
        this.odsElements.debugStyles();
    }

    /**
     * Enable styles freeze
     */
    public void freezeStyles() {
        this.odsElements.freezeStyles();
    }

    @Override
    public Table getTable(final int n) throws FastOdsException {
        final List<Table> tables = this.odsElements.getTables();
        if (n < 0 || n >= tables.size()) {
            throw new FastOdsException("Wrong table number [" + n + "]");
        }

        return tables.get(n);
    }

    @Override
    public Table getTable(final String name) throws FastOdsException {
        final Table table = this.odsElements.getTable(name);
        if (table == null) {
            throw new FastOdsException("Wrong table name [" + name + "]");
        }
        return table;
    }

    @Override
    public Table getOrAddTable(final String name) throws IOException {
        Table table = this.odsElements.getTable(name);
        if (table == null) {
            table = this.addTable(name);
        }
        return table;
    }


    @Override
    public String getTableName(final int n) throws FastOdsException {
        final Table t = this.getTable(n);
        return t.getName();
    }

    @Override
    public int getTableNumber(final String name) {
        final ListIterator<Table> iterator = this.odsElements.getTables().listIterator();
        while (iterator.hasNext()) {
            final int n = iterator.nextIndex();
            final Table tab = iterator.next();
            if (tab.getName().equals(name)) {
                return n;
            }
        }

        return -1;
    }

    @Override
    public List<Table> getTables() {
        return this.odsElements.getTables();
    }

    /**
     * Prepare the document for flush (ie write empty elements, manifest, mimetype, ...)
     *
     * @throws IOException if an element can't be written
     */
    public void prepareFlush() throws IOException {
        this.odsElements.prepare();
    }

    /**
     * Save the document. Note that the odsElements field has a reference to a writer.
     *
     * @throws IOException if the save fails
     */
    public void save() throws IOException {
        this.odsElements.save();
    }

    /**
     * Saves a file
     *
     * @param writer where to write
     * @throws IOException if the document can't be saved
     */
    public void save(final ZipUTF8Writer writer) throws IOException {
        try {
            this.odsElements.createEmptyElements(writer);
            this.odsElements.writeImmutableElements(this.xmlUtil, writer);
            this.odsElements.writeMeta(this.xmlUtil, writer);
            this.odsElements.writeStyles(this.xmlUtil, writer);
            this.odsElements.writeContent(this.xmlUtil, writer);
            this.odsElements.writeSettings(this.xmlUtil, writer);
        } finally {
            writer.close();
        }
        this.logger.log(Level.FINE, "file saved");
    }

    @Override
    public boolean setActiveTable(final int tableIndex) {
        if (tableIndex < 0 || tableIndex >= this.odsElements.getTableCount()) return false;

        final Table table = this.odsElements.getTable(tableIndex);
        this.odsElements.setActiveTable(table);
        return true;
    }

    @Override
    public void setViewSetting(final String viewId, final String item, final String value) {
        this.odsElements.setViewSetting(viewId, item, value);
    }

    @Override
    public int tableCount() {
        return this.odsElements.getTableCount();
    }

    @Override
    public void addAutofilter(final Table table, final int r1, final int c1, final int r2, final int c2) {
        this.odsElements.addAutofilter(table, r1, c1, r2, c2);
    }
}
