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

		OdsFile file = new OdsFile("5columns.ods");
		Optional<Table> optTable = file.addTable("test");
		Assert.assertTrue(optTable.isPresent());

		Table table = optTable.get();
		for (int y = 0; y < 50; y++) {
			final TableRow row = table.nextRow();
			for (int x = 0; x < 5; x++) {
				row.setCell(x, String.valueOf(random.nextInt(1000)));
				switch (x) {
				case 1:
					row.setCellStyle(x, TableCellStyle.builder().name("tcs1")
							.backgroundColor("#00FF00").build());
					break;
				case 2:
					row.setCellStyle(x, TableCellStyle.builder().name("tcs2")
							.fontWeightBold().build());
					break;
				case 3:
					row.setCellStyle(x, TableCellStyle.builder().name("tcs3")
							.fontWeightItalic().build());
					break;
				default:
					break;
				}
			}
		}
		file.save();

		long t2 = System.currentTimeMillis();
		System.out.println("Filled in " + (t2 - t1) + " ms");
	}

//	@Test
	public final void test100000() throws FastOdsException {
		System.out.println("Filling a 100000 rows, 20 columns spreadsheet");
		long t1 = System.currentTimeMillis();
		final Random random = new Random();

		OdsFile file = new OdsFile("100000columns.ods");
		Optional<Table> optTable = file.addTable("test");
		Assert.assertTrue(optTable.isPresent());

		Table table = optTable.get();
		for (int y = 0; y < 100000; y++) {
			final TableRow row = table.nextRow();
			for (int x = 0; x < 20; x++) {
				row.setCell(x, String.valueOf(random.nextInt(1000)));
			}
		}

		file.save();

		long t2 = System.currentTimeMillis();
		System.out.println("Filled in " + (t2 - t1) + " ms");
	}

//	@Test
	public final void test1000() throws FastOdsException {
		System.out.println("Filling a 10000 rows, 300 columns spreadsheet");
		long t1 = System.currentTimeMillis();
		final Random random = new Random();

		OdsFile file = new OdsFile("1000columns.ods");
		Optional<Table> optTable = file.addTable("test");
		Assert.assertTrue(optTable.isPresent());

		Table table = optTable.get();
		for (int y = 0; y < 1000; y++) {
			final TableRow row = table.nextRow();
			for (int x = 0; x < 300; x++) {
				row.setCell(x, String.valueOf(random.nextInt(1000)));
			}
		}

		file.save();

		long t2 = System.currentTimeMillis();
		System.out.println("Filled in " + (t2 - t1) + " ms");
	}
}
