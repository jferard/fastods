package com.github.jferard.fastods;

import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.simpleods.ObjectQueue;

import com.google.common.base.Optional;

public class BenchTest {
	private static final int COL_COUNT = 40;
	private static final int ROW_COUNT = 10000;
	private Random random;

	@Before
	public final void setUp() {
		this.random = new Random();
	}
	
	@Test
	public final void testFast() throws FastOdsException {
		System.out.println("Fast : filling a "+ROW_COUNT+" rows, "+COL_COUNT+" columns spreadsheet");
		long t1 = System.currentTimeMillis();

		// Load the file.
		OdsFile file = new OdsFile("20columns.ods");
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

		long t2 = System.currentTimeMillis();
		System.out.println("Filled in " + (t2 - t1) + " ms");
	}
	
	@Test
	public final void testSimple() throws org.simpleods.SimpleOdsException {
		System.out.println("Simple : filling a "+ROW_COUNT+" rows, "+COL_COUNT+" columns spreadsheet");
		long t1 = System.currentTimeMillis();

		// Load the file.
		org.simpleods.OdsFile file = new org.simpleods.OdsFile("20columns.ods");
		file.addTable("test");
		org.simpleods.Table table = (org.simpleods.Table) file.getContent().getTableQueue().get(0);
		
		final ObjectQueue rows = table.getRows();
		for (int y = 0; y < ROW_COUNT; y++) {
			final org.simpleods.TableRow row = new org.simpleods.TableRow();
			rows.add(row);
			for (int x = 0; x < COL_COUNT; x++) {
				row.setCell(x, String.valueOf(this.random.nextInt(1000)));
			}
		}

		file.save();

		long t2 = System.currentTimeMillis();
		System.out.println("Filled in " + (t2 - t1) + " ms");
	}
}
