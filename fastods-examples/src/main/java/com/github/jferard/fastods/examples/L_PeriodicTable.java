/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.AnonymousOdsFileWriter;
import com.github.jferard.fastods.Footer;
import com.github.jferard.fastods.Header;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.PageSection;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.TableRowImpl;
import com.github.jferard.fastods.Text;
import com.github.jferard.fastods.TextBuilder;
import com.github.jferard.fastods.attribute.BorderStyle;
import com.github.jferard.fastods.attribute.CellAlign;
import com.github.jferard.fastods.attribute.Color;
import com.github.jferard.fastods.attribute.PageCentering;
import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.attribute.SimpleLength;
import com.github.jferard.fastods.attribute.VerticalAlign;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.tool.ResultSetDataWrapper;
import com.github.jferard.fastods.util.CharsetUtil;
import com.github.jferard.fastods.util.XMLUtil;
import org.h2.jdbcx.JdbcDataSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Section 10 of the tutorial
 *
 * @author J. Férard
 */
class L_PeriodicTable {
    /**
     * @throws IOException  if the file can't be written
     * @throws SQLException in something goes wrong with the local database
     */
    static void example() throws IOException, SQLException {
        // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)
        //
        // # The periodic table of the elements
        //
        // We will store the data in a small database
        //
        // We need a sheet `table` to create the table:
        final Logger periodicLogger = Logger.getLogger("periodic");
        final OdsFactory odsFactory = OdsFactory.create(periodicLogger, Locale.US);
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();
        final Table table = document.addTable("table");

        // a simple sheet `data` to write the data:
        final Table dataTable = document.addTable("data");

        // ## The data
        //
        // We'll use h2 again (see Advanced part of the tutorial). The content of the resources
        // files can be found at https://github
        // .com/jferard/fastods/blob/master/fastods-examples/src/test/resources/create.sql
        // and https://github.com/jferard/fastods/blob/master/fastods-examples/src/test/resources
        // /insert.sql.
        //
        // The results where parsed from the article https://en.wikipedia
        // .org/wiki/List_of_chemical_elements.
        //
        // We open a connection and populate the database:
        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test");
        try {
            final Connection connection = dataSource.getConnection();
            try {
                final Statement s = connection.createStatement();
                s.execute(resourceToString("create.sql"));
                s.execute(resourceToString("insert.sql"));

                // The function `resourceToString` is defined at the bottom of the section
                //
                // Now, we have a database and we can build the data table as in the Advanced
                // section but
                // we don't need the intervals:

                ResultSet rs = s.executeQuery("SELECT * FROM chemical_element");
                final TableCellWalker walker = dataTable.getWalker();
                walker.addData(ResultSetDataWrapper.builder("rs", rs).build());

                // ## The table
                // Ok, that was the easy part, just to show once more how easy it is to write a
                // ResultSet to a sheet.
                //
                // ### Style
                // Cells have a color that depends on the subcategory:
                final TableCellStyle baseCellStyle =
                        TableCellStyle.builder("ce3").textAlign(CellAlign.CENTER)
                                .verticalAlign(VerticalAlign.MIDDLE).build();


                // We put those styles in a map:
                final Map<String, TableCellStyle> cellStyleBySubcategory =
                        new HashMap<String, TableCellStyle>();
                cellStyleBySubcategory
                        .put("alkali metal", getCellStyle("alkalimetal", SimpleColor.ORANGERED));
                cellStyleBySubcategory.put("alkaline earth metal",
                        getCellStyle("alkalineearthmetal", SimpleColor.ORANGE));
                cellStyleBySubcategory.put("transition metal",
                        getCellStyle("transitionmetal", SimpleColor.YELLOW));
                cellStyleBySubcategory.put("actinide", getCellStyle("actinide", SimpleColor.GREEN));
                cellStyleBySubcategory
                        .put("metalloid", getCellStyle("metalloid", SimpleColor.LIGHTCYAN));
                cellStyleBySubcategory
                        .put("noble gas", getCellStyle("noblegas", SimpleColor.VIOLET));
                cellStyleBySubcategory.put("post-transition metal",
                        getCellStyle("posttransitionmetal", SimpleColor.STEELBLUE));
                cellStyleBySubcategory.put("reactive nonmetal",
                        getCellStyle("reactivenonmetal", SimpleColor.BLUE));
                cellStyleBySubcategory
                        .put("lanthanide", getCellStyle("lanthanide", SimpleColor.YELLOWGREEN));
                final TableCellStyle unknownStyle = getCellStyle("other", SimpleColor.WHITE);

                // The function `getCellStyle` is defined at the bottom of the section

                // Cells must be square:
                final SimpleLength CELL_SIZE = SimpleLength.cm(1.5);
                final TableColumnStyle tableColumnStyle =
                        TableColumnStyle.builder("co2").columnWidth(CELL_SIZE).build();
                for (int c = 0; c < 18; c++) {
                    table.setColumnStyle(c, tableColumnStyle);
                    table.setColumnDefaultCellStyle(c, baseCellStyle);
                }
                final TableRowStyle tableRowStyle =
                        TableRowStyle.builder("ro2").rowHeight(CELL_SIZE).build();

                // We need some styles:
                final TextStyle elementStyle =
                        TextStyle.builder("elementStyle").fontSize(SimpleLength.pt(6)).build();
                final TextStyle atomicNumberStyle =
                        TextStyle.builder("atomicNumberStyle").fontSize(SimpleLength.pt(8)).build();
                final TextStyle symbolStyle =
                        TextStyle.builder("symbolStyle").fontSize(SimpleLength.pt(12))
                                .fontWeightBold().build();

                // ### Cells content
                // The row of the element is given by its `period` and the column is the `pt_group`.
                //
                // Let's execute the same query again:
                rs = s.executeQuery("SELECT * FROM chemical_element ORDER BY atomic_number");
                while (rs.next()) {

                    // And retrieve the interesting parts
                    final String symbol = rs.getString("symbol");
                    final String elementName = rs.getString("element_name");
                    final int atomicNumber = rs.getInt("atomic_number");
                    final String subcategory = rs.getString("subcategory");
                    final int period = rs.getInt("period");
                    final int ptGroup = rs.getInt("pt_group");
                    final float atomicWeight = rs.getFloat("atomic_weight");

                    // First we ned to compute the row and the column. If the element is part of the
                    // main block, it's easy, but things get complicated when the element is part of
                    // the f-block.
                    final int r;
                    final int c;

                    if (ptGroup == 0) { // the f-block
                        r = period + 2;
                        c = (atomicNumber - 58) % 32 + 3;
                    } else {
                        r = period - 1;
                        c = ptGroup - 1;
                    }

                    // If we write something like:
                    // ```
                    // table.getRow(r).getOrCreateCell(c).setStringValue(symbol);
                    // ```
                    // We'll get a first draft. But we want something nicer.
                    //
                    // Let's look at a cell of the periodic table of elements:
                    //
                    // ```
                    // -----------------
                    // | element_name  |
                    // |               |
                    // |               |
                    // | atomic_number |
                    // |     symbol    |
                    // | atomic_weight |
                    // -----------------
                    // ```
                    // The background color depends on the `subcategory`.
                    //
                    //
                    final TableRowImpl row = table.getRow(r);
                    row.setRowStyle(tableRowStyle);
                    final Text text =
                            TextBuilder.create().parStyledContent(elementName, elementStyle)
                                    .parStyledContent(String.valueOf(atomicNumber),
                                            atomicNumberStyle).parStyledContent(symbol, symbolStyle)
                                    .parStyledContent(String.format("%.3f", atomicWeight),
                                            elementStyle).build();

                    final TableCell cell = row.getOrCreateCell(c);
                    cell.setText(text);
                    TableCellStyle cellStyle = cellStyleBySubcategory.get(subcategory);
                    if (cellStyle == null) {
                        cellStyle = unknownStyle;
                    }
                    cell.setStyle(cellStyle);

                    // ### Printing
                    // It's almost over. We just need a footer and a header. It's a copycat from
                    // the previous section:
                    final TextStyle titleStyle = TextStyle.builder("title").fontWeightBold()
                            .fontSize(SimpleLength.pt(24)).build();
                    final TextStyle dedicationStyle =
                            TextStyle.builder("dedication").fontSize(SimpleLength.pt(8))
                                    .fontStyleItalic().build();
                    final Text headerText =
                            Text.builder().parStyledContent("Periodic Table", titleStyle)
                                    .parStyledContent("For Maia", dedicationStyle).build();
                    final Header header = PageSection.simpleBuilder().text(headerText)
                            .minHeight(SimpleLength.cm(2)).buildHeader();
                    final String footerText = XMLUtil.create().escapeXMLContent(
                            "Copyright (C) 2019 J. Férard <https://github.com/jferard> " +
                                    "Creative Commons BY-SA / created with FastODS " +
                                    "(https://github.com/jferard/fastods)");
                    final Footer footer =
                            PageSection.simpleBuilder().styledContent(footerText, dedicationStyle)
                                    .buildFooter();
                    final PageStyle pageStyle =
                            PageStyle.builder("page").header(header).footer(footer)
                                    .printOrientationHorizontal().scaleTo(95)
                                    .centering(PageCentering.BOTH).build();
                    final TableStyle tableStyle =
                            TableStyle.builder("table").pageStyle(pageStyle).build();
                    table.setStyle(tableStyle);
                }
            } catch (final SQLException e) {
                periodicLogger.log(Level.SEVERE, "", e);
            } finally {
                connection.close();
            }
        } catch (final SQLException e) {
            periodicLogger.log(Level.SEVERE, "", e);
            throw e;
        }

        // And save the file.
        writer.saveAs(new File("generated_files", "l_periodic_table.ods"));
        // << END TUTORIAL (directive to extract part of a tutorial from this file)
        final File destFile = new File("generated_files", "l_periodic_table.ods");
        ExamplesTestHelper.validate(destFile);
    }
    // >> BEGIN TUTORIAL (directive to extract part of a tutorial from this file)

    // Finally, the expected functions:
    private static String resourceToString(final String resourceName) throws IOException {
        final InputStream in = L_PeriodicTable.class.getClassLoader().getResourceAsStream(resourceName);
        assert in != null : "Can't find resource "+resourceName;
        final Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
        final char[] arr = new char[8 * 1024];
        final StringBuilder sb = new StringBuilder();
        int count = reader.read(arr, 0, arr.length);
        while (count != -1) {
            sb.append(arr, 0, count);
            count = reader.read(arr, 0, arr.length);
        }
        return sb.toString();
    }

    // And to produce similar cell styles:
    private static TableCellStyle getCellStyle(final String name, final Color color) {
        return TableCellStyle.builder(name).textAlign(CellAlign.CENTER)
                .verticalAlign(VerticalAlign.MIDDLE).backgroundColor(color)
                .borderAll(SimpleLength.pt(2), SimpleColor.BLACK, BorderStyle.SOLID).build();
    }

    // *Note*: The code of this section is badly structured because of the tutorial format. I
    // don't want to create a lot a small functions because it would be harder for the reader
    // to follow the logic.
    // << END TUTORIAL (directive to extract part of a tutorial from this file)
}
