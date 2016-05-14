/*
*	SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
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
package org.simpleods;

/**
 * @author Martin Schulz<br>
 * 
 * Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net><br>
 * 
 * This file Footer.java is part of SimpleODS.
 *
 */
public class Footer {
	
	
	/**
	 * The footer region on the left page side. 
	 */
	public static final int FLG_REGION_LEFT = 0;
	/**
	 * The footer region in the middle of the page. 
	 */
	public static final int FLG_REGION_CENTER = 1;
	/**
	 * The footer region on the right page side. 
	 */
	public static final int FLG_REGION_RIGHT = 2;
	
	private ObjectQueue qLeftRegion		= new ObjectQueue();
	private ObjectQueue qCenterRegion	= new ObjectQueue();
	private ObjectQueue qRightRegion 	= new ObjectQueue();
	
	private String sMinHeight   = "0cm";
	private String sMarginLeft  = "0cm"; 
	private String sMarginRight = "0cm"; 
	private String sMarginTop   = "0cm"; 
	
	/**
	 * The OdsFile where this object belong to.
	 */
	private OdsFile o;
	
	/**
	 * Create a new footer object.
	 * @param odsFile	- The OdsFile to which this footer belongs to.
	 */
	public Footer(final OdsFile odsFile) {
		this.o = odsFile;
		o.getStyles().setFooter(this);	// Add this Footer object
		qCenterRegion.add(new ObjectQueue());	// Create the first paragraph 
	}	
	
	/**
	 * @return The current minimum height of the footer.
	 */
	public String getMinHeight() {
		return sMinHeight;
	}

	public void setMinHeight(String sMinHeight) {
		this.sMinHeight = sMinHeight;
	}

	/**
	 * @return The current left margin of the footer.
	 */
	public String getMarginLeft() {
		return sMarginLeft;
	}

	public void setMarginLeft(String sMarginLeft) {
		this.sMarginLeft = sMarginLeft;
	}

	/**
	 * @return The current right margin of the footer.
	 */
	public String getMarginRight() {
		return sMarginRight;
	}

	public void setMarginRight(String sMarginRight) {
		this.sMarginRight = sMarginRight;
	}

	public String getMarginTop() {
		return sMarginTop;
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
		StringBuffer sbTemp = new StringBuffer();

		sbTemp.append("<style:style style:name=\"Footer\" style:family=\"paragraph\" style:parent-style-name=\"Standard\"  style:class=\"extra\">");
		sbTemp.append("<style:paragraph-properties  text:number-lines=\"false\" text:line-number=\"0\">");

		sbTemp.append("</style:paragraph-properties>");
		sbTemp.append("</style:style>");

		return sbTemp.toString();
	}
	
	/**
	 * Used in file styles.xml, in <office:master-styles>,<style:master-page />.
	 * @return
	 */
	protected String toMasterStyleXML() {

		StringBuffer sbTemp = new StringBuffer();

		this.writeRegion(sbTemp, this.qLeftRegion,"region-left");
		
		this.writeRegion(sbTemp, this.qCenterRegion,"region-center");

		this.writeRegion(sbTemp, this.qRightRegion,"region-right");

		// </style:footer> is written by PageStyle.toMasterStyleXML()

		return sbTemp.toString();

	}
	
	private void writeRegion(StringBuffer sbTemp, ObjectQueue qRegion, final String sRegionName) {
		
		if (qRegion.size() == 0) {
			return;
		}	
		
		sbTemp.append("<style:" + sRegionName + ">");
		
		for (int n = 0; n < qRegion.size(); n++) {

			// <style:footer> is written by PageStyle.toMasterStyleXML()

			ObjectQueue qStyledText = (ObjectQueue) qRegion.get(n);

			sbTemp.append("<text:p>");

			// Check if a qStyles object is null and add an empty paragraph for this
			if (qStyledText == null) {
				sbTemp.append("<text:span />");
			} else {
				// Add all styles and text for this paragraphs
				for (int i = 0; i < qStyledText.size(); i++) {
					StyledText st = (StyledText) qStyledText.get(i);
					sbTemp.append(st.toMasterStyleXML());

				}
			}

			sbTemp.append("</text:p>");

		}
		
		sbTemp.append("</style:" + sRegionName + ">");
		
		return;
	}
	
	/**
	 * Adds a TextStyle and text to the footer region specified by nFooterRegion.<br>
	 * The paragraph to be used is nParagraph.<br>
	 * The text will be shown in the order it was added with this function.
	 * @param ts		The text style to be used
	 * @param sText		The string with the text
	 * @param nFooterRegion		One of : Footer.FLG_REGION_LEFT, Footer.FLG_REGION_CENTER or Footer.FLG_REGION_RIGHT
	 * @param nParagraph 		The paragraph number to be used
	 */
	public void addStyledText(final TextStyle ts, final String sText, final int nFooterRegion, final int nParagraph) {
		ObjectQueue qStyledText = null;
		
		switch(nFooterRegion) {
		case Footer.FLG_REGION_LEFT:	// Use left region
			qStyledText = this.checkParagraph(this.qLeftRegion, nParagraph);
			break;
		case Footer.FLG_REGION_CENTER:	// Use center region
			qStyledText = this.checkParagraph(this.qCenterRegion, nParagraph);
			break;
		case Footer.FLG_REGION_RIGHT:	// Use right region
			qStyledText = this.checkParagraph(this.qRightRegion, nParagraph);
			break;
		default:	// Invalid nFooterRegionValue, use center region as default
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
	private ObjectQueue checkParagraph(final ObjectQueue qRegion, final int nParagraph) {
		ObjectQueue qStyledText = (ObjectQueue) qRegion.get(nParagraph);
		// Check if the paragraph already exists and add a ObjectQueue if not
		if (qStyledText == null) {
			qRegion.setAt(nParagraph, new ObjectQueue());	// Create this paragraph 
			qStyledText = (ObjectQueue) qRegion.get(nParagraph);
		}
		
		return qStyledText;		
	}
	
	public void addPageNumber(final TextStyle ts, final int nFooterRegion, final int nParagraph) {
		addStyledText(ts, "<text:page-number>1</text:page-number>", nFooterRegion, nParagraph);
	}

	public void addPageCount(final TextStyle ts, final int nFooterRegion, final int nParagraph) {
		addStyledText(ts, "<text:page-count>99</text:page-count>", nFooterRegion, nParagraph);
	}

}
