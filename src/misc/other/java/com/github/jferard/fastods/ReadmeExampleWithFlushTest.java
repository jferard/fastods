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
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

/**
 */
public class ReadmeExampleWithFlushTest {
	private Logger logger;
	private OdsFactory odsFactory;

	@Before
	public void setUp() {
		this.logger = Logger.getLogger("OdsFileCreation");
		this.odsFactory = new OdsFactory(this.logger, Locale.US);
	}

	@Test
	public void readme() throws IOException {
		final OdsDocument document = this.odsFactory.createDocument();
		final OdsFileWriter writer =
				this.odsFactory.createWriter(document, new File("generated_files", "readme.ods"));
		final Table table = document.addTable("test");

		final TableCellStyle style = TableCellStyle.builder("tcs1").backgroundColor("#00FF00").build();
		for (int y = 0; y < 50; y++) {
			final HeavyTableRow row = table.nextRow();
			final TableCellWalker cell = row.getWalker();
			for (int x = 0; x < 5; x++) {
				cell.setFloatValue(x*y);
				cell.setStyle(style);
				cell.next();
			}
		}

		writer.save();
	}
}
