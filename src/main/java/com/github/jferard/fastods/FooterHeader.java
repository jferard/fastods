/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods;

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.Margins;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * This file FooterHeader.java is part of FastODS.
 * <p>
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:footer
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:header
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class FooterHeader {
	private final Type footerHeaderType;
	private final FooterHeaderContent content;
	private final FooterHeaderStyle style;

	/**
	 * Create a new footer/header object.
	 */
	FooterHeader(final FooterHeader.Type footerHeaderType, final FooterHeaderContent content,
				 final FooterHeaderStyle style) {
		this.footerHeaderType = footerHeaderType;
		this.content = content;
		this.style = style;
	}

	/**
	 * Secure version of {@code footerHeader.appendFooterHeaderStyleXMLToAutomaticStyle}: if the footer (or header) is null,
	 * then the default type is used.
	 *
	 * @param footerHeader the footer or header, could be null
	 * @param defaultType the type if footerHeader is null.
	 * @param util an util to write XML data
	 * @param appendable the object ot which append footer/header style
	 * @throws IOException if footer/header style wasn't appended.
	 */
	public static void appendFooterHeaderStyleXMLToAutomaticStyle(final FooterHeader footerHeader, final Type defaultType,
																  final XMLUtil util,
																  final Appendable appendable) throws IOException {
		if (footerHeader == null)
			appendable.append("<style:").append(defaultType.typeName)
					.append("-style />");
		else
			footerHeader.appendFooterHeaderStyleXMLToAutomaticStyle(util,
					appendable);
	}

	public static RegionFooterHeaderBuilder regionBuilder(
			final FooterHeader.Type footerHeaderType) {
		return new RegionFooterHeaderBuilder(footerHeaderType);
	}

	public static SimpleFooterHeaderBuilder simpleBuilder(
			final FooterHeader.Type footerHeaderType) {
		return new SimpleFooterHeaderBuilder(footerHeaderType);
	}

	public static FooterHeader simpleFooter(final String text,
											final TextStyle ts) {
		return new SimpleFooterHeaderBuilder(Type.FOOTER)
				.text(Text.styledContent(text, ts)).build();
	}

	public static FooterHeader simpleHeader(final String text,
											final TextStyle ts) {
		return new SimpleFooterHeaderBuilder(Type.HEADER)
				.text(Text.styledContent(text, ts)).build();
	}

	public void addEmbeddedStylesToStylesElement(
			final StylesContainer stylesContainer) {
		this.content.addEmbeddedStylesToStylesElement(stylesContainer);

	}

	public void addEmbeddedStylesToStylesElement(
			final StylesContainer stylesContainer, final Mode mode) {
		this.content.addEmbeddedStylesToStylesElement(stylesContainer, mode);
	}

	/**
	 * @param util an instance of the util class for XML writing
	 * @param appendable the appendable element where the method will write the XML
	 * @throws IOException If an I/O error occurs
	 */
	public void appendFooterHeaderStyleXMLToAutomaticStyle(final XMLUtil util,
														   final Appendable appendable) throws IOException {
		FooterHeaderStyle.appendFooterHeaderStyleXMLToAutomaticStyle(this.style,  this.footerHeaderType, util, appendable);
	}

	/**
	 * @param util an instance of the util class for XML writing
	 * @param appendable the appendable element where the method will write the XML
	 * @throws IOException If an I/O error occurs
	 */
	public void appendXMLToMasterStyle(final XMLUtil util,
									   final Appendable appendable) throws IOException {
		this.content.appendXMLToMasterStyle(util, appendable);
	}

	/**
	 * @return The current margins of the footer/header.
	 */
	public Margins getMargins() {
		return this.style.getMargins();
	}

	/**
	 * @return The current minimum height of the footer/header.
	 */
	public Length getMinHeight() {
		return this.style.getMinHeight();
	}

	/**
	 * @return The type name
	 */
	public String getTypeName() {
		return this.footerHeaderType.typeName;
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
}
