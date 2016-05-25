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

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file PageStyle.java is part of FastODS.
 *
 *         WHERE ?
 *         styles.xml/office:document-styles/office:master-styles/style:master-
 *         page
 */
public class PageStyle
		implements NamedObject<AutomaticStyle> {
	public static enum PaperFormat {
		A3("42.0cm", "29.7cm"), A4("29.7cm", "21.0cm"), A5("21.0cm",
				"14.8cm"), LETTER("27.94cm",
						"21.59cm"), LEGAL("35.57cm", "21.59cm"), USER("", "");

		private final String height;
		private final String width;

		private PaperFormat(String height, String width) {
			this.height = height;
			this.width = width;

		}

		String getHeight() {
			return this.height;
		}

		String getWidth() {
			return this.width;
		}
	}

	public static enum PrintOrientation {
		VERTICAL("portrait"), HORIZONTAL("landscape");

		private final String attrValue;

		private PrintOrientation(String attrValue) {
			this.attrValue = attrValue;
		}

		String getAttrValue() {
			return this.attrValue;
		}
	}

	public static enum WritingMode {
		LRTB("lr-tb"), RLTB("rl-tb"), TBRL("tb-rl"), TBLR("tb_lr"), LR(
				"lr"), RL("rl"), TB("tb"), PAGE("page");

		private final String attrValue;

		private WritingMode(String attrValue) {
			this.attrValue = attrValue;
		}

		String getAttrValue() {
			return this.attrValue;
		}

	}

	public static final WritingMode DEFAULT_WRITINGMODE = WritingMode.LRTB;

	public static final PaperFormat DEFAULT_FORMAT = PaperFormat.A4;

	public static final PrintOrientation DEFAULT_PRINTORIENTATION = PrintOrientation.VERTICAL;

	public static PageStyleBuilder builder() {
		return new PageStyleBuilder();
	}

	private final String sName;
	private final String sMarginTop;
	private final String sMarginBottom;
	private final String sMarginLeft;

	private final String sMarginRight;
	private final String sPageWidth;
	private final String sPageHeight;
	private final String sNumFormat;

	private final String sBackgroundColor;
	private final String sTextStyleFooter;
	private final String sTextStyleHeader;
	private final String sTextHeader;

	private final String sTextFooter;
	private final PrintOrientation printOrientation;
	private final PaperFormat paperFormat;
	private final WritingMode writingMode;
	private FooterHeader header;

	private FooterHeader footer;

	/**
	 * Create a new page style. Version 0.5.0 Added parameter OdsFile o
	 * 
	 * @param sName
	 *            A unique name for this style
	 * @param header2
	 * @param footer
	 */
	public PageStyle(String sName, String sMarginTop, String sMarginBottom,
			String sMarginLeft, String sMarginRight, String sPageWidth,
			String sPageHeight, String sNumFormat, String sBackgroundColor,
			FooterHeader footer, String sTextStyleFooter, FooterHeader header,
			String sTextStyleHeader, String sTextHeader, String sTextFooter,
			PrintOrientation printOrientation, PaperFormat paperFormat,
			WritingMode writingMode) {
		this.sName = sName;
		this.sMarginTop = sMarginTop;
		this.sMarginBottom = sMarginBottom;
		this.sMarginLeft = sMarginLeft;
		this.sMarginRight = sMarginRight;
		this.sPageWidth = sPageWidth;
		this.sPageHeight = sPageHeight;
		this.sNumFormat = sNumFormat;
		this.sBackgroundColor = sBackgroundColor;
		this.footer = footer;
		this.sTextStyleFooter = sTextStyleFooter;
		this.header = header;
		this.sTextStyleHeader = sTextStyleHeader;
		this.sTextHeader = sTextHeader;
		this.sTextFooter = sTextFooter;
		this.printOrientation = printOrientation;
		this.paperFormat = paperFormat;
		this.writingMode = writingMode;
	}

	public void addToFile(OdsFile odsFile) {
		odsFile.getStyles().addPageStyle(this);
	}

	/**
	 * Return the master-style informations for this PageStyle.
	 * 
	 * @return The master style representation for styles.xml
	 * @throws IOException
	 */
	public void appendXML(Util util, Appendable appendable, MasterStyle where)
			throws IOException {
		appendable.append("<style:master-page");
		util.appendEAttribute(appendable, "style:name", "DefaultMasterPage");
		util.appendAttribute(appendable, "style:page-layout-name", this.sName);
		appendable.append("><style:header>");
		if (this.header == null) {
			appendable.append("<text:p");
			appendDefaultFooterHeader(util, appendable, this.sTextStyleHeader,
					this.sTextHeader);
		} else {
			this.header.appendXML(util, appendable, MasterStyle.instance);
		}
		appendable.append("</style:header>");

		appendable.append("<style:footer>");
		if (this.footer == null) {
			appendDefaultFooterHeader(util, appendable, this.sTextStyleFooter,
					this.sTextFooter);
		} else {
			this.footer.appendXML(util, appendable, MasterStyle.instance);
		}
		appendable.append("</style:footer>");
		appendable.append("</style:master-page>");
	}

	private void appendDefaultFooterHeader(Util util, Appendable appendable,
			String sTextStyle, String sText) throws IOException {
		appendable.append("<text:p");
		util.appendAttribute(appendable, "text:style-name", sTextStyle);
		appendable.append(">");
		appendable.append(util.escapeXMLContent(sText)).append("</text:p>");
	}

	/**
	 * Write the XML format for this object.<br>
	 * This is used while writing the ODS file.
	 * 
	 */
	@Override
	public void appendXML(Util util, Appendable appendable,
			AutomaticStyle where) throws IOException {
		appendable.append("<style:page-layout");
		util.appendAttribute(appendable, "style:name", this.sName);
		appendable.append("><style:page-layout-properties");
		util.appendAttribute(appendable, "fo:page-width", this.sPageWidth);
		util.appendAttribute(appendable, "fo:page-height", this.sPageHeight);
		util.appendAttribute(appendable, "style:num-format", this.sNumFormat);
		util.appendEAttribute(appendable, "style:writing-mode",
				this.writingMode.getAttrValue());
		util.appendEAttribute(appendable, "style:print-orientation",
				this.printOrientation.getAttrValue());
		this.appendBackgroundColor(util, appendable);
		util.appendAttribute(appendable, "fo:margin-top", this.sMarginTop);
		util.appendAttribute(appendable, "fo:margin-bottom",
				this.sMarginBottom);
		util.appendAttribute(appendable, "fo:margin-left", this.sMarginLeft);
		util.appendAttribute(appendable, "fo:margin-right", this.sMarginRight);
		appendable.append("/>"); // End of page-layout-properties

		this.addFooterHeaderStyle(appendable, this.header,
				"style:header-style");
		this.addFooterHeaderStyle(appendable, this.header,
				"style:footer-style");
		/*
		if( styles.getFooter()==null ) {
			sbTemp.append("<style:footer-style />");
		} else {
			FooterHeader f = styles.getFooter();
			sbTemp.append("<style:footer-style>");
			sbTemp.append("<style:header-footer-properties ");
			sbTemp.append("fo:min-height=\""+f.getMinHeight()+"\" ");
			sbTemp.append("fo:margin-left=\""+f.getMarginLeft()+"\" ");
			sbTemp.append("fo:margin-right=\""+f.getMarginRight()+"\" ");
			sbTemp.append("fo:margin-top=\""+f.getsMarginTop()+"\"/>");
			sbTemp.append("</style:footer-style>");		
		}*/
		appendable.append("</style:page-layout>");
	}

	public String getBackgroundColor() {
		return this.sBackgroundColor;
	}

	public String getMarginBottom() {
		return this.sMarginBottom;
	}

	public String getMarginLeft() {
		return this.sMarginLeft;
	}

	public String getMarginRight() {
		return this.sMarginRight;
	}

	public String getMarginTop() {
		return this.sMarginTop;
	}

	/**
	 * Get the name of this page style.
	 * 
	 * @return The page style name
	 */
	@Override
	public String getName() {
		return this.sName;
	}

	public String getPageHeight() {
		return this.sPageHeight;
	}

	public String getPageWidth() {
		return this.sPageWidth;
	}

	/**
	 * Get the paper format as one of PageStyle.STYLE_PAPERFORMAT_*.
	 */
	public PaperFormat getPaperFormat() {
		return this.paperFormat;
	}

	/**
	 * Get the writing mode<br>
	 * . STYLE_WRITINGMODE_LRTB lr-tb (left to right; top to bottom)<br>
	 * STYLE_WRITINGMODE_RLTB<br>
	 * STYLE_WRITINGMODE_TBRL<br>
	 * STYLE_WRITINGMODE_TBLR<br>
	 * STYLE_WRITINGMODE_LR<br>
	 * STYLE_WRITINGMODE_RL<br>
	 * STYLE_WRITINGMODE_TB<br>
	 * STYLE_WRITINGMODE_PAGE<br>
	 * 
	 * @return The current writing mode.
	 */
	public WritingMode getWritingMode() {
		return this.writingMode;
	}

	private void addFooterHeaderStyle(final Appendable appendable,
			final FooterHeader header, final String tag) throws IOException {
		if (header == null) {
			appendable.append("<").append(tag).append(" />");
		} else {
			FooterHeader h = header;
			appendable.append("<").append(tag).append(">");
			appendable.append("<style:header-footer-properties ");
			appendable.append("fo:min-height=\"").append(h.getMinHeight())
					.append("\" ");
			appendable.append("fo:margin-left=\"").append(h.getMarginLeft())
					.append("\" ");
			appendable.append("fo:margin-right=\"").append(h.getMarginRight())
					.append("\" ");
			appendable.append("fo:margin-top=\"").append(h.getMarginTop())
					.append("\"/>");
			appendable.append("</").append(tag).append(">");
		}
	}

	private void appendBackgroundColor(Util util, Appendable appendable)
			throws IOException {
		if (this.getBackgroundColor().length() > 0) {
			util.appendAttribute(appendable, "fo:background-color",
					this.sBackgroundColor);
		}
	}
}
