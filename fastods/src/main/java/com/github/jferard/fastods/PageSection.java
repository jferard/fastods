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
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * This file PageSection.java is part of FastODS.
 * <p>
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:footer
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:header
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class PageSection {
	private final PageSectionContent content;
	private final PageSectionStyle style;

	/**
	 * Create a new footer/header object.
	 */
	PageSection(final PageSectionContent content,
				final PageSectionStyle style) {
		this.content = content;
		this.style = style;
	}

	/**
	 * Secure version of {@code pageSection.appendPageSectionStyleXMLToAutomaticStyle}: if the footer (or header) is null,
	 * then the default type is used.
	 *
	 * @param header the footer or header, could be null
	 * @param util an util to write XML data
	 * @param appendable the object ot which append footer/header style
	 * @throws IOException if footer/header style wasn't appended.
	 */
	public static void appendPageSectionStyleXMLToAutomaticStyle(final Header header,
																 final XMLUtil util,
																 final Appendable appendable) throws IOException {
		if (header == null)
			appendable.append("<style:header-style />");
		else
			header.appendPageSectionStyleXMLToAutomaticStyle(util,
					appendable);
	}

	/**
	 * Secure version of {@code pageSection.appendPageSectionStyleXMLToAutomaticStyle}: if the footer (or header) is null,
	 * then the default type is used.
	 *
	 * @param footer the footer or header, could be null
	 * @param util an util to write XML data
	 * @param appendable the object ot which append footer/header style
	 * @throws IOException if footer/header style wasn't appended.
	 */
	public static void appendPageSectionStyleXMLToAutomaticStyle(final Footer footer,
																 final XMLUtil util,
																 final Appendable appendable) throws IOException {
		if (footer == null)
			appendable.append("<style:footer-style />");
		else
			footer.appendPageSectionStyleXMLToAutomaticStyle(util,
					appendable);
	}


	public static RegionPageSectionBuilder regionBuilder() {
		return new RegionPageSectionBuilder();
	}

	public static SimplePageSectionBuilder simpleBuilder() {
		return new SimplePageSectionBuilder();
	}

	public static Footer simpleFooter(final String text,
										   final TextStyle ts) {
		final PageSection pageSection = new SimplePageSectionBuilder()
				.text(Text.styledContent(text, ts)).build();
		return new Footer(pageSection);
	}

	public static Header simpleHeader(final String text,
										   final TextStyle ts) {
		final PageSection pageSection = new SimplePageSectionBuilder()
				.text(Text.styledContent(text, ts)).build();
		return new Header(pageSection);
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
	 * @param pageSectionType
	 * @throws IOException If an I/O error occurs
	 */
	public void appendPageSectionStyleXMLToAutomaticStyle(final XMLUtil util,
														  final Appendable appendable, final Type pageSectionType) throws IOException {
		PageSectionStyle.appendFooterHeaderStyleXMLToAutomaticStyle(this.style, pageSectionType, util, appendable);
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
	 * Footer or Header ?
	 */
	public enum Type {
		FOOTER("footer"), HEADER("header");

		private final String typeName;

		Type(final String typeName) {
			this.typeName = typeName;
		}

		public String getTypeName() {
			return this.typeName;
		}
	}
}
