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

package com.github.jferard.fastods.extra.it;

import com.github.jferard.fastods.extra.CSVUtil;
import com.github.jferard.javamcsv.MetaCSVDataException;
import com.github.jferard.javamcsv.MetaCSVParseException;
import com.github.jferard.javamcsv.MetaCSVReadException;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class CSVUtilIT {
    @Test
    public void test()
            throws IOException, MetaCSVReadException, MetaCSVDataException, MetaCSVParseException {
        final InputStream is = getResourceAsStream("example.csv");
        final InputStream metaIs = getResourceAsStream("example.mcsv");
        final File file = new File("csv-test.ods");

        final OutputStream os = new FileOutputStream(
                file);
        final String tableName = "table";
        try {
            CSVUtil.csvToOds(is, metaIs, os, tableName);
        } finally {
            os.flush();
            os.close();
        }
    }

    private static InputStream getResourceAsStream(final String name) {
        return CSVUtilIT.class.getClassLoader().getResourceAsStream(name);
    }

    public static File getResourceAsFile(final String name) throws URISyntaxException {
        return new File(
                CSVUtilIT.class.getClassLoader().getResource(name).toURI().getPath());
    }

}
