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

import com.github.jferard.fastods.util.NamedObject;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * @author Julien Férard
 */
public interface ObjectStyle extends NamedObject, AddableToOdsElements {
	/**
	 * Append the style to an appendable
	 * @param util a helper object
	 * @param appendable the appendable to append data to
	 * @throws IOException if the style can't be added
	 */
	void appendXML(XMLUtil util, Appendable appendable) throws IOException;

	/**
	 * 19.476 style:family
	 * @return the famlily name of this style
	 */
	String getFamily();

	/**
	 * The identifier of this style ius built on the name (19.498 style:name) and the family
	 * @return a unique identifier for this style
	 */
	String getKey();

	/**
	 * @return true if the style is an automatic style (3.15.3. office:automatic-styles)
	 */
	boolean isHidden();
}
