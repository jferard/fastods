/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.Footer;
import com.github.jferard.fastods.Header;
import com.github.jferard.fastods.PageSection;
import com.github.jferard.fastods.style.PageStyle.PrintOrientation;
import com.github.jferard.fastods.style.PageStyle.WritingMode;
import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.SimpleLength;

/**
 * @author Julien Férard
 */
public class PageStyleBuilder {
	private String backgroundColor;
	private Footer footer;
	private Header header;
	private final MarginsBuilder marginsBuilder;
	private final String name;

	private final String numFormat;
	private Length pageHeight;
	private Length pageWidth;
	private PaperFormat paperFormat;

	private PrintOrientation printOrientation;
	private WritingMode writingMode;
	private MasterPageStyle masterPageStyle;
	private PageLayoutStyle pageLayoutStyle;

	/**
	 * Create a new page style.
	 *
	 */
	PageStyleBuilder(final String name) {
		if (name == null)
			throw new IllegalStateException();

		this.name = name;
		this.marginsBuilder = new MarginsBuilder();
		this.marginsBuilder.all(SimpleLength.cm(1.5));

		this.paperFormat = PageStyle.DEFAULT_FORMAT;
		this.pageWidth = PageStyle.DEFAULT_FORMAT.getWidth();
		this.pageHeight = PageStyle.DEFAULT_FORMAT.getHeight();
		this.numFormat = "1";
		this.backgroundColor = "";

		this.printOrientation = PageStyle.DEFAULT_PRINT_ORIENTATION;
		this.writingMode = PageStyle.DEFAULT_WRITING_MODE;

		final TextStyle noneStyle = TextProperties.builder().buildStyle("none");
		this.header = PageSection.simpleHeader("", noneStyle);
		this.footer = PageSection.simpleFooter("", noneStyle);
	}

	/**
	 * Set the margin at the top, bottom, left and right. margin is a length value
	 *
	 * @param margin the length of the margin
	 * @return this for fluent style
	 */
	public PageStyleBuilder allMargins(final Length margin) {
		this.marginsBuilder.all(margin);
		return this;
	}

	/**
	 * Set the background color to color, a six-digit hex value. Example:
	 * #aa32f0.<br>
	 * The background color may also be set to 'transparent' if a background
	 * image is used (currently unsupported).
	 *
	 * @param color the color as an hexadecimal value
	 * @return this for fluent style
	 */
	public PageStyleBuilder backgroundColor(final String color) {
		this.backgroundColor = color;
		return this;
	}

	public PageStyle build() {
		if (this.masterPageStyle == null) {
			this.masterPageStyle = new MasterPageStyle(this.name, this.name, this.header, this.footer);
		}

		if (this.pageLayoutStyle == null) {
			this.pageLayoutStyle = new PageLayoutStyle(this.name, this.marginsBuilder.build(), this
					.pageWidth,
					this.pageHeight, this.numFormat, this.backgroundColor, this.header, this.footer, this
					.printOrientation, this.paperFormat, this.writingMode);
		}
		return new PageStyle(this.masterPageStyle, this.pageLayoutStyle);
	}

	public PageStyleBuilder masterPageStyle(final MasterPageStyle masterPageStyle) {
		this.masterPageStyle = masterPageStyle;
		return this;
	}

	public PageStyleBuilder pageLayoutStyle(final PageLayoutStyle pageLayoutStyle) {
		this.pageLayoutStyle = pageLayoutStyle;
		return this;
	}

	public PageStyleBuilder footer(final Footer footer) {
		this.footer = footer;
		return this;
	}

	public PageStyleBuilder header(final Header header) {
		this.header = header;
		return this;
	}

	/**
	 * Set the bottom margin. margin is a length value
	 *
	 * @param margin the length of the margin
	 * @return this for fluent style
	 */
	public PageStyleBuilder marginBottom(final Length margin) {
		this.marginsBuilder.bottom(margin);
		return this;
	}

	/**
	 * Set the left margin. margin is a length value
	 *
	 * @param margin the length of the margin
	 * @return this for fluent style
	 */
	public PageStyleBuilder marginLeft(final Length margin) {
		this.marginsBuilder.left(margin);
		return this;
	}

	/**
	 * Set the right margin. margin is a length value
	 *
	 * @param margin the length of the margin
	 * @return this for fluent style
	 */
	public PageStyleBuilder marginRight(final Length margin) {
		this.marginsBuilder.right(margin);
		return this;
	}

	/**
	 * Set the top margin. margin is a length value
	 *
	 * @param margin the length of the margin
	 * @return this for fluent style
	 */
	public PageStyleBuilder marginTop(final Length margin) {
		this.marginsBuilder.top(margin);
		return this;
	}

	/**
	 * Set the page height. pageHeight is a length value.
	 * Using this method sets the paper format to PageStyle.STYLE_PAPERFORMAT_USER
	 *
	 * @param pageHeight the height of the page
	 * @return this for fluent style
	 */
	public PageStyleBuilder pageHeight(final Length pageHeight) {
		this.paperFormat = PaperFormat.USER;
		this.pageHeight = pageHeight;
		return this;
	}

	/**
	 * Set the page width. pageWidth is a length value.
	 * Using this method sets the paper format to PageStyle.STYLE_PAPERFORMAT_USER
	 *
	 * @param pageWidth the page width
	 * @return this for fluent style
	 */
	public PageStyleBuilder pageWidth(final Length pageWidth) {
		this.paperFormat = PaperFormat.USER;
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
	 * @param paperFormat the format of the page
	 * @return this for fluent style
	 */
	public final PageStyleBuilder paperFormat(
			final PaperFormat paperFormat) {
		this.paperFormat = paperFormat;
		this.pageWidth = paperFormat.getWidth();
		this.pageHeight = paperFormat.getHeight();
		return this;
	}

	public PageStyleBuilder printOrientationHorizontal() {
		this.printOrientation = PageStyle.PrintOrientation.HORIZONTAL;
		return this;
	}

	public PageStyleBuilder printOrientationVertical() {
		this.printOrientation = PageStyle.PrintOrientation.VERTICAL;
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
	 * @param writingMode the writing mode
	 * @return this for fluent style
	 */
	public PageStyleBuilder writingMode(final WritingMode writingMode) {
		if (writingMode == null)
			throw new IllegalArgumentException();

		this.writingMode = writingMode;
		return this;
	}
}
