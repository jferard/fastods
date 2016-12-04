/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.fastods.style;

import com.github.jferard.fastods.FooterHeader;
import com.github.jferard.fastods.style.MasterPageStyle.PaperFormat;
import com.github.jferard.fastods.style.MasterPageStyle.PrintOrientation;
import com.github.jferard.fastods.style.MasterPageStyle.WritingMode;

/**
 * @author Julien Férard
 */
public class MasterPageStyleBuilder {
	private String backgroundColor;
	private FooterHeader footer;
	private FooterHeader header;
	private final MarginsBuilder marginsBuilder;
	private final String name;

	private final String numFormat;
	private String pageHeight;
	private String pageWidth;
	private PaperFormat paperFormat;

	private PrintOrientation printOrientation;
	private WritingMode writingMode;

	/**
	 * Create a new page style.
	 *
	 */
	public MasterPageStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalStateException();

		this.name = name;
		this.marginsBuilder = new MarginsBuilder();
		this.marginsBuilder.all("1.5cm");

		this.paperFormat(MasterPageStyle.DEFAULT_FORMAT);
		this.numFormat = "1";
		this.backgroundColor = "";

		this.printOrientation = MasterPageStyle.DEFAULT_PRINTORIENTATION;
		this.writingMode = MasterPageStyle.DEFAULT_WRITING_MODE;

		final TextStyle noneStyle = TextProperties.builder().buildStyle("none");
		this.header = FooterHeader.simpleHeader("", noneStyle);
		this.footer = FooterHeader.simpleFooter("", noneStyle);
	}

	/**
	 * Set the margin at the top,bottom,left and right. margin is a length value
	 * expressed as a number followed by a unit of measurement e.g. 1.5cm or
	 * 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 *
	 * @param margin
	 * @return this for fluent style
	 */
	public MasterPageStyleBuilder allMargins(final String margin) {
		this.marginsBuilder.all(margin);
		return this;
	}

	/**
	 * Set the background color to color, a six-digit hex value. Example:
	 * #aa32f0.<br>
	 * The background color may also be set to 'transparent' if a background
	 * image is used (currently unsupported).
	 *
	 * @param color
	 * @return this for fluent style
	 */
	public MasterPageStyleBuilder backgroundColor(final String color) {
		this.backgroundColor = color;
		return this;
	}

	public MasterPageStyle build() {
		// TODO : create MarginAttribute and use a
		// EnumMap<MarginAtribute.Position, MarginAttribute>
		return new MasterPageStyle(this.name, this.marginsBuilder.build(),
				this.pageWidth, this.pageHeight, this.numFormat,
				this.backgroundColor, this.footer, this.header,
				this.printOrientation, this.paperFormat, this.writingMode);
	}

	public MasterPageStyleBuilder footer(final FooterHeader footer) {
		this.footer = footer;
		return this;
	}

	public MasterPageStyleBuilder header(final FooterHeader header) {
		this.header = header;
		return this;
	}

	/**
	 * Set the margin at the bottom. margin is a length value expressed as a
	 * number followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).<br>
	 *
	 * @param margin
	 * @return this for fluent style
	 */
	public MasterPageStyleBuilder marginBottom(final String margin) {
		this.marginsBuilder.bottom(margin);
		return this;
	}

	/**
	 * Set the margin at the left. margin is a length value expressed as a
	 * number followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).<br>
	 *
	 * @param margin
	 * @return this for fluent style
	 */
	public MasterPageStyleBuilder marginLeft(final String margin) {
		this.marginsBuilder.left(margin);
		return this;
	}

	/**
	 * Set the margin at the right. margin is a length value expressed as a
	 * number followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 *
	 * @param margin
	 * @return this for fluent style
	 */
	public MasterPageStyleBuilder marginRight(final String margin) {
		this.marginsBuilder.right(margin);
		return this;
	}

	/**
	 * Set the margin at the top. margin is a length value expressed as a number
	 * followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 *
	 * @param margin
	 * @return this for fluent style
	 */
	public MasterPageStyleBuilder marginTop(final String margin) {
		this.marginsBuilder.top(margin);
		return this;
	}

	/**
	 * Set the page height. pageHeight is a length value expressed as a number
	 * followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).<br>
	 * When using this method, the paper format is set to
	 * PageStyle.STYLE_PAPERFORMAT_USER
	 *
	 * @param pageHeight
	 * @return this for fluent style
	 */
	public MasterPageStyleBuilder pageHeight(final String pageHeight) {
		this.paperFormat = MasterPageStyle.PaperFormat.USER;
		this.pageHeight = pageHeight;
		return this;
	}

	/**
	 * Set the page width. pageWidth is a length value expressed as a number
	 * followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).<br>
	 * When using this method, the paper format is set to
	 * PageStyle.STYLE_PAPERFORMAT_USER
	 *
	 * @param pageWidth
	 * @return this for fluent style
	 */
	public MasterPageStyleBuilder pageWidth(final String pageWidth) {
		this.paperFormat = MasterPageStyle.PaperFormat.USER;
		this.pageWidth = pageWidth;
		return this;
	}

	/**
	 * Set the paper format to one of<br>
	 * PageStyle.STYLE_PAPERFORMAT_A3<br>
	 * PageStyle.STYLE_PAPERFORMAT_A4<br>
	 * PageStyle.STYLE_PAPERFORMAT_A5<br>
	 * PageStyle.STYLE_PAPERFORMAT_LETTER<br>
	 * PageStyle.STYLE_PAPERFORMAT_LEGAL<br>
	 * PageStyle.STYLE_PAPERFORMAT_USER , automatically used if you use
	 * setPageHeight() or setPageWidth().
	 *
	 * @param paperFormat
	 * @return this for fluent style
	 */
	public final MasterPageStyleBuilder paperFormat(
			final PaperFormat paperFormat) {
		this.paperFormat = paperFormat;
		this.pageWidth = paperFormat.getWidth();
		this.pageHeight = paperFormat.getHeight();
		return this;
	}

	public MasterPageStyleBuilder printOrientationHorizontal() {
		this.printOrientation = MasterPageStyle.PrintOrientation.HORIZONTAL;
		return this;
	}

	public MasterPageStyleBuilder printOrientationVertical() {
		this.printOrientation = MasterPageStyle.PrintOrientation.VERTICAL;
		return this;
	}

	/**
	 * Set the writing mode to one of<br>
	 * STYLE_WRITINGMODE_LRTB lr-tb (left to right; top to bottom)<br>
	 * STYLE_WRITINGMODE_RLTB<br>
	 * STYLE_WRITINGMODE_TBRL<br>
	 * STYLE_WRITINGMODE_TBLR<br>
	 * STYLE_WRITINGMODE_LR<br>
	 * STYLE_WRITINGMODE_RL<br>
	 * STYLE_WRITINGMODE_TB<br>
	 * STYLE_WRITINGMODE_PAGE<br>
	 *
	 * @param writingMode
	 * @return
	 */
	public MasterPageStyleBuilder writingMode(final WritingMode writingMode) {
		if (writingMode == null)
			throw new IllegalArgumentException();

		this.writingMode = writingMode;
		return this;
	}
}
