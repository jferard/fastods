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
package com.github.jferard.fastods.style;

import java.io.IOException;

import com.github.jferard.fastods.util.XMLUtil;

public class Margins {
	private final String top;
	private final String right;
	private final String bottom;
	private final String left;
	
	Margins(final String top, final String right, final String bottom, final String left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	public String getTop() {
		return this.top;
	}

	public String getRight() {
		return this.right;
	}

	public String getBottom() {
		return this.bottom;
	}

	public String getLeft() {
		return this.left;
	}
	
	public void appendXMLToTableCellStyle(final XMLUtil util,
			final Appendable appendable) throws IOException {
		if (this.top != null)
			util.appendAttribute(appendable, "fo:margin-top", this.top);
		
		if (this.right != null)
			util.appendAttribute(appendable, "fo:margin-right", this.right);

		if (this.bottom != null)	
			util.appendAttribute(appendable, "fo:margin-bottom", this.bottom);
		
		if (this.left != null)	
			util.appendAttribute(appendable, "fo:margin-left", this.left);
	}
}
