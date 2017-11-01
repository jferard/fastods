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
public abstract class PageSectionContent {
	public enum Region {
		CENTER, LEFT, RIGHT
	}

	/*
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
	*/

	public abstract void addEmbeddedStylesToStylesElement(
			StylesContainer stylesContainer);

	public abstract void addEmbeddedStylesToStylesElement(
			StylesContainer stylesContainer, Mode mode);

	/**
	 * @throws IOException If an I/O error occurs
	 */
	public abstract void appendXMLToMasterStyle(final XMLUtil util,
												final Appendable appendable) throws IOException;
}
