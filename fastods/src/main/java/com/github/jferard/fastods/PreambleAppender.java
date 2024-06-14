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

package com.github.jferard.fastods;

import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class PreambleAppender {
    private static final int MAX_COLUMN_COUNT = 1024;
    private final TableModel model;

    public PreambleAppender(final TableModel model) {
        this.model = model;
    }

    public void appendForms(final XMLUtil util, final Appendable appendable) throws IOException {
        final List<XMLConvertible> forms = this.model.getForms();
        if (forms == null || forms.isEmpty()) {
            return;
        }

        appendable.append("<office:forms");
        util.appendAttribute(appendable, "form:automatic-focus", false);
        util.appendAttribute(appendable, "form:apply-design-mode", false);
        appendable.append(">");
        for (final XMLConvertible form : forms) {
            form.appendXMLContent(util, appendable);
        }
        appendable.append("</office:forms>");
    }

    public void appendShapes(final XMLUtil util, final Appendable appendable) throws IOException {
        final List<Shape> shapes = this.model.getShapes();
        if (shapes == null || shapes.isEmpty()) {
            return;
        }

        appendable.append("<table:shapes>");
        for (final Shape shape : shapes) {
            shape.appendXMLContent(util, appendable);
        }
        appendable.append("</table:shapes>");
    }

    public void appendColumns(final XMLUtil xmlUtil, final Appendable appendable)
            throws IOException {
        final Iterator<TableColumnImpl> iterator = this.model.getColumns().iterator();
        if (!iterator.hasNext()) {
            TableColumnImpl.DEFAULT_TABLE_COLUMN
                    .appendXMLToTable(xmlUtil, appendable, this.model.getColumnCapacity());
            return;
        }

        int headerColumnsCount = this.model.getHeaderColumnsCount();
        if (headerColumnsCount > 0) {
            appendable.append("<table:table-header-columns>");
        }
        int count = 1;
        int endCount = MAX_COLUMN_COUNT;
        TableColumnImpl curColumn = iterator.next(); // will be shifted to prevTCS
        if (curColumn == null) {
            curColumn = TableColumnImpl.DEFAULT_TABLE_COLUMN;
        }
        while (iterator.hasNext()) {
            headerColumnsCount--;
            final TableColumnImpl prevColumn = curColumn;
            curColumn = iterator.next();
            if (curColumn == null) {
                curColumn = TableColumnImpl.DEFAULT_TABLE_COLUMN;
            }

            if (headerColumnsCount == 0) {
                prevColumn.appendXMLToTable(xmlUtil, appendable, count);
                endCount -= count;
                count = 1;
                appendable.append("</table:table-header-columns>");
            } else if (curColumn.equals(prevColumn)) {
                count++;
            } else {
                prevColumn.appendXMLToTable(xmlUtil, appendable, count);
                endCount -= count;
                count = 1;
            }
        }
        headerColumnsCount--;
        curColumn.appendXMLToTable(xmlUtil, appendable, count);
        endCount -= count;
        if (headerColumnsCount == 0) {
            appendable.append("</table:table-header-columns>");
        } else if (headerColumnsCount > 0) {
            TableColumnImpl.DEFAULT_TABLE_COLUMN.appendXMLToTable(xmlUtil, appendable, headerColumnsCount);
            endCount -= headerColumnsCount;
            appendable.append("</table:table-header-columns>");
        }
        TableColumnImpl.DEFAULT_TABLE_COLUMN.appendXMLToTable(xmlUtil, appendable, endCount);
    }
}
