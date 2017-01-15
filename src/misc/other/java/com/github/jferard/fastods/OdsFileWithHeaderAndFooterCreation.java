/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
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
 * ****************************************************************************/
package com.github.jferard.fastods;

import com.github.jferard.fastods.FooterHeader.Region;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableColumnStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.style.TableStyle;
import com.github.jferard.fastods.style.TextProperties;
import com.github.jferard.fastods.style.TextStyle;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Julien Férard
 */
public class OdsFileWithHeaderAndFooterCreation {
	private Logger logger;

	@BeforeClass
	public static final void beforeClass() {
		File generated_files = new File("generated_files");
		if (generated_files.exists())
			return;

		generated_files.mkdir();
	}

	@Before
	public void setUp() {
		this.logger = Logger.getLogger("OdsFileCreation");
	}

	@Test
	public final void test50() throws FastOdsException, IOException {
		this.logger.info("Creating a file with footer and header");
		final TextStyle lts = TextProperties.builder().fontColor(Color.RED)
				.buildStyle("test1");
		final TextStyle cts = TextProperties.builder().fontColor(Color.BLUE)
				.buildStyle("test2");
		final TextStyle rts = TextProperties.builder().fontColor(Color.GREEN)
				.buildStyle("test3");
		final TextStyle boldStyle = TextProperties.builder().fontWeightBold()
				.buildStyle("style");
		final TextStyle italicStyle = TextProperties.builder().fontStyleItalic()
				.buildStyle("style2");

		final Text leftHeader = Text.styledContent("left header", lts);
		final Text centerHeader = Text.builder().par()
				.styledSpan("center header", cts).span(Text.TEXT_PAGE_NUMBER)
				.build();
		final Text rightHeader = Text.styledContent("right header", rts);
		final FooterHeader header = FooterHeader
				.regionBuilder(FooterHeader.Type.HEADER).region(Region.LEFT)
				.text(leftHeader).region(Region.CENTER).text(centerHeader)
				.region(Region.RIGHT).text(rightHeader).build();

		final Text leftFooter = Text.styledContent("left footer", cts);
		final Text centerFooter = Text.builder().par()
				.styledSpan("center footer", rts).span(Text.TEXT_PAGE_COUNT)
				.build();
		final Text rightFooter = Text.styledContent("right footer", lts);
		final FooterHeader footer = FooterHeader
				.regionBuilder(FooterHeader.Type.FOOTER).region(Region.LEFT)
				.text(leftFooter).region(Region.CENTER).text(centerFooter)
				.region(Region.RIGHT).text(rightFooter).build();

		final MasterPageStyle ps = MasterPageStyle.builder("test")
				.footer(footer).header(header).build();

		final OdsDocument file = new OdsFactory().createDocument();
		file.addPageStyle(ps);
		final Table table = file.addTable("test", 1, 5);
		final Table table2 = file.addTable("target", 1, 1);
		final TableStyle ttts = TableStyle.builder("a").masterPageStyle(ps)
				.build();
		table.setStyle(ttts);

		HeavyTableRow row = table.getRow(0);
		final TableRowStyle trs = TableRowStyle.builder("rr").rowHeight("5cm")
				.build();
		final TableCellStyle tcls = TableCellStyle.builder("cc")
				.backgroundColor("#dddddd").fontWeightBold().build();
		row.setStyle(trs);
		row.setDefaultCellStyle(tcls);
		final TableColumnStyle tcns = TableColumnStyle.builder("ccs")
				.columnWidth("10cm").defaultCellStyle(tcls).build();
		table.setColumnStyle(0, tcns);

		row = table.getRow(0);
		row.setText(0,
				Text.builder().parContent("This is a")
						.parStyledContent("multiline", italicStyle)
						.parStyledContent("cell", boldStyle).build());
		row.setStringValue(1, "text2");
		row.setStringValue(2, "text3");
		row = table.getRow(1);
		row.setText(0,
				Text.builder().par().span("before link to table: ").link("table", table2).span(" after link to table").build());
		row.setText(1,
				Text.builder().par().span("before link to url: ").link("url", new URL("https://www.github.com/jferard/fastods")).span(" after link to url").build());
		row.setText(2,
				Text.builder().par().span("before link to file: ").link("file", new File("generated_files", "fastods_50_5.ods")).span(" after link to file").build());

		// let's display logging infos
		final Logger rootLogger = Logger.getLogger("");
		rootLogger.setLevel(Level.FINEST);
		for (final Handler h : rootLogger.getHandlers())
			h.setLevel(Level.FINEST);
		file.saveAs(new File("generated_files", "fastods_fh.ods"));
	}
}
