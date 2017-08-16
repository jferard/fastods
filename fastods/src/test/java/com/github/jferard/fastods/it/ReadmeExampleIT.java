/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.it;

import com.github.jferard.fastods.*;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.style.TableCellStyle;
import org.junit.Assert;
import org.junit.Test;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElementBase;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStyle;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * This is the test for the README.md
 */
public class ReadmeExampleIT {
	@Test
	public void readmeIT() throws Exception {
		this.readme();
		this.validateReadme();
	}

	private void validateReadme() throws Exception {
		final SpreadsheetDocument document = SpreadsheetDocument.loadDocument(new File("generated_files", "readme_example.ods"));
		Assert.assertEquals(1, document.getSheetCount());
		final org.odftoolkit.simple.table.Table sheet = document.getSheetByName("test");
		Assert.assertNotNull(sheet);
		Assert.assertEquals(50, sheet.getRowCount());
		final OdfStyle gcs = document.getStylesDom().getOfficeStyles().getStyle("green cell style", OdfStyleFamily.TableCell);
		Assert.assertEquals("Default", gcs.getStyleParentStyleNameAttribute());
		final Node properties = gcs.getElementsByTagName("style:table-cell-properties").item(0);
		final NamedNodeMap attributes = properties.getAttributes();
		Assert.assertEquals("#00FF00", attributes.getNamedItem("fo:background-color").getTextContent());
		for (int y = 0; y < 50; y++) {
			for (int x = 0; x < 5; x++) {
				final org.odftoolkit.simple.table.Cell cell = sheet.getCellByPosition(x, y);
				Assert.assertEquals(Double.valueOf(x*y), cell.getDoubleValue());
				Assert.assertEquals("float", cell.getValueType());

				final TableTableCellElementBase element = cell.getOdfElement();
				Assert.assertEquals("green cell style@@float-data", element.getStyleName());
				Assert.assertEquals("table-cell", element.getStyleFamily().toString());
				Assert.assertEquals("green cell style", element.getAutomaticStyle().getStyleParentStyleNameAttribute());
			}
		}
	}

	private void readme() throws IOException {
		final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger("example"), Locale.US);
		final AnonymousOdsFileWriter writer = odsFactory.createWriter();
		final OdsDocument document = writer.document();
		final Table table = document.addTable("test");

		final TableCellStyle style = TableCellStyle.builder("green cell style").backgroundColor("#00FF00").build();
		for (int y = 0; y < 50; y++) {
			final TableRow row = table.nextRow();
			final TableCellWalker cell = row.getWalker();
			for (int x = 0; x < 5; x++) {
				cell.setFloatValue(x*y);
				cell.setStyle(style);
				cell.next();
			}
		}

		writer.saveAs(new File("generated_files", "readme_example.ods"));
	}
}
