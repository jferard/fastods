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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file FooterHeader.java is part of FastODS.
 *
 *         styles.xml/office:document-styles/office:master-styles/style:master-
 *         page/style:footer
 *         styles.xml/office:document-styles/office:master-styles/style:master-
 *         page/style:header
 */
public class FooterHeader {
	public static enum Region {
		LEFT, CENTER, RIGHT;
	}

	/**
	 * Footer or Header ?
	 */
	public static enum Type {
		FOOTER, HEADER;
	}

	private final List<List<StyledText>> qLeftRegion;
	private final List<List<StyledText>> qCenterRegion;
	private final List<List<StyledText>> qRightRegion;
	private String sMinHeight;
	private String sMarginLeft;
	private String sMarginRight;
	private String sMarginTop;
	/**
	 * The OdsFile where this object belong to.
	 */
	private Type footerHeaderType;

	/**
	 * Create a new footer object.
	 * 
	 * @param odsFile
	 *            - The OdsFile to which this footer belongs to.
	 */
	public FooterHeader(Type footerHeaderType) {
		this.footerHeaderType = footerHeaderType;
		this.qLeftRegion = new LinkedList<List<StyledText>>();
		this.qCenterRegion = new LinkedList<List<StyledText>>();
		this.qRightRegion = new LinkedList<List<StyledText>>();
		this.sMinHeight = "0cm";
		this.sMarginLeft = "0cm";
		this.sMarginRight = "0cm";
		this.sMarginTop = "0cm";
		this.qCenterRegion.add(new LinkedList<StyledText>()); // Create
																// the
		// first
		// paragraph
	}

	public void addToFile(OdsFile odsFile) {
		if (this.footerHeaderType == Type.FOOTER)
			odsFile.getStyles().setFooter(this); // Add this FooterHeader object
		else if (this.footerHeaderType == Type.HEADER)
			odsFile.getStyles().setHeader(this); // Add this FooterHeader object
	}

	public void addPageCount(final TextStyle ts, final Region region,
			final int nParagraph) {
		this.addStyledText(ts, "<text:page-count>99</text:page-count>", region,
				nParagraph);
	}

	public void addPageNumber(final TextStyle ts, final Region region,
			final int nParagraph) {
		this.addStyledText(ts, "<text:page-number>1</text:page-number>", region,
				nParagraph);
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
	 */
	public void addStyledText(final TextStyle ts, final String sText,
			final Region region, final int nParagraph) {
		List<StyledText> qStyledText = null;

		switch (region) {
		case LEFT: // Use left region
			qStyledText = this.checkParagraph(this.qLeftRegion, nParagraph);
			break;
		case CENTER: // Use center region
			qStyledText = this.checkParagraph(this.qCenterRegion, nParagraph);
			break;
		case RIGHT: // Use right region
			qStyledText = this.checkParagraph(this.qRightRegion, nParagraph);
			break;
		default: // Invalid nFooterRegionValue, use center region as default
			throw new IllegalStateException();
		}

		StyledText st = new StyledText(ts, sText);
		qStyledText.add(st);

	}

	/**
	 * @return The current left margin of the footer/header.
	 */
	public String getMarginLeft() {
		return this.sMarginLeft;
	}

	/**
	 * @return The current right margin of the footer/header.
	 */
	public String getMarginRight() {
		return this.sMarginRight;
	}

	/**
	 * @return The current top margin of the footer/header.
	 */
	public String getMarginTop() {
		return this.sMarginTop;
	}

	/**
	 * @return The current minimum height of the footer/header.
	 */
	public String getMinHeight() {
		return this.sMinHeight;
	}

	public void setMarginLeft(String sMarginLeft) {
		this.sMarginLeft = sMarginLeft;
	}

	public void setMarginRight(String sMarginRight) {
		this.sMarginRight = sMarginRight;
	}

	public void setMarginTop(String sMarginTop) {
		this.sMarginTop = sMarginTop;
	}

	public void setMinHeight(String sMinHeight) {
		this.sMinHeight = sMinHeight;
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
	private List<StyledText> checkParagraph(
			final List<List<StyledText>> qRegion, final int nParagraph) {
		List<StyledText> qStyledText = qRegion.get(nParagraph);
		// Check if the paragraph already exists and add a List if not
		if (qStyledText == null) {
			qRegion.set(nParagraph, new LinkedList<StyledText>()); // Create
																	// this
			// paragraph
			qStyledText = qRegion.get(nParagraph);
		}

		return qStyledText;
	}

	private void writeRegion(Util util, Appendable appendable,
			List<List<StyledText>> qRegion, final String sRegionName)
			throws IOException {

		if (qRegion.size() == 0) {
			return;
		}

		appendable.append("<style:")
				.append(util.escapeXMLAttribute(sRegionName)).append(">");

		for (List<StyledText> qStyledText : qRegion) {
			// <style:footer/header> is written by PageStyle.toMasterStyleXML()
			appendable.append("<text:p>");

			// Check if a qStyles object is null and add an empty paragraph for
			// this
			if (qStyledText == null) {
				appendable.append("<text:span />");
			} else {
				// Add all styles and text for this paragraphs
				for (StyledText st : qStyledText)
					st.appendMasterStyleXML(util, appendable);
			}

			appendable.append("</text:p>");
		}

		appendable.append("</style:").append(sRegionName).append(">");

		return;
	}

	/**
	 * Used in file styles.xml, in <office:master-styles>,<style:master-page />.
	 * 
	 * @throws IOException
	 */
	public void appendMasterStyleXML(Util util, Appendable appendable)
			throws IOException {
		this.writeRegion(util, appendable, this.qLeftRegion, "region-left");
		this.writeRegion(util, appendable, this.qCenterRegion, "region-center");
		this.writeRegion(util, appendable, this.qRightRegion, "region-right");
	}

}
