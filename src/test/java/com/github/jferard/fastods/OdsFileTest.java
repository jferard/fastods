/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jferard.fastods;

import java.util.Calendar;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Optional;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file BenchTest.java is part of FastODS.
 */
public class OdsFileTest {

	@Test
	public final void test50() throws FastOdsException {
		System.out.println("Filling a 50 rows, 5 columns spreadsheet");
		long t1 = System.currentTimeMillis();
		final Random random = new Random();

		OdsFile file = OdsFile.create("5columns.ods");
		Optional<Table> optTable = file.addTable("test");
		Assert.assertTrue(optTable.isPresent());

		Table table = optTable.get();
		TableRow row = table.getRow(0);
		TableRowStyle trs = TableRowStyle.builder("rr").rowHeight("5cm")
				.build();
//		trs.addToFile(file);
		TableCellStyle tcls = TableCellStyle.builder("cc")
				.backgroundColor("#dddddd").fontWeightBold().build();
//		tcls.addToFile(file);
		row.setStyle(trs);
		row.setDefaultCellStyle(tcls);
		TableCellStyle tcls2 = TableCellStyle.builder("dd").fontColor("#ff0000")
				.build();
//		tcls2.addToFile(file);
		TableColumnStyle tcns = TableColumnStyle.builder("ccs")
				.columnWidth("10cm").defaultCellStyle(tcls).build();
//		tcns.addToFile(file);
		table.setColumnStyle(0, tcns);

		for (int y = 0; y < 50; y++) {
			row = table.getRow(y);
			for (int x = 0; x < 5; x++) {
				TableCell cell = row.getCell(x);
				cell.setFloatValue(random.nextInt(1000));
				if ((y + 1) % 3 == 0) {
					switch (x) {
					case 0:
						cell.setStyle(TableCellStyle.builder("tcs0")
								.backgroundColor("#0000ff").build());
						break;
					case 1:
						cell.setStyle(TableCellStyle.builder("tcs1")
								.backgroundColor("#00FF00").build());
						break;
					case 2:
						cell.setStyle(TableCellStyle.builder("tcs2")
								.fontWeightBold().build());
						break;
					case 3:
						cell.setStyle(TableCellStyle.builder("tcs3")
								.fontStyleItalic().build());
						break;
					default:
						break;
					}
				} else if (y == 6) {
					switch (x) {
					case 0:
						cell.setBooleanValue(true); break;
					case 1:
						cell.setCurrencyValue(150.5, "€"); break;
					case 2:
						cell.setDateValue(Calendar.getInstance());
						final DateStyle build0 = DateStyle.builder("trgfgbf").build();
//						build0.addToFile(file);
						final TableCellStyle build1 = TableCellStyle.builder("ttete").dataStyle(build0).build();
//						build1.addToFile(file);
						cell.setStyle(build1);
						 break;
					case 3:
						cell.setPercentageValue(70.3); break;
					case 4:
						cell.setStringValue("foobar"); break;
					default: break;
					}
				} else if (y == 9) {
					switch (x) {
					case 0:
					case 3:
						cell.setStyle(tcls); break;
					default:
						cell.setTimeValue(x*60*1000);
					}
				}
			}
		}
		file.save();

		long t2 = System.currentTimeMillis();
		System.out.println("Filled in " + (t2 - t1) + " ms");
	}

	// @Test
	public final void test100000() throws FastOdsException {
		System.out.println("Filling a 100000 rows, 20 columns spreadsheet");
		long t1 = System.currentTimeMillis();
		final Random random = new Random();

		OdsFile file = OdsFile.create("100000columns.ods");
		Optional<Table> optTable = file.addTable("test");
		Assert.assertTrue(optTable.isPresent());

		Table table = optTable.get();
		for (int y = 0; y < 100000; y++) {
			final TableRow row = table.nextRow();
			for (int x = 0; x < 20; x++) {
				TableCell cell = row.getCell(x);
				cell.setFloatValue(random.nextInt(1000));
			}
		}

		file.save();

		long t2 = System.currentTimeMillis();
		System.out.println("Filled in " + (t2 - t1) + " ms");
	}

	// @Test
	public final void test1000() throws FastOdsException {
		System.out.println("Filling a 10000 rows, 300 columns spreadsheet");
		long t1 = System.currentTimeMillis();
		final Random random = new Random();

		OdsFile file = OdsFile.create("1000columns.ods");
		Optional<Table> optTable = file.addTable("test");
		Assert.assertTrue(optTable.isPresent());

		Table table = optTable.get();
		for (int y = 0; y < 1000; y++) {
			final TableRow row = table.nextRow();
			for (int x = 0; x < 300; x++) {
				TableCell cell = row.getCell(x);
				cell.setFloatValue(random.nextInt(1000));
			}
		}

		file.save();

		long t2 = System.currentTimeMillis();
		System.out.println("Filled in " + (t2 - t1) + " ms");
	}
}
