/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.Footer;
import com.github.jferard.fastods.Header;
import com.github.jferard.fastods.StyleWithEmbeddedStyles;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * OpenDocument 16.9 style:master-page
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class MasterPageStyle implements AddableToOdsElements, StyleWithEmbeddedStyles {
	private final String layoutName;
	private final Footer footer;
	private final Header header;

	private final String name;

	/**
	 * Create a new page style. Version 0.5.0 Added parameter NamedOdsDocument o
	 *  @param name   A unique name for this style
	 * @param layoutName the name of the layout linked to this style
	 * @param header the header for this style
	 * @param footer the footer for this style
	 */
	MasterPageStyle(final String name, final String layoutName,
					final Header header, final Footer footer) {
		this.name = name;
		this.layoutName = layoutName;
		this.footer = footer;
		this.header = header;
	}

	/**
	 * Add the style embedded in this master page style to a container
	 * @param stylesContainer the container
	 */
	@Override
	public void addEmbeddedStyles(
			final StylesContainer stylesContainer) {
		if (this.header != null)
			this.header.addEmbeddedStyles(stylesContainer);
		if (this.footer != null)
			this.footer.addEmbeddedStyles(stylesContainer);
	}

    /**
     * Add the style embedded in this master page style to a container
     * @param stylesContainer the container
     * @param mode CREATE, UPDATE, CREATE_OR_UPDATE
     */
    @Override
	public void addEmbeddedStyles(
			final StylesContainer stylesContainer, final Mode mode) {
		if (this.header != null)
			this.header.addEmbeddedStyles(stylesContainer, mode);
		if (this.footer != null)
			this.footer.addEmbeddedStyles(stylesContainer, mode);
	}


	@Override
	public void addToElements(final OdsElements odsElements) {
		odsElements.addMasterPageStyle(this);
	}

	/**
	 * Return the master-style informations for this PageStyle.
	 *
	 * @param util a util for XML writing
	 * @param appendable where to write
	 * @throws IOException If an I/O error occurs
	 */
	public void appendXMLToMasterStyle(final XMLUtil util,
									   final Appendable appendable) throws IOException {
		appendable.append("<style:master-page");
		util.appendEAttribute(appendable, "style:name", this.name);
		util.appendEAttribute(appendable, "style:page-layout-name", this.layoutName);
		appendable.append("><style:header>");
		this.header.appendXMLToMasterStyle(util, appendable);
		appendable.append("</style:header>");
		appendable.append("<style:header-left");
		util.appendAttribute(appendable, "style:display", false);
		appendable.append("/>");
		appendable.append("<style:footer>");
		this.footer.appendXMLToMasterStyle(util, appendable);
		appendable.append("</style:footer>");
		appendable.append("<style:footer-left");
		util.appendAttribute(appendable, "style:display", false);
		appendable.append("/>");
		appendable.append("</style:master-page>");
	}

    /**
	 * @return true if the page style has a footer
     */
	public boolean hasFooter() {
		return this.footer != null;
	}

    /**
     * @return true if the page style has a header
     */
	public boolean hasHeader() {
		return this.header != null;
	}

	/**
	 * Get the name of this page style.
	 *
	 * @return The page style name
	 */
	public String getName() {
		return this.name;
	}

}
