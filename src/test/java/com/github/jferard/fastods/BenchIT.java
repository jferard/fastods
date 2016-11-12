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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.simpleods.ObjectQueue;
import org.simpleods.SimpleOdsException;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 *
 * mvn -Dtest=BenchIT#test test
 */
public class BenchIT {
	private static final int TIMES = 5;
	private static final int COL_COUNT = 20;
	private static final int ROW_COUNT = 5000;
	private Random random;

	@Rule public TestName name = new TestName();
	
	@Before
	public final void setUp() {
		this.random = new Random();
	}

	@Test
	public void test1() throws SimpleOdsException, IOException {
		for (int i = 0 ; i<TIMES ; i++) {
			testFast(BenchIT.ROW_COUNT, BenchIT.COL_COUNT);
			testSimple(BenchIT.ROW_COUNT, BenchIT.COL_COUNT);
			testJOpen(BenchIT.ROW_COUNT, BenchIT.COL_COUNT);
		}
	}
	
	@Test
	public void test2() throws SimpleOdsException, IOException {
		for (int i = 0 ; i<TIMES ; i++) {
			testFast(2*BenchIT.ROW_COUNT, 2*BenchIT.COL_COUNT);
			testSimple(2*BenchIT.ROW_COUNT, 2*BenchIT.COL_COUNT);
			testJOpen(2*BenchIT.ROW_COUNT, 2*BenchIT.COL_COUNT);
		}
	}
	
	@Test
	public void test3() throws SimpleOdsException, IOException {
		for (int i = 0 ; i<TIMES ; i++) {
			testFast(3*BenchIT.ROW_COUNT, 3*BenchIT.COL_COUNT);
			testSimple(3*BenchIT.ROW_COUNT, 3*BenchIT.COL_COUNT);
			testJOpen(3*BenchIT.ROW_COUNT, 3*BenchIT.COL_COUNT);
		}
	}
	
	public final void testFast(final int rowCount, final int colCount) {
		// Open the file.
		System.out.println("testFast: filling a " + rowCount + " rows, "
				+ colCount + " columns spreadsheet");
		long t1 = System.currentTimeMillis();
		OdsFile file = OdsFile.create("f20columns.ods");
		final Table table = file.addTable("test", rowCount, colCount);

		for (int y = 0; y < rowCount; y++) {
			final HeavyTableRow row = table.nextRow();
			TableCellWalker walker = row.getWalker();
			for (int x = 0; x < colCount; x++) {
				walker.lastCell();
				walker.setFloatValue(this.random.nextInt(1000));
			}
		}

		file.save();
		long t2 = System.currentTimeMillis();
		System.out.println("Filled in " + (t2 - t1) + " ms");
	}
	
	public final void testSimple(int rowCount, int colCount) throws SimpleOdsException {
		// Open the file.
		System.out.println("testSimple: filling a " + rowCount + " rows, "
				+ colCount + " columns spreadsheet");
		long t1 = System.currentTimeMillis();
		org.simpleods.OdsFile file = new org.simpleods.OdsFile(
				"s20columns.ods");
		file.addTable("test");
		org.simpleods.Table table = (org.simpleods.Table) file.getContent()
				.getTableQueue().get(0);

		final ObjectQueue rows = table.getRows();
		for (int y = 0; y < rowCount; y++) {
			final org.simpleods.TableRow row = new org.simpleods.TableRow();
			rows.add(row);
			for (int x = 0; x < colCount; x++) {
				row.setCell(x, String.valueOf(this.random.nextInt(1000)));
			}
		}

		file.save();
		long t2 = System.currentTimeMillis();
		System.out.println("Filled in " + (t2 - t1) + " ms");
	}

	public final void testJOpen(int rowCount, int colCount) throws IOException {
		// the file.
		System.out.println("testJOpen: filling a " + rowCount + " rows, "
				+ colCount + " columns spreadsheet");
		long t1 = System.currentTimeMillis();
		final Sheet sheet = SpreadSheet.createEmpty(new DefaultTableModel()).getSheet(0);
		sheet.ensureColumnCount(colCount);
		sheet.ensureRowCount(rowCount);
		
		for (int y = 0; y < rowCount; y++) {
			for (int x = 0; x < colCount; x++) {
				sheet.setValueAt(String.valueOf(this.random.nextInt(1000)), x, y);
			}
		}
		File outputFile = new File("j20columns.ods");
		sheet.getSpreadSheet().saveAs(outputFile);
		long t2 = System.currentTimeMillis();
		System.out.println("Filled in " + (t2 - t1) + " ms");
	}

}
