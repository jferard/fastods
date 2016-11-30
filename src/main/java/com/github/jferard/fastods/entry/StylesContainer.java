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
import java.util.Map;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.util.Container;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.MultiContainer;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

/**
 * content.xml/office:document-content
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class StylesContainer {
	public enum Dest {
		CONTENT_AUTOMATIC_STYLES, STYLES_AUTOMATIC_STYLES, STYLES_COMMON_STYLES,
	}

	private final MultiContainer<String, StyleTag, Dest> stylesContainer;
	private final Container<String, DataStyle> dataStylesContainer;
	private final Container<String, MasterPageStyle> masterPageStylesContainer;

	StylesContainer() {
		this.stylesContainer = new MultiContainer<String, StyleTag, Dest>(
				Dest.class);
		this.dataStylesContainer = new Container<String, DataStyle>();
		this.masterPageStylesContainer = new Container<String, MasterPageStyle>();
	}

	public void addStyleToContentAutomaticStyles(final StyleTag styleTag) {
		this.addStyle(styleTag, Dest.CONTENT_AUTOMATIC_STYLES,
				Mode.CREATE_OR_UPDATE);
	}

	public boolean addStyle(StyleTag styleTag, Dest dest, Mode mode) {
		return this.stylesContainer.add(this.buildKey(styleTag), styleTag, dest,
				mode);
	}

	public boolean addStyleToContentAutomaticStyles(final StyleTag styleTag,
			final Mode mode) {
		return this.addStyle(styleTag, Dest.CONTENT_AUTOMATIC_STYLES, mode);
	}

	public void addStyleToStylesAutomaticStyles(final StyleTag styleTag) {
		this.addStyle(styleTag, Dest.STYLES_AUTOMATIC_STYLES,
				Mode.CREATE_OR_UPDATE);
	}

	public boolean addStyleToStylesAutomaticStyles(final StyleTag styleTag,
			final Mode mode) {
		return this.addStyle(styleTag, Dest.STYLES_AUTOMATIC_STYLES, mode);
	}

	public void addStyleToStylesCommonStyles(final StyleTag styleTag) {
		this.addStyle(styleTag, Dest.STYLES_COMMON_STYLES,
				Mode.CREATE_OR_UPDATE);
	}

	public boolean addStyleToStylesCommonStyles(final StyleTag styleTag,
			final Mode mode) {
		return this.addStyle(styleTag, Dest.STYLES_COMMON_STYLES, mode);
	}

	/*
	@Deprecated
	public Map<String, StyleTag> getStyleTagByName() {
		return this.contentAutomaticStyleTagByName;
	}
	*/

	public void writeContentAutomaticStyles(final XMLUtil util,
			final ZipUTF8Writer writer) throws IOException {
		this.write(
				this.stylesContainer.getValues(Dest.CONTENT_AUTOMATIC_STYLES),
				util, writer);
	}

	public void writeStylesAutomaticStyles(final XMLUtil util,
			final ZipUTF8Writer writer) throws IOException {
		this.write(this.stylesContainer.getValues(Dest.STYLES_AUTOMATIC_STYLES),
				util, writer);
	}

	public void writeStylesCommonStyles(final XMLUtil util,
			final ZipUTF8Writer writer) throws IOException {
		this.write(this.stylesContainer.getValues(Dest.STYLES_COMMON_STYLES),
				util, writer);
	}

	private String buildKey(final StyleTag styleTag) {
		final String name = styleTag.getName();
		final String family = styleTag.getFamily();
		return family + "@" + name;
	}

	private void write(final Iterable<StyleTag> iterable, final XMLUtil util,
			final ZipUTF8Writer writer) throws IOException {
		for (final StyleTag ts : iterable)
			ts.appendXMLToStylesEntry(util, writer);
	}

	public void addDataStyle(DataStyle dataStyle) {
		this.dataStylesContainer.add(dataStyle.getName(), dataStyle, Mode.CREATE_OR_UPDATE);
	}

	public Map<String, StyleTag> getStyleTagByName(Dest dest) {
		return this.stylesContainer.getValueByKey(dest);
	}
}
