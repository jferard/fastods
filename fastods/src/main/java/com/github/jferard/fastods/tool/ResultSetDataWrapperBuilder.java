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

package com.github.jferard.fastods.tool;

import com.github.jferard.fastods.CellValue;
import com.github.jferard.fastods.StringValue;
import com.github.jferard.fastods.TimeValue;
import com.github.jferard.fastods.attribute.CellType;
import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.style.TableCellStyle;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A builder for a ResultSetDataWrapper
 *
 * @author J. Férard
 */
public class ResultSetDataWrapperBuilder {
    private static final TableCellStyle HEADER_STYLE =
            TableCellStyle.builder("rs-data-wrapper").backgroundColor(SimpleColor.GRAY64)
                    .fontWeightBold().build();

    private final String rangeName;
    private final ResultSet rs;
    private final Map<Integer, CellType> cellTypeByIndex;
    private Charset charset;
    private SQLToCellValueConverter.IntervalConverter converter;
    private String currency;
    private Logger logger;
    private TableCellStyle headerStyle;
    private boolean autoFilter;
    private int max;
    private CellValue nullValue;


    /**
     * @param rangeName
     * @param rs        the result set
     */
    public ResultSetDataWrapperBuilder(final String rangeName, final ResultSet rs) {
        this.rangeName = rangeName;
        this.rs = rs;
        this.logger = null;
        this.headerStyle = HEADER_STYLE;
        this.autoFilter = true;
        this.max = -1;
        this.cellTypeByIndex = new HashMap<Integer, CellType>();
        this.currency = NumberFormat.getCurrencyInstance(Locale.US).getCurrency().getSymbol();
        this.charset = Charset.forName("US-ASCII");
        this.nullValue = new StringValue("<NULL>");
        this.converter = new SQLToCellValueConverter.IntervalConverter() {
            @Override
            public TimeValue castToInterval(final Object o) {
                return null;
            }
        };
    }

    /**
     * Set a logger
     *
     * @param logger the logger
     * @return this for fluent style
     */
    public ResultSetDataWrapperBuilder logger(final Logger logger) {
        this.logger = logger;
        return this;
    }

    /**
     * Set a header style
     *
     * @param headerStyle the cell style for the header
     * @return this for fluent style
     */
    public ResultSetDataWrapperBuilder headerStyle(final TableCellStyle headerStyle) {
        this.headerStyle = headerStyle;
        return this;
    }

    /**
     * Remove the default header style
     *
     * @return this for fluent style
     */
    public ResultSetDataWrapperBuilder noHeaderStyle() {
        this.headerStyle = null;
        return this;
    }

    /**
     * Set a limit to the number of rows
     *
     * @param max the last line written
     * @return this for fluent style
     */
    public ResultSetDataWrapperBuilder max(final int max) {
        this.max = max;
        return this;
    }

    /**
     * Remove the auto filter
     *
     * @return this for fluent style
     */
    public ResultSetDataWrapperBuilder noAutoFilter() {
        this.autoFilter = false;
        return this;
    }

    /**
     * Give a hint for a column type
     *
     * @param j        the col index
     * @param cellType the expected cell type
     * @return this for fluent style
     */
    public ResultSetDataWrapperBuilder typeValue(final int j, final CellType cellType) {
        this.cellTypeByIndex.put(j, cellType);
        return this;
    }

    /**
     * Set a value for SQL NULLs.
     *
     * @param nullValue the null value
     * @return this for fluent style
     */
    public ResultSetDataWrapperBuilder nullValue(final CellValue nullValue) {
        this.nullValue = nullValue;
        return this;
    }

    /**
     * Set the currency
     *
     * @param currency the currency
     * @return this for fluent style
     */
    public ResultSetDataWrapperBuilder currency(final String currency) {
        this.currency = currency;
        return this;
    }

    /**
     * Set the charset for byte values
     *
     * @param charset the charset
     * @return this for fluent style
     */
    public ResultSetDataWrapperBuilder charset(final Charset charset) {
        this.charset = charset;
        return this;
    }

    /**
     * JDBC misses some way to handle intervals. If the Result set
     * contains intervals, one must provide a custom converter.
     *
     * @param converter the converter for Interval
     * @return this for fluent style
     */
    public ResultSetDataWrapperBuilder converter(
            final SQLToCellValueConverter.IntervalConverter converter) {
        this.converter = converter;
        return this;
    }

    /**
     * @return the data wrapper
     */
    public ResultSetDataWrapper build() {
        final SQLToCellValueConverter sqlToCellValueConverter =
                SQLToCellValueConverter.create(this.converter, this.currency, this.charset);
        final Map<Integer, CellType> cellTypeByIndexOrNull =
                this.cellTypeByIndex.isEmpty() ? null : this.cellTypeByIndex;
        return new ResultSetDataWrapper(this.logger, sqlToCellValueConverter, this.rangeName,
                this.rs, this.headerStyle, this.autoFilter, cellTypeByIndexOrNull, this.nullValue,
                this.max);
    }
}
