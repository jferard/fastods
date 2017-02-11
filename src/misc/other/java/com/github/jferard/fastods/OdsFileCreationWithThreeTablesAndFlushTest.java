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
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Julien Férard
 */
public class OdsFileCreationWithThreeTablesAndFlushTest {
	@BeforeClass
	public static final void beforeClass() {
		final File generated_files = new File("generated_files");
		if (generated_files.exists())
			return;

		generated_files.mkdir();
	}
	private Logger logger;
	private OdsFactory odsFactory;
	private Random random;

	@Before
	public void setUp() {
		this.logger = Logger.getLogger("OdsFileCreation");
		this.odsFactory = new OdsFactory(this.logger, Locale.US);
		this.random = new Random(0L);
	}

	@Test
	public final void test3Tables() throws FastOdsException, IOException {
		this.logger.info("Filling a 3 tables, 5 rows, 5 columns spreadsheet");
		final long t1 = System.currentTimeMillis();

		final OdsFileWriter writer =
				this.odsFactory.createWriter(new File("generated_files", "fastods_3t.ods"));
		final OdsDocument document = writer.document();

		try {
			document.setViewSetting("View1", "ZoomValue", "200");

			final TableRowStyle trs = TableRowStyle.builder("rr").rowHeight("5cm")
					.build();
			final TableCellStyle tcls = TableCellStyle.builder("cc")
					.backgroundColor("#dddddd").fontWeightBold().build();
			final TableColumnStyle tcns = TableColumnStyle.builder("ccs")
					.columnWidth("10cm").defaultCellStyle(tcls).build();
			final TableCellStyle tcs0 = TableCellStyle.builder("tcs0")
					.backgroundColor("#0000ff").build();
			final TableCellStyle tcs1 = TableCellStyle.builder("tcs1")
					.backgroundColor("#00FF00").build();
			final TableCellStyle tcs2 = TableCellStyle.builder("tcs2")
					.fontWeightBold().build();
			final TableCellStyle tcs3 = TableCellStyle.builder("tcs3")
					.fontStyleItalic().build();
			document.addStyleTag(trs);
			document.addStyleTag(tcls);
			document.addStyleTag(tcns);
			document.addStyleTag(tcs0);
			document.addStyleTag(tcs1);
			document.addStyleTag(tcs2);
			document.addStyleTag(tcs3);
			document.addChildCellStyle(TableCell.Type.FLOAT);
			document.addChildCellStyle(tcs0, TableCell.Type.FLOAT);
			document.addChildCellStyle(tcs1, TableCell.Type.FLOAT);
			document.addChildCellStyle(tcs2, TableCell.Type.FLOAT);
			document.addChildCellStyle(tcs3, TableCell.Type.FLOAT);
			document.freezeStyles();

			for (int i = 0; i < 3; i++) {
				final Table table = document.addTable("test" + i, 5, 5);
				table.setSettings("View1", "ZoomValue", "200");
				table.setColumnStyle(0, tcns);
				HeavyTableRow row = table.getRow(0);
				row.setStyle(trs);
				row.setDefaultCellStyle(tcls);

				row = table.getRow(0);
				row.setStringValue(0, "éèà");
				row.setStringValue(1, "€€€€");
				row.setStringValue(2, "£");
				for (int y = 1; y < 5; y++) {
					row = table.getRow(y);
					final TableCellWalker walker = row.getWalker();
					for (int x = 0; x < 5; x++) {
						walker.setFloatValue(this.random.nextInt(1000));
						if ((y + 1) % 3 == 0) {
							switch (x) {
								case 0:
									walker.setStyle(tcs0);
									break;
								case 1:
									walker.setStyle(tcs1);
									break;
								case 2:
									walker.setStyle(tcs2);
									break;
								case 3:
									walker.setStyle(tcs3);
									break;
								default:
									break;
							}
						} else if (y == 6) {
							switch (x) {
								case 0:
									walker.setBooleanValue(true);
									break;
								case 1:
									walker.setCurrencyValue(150.5, "EUR");
									walker.setTooltip("That's a tooltip !");
									break;
								case 2:
									walker.setDateValue(Calendar.getInstance());
									break;
								case 3:
									walker.setPercentageValue(70.3);
									break;
								case 4:
									walker.setStringValue("foobar");
									break;
								default:
									break;
							}
						} else if (y == 9) {
							switch (x) {
								case 0:
									walker.setColumnsSpanned(2);
									break;
								case 2:
									walker.setCurrencyValue(-150.5, "€");
									break;
								case 3:
									walker.setStyle(tcls);
									break;
								default:
									walker.setTimeValue(x * 60 * 1000);
							}
						}
						walker.next();
					}
				}
			}
		} finally {
			document.save();
		}
		final long t2 = System.currentTimeMillis();
		this.logger.info("Filled in " + (t2 - t1) + " ms");
	}
}
