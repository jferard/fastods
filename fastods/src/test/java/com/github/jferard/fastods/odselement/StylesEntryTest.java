/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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
 */
package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.datastyle.BooleanStyleBuilder;
import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.SimpleLength;
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Before;

import java.util.Locale;

public class StylesEntryTest {
	private DataStyle ds1;
	private DataStyle ds2;
	private Locale locale;
	private PageStyle ps1;
	private PageStyle ps2;
	private TableCellStyle st1;
	private TableCellStyle st2;
	private StylesContainer stylesContainer;
	private StylesElement stylesElement;
	private XMLUtil util;

	@Before
	public void setUp() {
		this.stylesContainer = new StylesContainer();
		this.stylesElement = new StylesElement(this.stylesContainer);
		this.util = XMLUtil.create();
		this.locale = Locale.US;

		this.st1 = TableCellStyle.builder("a").fontStyleItalic().build();
		this.st2 = TableCellStyle.builder("a").fontWeightBold().build();

		this.ds1 = new BooleanStyleBuilder("a", this.locale).country("a").build();
		this.ds2 = new BooleanStyleBuilder("a", this.locale).country("b").build();

		this.ps1 = PageStyle.builder("a").allMargins(SimpleLength.pt(1.0)).build();
		this.ps2 = PageStyle.builder("a").allMargins(SimpleLength.pt(2.0)).build();
	}
}
