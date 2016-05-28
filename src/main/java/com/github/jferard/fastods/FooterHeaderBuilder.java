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

import java.util.LinkedList;
import java.util.List;

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
	 * The OdsFile where this object belong to.
	 */
	protected final FooterHeader.Type footerHeaderType;
	protected String sMarginLeft;
	protected String sMarginRight;
	protected String sMarginTop;
	protected String sMinHeight;
	protected List<List<StyledText>> curRegion;

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

	public FooterHeaderBuilder pageCount(final TextStyle ts) {
		this.styledText(ts, "<text:page-count>99</text:page-count>");
		return this;
	}

	public FooterHeaderBuilder pageCount(final TextStyle ts,
			final int nParagraph) {
		this.styledText(ts, "<text:page-count>99</text:page-count>",
				nParagraph);
		return this;
	}

	public FooterHeaderBuilder pageNumber(final TextStyle ts) {
		this.styledText(ts, "<text:page-number>1</text:page-number>");
		return this;
	}

	public FooterHeaderBuilder pageNumber(final TextStyle ts, final int nParagraph) {
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
	public FooterHeaderBuilder styledText(final TextStyle ts, final String sText,
			final int nParagraph) {
		List<StyledText> qStyledText = FooterHeaderBuilder.checkParagraph(this.curRegion,
				nParagraph);
		final StyledText st = new StyledText(ts, sText);
		qStyledText.add(st);
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
	public FooterHeaderBuilder styledText(final TextStyle ts, final String sText) {
		List<StyledText> qStyledText = new LinkedList<StyledText>();
		final StyledText st = new StyledText(ts, sText);
		qStyledText.add(st);
		this.curRegion.add(qStyledText);
		return this;
	}

	/**
	 * Checks if nParagraph is present in qRegion and return it if yes, if it is
	 * not present, create a new List and add it to qRegion. Return the new
	 * List.
	 *
	 * @param qRegion
	 * @param nParagraph
	 * @return The List with StyledText elements.
	 */
	protected static List<StyledText> checkParagraph(
			final List<List<StyledText>> qRegion, final int nParagraph) {
		List<StyledText> qStyledText = qRegion.get(nParagraph);

		// Check if the paragraph already exists and add a List if not
		if (qStyledText == null) {
			qStyledText = new LinkedList<StyledText>();
			qRegion.set(nParagraph, qStyledText);
		}

		return qStyledText;
	}

	public abstract FooterHeader build();
}