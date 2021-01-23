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

package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.CellValue;
import com.github.jferard.fastods.DataWrapper;
import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.ToCellValueConverter;
import com.github.jferard.fastods.attribute.CellType;
import com.github.jferard.fastods.style.TableCellStyle;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A wrapper for a result set.
 *
 * @author Julien Férard
 */
public final class ResultSetDataWrapper implements DataWrapper {
    /**
     * @param rs the result set
     * @return the data wrapper
     */
    public static ResultSetDataWrapperBuilder builder(final String rangeName, final ResultSet rs) {
        return new ResultSetDataWrapperBuilder(rangeName, rs);
    }

    /**
     * column count of the ResultSet.
     */
    private final TableCellStyle headCellStyle;
    private final Logger logger;
    private final boolean autoFilter;

    /**
     * maximum number of lines to be written
     */
    private final int max;
    private final ToCellValueConverter converter;
    private final String rangeName;
    /**
     * the ResultSet.
     */
    private final ResultSet resultSet;
    private final Map<Integer, CellType> cellTypeByColIndex;
    private final CellValue nullValue;


    /**
     * @param logger             a logger
     * @param converter          a converter SQL -> OpenDocument
     * @param rangeName          the name of the range
     * @param rs                 the result cell
     * @param headCellStyle      a style for header, null if none
     * @param autoFilter         set an auto filter if true
     * @param cellTypeByColIndex a hint for cell types
     * @param nullValue          the default value for NULL
     * @param max                the maximum number of rows, -1 for unlimited
     */
    public ResultSetDataWrapper(final Logger logger, final ToCellValueConverter converter,
                                final String rangeName, final ResultSet rs,
                                final TableCellStyle headCellStyle, final boolean autoFilter,
                                final Map<Integer, CellType> cellTypeByColIndex,
                                final CellValue nullValue, final int max) {
        this.logger = logger;
        this.converter = converter;
        this.rangeName = rangeName;
        this.resultSet = rs;
        this.headCellStyle = headCellStyle;
        this.autoFilter = autoFilter;
        this.cellTypeByColIndex = cellTypeByColIndex;
        this.nullValue = nullValue;
        this.max = max;
    }

    @Override
    public boolean addToTable(final TableCellWalker walker) throws IOException {
        int rowCount = 0; // at least
        try {
            final ResultSetMetaData metadata = this.resultSet.getMetaData();
            try {
                final int r1 = walker.rowIndex();
                final int c1 = walker.colIndex();

                final int columnCount = metadata.getColumnCount();

                this.writeFirstLineDataTo(walker, metadata);
                if (this.resultSet.next()) {
                    do {
                        if (this.max == -1 || ++rowCount <= this.max) {
                            walker.nextRow();
                            walker.to(c1);
                            this.writeDataLineTo(walker, columnCount);
                        }
                    } while (this.resultSet.next());
                }
                final boolean oneBlankLine = rowCount == 0 || rowCount > this.max;
                if (oneBlankLine) {
                    walker.nextRow();
                    walker.to(c1);
                    this.writeLastLineDataTo(walker, columnCount, rowCount);
                }
                if (this.autoFilter) {
                    final int r2 = walker.rowIndex();
                    final int c2 = c1 + columnCount - 1;
                    walker.getTable().addAutoFilter(this.rangeName, r1, c1, r2, c2);
                }
                walker.nextRow();
            } catch (final SQLException e) {
                if (this.logger != null) {
                    this.logger.log(Level.SEVERE, "Can't read ResultSet row", e);
                }
                throw new RuntimeException(e);
            } catch (final FastOdsException e) {
                throw new RuntimeException(e);
            }
        } catch (final SQLException e) {
            if (this.logger != null) {
                this.logger.log(Level.SEVERE, "Can't read ResultSet metadata", e);
            }
            throw new RuntimeException(e);
        }
        return rowCount > 0;
    }

    /**
     * @param metadata the result set metadata
     * @return the name of the columns
     * @throws SQLException if a database access error occurs
     */
    private List<String> getColumnLabels(final ResultSetMetaData metadata) throws SQLException {
        final int columnCount = metadata.getColumnCount();
        final List<String> names = new ArrayList<String>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            names.add(metadata.getColumnLabel(i + 1));
        }

        return names;
    }

    /**
     * @return the values of the current column
     * @throws SQLException if a database access error occurs
     */
    private List<Object> getColumnValues(final int columnCount) throws SQLException {
        final List<Object> values = new ArrayList<Object>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            values.add(this.resultSet.getObject(i + 1));
        }
        return values;
    }

    private void writeFirstLineDataTo(final TableCellWalker walker,
                                      final ResultSetMetaData metadata) throws SQLException {
        final int columnCount = metadata.getColumnCount();
        final List<String> columnLabels = this.getColumnLabels(metadata);
        for (int j = 0; j <= columnCount - 1; j++) {
            final String label = columnLabels.get(j);
            walker.setStringValue(label);
            if (this.headCellStyle != null) {
                walker.setStyle(this.headCellStyle);
            }
            walker.next();
        }
    }

    private void writeDataLineTo(final TableCellWalker walker, final int columnCount)
            throws SQLException, FastOdsException {
        final List<Object> columnValues = this.getColumnValues(columnCount);
        for (int j = 0; j <= columnCount - 1; j++) {
            final Object object = columnValues.get(j);
            if (object == null) {
                walker.setCellValue(this.nullValue);
            } else if (this.cellTypeByColIndex != null) {
                final CellType cellType = this.cellTypeByColIndex.get(j);
                if (cellType != null) {
                    walker.setCellValue(this.converter.from(cellType, object));
                } else {
                    walker.setCellValue(this.converter.from(object));
                }
            } else {
                walker.setCellValue(this.converter.from(object));
            }
            walker.next();
        }
    }

    private void writeLastLineDataTo(final TableCellWalker walker, final int columnCount,
                                     final int rowCount) {
        if (rowCount == 0) { // no data row
            for (int j = 0; j <= columnCount - 1; j++) {
                walker.setStringValue("");
                walker.next();
            }
        } else if (rowCount > this.max) {
            for (int j = 0; j <= columnCount - 1; j++) {
                walker.setStringValue(
                        String.format("... (%d rows remaining)", rowCount - this.max));
                walker.next();
            }
        }
    }
}
