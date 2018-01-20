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

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * content.xml/office:document-content/office:automatic-styles
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class TableStyle implements ObjectStyle {
	/**
	 * the default (empty) table style
	 */
	public static final TableStyle DEFAULT_TABLE_STYLE = TableStyle
			.builder("ta1").buildHidden();

	private final boolean hidden;
	private final PageStyle pageStyle;
	private final String name;
	private String key;

	/**
	 * Create a new table style and add it to contentEntry.<br>
	 * Version 0.5.0 Added parameter NamedOdsDocument o
	 *
	 * @param styleName A unique name for this style
	 * @param hidden if the style is automatic
	 * @param pageStyle The master page style for this table
	 */
	TableStyle(final String styleName, final boolean hidden, final PageStyle pageStyle) {
		this.name = styleName;
		this.hidden = hidden;
		this.pageStyle = pageStyle;
	}

	/**
	 * @param name the name of the style
	 * @return the builder
	 */
	public static TableStyleBuilder builder(final String name) {
		return new TableStyleBuilder(name);
	}

	@Override
	public void addToElements(final OdsElements odsElements) {
		odsElements.addObjectStyle(this);
	}

	@Override
	public void appendXMLRepresentation(final XMLUtil util, final Appendable appendable)
			throws IOException {
		appendable.append("<style:style");
		util.appendEAttribute(appendable, "style:name", this.name);
		util.appendAttribute(appendable, "style:family", "table");
		if (this.pageStyle != null)
			util.appendEAttribute(appendable, "style:master-page-name",
					this.pageStyle.getMasterName());
		appendable.append("><style:table-properties");
		util.appendAttribute(appendable, "table:display", "true");
		util.appendAttribute(appendable, "style:writing-mode", "lr-tb");
		appendable.append("/></style:style>");
	}

	@Override
	public ObjectStyleFamily getFamily() {
		return ObjectStyleFamily.TABLE;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getKey() {
		if (this.key == null)
			this.key = this.getFamily() + "@" + this.getName();
		return this.key;
	}

	@Override
	public boolean isHidden() {
		return this.hidden;
	}

	/**
	 * @return the page style
	 */
	public PageStyle getPageStyle() {
		return this.pageStyle;
	}
}
