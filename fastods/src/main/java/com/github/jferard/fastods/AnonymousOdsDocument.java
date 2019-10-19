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

package com.github.jferard.fastods;

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.ScriptEventListener;
import com.github.jferard.fastods.util.AutoFilter;
import com.github.jferard.fastods.util.Container;
import com.github.jferard.fastods.util.PilotTable;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An ods document. Anonymous means that the destination file is not set.
 * The content of the document is only flushed once, when the document is saved.
 * That means that one doesn't have to define the style early.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public final class AnonymousOdsDocument implements OdsDocument {
    /**
     * Create a new anonymous ODS document.
     *
     * @param logger      the logger
     * @param xmlUtil     a util for XML writing
     * @param odsElements the ods elements (file entries in zip archive)
     * @return an anonymous document
     */
    static AnonymousOdsDocument create(final Logger logger, final XMLUtil xmlUtil,
                                       final OdsElements odsElements) {
        return new AnonymousOdsDocument(logger, xmlUtil, odsElements,
                new CommonOdsDocument(odsElements));
    }

    private final Logger logger;
    private final OdsElements odsElements;
    private final XMLUtil xmlUtil;
    private final CommonOdsDocument commonOdsDocument;

    /**
     * Create a new anonymous ODS document.
     *
     * @param logger            the logger
     * @param xmlUtil           a util for XML writing
     * @param odsElements       the ods elements (file entries in zip archive)
     * @param commonOdsDocument the common part for an ods document
     */
    private AnonymousOdsDocument(final Logger logger, final XMLUtil xmlUtil,
                                 final OdsElements odsElements,
                                 final CommonOdsDocument commonOdsDocument) {
        this.logger = logger;
        this.odsElements = odsElements;
        this.xmlUtil = xmlUtil;
        this.commonOdsDocument = commonOdsDocument;
    }

    @Override
    public Table addTable(final String name) throws IOException {
        return this.commonOdsDocument.addTable(name);
    }

    @Override
    public Table addTable(final String name, final int rowCapacity, final int columnCapacity)
            throws IOException {
        return this.commonOdsDocument.addTable(name, rowCapacity, columnCapacity);
    }

    @Override
    public boolean addTable(final Table table)
            throws IOException {
        return this.commonOdsDocument.addTable(table);
    }

    @Override
    public Table createTable(final String name) throws IOException {
        return this.odsElements.createTable(name, CommonOdsDocument.DEFAULT_ROW_CAPACITY,
                CommonOdsDocument.DEFAULT_COLUMN_CAPACITY);
    }

    @Override
    public Table createTable(final String name, final int rowCapacity, final int columnCapacity)
            throws IOException {
        return this.commonOdsDocument.createTable(name, rowCapacity, columnCapacity);
    }

    @Override
    public void addExtraFile(final String fullPath, final String mediaType, final byte[] bytes) {
        this.commonOdsDocument.addExtraFile(fullPath, mediaType, bytes);
    }

    @Override
    public void addExtraDir(final String fullPath) {
        this.commonOdsDocument.addExtraDir(fullPath);
    }

    @Override
    public void addEvents(final ScriptEventListener... events) {
        this.commonOdsDocument.addEvents(events);
    }

    @Override
    public void addPilotTable(final PilotTable pilot) {
        this.commonOdsDocument.addPilotTable(pilot);
    }

    @Override
    public void addAutoFilter(final AutoFilter autoFilter) {
        this.commonOdsDocument.addAutoFilter(autoFilter);
    }

    @Override
    public Table getTable(final int n) throws FastOdsException {
        return this.commonOdsDocument.getTable(n);
    }

    @Override
    public Table getTable(final String name) throws FastOdsException {
        return this.commonOdsDocument.getTable(name);
    }

    @Override
    public Table getOrAddTable(final String name) throws IOException {
        return this.commonOdsDocument.getOrAddTable(name);
    }


    @Override
    public String getTableName(final int n) throws FastOdsException {
        return this.commonOdsDocument.getTableName(n);
    }

    @Override
    public int getTableNumber(final String name) {
        return this.commonOdsDocument.getTableNumber(name);
    }

    @Override
    public List<Table> getTables() {
        return this.commonOdsDocument.getTables();
    }

    @Override
    public boolean setActiveTable(final int tableIndex) {
        return this.commonOdsDocument.setActiveTable(tableIndex);
    }

    @Override
    public void setViewSetting(final String viewId, final String item, final String value) {
        this.commonOdsDocument.setViewSetting(viewId, item, value);
    }

    @Override
    public int tableCount() {
        return this.commonOdsDocument.tableCount();
    }

    @Override
    public void addAutoFilter(final Table table, final int r1, final int c1, final int r2,
                              final int c2) {
        this.commonOdsDocument.addAutoFilter(table, r1, c1, r2, c2);
    }

    @Override
    public void freezeCells(final Table table, final int rowCount, final int colCount) {
        this.commonOdsDocument.freezeCells(table, rowCount, colCount);
    }

    @Override
    public void setDataStylesMode(final Container.Mode mode) {
        this.commonOdsDocument.setDataStylesMode(mode);
    }

    @Override
    public void setMasterPageStyleMode(final Container.Mode mode) {
        this.commonOdsDocument.setMasterPageStyleMode(mode);

    }

    @Override
    public void setPageLayoutStyleMode(final Container.Mode mode) {
        this.commonOdsDocument.setPageLayoutStyleMode(mode);
    }

    @Override
    public void setPageStyleMode(final Container.Mode mode) {
        this.commonOdsDocument.setPageStyleMode(mode);
    }

    @Override
    public void setObjectStyleMode(final Container.Mode mode) {
        this.commonOdsDocument.setObjectStyleMode(mode);
    }

    /**
     * Saves a file.
     * Do not close the writer (see https://github.com/jferard/fastods/issues/138)
     *
     * @param writer where to write
     * @throws IOException if the document can't be saved
     */
    void save(final ZipUTF8Writer writer) throws IOException {
        this.odsElements.createEmptyElements(writer);
        this.odsElements.writeMimeType(this.xmlUtil, writer);
        this.odsElements.writeMeta(this.xmlUtil, writer);
        this.odsElements.writeStyles(this.xmlUtil, writer);
        this.odsElements.writeContent(this.xmlUtil, writer);
        this.odsElements.writeSettings(this.xmlUtil, writer);
        this.odsElements.writeManifest(this.xmlUtil, writer);
        this.odsElements.writeExtras(writer);
        this.logger.log(Level.FINE, "file saved");
    }
}
