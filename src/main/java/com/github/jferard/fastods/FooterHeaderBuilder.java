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
/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
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

import java.util.List;

import com.github.jferard.fastods.style.FHTextStyle;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 *
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file FooterHeaderBuilder.java is part of FastODS.
 *
 *         styles.xml/office:document-styles/office:master-styles/style:master-
 *         page/style:footer
 *         styles.xml/office:document-styles/office:master-styles/style:master-
 *         page/style:header
 */
abstract class FooterHeaderBuilder {
	/**
	 * Checks if nParagraph is present in qRegion and return it if yes, if it is
	 * not present, create a new List and add it to qRegion. Return the new
	 * List.
	 *
	 * @param curRegion
	 * @param nParagraph
	 * @return The List with FHText elements.
	 */
	protected static FHParagraph checkParagraph(
			final List<FHParagraph> curRegion, final int nParagraph) {
		FHParagraph qStyledText = curRegion.get(nParagraph);

		// Check if the paragraph already exists and add a List if not
		if (qStyledText == null) {
			qStyledText = new FHParagraph();
			curRegion.set(nParagraph, qStyledText);
		}

		return qStyledText;
	}

	protected List<FHParagraph> curRegion;
	/**
	 * The OdsFile where this object belong to.
	 */
	protected final FooterHeader.Type footerHeaderType;
	protected String sMarginLeft;
	protected String sMarginRight;
	protected String sMarginTop;

	protected String sMinHeight;

	/**
	 * Create a new footer object.
	 *
	 * @param odsFile
	 *            - The OdsFile to which this footer belongs to.
	 */
	FooterHeaderBuilder(final FooterHeader.Type footerHeaderType) {
		this.footerHeaderType = footerHeaderType;
		this.sMinHeight = "0cm";
		this.sMarginLeft = "0cm";
		this.sMarginRight = "0cm";
		this.sMarginTop = "0cm";
	}

	public abstract FooterHeader build();

	public FooterHeaderBuilder pageCount(final FHTextStyle ts) {
		this.styledText(ts, "<text:page-count>99</text:page-count>");
		return this;
	}

	public FooterHeaderBuilder pageCount(final FHTextStyle ts,
			final int nParagraph) {
		this.styledText(ts, "<text:page-count>99</text:page-count>",
				nParagraph);
		return this;
	}

	public FooterHeaderBuilder pageNumber(final FHTextStyle ts) {
		this.styledText(ts, "<text:page-number>1</text:page-number>");
		return this;
	}

	public FooterHeaderBuilder pageNumber(final FHTextStyle ts,
			final int nParagraph) {
		this.styledText(ts, "<text:page-number>1</text:page-number>",
				nParagraph);
		return this;
	}

	/**
	 * Adds a TextStyle and text to the footer/header region specified by
	 * nRegion.<br>
	 * The paragraph to be used is nParagraph.<br>
	 * The text will be shown in the order it was added with this function.
	 *
	 * @param ts
	 *            The text style to be used
	 * @param sText
	 *            The string with the text
	 * @param region
	 *            One of : FooterHeader.FLG_REGION_LEFT,
	 *            FooterHeader.FLG_REGION_CENTER or
	 *            FooterHeader.FLG_REGION_RIGHT
	 * @param nParagraph
	 *            The paragraph number to be used
	 * @return
	 */
	public FooterHeaderBuilder styledText(final FHTextStyle ts,
			final String sText) {
		final FHParagraph qStyledText = new FHParagraph();
		final FHText st = new FHText(sText, ts);
		qStyledText.add(st);
		this.curRegion.add(qStyledText);
		return this;
	}

	/**
	 * Adds a TextStyle and text to the footer/header region specified by
	 * nRegion.<br>
	 * The paragraph to be used is nParagraph.<br>
	 * The text will be shown in the order it was added with this function.
	 *
	 * @param ts
	 *            The text style to be used
	 * @param sText
	 *            The string with the text
	 * @param region
	 *            One of : FooterHeader.FLG_REGION_LEFT,
	 *            FooterHeader.FLG_REGION_CENTER or
	 *            FooterHeader.FLG_REGION_RIGHT
	 * @param nParagraph
	 *            The paragraph number to be used
	 * @return
	 */
	public FooterHeaderBuilder styledText(final FHTextStyle ts,
			final String sText, final int nParagraph) {
		final FHParagraph qStyledText = FooterHeaderBuilder
				.checkParagraph(this.curRegion, nParagraph);
		final FHText st = new FHText(sText, ts);
		qStyledText.add(st);
		return this;
	}
}
