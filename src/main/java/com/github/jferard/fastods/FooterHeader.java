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

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file FooterHeader.java is part of SimpleODS.
 *
 */
public class FooterHeader {

	/**
	 * The region on the left page side.
	 */
	public static final int FLG_REGION_LEFT = 0;
	/**
	 * The region in the middle of the page.
	 */
	public static final int FLG_REGION_CENTER = 1;
	/**
	 * The region on the right page side.
	 */
	public static final int FLG_REGION_RIGHT = 2;

	private ObjectQueue<ObjectQueue<StyledText>> qLeftRegion = ObjectQueue
			.newQueue();
	private ObjectQueue<ObjectQueue<StyledText>> qCenterRegion = ObjectQueue
			.newQueue();
	private ObjectQueue<ObjectQueue<StyledText>> qRightRegion = ObjectQueue
			.newQueue();

	private String sMinHeight = "0cm";
	private String sMarginLeft = "0cm";
	private String sMarginRight = "0cm";
	private String sMarginTop = "0cm";

	/**
	 * The OdsFile where this object belong to.
	 */
	private OdsFile o;

	/**
	 * Footer or Header ?
	 */
	public static final int FOOTER = 1;
	public static final int HEADER = 2;

	private int footerHeaderType;

	/**
	 * Create a new footer object.
	 * 
	 * @param odsFile
	 *            - The OdsFile to which this footer belongs to.
	 */
	public FooterHeader(final OdsFile odsFile, int footerHeaderType) {
		this.o = odsFile;
		this.footerHeaderType = footerHeaderType;
		if (this.footerHeaderType == FOOTER)
			this.o.getStyles().setFooter(this); // Add this FooterHeader object
		else if (this.footerHeaderType == HEADER)
			this.o.getStyles().setHeader(this); // Add this FooterHeader object
		else
			throw new IllegalArgumentException();
		this.qCenterRegion.add(ObjectQueue.<StyledText> newQueue()); // Create
																		// the
		// first
		// paragraph
	}

	/**
	 * @return The current minimum height of the footer/header.
	 */
	public String getMinHeight() {
		return this.sMinHeight;
	}

	public void setMinHeight(String sMinHeight) {
		this.sMinHeight = sMinHeight;
	}

	/**
	 * @return The current left margin of the footer/header.
	 */
	public String getMarginLeft() {
		return this.sMarginLeft;
	}

	public void setMarginLeft(String sMarginLeft) {
		this.sMarginLeft = sMarginLeft;
	}

	/**
	 * @return The current right margin of the footer/header.
	 */
	public String getMarginRight() {
		return this.sMarginRight;
	}

	public void setMarginRight(String sMarginRight) {
		this.sMarginRight = sMarginRight;
	}

	/**
	 * @return The current top margin of the footer/header.
	 */
	public String getMarginTop() {
		return this.sMarginTop;
	}

	public void setMarginTop(String sMarginTop) {
		this.sMarginTop = sMarginTop;
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 * @return The XML string for this object.
	 */
	protected String toXML() {
		StringBuilder sbTemp = new StringBuilder();

		if (this.footerHeaderType == FOOTER)
			sbTemp.append(
					"<style:style style:name=\"Footer\" style:family=\"paragraph\" style:parent-style-name=\"Standard\"  style:class=\"extra\">");
		else if (this.footerHeaderType == HEADER)
			sbTemp.append(
					"<style:style style:name=\"Header\" style:family=\"paragraph\" style:parent-style-name=\"Standard\"  style:class=\"extra\">");
		
		sbTemp.append(
				"<style:paragraph-properties  text:number-lines=\"false\" text:line-number=\"0\">");

		sbTemp.append("</style:paragraph-properties>");
		sbTemp.append("</style:style>");

		return sbTemp.toString();
	}

	/**
	 * Used in file styles.xml, in <office:master-styles>,<style:master-page />.
	 * 
	 * @return
	 */
	protected String toMasterStyleXML() {

		StringBuilder sbTemp = new StringBuilder();

		this.writeRegion(sbTemp, this.qLeftRegion, "region-left");

		this.writeRegion(sbTemp, this.qCenterRegion, "region-center");

		this.writeRegion(sbTemp, this.qRightRegion, "region-right");

		// </style:footer/header> is written by PageStyle.toMasterStyleXML()

		return sbTemp.toString();

	}

	private void writeRegion(StringBuilder sbTemp,
			ObjectQueue<ObjectQueue<StyledText>> qRegion,
			final String sRegionName) {

		if (qRegion.size() == 0) {
			return;
		}

		sbTemp.append("<style:").append(sRegionName).append(">");

		for (ObjectQueue<StyledText> qStyledText : qRegion) {
			// <style:footer/header> is written by PageStyle.toMasterStyleXML()
			sbTemp.append("<text:p>");

			// Check if a qStyles object is null and add an empty paragraph for
			// this
			if (qStyledText == null) {
				sbTemp.append("<text:span />");
			} else {
				// Add all styles and text for this paragraphs
				for (StyledText st : qStyledText)
					sbTemp.append(st.toMasterStyleXML());
			}

			sbTemp.append("</text:p>");
		}

		sbTemp.append("</style:").append(sRegionName).append(">");

		return;
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
	 * @param nRegion
	 *            One of : FooterHeader.FLG_REGION_LEFT,
	 *            FooterHeader.FLG_REGION_CENTER or
	 *            FooterHeader.FLG_REGION_RIGHT
	 * @param nParagraph
	 *            The paragraph number to be used
	 */
	public void addStyledText(final TextStyle ts, final String sText,
			final int nRegion, final int nParagraph) {
		ObjectQueue<StyledText> qStyledText = null;

		switch (nRegion) {
		case FooterHeader.FLG_REGION_LEFT: // Use left region
			qStyledText = this.checkParagraph(this.qLeftRegion, nParagraph);
			break;
		case FooterHeader.FLG_REGION_CENTER: // Use center region
			qStyledText = this.checkParagraph(this.qCenterRegion, nParagraph);
			break;
		case FooterHeader.FLG_REGION_RIGHT: // Use right region
			qStyledText = this.checkParagraph(this.qRightRegion, nParagraph);
			break;
		default: // Invalid nFooterRegionValue, use center region as default
			qStyledText = this.checkParagraph(this.qCenterRegion, nParagraph);
		}

		StyledText st = new StyledText(ts, sText);
		qStyledText.add(st);

	}

	/**
	 * Checks if nParagraph is present in qRegion and return it if yes, if it is
	 * not present, create a new ObjectQueue and add it to qRegion. Return the
	 * new ObjectQueue.
	 * 
	 * @param qRegion
	 * @param nParagraph
	 * @return The ObjectQueue with StyledText elements.
	 */
	private ObjectQueue<StyledText> checkParagraph(
			final ObjectQueue<ObjectQueue<StyledText>> qRegion,
			final int nParagraph) {
		ObjectQueue<StyledText> qStyledText = qRegion.get(nParagraph);
		// Check if the paragraph already exists and add a ObjectQueue if not
		if (qStyledText == null) {
			qRegion.setAt(nParagraph, ObjectQueue.<StyledText> newQueue()); // Create
																			// this
			// paragraph
			qStyledText = qRegion.get(nParagraph);
		}

		return qStyledText;
	}

	public void addPageNumber(final TextStyle ts, final int nFooterRegion,
			final int nParagraph) {
		addStyledText(ts, "<text:page-number>1</text:page-number>",
				nFooterRegion, nParagraph);
	}

	public void addPageCount(final TextStyle ts, final int nFooterRegion,
			final int nParagraph) {
		addStyledText(ts, "<text:page-count>99</text:page-count>",
				nFooterRegion, nParagraph);
	}

}
