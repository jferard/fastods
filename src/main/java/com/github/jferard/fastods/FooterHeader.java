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
public abstract class FooterHeader {
	public static enum Region {
		CENTER, LEFT, RIGHT;
	}
	
	/**
	 * Footer or Header ?
	 */
	public static enum Type {
		FOOTER("footer"), HEADER("header");

		private final String typeName;

		private Type(String typeName) {
			this.typeName = typeName;
		}

		public String getTypeName() {
			return this.typeName;
		}
	}
	
	public static RegionFooterHeaderBuilder regionBuilder(
			final RegionFooterHeader.Type footerHeaderType) {
		return new RegionFooterHeaderBuilder(footerHeaderType);
	}
	
	public static SimpleFooterHeaderBuilder simpleBuilder(
			final FooterHeader.Type footerHeaderType) {
		return new SimpleFooterHeaderBuilder(footerHeaderType);
	}
	
	
	public static FooterHeader simpleFooter(FHTextStyle ts, String sText) {
		return new SimpleFooterHeaderBuilder(Type.FOOTER).styledText(ts, sText).build();
	}

	public static FooterHeader simpleHeader(FHTextStyle ts, String sText) {
		return new SimpleFooterHeaderBuilder(Type.HEADER).styledText(ts, sText).build();
	}

	protected static void appendXMLRegionBodyToMasterStyle(final Util util,
			final Appendable appendable, final List<FHParagraph> region)
			throws IOException {
		for (final FHParagraph paragraph : region) {
			if (paragraph == null)
				appendable.append("<text:p/>");
			else {
				paragraph.appendXMLToRegionBody(util, appendable);
			}
		}
	}
	/**
	 * The OdsFile where this object belong to.
	 */
	protected final Type footerHeaderType;
	protected final String sMarginLeft;
	protected final String sMarginRight;
	protected final String sMarginTop;

	protected final String sMinHeight;

	/**
	 * Create a new footer object.
	 *
	 * @param odsFile
	 *            - The OdsFile to which this footer belongs to.
	 */
	FooterHeader(final FooterHeader.Type footerHeaderType, String sMarginLeft,
			String sMarginRight, String sMarginTop, String sMinHeight) {
		this.footerHeaderType = footerHeaderType;
		this.sMarginLeft = sMarginLeft;
		this.sMarginRight = sMarginRight;
		this.sMarginTop = sMarginTop;
		this.sMinHeight = sMinHeight;
	}
	
	public void appendXMLToAutomaticStyle(Util util, Appendable appendable)
			throws IOException {
		appendable.append("<style:").append(this.footerHeaderType.typeName)
				.append("-style>");
		appendable.append("<style:header-footer-properties");
		util.appendAttribute(appendable, "fo:min-height", this.sMinHeight);
		util.appendAttribute(appendable, "fo:margin-left", this.sMarginLeft);
		util.appendAttribute(appendable, "fo:margin-right", this.sMarginRight);
		util.appendAttribute(appendable, "fo:margin-top", this.sMarginTop);
		appendable.append("/></style:").append(this.footerHeaderType.typeName)
				.append("-style>");
	}

	/**
	 * Used in file styles.xml, in <office:master-styles>,<style:master-page />.
	 *
	 * @throws IOException
	 */
	public abstract void appendXMLToMasterStyle(final Util util,
			final Appendable appendable) throws IOException;

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
	
	public String getTypeName() {
		return this.footerHeaderType.typeName;
	}
	
}
