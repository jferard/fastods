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
				row.setCell(x, String.valueOf(this.random.nextInt(1000)));
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

	@Test
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
