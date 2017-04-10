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

package com.github.jferard.fastods.odselement.config;

import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * There is a hierarchy in config block:
 * <ul>
 * <li>{@code config:config-item-set} is the root. It may contain any config block</li>
 * <li>{@code config:config-item} is the simplest config block</li>
 * <li>{@code config:config-item-map-indexed} is a config block that contains {@code config:config-item-map-entry}</li>
 * <li>{@code config:config-item-map-named} is a config block that contains {@code config:config-item-map-entry}</li>
 * <li>{@code config:config-item-map-entry} may contain any config block</li>
 * </ul>
 * @author Julien Férard
 */
public interface ConfigBlock {
	/**
	 * @return the name of this config block (19.29 config:name)
	 */
	String getName();

	/**
	 * Append a block to an appendable.
	 *
	 * @param util the XML util to create XML tags
	 * @param appendable where to add the block
	 * @throws IOException if the block can't be appended.
	 */
	void appendXML(final XMLUtil util,
				   final Appendable appendable) throws IOException;
}
