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
package com.github.jferard.fastods.entry;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.jferard.fastods.entry.OdsEntryWithStyles.Mode;
import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

/**
 * content.xml/office:document-content
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class StyleTagsContainer {
	private final Map<String, StyleTag> styleTagByName;

	StyleTagsContainer() {
		this.styleTagByName = new HashMap<String, StyleTag>();
	}

	public void write(final XMLUtil util, final ZipUTF8Writer writer)
			throws IOException {
		for (final StyleTag ts : this.styleTagByName.values())
			ts.appendXMLToStylesEntry(util, writer);
	}

	public void addStyleTag(final StyleTag styleTag) {
		final String name = styleTag.getName();
		final String family = styleTag.getFamily();
		final String key = family + "@" + name;
		this.styleTagByName.put(key, styleTag);
	}

	public boolean addStyleTag(final StyleTag styleTag, Mode mode) {
		final String name = styleTag.getName();
		final String family = styleTag.getFamily();
		final String key = family + "@" + name;
		switch (mode) {
		case CREATE:
			if (this.styleTagByName.containsKey(key))
				return false;
			break;
		case UPDATE:
			if (!this.styleTagByName.containsKey(key))
				return false;
			break;
		default:
			break;
		}
		this.styleTagByName.put(key, styleTag);
		return true;
	}

	@Deprecated
	public Map<String, StyleTag> getStyleTagByName() {
		return this.styleTagByName;
	}
}
