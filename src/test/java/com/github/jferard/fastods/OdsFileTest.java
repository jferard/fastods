package com.github.jferard.fastods;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Optional;

public class OdsFileTest {

	@Test
	public final void test() throws SimpleOdsException {
		System.out.println("Filling a 50 rows, 5 columns spreadsheet");
		long t1 = System.currentTimeMillis();
		final Random random = new Random();

		// Load the file.
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
					row.setCellStyle(x, TableCellStyle.builder().name("tcs1").backgroundColor("#00FF00").build());
					break;
				case 2:
					row.setCellStyle(x, TableCellStyle.builder().name("tcs2").fontWeightBold().build());
					break;
				case 3:
					row.setCellStyle(x, TableCellStyle.builder().name("tcs3").fontWeightItalic().build());
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
}
