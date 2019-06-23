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

package com.github.jferard.fastods.examples;

import com.github.jferard.fastods.FastOdsException;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FastODSExamplesIT {
    @BeforeClass
    public static void beforeClass() throws IOException {
        Files.createDirectories(Paths.get("generated_files"));
    }

    @Test
    public void helloWorldTest() throws IOException, FastOdsException {
        A_HelloWorld.example();
    }

    @Test
    public void accessingTablesRowsAndCellsTest() throws IOException, FastOdsException {
        B_AccessingTablesRowsAndCells.example();
    }

    @Test
    public void cellValueTest() throws IOException, FastOdsException {
        C_SettingTheCellValue.example();
    }

    @Test
    public void cellDataStyleTest1() throws IOException, FastOdsException {
        D_SettingTheCellDataStyle.example1();
    }

    @Test
    public void cellDataStyleTest2() throws IOException, FastOdsException {
        D_SettingTheCellDataStyle.example2();
    }

    @Test
    public void cellStyleTest() throws IOException, FastOdsException {
        E_SettingTheCellStyle.example();
    }

    @Test
    public void moreOnCellsTest() throws IOException, FastOdsException, URISyntaxException {
        F_MoreOnCells.example();
    }

    @Test
    public void pageFormatTest() throws IOException, FastOdsException {
        G_PageFormat.example();
    }

    @Test
    public void advancedTest() throws IOException, FastOdsException {
        H_Advanced.example();
    }
}