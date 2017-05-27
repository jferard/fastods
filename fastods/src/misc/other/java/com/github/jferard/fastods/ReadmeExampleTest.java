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

package com.github.jferard.fastods;

import com.github.jferard.fastods.style.TableCellStyle;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

/**
 */
public class ReadmeExampleTest {

	private OdsFactory odsFactory;

	@Test
	public void readme() throws IOException {
		this.odsFactory = OdsFactory.create(Logger.getLogger("example"), Locale.US);
		final AnonymousOdsFileWriter writer = this.odsFactory.createWriter();
		final OdsDocument document = writer.document();
		final Table table = document.addTable("test");

		final TableCellStyle style = TableCellStyle.builder("tcs1").backgroundColor("#00FF00").build();
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
