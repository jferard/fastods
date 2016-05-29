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

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.table.DefaultTableModel;

import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.simpleods.ObjectQueue;

import com.google.common.base.Optional;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file BenchTest.java is part of FastODS.
 */
public class BenchTest {
	private static final int COL_COUNT = 40;
	private static final int ROW_COUNT = 10000;
	private Random random;
	private long t1;

	@Rule public TestName name = new TestName();
	
	@Before
	public final void setUp() {
		this.random = new Random();
		System.out.println(this.name.getMethodName()+" : filling a " + ROW_COUNT + " rows, "
				+ COL_COUNT + " columns spreadsheet");
		this.t1 = System.currentTimeMillis();
	}
	
	@After
	public final void tearDown() {
		long t2 = System.currentTimeMillis();
		System.out.println("Filled in " + (t2 - this.t1) + " ms");
	}

	@Test
	public final void testFast() throws FastOdsException {
		// Open the file.
		OdsFile file = new OdsFile("f20columns.ods");
		Optional<Table> optTable = file.addTable("test");
		Assert.assertTrue(optTable.isPresent());

		Table table = optTable.get();
		for (int y = 0; y < ROW_COUNT; y++) {
			final TableRow row = table.nextRow();
			for (int x = 0; x < COL_COUNT; x++) {
				TableCell cell = row.getCell(x);
				cell.setFloatValue(this.random.nextInt(1000));
			}
		}

		file.save();
	}
	
//	@Test
	public final void testFast2() throws FastOdsException {
		// Open the file.
		OdsFile file = new OdsFile("f60columns.ods");
		Optional<Table> optTable = file.addTable("test");
		Assert.assertTrue(optTable.isPresent());

		Table table = optTable.get();
		for (int y = 0; y < 3*ROW_COUNT; y++) {
			final TableRow row = table.nextRow();
			for (int x = 0; x < 3*COL_COUNT; x++) {
				TableCell cell = row.getCell(x);
				cell.setFloatValue(this.random.nextInt(1000));
			}
		}

		file.save();
	}
	

	@Test
	public final void testSimple() throws org.simpleods.SimpleOdsException {
		// Open the file.
		org.simpleods.OdsFile file = new org.simpleods.OdsFile(
				"s20columns.ods");
		file.addTable("test");
		org.simpleods.Table table = (org.simpleods.Table) file.getContent()
				.getTableQueue().get(0);

		final ObjectQueue rows = table.getRows();
		for (int y = 0; y < ROW_COUNT; y++) {
			final org.simpleods.TableRow row = new org.simpleods.TableRow();
			rows.add(row);
			for (int x = 0; x < COL_COUNT; x++) {
				row.setCell(x, String.valueOf(this.random.nextInt(1000)));
			}
		}

		file.save();
	}
	
//	@Test
	public final void testSimple2() throws org.simpleods.SimpleOdsException {
		// Open the file.
		org.simpleods.OdsFile file = new org.simpleods.OdsFile(
				"s60columns.ods");
		file.addTable("test");
		org.simpleods.Table table = (org.simpleods.Table) file.getContent()
				.getTableQueue().get(0);

		final ObjectQueue rows = table.getRows();
		for (int y = 0; y < 3*ROW_COUNT; y++) {
			final org.simpleods.TableRow row = new org.simpleods.TableRow();
			rows.add(row);
			for (int x = 0; x < 3*COL_COUNT; x++) {
				row.setCell(x, String.valueOf(this.random.nextInt(1000)));
			}
		}

		file.save();
	}
	
//	@Test
	public final void testJOpen() throws IOException {
		// the file.
		final Sheet sheet = SpreadSheet.createEmpty(new DefaultTableModel()).getSheet(0);
		sheet.ensureColumnCount(COL_COUNT);
		sheet.ensureRowCount(ROW_COUNT);
		
		for (int y = 0; y < ROW_COUNT; y++) {
			for (int x = 0; x < COL_COUNT; x++) {
				sheet.setValueAt(String.valueOf(this.random.nextInt(1000)), x, y);
			}
		}
		File outputFile = new File("j20columns.ods");
		sheet.getSpreadSheet().saveAs(outputFile);
	}

}
