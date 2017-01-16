/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
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
 * ****************************************************************************/
package com.github.jferard.fastods;

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.Margins;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

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
public class FooterHeader {
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

	public static void appendStyleFooterHeaderXMLToAutomaticStyle(final FooterHeader footerHeader, final Type type, final XMLUtil util,
																  final Appendable appendable) throws IOException {
		if (footerHeader == null)
			appendable.append("<style:").append(type.typeName)
					.append("-style />");
		else
			footerHeader.appendStyleFooterHeaderXMLToAutomaticStyle(util,
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

	public void addEmbeddedStylesToStylesElement(
			StylesContainer stylesContainer) {
		this.content.addEmbeddedStylesToStylesElement(stylesContainer);

	}

	public void addEmbeddedStylesToStylesElement(
			StylesContainer stylesContainer, Mode mode) {
		this.content.addEmbeddedStylesToStylesElement(stylesContainer, mode);
	}

	public void appendStyleFooterHeaderXMLToAutomaticStyle(final XMLUtil util,
			final Appendable appendable) throws IOException {
		this.style.appendFooterHeaderStyleXMLToAutomaticStyle(util, appendable);
	}

	/**
	 * @throws IOException
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
	public String getMinHeight() {
		return this.style.getMinHeight();
	}

	public String getTypeName() {
		return this.footerHeaderType.typeName;
	}
}
