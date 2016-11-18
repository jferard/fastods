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

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.github.jferard.fastods.FooterHeader.Region;
import com.github.jferard.fastods.style.FHTextStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;

/**
 * @author Julien Férard
 */
public class OdsFileWithHeaderAndFooterCreation {
	private Logger logger;

	@Before
	public void setUp() {
		this.logger = Logger.getLogger("OdsFileCreation");
	}

	@Test
	public final void test50() throws FastOdsException {
		this.logger.info("Creating a file with footer and header");
		FHTextStyle lts = FHTextStyle.builder("test1").fontColor(Color.RED)
				.build();
		FHTextStyle cts = FHTextStyle.builder("test2").fontColor(Color.BLUE)
				.build();
		FHTextStyle rts = FHTextStyle.builder("test3").fontColor(Color.GREEN)
				.build();
		final FHTextStyle pnts = FHTextStyle.builder("style").fontWeightBold()
				.build();
		FooterHeader header = FooterHeader
				.regionBuilder(FooterHeader.Type.HEADER).region(Region.LEFT)
				.styledText(lts, "left header").region(Region.CENTER)
				.styledText(cts, "center header").pageNumber(pnts)
				.region(Region.RIGHT).styledText(rts, "right header").build();

		FooterHeader footer = FooterHeader
				.regionBuilder(FooterHeader.Type.FOOTER).region(Region.LEFT)
				.styledText(cts, "left footer").region(Region.CENTER)
				.styledText(rts, "center footer").pageCount(pnts)
				.region(Region.RIGHT).styledText(lts, "right footer").build();

		PageStyle ps = PageStyle.builder("test").footer(footer).header(header)
				.build();

		OdsFile file = OdsFile.create("fastods_fh.ods");
		file.addPageStyle(ps);
		final Table table = file.addTable("test", 1, 5);
		HeavyTableRow row = table.getRow(0);
		TableRowStyle trs = TableRowStyle.builder("rr").rowHeight("5cm")
				.build();
		TableCellStyle tcls = TableCellStyle.builder("cc")
				.backgroundColor("#dddddd").fontWeightBold().build();
		row.setStyle(trs);
		row.setDefaultCellStyle(tcls);
		TableColumnStyle tcns = TableColumnStyle.builder("ccs")
				.columnWidth("10cm").defaultCellStyle(tcls).build();
		table.setColumnStyle(0, tcns);

		row = table.getRow(0);
		row.setStringValue(0, "text1");
		row.setStringValue(1, "text2");
		row.setStringValue(2, "text3");

		// let's display logging infos
		final Logger rootLogger = Logger.getLogger("");
		rootLogger.setLevel(Level.FINEST);
		for (Handler h : rootLogger.getHandlers())
			h.setLevel(Level.FINEST);
		file.save();
	}
}
