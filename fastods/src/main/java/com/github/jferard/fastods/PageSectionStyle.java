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

import com.github.jferard.fastods.PageSection.Type;
import com.github.jferard.fastods.style.Margins;
import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
class PageSectionStyle {
	private final Margins margins;
	private final Length minHeight;

	/**
	 * Create a new footer object.
	 * @param margins the margins
	 * @param minHeight the height of the footer/header
	 */
	PageSectionStyle(final Margins margins, final Length minHeight) {
		this.margins = margins;
		this.minHeight = minHeight;
	}

	/**
	 * Secure version of {@code pageSectionStyle.appendPageSectionStyleXMLToAutomaticStyle}: if the footer (or header) is null,
	 * then the default type is used.
	 *
	 * @param pageSectionStyle the footer or header style, could be null
	 * @param pageSectionType the type if pageSectionStyle is null.
	 * @param util an util
	 * @param appendable the destination
	 * @throws IOException if an I/O error occurs
	 */
	public static void appendFooterHeaderStyleXMLToAutomaticStyle(final PageSectionStyle pageSectionStyle,
																  final Type pageSectionType, final XMLUtil util,
																  final Appendable appendable) throws IOException {
		if (pageSectionStyle == null)
			appendable.append("</style:").append(pageSectionType.getTypeName())
					.append("-style />");
		else
			pageSectionStyle.appendFooterHeaderStyleXMLToAutomaticStyle(util,
					appendable, pageSectionType);
	}

	/**
	 * Append a footer/header to styles.xml/automatic-styles
	 * @param util an util
	 * @param appendable the destination
	 * @param pageSectionType the type if pageSectionStyle is null.
	 * @throws IOException if an I/O error occurs
	 */
	public void appendFooterHeaderStyleXMLToAutomaticStyle(final XMLUtil util,
														   final Appendable appendable, final Type pageSectionType) throws IOException {
		appendable.append("<style:").append(pageSectionType.getTypeName())
				.append("-style>");
		appendable.append("<style:header-footer-properties");
		util.appendAttribute(appendable, "fo:min-height", this.minHeight.toString());
		this.margins.appendXMLContent(util, appendable);
		appendable.append("/></style:").append(pageSectionType.getTypeName())
				.append("-style>");
	}
}