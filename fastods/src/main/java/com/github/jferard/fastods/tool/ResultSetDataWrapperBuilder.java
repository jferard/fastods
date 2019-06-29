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
import com.github.jferard.fastods.ObjectToCellValueConverter;
import com.github.jferard.fastods.SimpleColor;
import com.github.jferard.fastods.StringValue;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.TimeValue;
import com.github.jferard.fastods.style.TableCellStyle;

import java.sql.ResultSet;
import java.util.Map;
import java.util.logging.Logger;

public class ResultSetDataWrapperBuilder {
    public static final TableCellStyle HEAD_STYLE = TableCellStyle.builder("rs-data-wrapper")
            .backgroundColor(SimpleColor.GRAY48).fontWeightBold().build();

    private final ResultSet rs;
    private final Map<Integer, TableCell.Type> cellTypeByIndex;
    private SQLToCellValueConverter.IntervalConverter converter;
    private String currency;
    private Logger logger;
    private TableCellStyle headStyle;
    private boolean autoFilter;
    private int max;
    private CellValue nullValue;


    public ResultSetDataWrapperBuilder(final ResultSet rs) {
        this.rs = rs;
        this.logger = null;
        this.headStyle = HEAD_STYLE;
        this.autoFilter = true;
        this.max = -1;
        this.cellTypeByIndex = null;
        this.currency = "USD";
        this.nullValue = new StringValue("<NULL>");
        this.converter = new SQLToCellValueConverter.IntervalConverter() {
            @Override
            public TimeValue castToInterval(final Object o) {
                return null;
            }
        };
    }

    public ResultSetDataWrapperBuilder logger(final Logger logger) {
        this.logger = logger;
        return this;
    }

    public ResultSetDataWrapperBuilder headStyle(final TableCellStyle headStyle) {
        this.headStyle = headStyle;
        return this;
    }

    public ResultSetDataWrapperBuilder noHeadStyle() {
        this.headStyle = null;
        return this;
    }

    public ResultSetDataWrapperBuilder max(final int max) {
        this.max = max;
        return this;
    }

    public ResultSetDataWrapperBuilder noAutoFilter() {
        this.autoFilter = false;
        return this;
    }

    public ResultSetDataWrapperBuilder typeValue(final int j, final TableCell.Type cellType) {
        this.cellTypeByIndex.put(j, cellType);
        return this;
    }

    public ResultSetDataWrapperBuilder nullValue(final CellValue nullValue) {
        this.nullValue = nullValue;
        return this;
    }

    public ResultSetDataWrapperBuilder currency(final String currency) {
        this.currency = currency;
        return this;
    }

    public ResultSetDataWrapperBuilder converter(
            final SQLToCellValueConverter.IntervalConverter converter) {
        this.converter = converter;
        return this;
    }

    public ResultSetDataWrapper build() {
        final SQLToCellValueConverter sqlToCellValueConverter = new SQLToCellValueConverter(
                new ObjectToCellValueConverter(this.currency), this.converter);
        return new ResultSetDataWrapper(this.logger,
                sqlToCellValueConverter, this.rs, this.headStyle,
                this.autoFilter, this.cellTypeByIndex, this.nullValue, this.max);
    }
}
