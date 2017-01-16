/*
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
 */
package com.github.jferard.fastods;

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.Container.Mode;
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
abstract class FooterHeaderContent {
	public static enum Region {
		CENTER, LEFT, RIGHT;
	}

	/**
	 * The OdsDocument where this object belong to.
	 */
	protected final FooterHeader.Type footerHeaderType;

	/**
	 * Create a new footer object.
	 */
	FooterHeaderContent(final FooterHeader.Type footerHeaderType) {
		this.footerHeaderType = footerHeaderType;
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
		return new SimpleFooterHeaderBuilder(FooterHeader.Type.FOOTER)
				.text(Text.styledContent(text, ts)).build();
	}

	public static FooterHeader simpleHeader(final String text,
												   final TextStyle ts) {
		return new SimpleFooterHeaderBuilder(FooterHeader.Type.HEADER)
				.text(Text.styledContent(text, ts)).build();
	}

	public abstract void addEmbeddedStylesToStylesElement(
			StylesContainer stylesContainer);

	public abstract void addEmbeddedStylesToStylesElement(
			StylesContainer stylesContainer, Mode mode);

	/**
	 * @throws IOException
	 */
	public abstract void appendXMLToMasterStyle(final XMLUtil util,
												final Appendable appendable) throws IOException;

	public String getTypeName() {
		return this.footerHeaderType.getTypeName();
	}
}
