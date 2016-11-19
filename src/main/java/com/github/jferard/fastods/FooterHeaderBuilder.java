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
package com.github.jferard.fastods;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.jferard.fastods.style.FHTextStyle;
import com.github.jferard.fastods.style.MarginsBuilder;

/**
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:footer
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:header
 *
 * @author Julien Férard
 */
abstract class FooterHeaderBuilder<F extends FooterHeaderBuilder<F>> {
	protected List<Paragraph> curRegion;
	/**
	 * The OdsFile where this object belong to.
	 */
	protected final FooterHeader.Type footerHeaderType;
	protected MarginsBuilder marginsBuilder;

	protected String minHeight;
	protected Set<FHTextStyle> textStyles;
	private Text text;

	/**
	 * Create a new footer object.
	 *
	 * @param odsFile
	 *            - The OdsFile to which this footer belongs to.
	 */
	FooterHeaderBuilder(final FooterHeader.Type footerHeaderType) {
		this.textStyles = new HashSet<FHTextStyle>();
		this.footerHeaderType = footerHeaderType;
		this.minHeight = "0cm";
		this.marginsBuilder = new MarginsBuilder();
		this.marginsBuilder.all("0cm");
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
	public F allMargins(final String margin) {
		this.marginsBuilder.all(margin);
		return (F) this;
	}

	public abstract FooterHeader build();

	/**
	 * Set the margin at the bottom. margin is a length value expressed as a
	 * number followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).<br>
	 *
	 * @param margin
	 * @return this for fluent style
	 */
	public F marginBottom(final String margin) {
		this.marginsBuilder.bottom(margin);
		return (F) this;
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
	public F marginLeft(final String margin) {
		this.marginsBuilder.left(margin);
		return (F) this;
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
	public F marginRight(final String margin) {
		this.marginsBuilder.right(margin);
		return (F) this;
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
	public F marginTop(final String margin) {
		this.marginsBuilder.top(margin);
		return (F) this;
	}

	/**
	 * Set the minimum height. min height is a length value expressed as a
	 * number followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 *
	 * @param margin
	 * @return this for fluent style
	 */
	public F minHeight(final String height) {
		this.minHeight = height;
 		return (F) this;
	}

	@Deprecated
	public F pageCount(final FHTextStyle ts) {
		this.textStyles.add(ts);
		this.styledText(ts, "<text:page-count>99</text:page-count>");
		return (F) this;
	}

	@Deprecated
	public F pageCount(final FHTextStyle ts, final int paragraph) {
		return this.pageCount(ts); // ignore paragraph
	}

	@Deprecated
	public F pageNumber(final FHTextStyle ts) {
		this.textStyles.add(ts);
		this.styledText(ts, "<text:page-number>1</text:page-number>");
		return (F) this;
	}

	@Deprecated
	public F pageNumber(final FHTextStyle ts, final int paragraph) {
		return this.pageNumber(ts); // ignore paragraph
	}

	/**
	 * Adds a TextStyle and text to the footer/header region specified by
	 * region.<br>
	 * The paragraph to be used is paragraph.<br>
	 * The text will be shown in the order it was added with this function.
	 *
	 * @param ts
	 *            The text style to be used
	 * @param text
	 *            The string with the text
	 * @param region
	 *            One of : FooterHeader.FLG_REGION_LEFT,
	 *            FooterHeader.FLG_REGION_CENTER or
	 *            FooterHeader.FLG_REGION_RIGHT
	 * @param paragraph
	 *            The paragraph number to be used
	 * @return
	 */
	@Deprecated
	public F styledText(final FHTextStyle ts, final String text) {
		this.textStyles.add(ts);
		final Paragraph styledText = new Paragraph();
		final Span st = new Span(text, ts);
		styledText.add(st);
		this.curRegion.add(styledText);
		return (F) this;
	}

	/**
	 * Adds a TextStyle and text to the footer/header region specified by
	 * region.<br>
	 * The paragraph to be used is paragraph.<br>
	 * The text will be shown in the order it was added with this function.
	 *
	 * @param ts
	 *            The text style to be used
	 * @param text
	 *            The string with the text
	 * @param region
	 *            One of : FooterHeader.FLG_REGION_LEFT,
	 *            FooterHeader.FLG_REGION_CENTER or
	 *            FooterHeader.FLG_REGION_RIGHT
	 * @param paragraphIndex
	 *            The paragraph number to be used
	 * @return
	 */
	@Deprecated
	public F styledText(final FHTextStyle ts, final String text,
			final int paragraphIndex) {
		return this.styledText(ts, text); // ignore paragraph
	}
	
	public F text(final Text text) {
		this.text = text;
		return (F) this;
	}
}
