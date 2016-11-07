/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A Heavyweight java library to create simple OpenOffice spreadsheets
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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import org.junit.Test;

import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file BenchIT.java is part of FastODS.
 */
public class OdsFile2Test {

	@Test
	public final void test50() throws FastOdsException {
		System.out.println("Filling a 50 rows, 5 columns spreadsheet");
		long t1 = System.currentTimeMillis();
		final Random random = new Random();

		OdsFile file = OdsFile.create("5columns.ods");
		final Table table = file.addTable("test", 50, 5);
		XMLUtil xmlUtil = XMLUtil.create();
		
		HeavyTableRow row = table.getRow(0);
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

		final TableCellStyle tcs0 = TableCellStyle.builder("tcs0")
				.backgroundColor("#0000ff").build();
		final TableCellStyle tcs1 = TableCellStyle.builder("tcs1")
				.backgroundColor("#00FF00").build();
		final TableCellStyle tcs2 = TableCellStyle.builder("tcs2")
				.fontWeightBold().build();
		final TableCellStyle tcs3 = TableCellStyle.builder("tcs3")
				.fontStyleItalic().build();
		
		row = table.getRow(0);
		row.setStringValue(0, "éèà");
		row.setStringValue(1, "€€€€");
		row.setStringValue(2, "£");
		for (int y = 1; y < 50; y++) {
			row = table.getRow(y);
			TableCellWalker walker = row.getWalker();
			for (int x = 0; x < 5; x++) {
				walker.lastCell();
				walker.setFloatValue(random.nextInt(1000));
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
						walker.setBooleanValue(true); break;
					case 1:
						walker.setCurrencyValue(150.5, "EUR"); 
						walker.setTooltip("That's a tooltip !"); break;
					case 2:
						walker.setDateValue(Calendar.getInstance());
//						final DateStyle build0 = DateStyle.builder("trgfgbf").build();
//						build0.addToFile(file);
//						final TableCellStyle build1 = TableCellStyle.builder("ttete").dataStyle(build0).build();
//						build1.addToFile(file);
//						cell.setStyle(build1);
						 break;
					case 3:
						walker.setPercentageValue(70.3); break;
					case 4:
						walker.setStringValue("foobar"); break;
					default: break;
					}
				} else if (y == 9) {
					switch (x) {
					case 0:
						walker.setColumnsSpanned(2); break;
					case 2:
						walker.setCurrencyValue(-150.5, "€"); break;
					case 3:
						walker.setStyle(tcls); break;
					default:
						walker.setTimeValue(x*60*1000);
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
		final Table table = file.addTable("test");

		for (int y = 0; y < 100000; y++) {
			final HeavyTableRow row = table.nextRow();
			TableCellWalker walker = row.getWalker();
			for (int x = 0; x < 20; x++) {
				walker.lastCell();
				walker.setFloatValue(random.nextInt(1000));
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
		final Table table = file.addTable("test");

		for (int y = 0; y < 1000; y++) {
			final HeavyTableRow row = table.nextRow();
			TableCellWalker walker = row.getWalker();
			for (int x = 0; x < 300; x++) {
				walker.lastCell();
				walker.setFloatValue(random.nextInt(1000));
			}
		}

		file.save();

		long t2 = System.currentTimeMillis();
		System.out.println("Filled in " + (t2 - t1) + " ms");
	}
}
