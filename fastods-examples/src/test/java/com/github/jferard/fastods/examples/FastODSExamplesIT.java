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

package com.github.jferard.fastods.examples;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class FastODSExamplesIT {
    @BeforeClass
    public static void beforeClass() {
        final File directory = new File("generated_files");
        directory.mkdir();
    }

    @Test
    public void helloWorldTest() throws IOException {
        A_HelloWorld.example();
    }

    @Test
    public void accessingTablesRowsAndCellsTest() throws IOException {
        B_AccessingTablesRowsAndCells.example();
    }

    @Test
    public void cellValueTest() throws IOException {
        C_SettingTheCellValue.example();
    }

    @Test
    public void cellDataStyleTest1() throws IOException {
        D_SettingTheCellDataStyle.example1();
    }

    @Test
    public void cellDataStyleTest2() throws IOException {
        D_SettingTheCellDataStyle.example2();
    }

    @Test
    public void cellStyleTest() throws IOException {
        E_SettingTheCellStyle.example();
    }

    @Test
    public void moreOnCellsTest() throws IOException, URISyntaxException {
        F_MoreOnCells.example1();
    }

    @Test
    public void moreOnCellsFormulasTest() throws IOException, URISyntaxException {
        F_MoreOnCells.example2();
    }

    @Test
    public void pageFormatTest1() throws IOException {
        G_PageFormat.example1();
    }

    @Test
    public void pageFormatTest2() throws IOException {
        G_PageFormat.example2();
    }

    @Test
    public void pageFormatTest3() throws IOException {
        G_PageFormat.example3();
    }

    @Test
    public void filtersAndPilotTest1() throws IOException {
        H_AutofiltersAndDataPilotTables.example1();
    }

    @Test
    public void filtersAndPilotTest2() throws IOException {
        H_AutofiltersAndDataPilotTables.example2();
    }

    @Test
    public void embeddingFileTest() throws IOException {
        I_Embedding.exampleWithFile();
    }

    @Test
    public void embeddingImageTest() throws IOException {
        I_Embedding.exampleWithImage();
    }

    @Test
    public void embeddingDocumentTest()
            throws IOException, ParserConfigurationException, SAXException {
        I_Embedding.exampleWithDocument();
    }

    @Test
    public void embeddingCommentBGTest() throws IOException {
        I_Embedding.exampleWithCommentBG();
    }

    @Test
    public void embeddingTableTest() throws IOException {
        I_Embedding.exampleWithTable();
    }

    @Test
    public void miscTest1() throws IOException {
        J_Misc.namedWriterExample();
    }

    @Test
    public void miscTest2() throws IOException, SQLException {
        J_Misc.resultSetExample();
    }

    @Test
    public void miscTest3() throws IOException, SQLException {
        J_Misc.databaseExample();
    }

    @Test
    public void miscTest4() throws IOException, SQLException {
        J_Misc.freezeCellExample();
    }

    @Test
    public void customAttributeExampleTest() throws IOException {
        J_Misc.customAttributeExample();
    }

    @Test
    public void miscTest5() throws IOException {
        J_Misc.customCell();
    }

    @Test
    public void miscTest6() throws IOException {
        J_Misc.namedSpacesExample();
    }

    @Test
    public void multiplicationTableTest() throws IOException {
        K_MultiplicationTable.example();
    }

    @Test
    public void periodicTableTest() throws IOException, SQLException {
        L_PeriodicTable.example();
    }

    @Test
    public void validationTest() throws IOException {
        M_Protection.validationExample();
    }

    @Test
    public void protectionTest() throws IOException, NoSuchAlgorithmException {
        M_Protection.protectionExample();
    }

    @Test
    public void cryptoTest() throws IOException, NoSuchAlgorithmException {
        M_Protection.cryptoExample();
    }

    @Test
    public void usingTheBusTest() throws IOException {
        N_UsingTheBus.example();
    }
}