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

import com.github.jferard.fastods.attribute.CellType;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.ScriptEventListener;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.FontFaceContainerStyle;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.ObjectStyle;
import com.github.jferard.fastods.style.PageLayoutStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.AutoFilter;
import com.github.jferard.fastods.util.Container;
import com.github.jferard.fastods.util.PilotTable;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An ods document with a name. The named is not stored in the NamedOdsDocument object, but the
 * NamedOdsDocument object is injected in a NamedOdsWriter.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class NamedOdsDocument implements OdsDocument, StylesContainer {
    /**
     * Create a new named ODS document.
     *
     * @param logger      the logger
     * @param xmlUtil     a util for XML writing
     * @param odsElements the ods elements (file entries in zip archive)
     * @return a named ods document
     */
    static NamedOdsDocument create(final Logger logger, final XMLUtil xmlUtil,
                                   final OdsElements odsElements) {
        return new NamedOdsDocument(logger, xmlUtil, odsElements,
                new CommonOdsDocument(odsElements));
    }

    private final Logger logger;
    private final OdsElements odsElements;
    private final XMLUtil xmlUtil;
    private final CommonOdsDocument commonOdsDocument;

    /**
     * /**
     * Create a new named ODS document.
     *
     * @param logger            the logger
     * @param xmlUtil           a util for XML writing
     * @param odsElements       the ods elements (file entries in zip archive)
     * @param commonOdsDocument the common part of an ods document
     */
    NamedOdsDocument(final Logger logger, final XMLUtil xmlUtil, final OdsElements odsElements,
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
    public boolean addTable(final Table table) throws IOException {
        return this.commonOdsDocument.addTable(table);
    }

    @Override
    public Table createTable(final String name) throws IOException {
        return this.commonOdsDocument.createTable(name);
    }

    @Override
    public Table createTable(final String name, final int rowCapacity, final int columnCapacity)
            throws IOException {
        return this.commonOdsDocument.createTable(name, rowCapacity, columnCapacity);
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

    /**
     * Add an observer (see Observer pattern).
     *
     * @param writer the writer where data will be flushed
     */
    void addObserver(final NamedOdsFileWriter writer) {
        this.odsElements.addObserver(writer);
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
        this.odsElements.addCellStyle(style, types);
    }

    /**
     * Add a data style to this document. Use only if you want to flush data before the end of
     * the document
     * construction.
     *
     * @param dataStyle the data style to add to this document
     */
    @Override
    public boolean addDataStyle(final DataStyle dataStyle) {
        return this.odsElements.addDataStyle(dataStyle);
    }

    @Override
    public boolean addMasterPageStyle(final MasterPageStyle masterPageStyle) {
        return this.odsElements.addMasterPageStyle(masterPageStyle);
    }

    @Override
    public boolean addNewDataStyleFromCellStyle(final TableCellStyle style) {
        return this.odsElements.addNewDataStyleFromCellStyle(style);
    }

    @Override
    public boolean addPageLayoutStyle(final PageLayoutStyle pageLayoutStyle) {
        return this.odsElements.addPageLayoutStyle(pageLayoutStyle);
    }

    @Override
    public boolean addPageStyle(final PageStyle ps) {
        return this.odsElements.addPageStyle(ps);
    }

    @Override
    public boolean addContentStyle(final ObjectStyle objectStyle) {
        return this.odsElements.addContentStyle(objectStyle);
    }

    @Override
    public boolean addStylesStyle(final ObjectStyle objectStyle) {
        return this.odsElements.addStylesStyle(objectStyle);
    }

    @Override
    public TableCellStyle addChildCellStyle(final TableCellStyle style, final DataStyle dataStyle) {
        return this.odsElements.addChildCellStyle(style, dataStyle);
    }

    @Override
    public boolean addContentFontFaceContainerStyle(final FontFaceContainerStyle objectStyle) {
        return this.odsElements.addContentFontFaceContainerStyle(objectStyle);
    }

    @Override
    public boolean addStylesFontFaceContainerStyle(final FontFaceContainerStyle ffcStyle) {
        return this.odsElements.addStylesFontFaceContainerStyle(ffcStyle);
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

    /**
     * Prepare the document for flush (ie write empty elements, manifest, mime type, ...)
     *
     * @throws IOException if an element can't be written
     */
    public void prepare() throws IOException {
        this.odsElements.prepareAsync();
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

    /**
     * Save the document. Note that the odsElements field has a reference to a writer.
     *
     * @throws IOException if the save fails
     */
    public void save() throws IOException {
        this.odsElements.saveAsync();
        this.logger.log(Level.FINE, "file saved");
    }
}
