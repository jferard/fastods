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

import java.util.List;

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
abstract class FooterHeaderBuilder {
	/**
	 * Checks if paragraph is present in region and return it if yes, if it is
	 * not present, create a new List and add it to region. Return the new List.
	 *
	 * @param curRegion
	 * @param paragraphIndex
	 * @return The List with FHText elements.
	 */
	protected static FHParagraph checkParagraph(
			final List<FHParagraph> curRegion, final int paragraphIndex) {
		FHParagraph styledText = curRegion.get(paragraphIndex);

		// Check if the paragraph already exists and add a List if not
		if (styledText == null) {
			styledText = new FHParagraph();
			curRegion.set(paragraphIndex, styledText);
		}

		return styledText;
	}

	protected List<FHParagraph> curRegion;
	/**
	 * The OdsFile where this object belong to.
	 */
	protected final FooterHeader.Type footerHeaderType;
	protected MarginsBuilder marginsBuilder;


	protected String minHeight;

	/**
	 * Create a new footer object.
	 *
	 * @param odsFile
	 *            - The OdsFile to which this footer belongs to.
	 */
	FooterHeaderBuilder(final FooterHeader.Type footerHeaderType) {
		this.footerHeaderType = footerHeaderType;
		this.minHeight = "0cm";
		this.marginsBuilder = new MarginsBuilder();
		this.marginsBuilder.all("0cm");
	}

	public abstract FooterHeader build();

	public FooterHeaderBuilder pageCount(final FHTextStyle ts) {
		this.styledText(ts, "<text:page-count>99</text:page-count>");
		return this;
	}

	public FooterHeaderBuilder pageCount(final FHTextStyle ts,
			final int paragraph) {
		this.styledText(ts, "<text:page-count>99</text:page-count>", paragraph);
		return this;
	}

	public FooterHeaderBuilder pageNumber(final FHTextStyle ts) {
		this.styledText(ts, "<text:page-number>1</text:page-number>");
		return this;
	}

	public FooterHeaderBuilder pageNumber(final FHTextStyle ts,
			final int paragraph) {
		this.styledText(ts, "<text:page-number>1</text:page-number>",
				paragraph);
		return this;
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
	public FooterHeaderBuilder styledText(final FHTextStyle ts,
			final String text) {
		final FHParagraph styledText = new FHParagraph();
		final FHText st = new FHText(text, ts);
		styledText.add(st);
		this.curRegion.add(styledText);
		return this;
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
	public FooterHeaderBuilder styledText(final FHTextStyle ts,
			final String text, final int paragraphIndex) {
		final FHParagraph styledText = FooterHeaderBuilder
				.checkParagraph(this.curRegion, paragraphIndex);
		final FHText st = new FHText(text, ts);
		styledText.add(st);
		return this;
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
	public FooterHeaderBuilder allMargins(final String margin) {
		this.marginsBuilder.all(margin);
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
	public FooterHeaderBuilder marginBottom(final String margin) {
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
	public FooterHeaderBuilder marginLeft(final String margin) {
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
	public FooterHeaderBuilder marginRight(final String margin) {
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
	public FooterHeaderBuilder marginTop(final String margin) {
		this.marginsBuilder.top(margin);
		return this;
	}
	
	/**
	 * Set the minimum height. min height is a length value expressed as a number
	 * followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 *
	 * @param margin
	 * @return this for fluent style
	 */
	public FooterHeaderBuilder minHeight(final String height) {
		this.minHeight = height;
		return this;
	}
}
