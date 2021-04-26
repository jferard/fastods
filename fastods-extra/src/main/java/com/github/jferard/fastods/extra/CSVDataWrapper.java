/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.extra;

import com.github.jferard.fastods.DataWrapper;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.javamcsv.DataType;
import com.github.jferard.javamcsv.MetaCSVMetaData;
import com.github.jferard.javamcsv.MetaCSVReadException;
import com.github.jferard.javamcsv.MetaCSVReader;
import com.github.jferard.javamcsv.MetaCSVRecord;
import com.github.jferard.javamcsv.description.CurrencyDecimalFieldDescription;
import com.github.jferard.javamcsv.description.CurrencyIntegerFieldDescription;
import com.github.jferard.javamcsv.description.ObjectFieldDescription;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class CSVDataWrapper implements DataWrapper {
    public static CSVDataWrapperBuilder builder(final File csvFile) {
        return new CSVDataWrapperBuilder(csvFile);
    }

    public static CSVDataWrapperBuilder builder(final InputStream is) {
        return new CSVDataWrapperBuilder(is);
    }

    private final Logger logger;
    private final String rangeName;
    private final MetaCSVReader metaCSVReader;
    private final ObjectProcessorFactory objectProcessorFactory;
    private final TableCellStyle headCellStyle;
    private final int max;

    public CSVDataWrapper(final Logger logger, final ObjectProcessorFactory objectProcessorFactory,
                          final String rangeName, final MetaCSVReader metaCSVReader,
                          final TableCellStyle headCellStyle,
                          final int max) {
        this.logger = logger;
        this.rangeName = rangeName;
        this.metaCSVReader = metaCSVReader;
        this.objectProcessorFactory = objectProcessorFactory;
        this.headCellStyle = headCellStyle;
        this.max = max;
    }

    @Override
    public boolean addToTable(final TableCellWalker walker) throws IOException {
        try {
        int rowCount = 0; // at least
        final int r1 = walker.rowIndex();
        final int c1 = walker.colIndex();
        final MetaCSVMetaData metaData = this.metaCSVReader.getMetaData();
        final Iterator<MetaCSVRecord> iterator = this.metaCSVReader.iterator();
        if (!iterator.hasNext()) {
            return false;
        }
        final MetaCSVRecord header = iterator.next();
        final int columnCount = header.size();
        final List<ValueProcessor> processors =
                this.createProcessors(metaData, columnCount);
            this.writeFirstLineTo(header, columnCount, walker, c1);
        while (iterator.hasNext()) {
            if (this.max >= 0 && ++rowCount > this.max) {
                break;
            }
            walker.to(c1);
            final MetaCSVRecord record = iterator.next();
            for (int i = 0; i < record.size(); i++) {
                processors.get(i).processValue(record, i, walker);
                walker.next();
            }
            walker.nextRow();
        }
        if (this.rangeName != null) {
            final int r2 = walker.rowIndex();
            final int c2 = c1 + columnCount - 1;
            walker.getTable().addAutoFilter(this.rangeName, r1, c1, r2, c2);
        }
        walker.nextRow();
        this.metaCSVReader.close();
        return true;
        } catch (final MetaCSVReadException e) {
            throw new IOException(e);
        }
    }

    private void writeFirstLineTo(final MetaCSVRecord header, final int columnCount,
                                  final TableCellWalker walker, final int c1)
            throws IOException, MetaCSVReadException {
        walker.to(c1);
        for (int i = 0; i < columnCount; i++) {
            walker.setStringValue(header.getText(i).toString());
            if (this.headCellStyle != null) {
                walker.setStyle(this.headCellStyle);
            }
            walker.next();
        }
        walker.nextRow();
    }

    private List<ValueProcessor> createProcessors(final MetaCSVMetaData metaData,
                                                  final int columnCount) {
        final List<ValueProcessor> processors = new ArrayList<ValueProcessor>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            final DataType dataType = metaData.getDataType(i);
            final ValueProcessor processor;
            switch (dataType) {
                case BOOLEAN:
                    processor = new ValueProcessor() {
                        @Override
                        public void processValue(final MetaCSVRecord record, final int i,
                                                 final TableCellWalker walker)
                                throws MetaCSVReadException {
                            walker.setBooleanValue(record.getBoolean(i));
                        }
                    };
                    break;
                case CURRENCY_DECIMAL:
                    final String currencyDecimalSymbol =
                            metaData.getDescription(i, CurrencyDecimalFieldDescription.class)
                                    .getCurrencySymbol();
                    processor = new ValueProcessor() {
                        @Override
                        public void processValue(final MetaCSVRecord record, final int i,
                                                 final TableCellWalker walker)
                                throws MetaCSVReadException {
                            walker.setCurrencyValue(record.getDecimal(i), currencyDecimalSymbol);
                        }
                    };
                    break;
                case CURRENCY_INTEGER:
                    final String currencyIntegerSymbol =
                            metaData.getDescription(i, CurrencyIntegerFieldDescription.class)
                                    .getCurrencySymbol();
                    processor = new ValueProcessor() {
                        @Override
                        public void processValue(final MetaCSVRecord record, final int i,
                                                 final TableCellWalker walker)
                                throws MetaCSVReadException {
                            walker.setCurrencyValue(record.getInteger(i),
                                    currencyIntegerSymbol);
                        }
                    };
                    break;
                case DECIMAL:
                    processor = new ValueProcessor() {
                        @Override
                        public void processValue(final MetaCSVRecord record, final int i,
                                                 final TableCellWalker walker)
                                throws MetaCSVReadException {
                            walker.setFloatValue(record.getDecimal(i));
                        }
                    };
                    break;
                case DATE:
                    processor = new ValueProcessor() {
                        @Override
                        public void processValue(final MetaCSVRecord record, final int i,
                                                 final TableCellWalker walker)
                                throws MetaCSVReadException {
                            walker.setDateValue(record.getDate(i));
                        }
                    };
                    break;
                case DATETIME:
                    processor = new ValueProcessor() {
                        @Override
                        public void processValue(final MetaCSVRecord record, final int i,
                                                 final TableCellWalker walker)
                                throws MetaCSVReadException {
                            walker.setDateValue(record.getDatetime(i));
                        }
                    };
                    break;
                case FLOAT:
                    processor = new ValueProcessor() {
                        @Override
                        public void processValue(final MetaCSVRecord record, final int i,
                                                 final TableCellWalker walker)
                                throws MetaCSVReadException {
                            walker.setFloatValue(record.getFloat(i));
                        }
                    };
                    break;
                case INTEGER:
                    processor = new ValueProcessor() {
                        @Override
                        public void processValue(final MetaCSVRecord record, final int i,
                                                 final TableCellWalker walker)
                                throws MetaCSVReadException {
                            walker.setFloatValue(record.getInteger(i));
                        }
                    };
                    break;
                case PERCENTAGE_DECIMAL:
                    processor = new ValueProcessor() {
                        @Override
                        public void processValue(final MetaCSVRecord record, final int i,
                                                 final TableCellWalker walker)
                                throws MetaCSVReadException {
                            walker.setPercentageValue(record.getDecimal(i));
                        }
                    };
                    break;
                case PERCENTAGE_FLOAT:
                    processor = new ValueProcessor() {
                        @Override
                        public void processValue(final MetaCSVRecord record, final int i,
                                                 final TableCellWalker walker)
                                throws MetaCSVReadException {
                            walker.setPercentageValue(record.getFloat(i));
                        }
                    };
                    break;
                case TEXT:
                    processor = new ValueProcessor() {
                        @Override
                        public void processValue(final MetaCSVRecord record, final int i,
                                                 final TableCellWalker walker)
                                throws MetaCSVReadException {
                            walker.setStringValue(record.getText(i).toString());
                        }
                    };
                    break;
                case OBJECT:
                    final List<String> parameters =
                            metaData.getDescription(i, ObjectFieldDescription.class)
                                    .getParameters();
                    processor = this.objectProcessorFactory.create(parameters);
                    break;
                default:
                    throw new IllegalStateException();
            }
            processors.add(processor);
        }
        return processors;
    }
}
