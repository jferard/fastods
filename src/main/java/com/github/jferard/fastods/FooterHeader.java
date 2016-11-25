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

import java.io.IOException;

import com.github.jferard.fastods.entry.StylesEntry;
import com.github.jferard.fastods.entry.StylesEntry.Mode;
import com.github.jferard.fastods.style.Margins;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * This file FooterHeader.java is part of FastODS.
 *
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:footer
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:header
 *
 * @author Julien Férard
 * @author Martin Schulz
 *
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

		private Type(final String typeName) {
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

	public static FooterHeader simpleFooter(final String text,
			final TextStyle ts) {
		return new SimpleFooterHeaderBuilder(Type.FOOTER)
				.text(Text.styledContent(text, ts))
				.build();
	}

	public static FooterHeader simpleHeader(final String text,
			final TextStyle ts) {
		return new SimpleFooterHeaderBuilder(Type.HEADER)
				.text(Text.styledContent(text, ts))
				.build();
	}

	/**
	 * The OdsFile where this object belong to.
	 */
	protected final Type footerHeaderType;

	protected final Margins margins;
	protected final String minHeight;

	/**
	 * Create a new footer object.
	 * 
	 * @param minHeight
	 *
	 * @param odsFile
	 *            - The OdsFile to which this footer belongs to.
	 */
	FooterHeader(final FooterHeader.Type footerHeaderType,
			final Margins margins, final String minHeight) {
		this.footerHeaderType = footerHeaderType;
		this.margins = margins;
		this.minHeight = minHeight;
	}

	public void appendStyleFooterHeaderXMLToAutomaticStyle(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<style:").append(this.footerHeaderType.typeName)
				.append("-style>");
		appendable.append("<style:header-footer-properties");
		util.appendAttribute(appendable, "fo:min-height", this.minHeight);
		this.margins.appendXMLToTableCellStyle(util, appendable);
		appendable.append("/></style:").append(this.footerHeaderType.typeName)
				.append("-style>");
	}

	/**
	 * Used in file styles.xml, in <office:master-styles>,<style:master-page />.
	 *
	 * @throws IOException
	 */
	public abstract void appendXMLToMasterStyle(final XMLUtil util,
			final Appendable appendable) throws IOException;

	/**
	 * @return The current margins of the footer/header.
	 */
	public Margins getMargins() {
		return this.margins;
	}

	/**
	 * @return The current minimum height of the footer/header.
	 */
	public String getMinHeight() {
		return this.minHeight;
	}

	public String getTypeName() {
		return this.footerHeaderType.typeName;
	}

	public abstract void addEmbeddedStylesToStylesEntry(StylesEntry stylesEntry);
	
	public abstract void addEmbeddedStylesToStylesEntry(StylesEntry stylesEntry, Mode mode);
}
