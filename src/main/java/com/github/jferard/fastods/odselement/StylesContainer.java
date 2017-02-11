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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.style.MasterPageStyle;
import com.github.jferard.fastods.style.PageLayoutStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.Container;
import com.github.jferard.fastods.util.Container.Mode;
import com.github.jferard.fastods.util.MultiContainer;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.Map;

/**
 * content.xml/office:document-content
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class StylesContainer {
	private final Container<String, DataStyle> dataStylesContainer;
	private final Container<String, MasterPageStyle> masterPageStylesContainer;
	private final Container<String, PageLayoutStyle> pageLayoutStylesContainer;
	private final MultiContainer<String, StyleTag, Dest> styleTagsContainer;

	StylesContainer() {
		this.styleTagsContainer = new MultiContainer<String, StyleTag, Dest>(
				Dest.class);
		this.dataStylesContainer = new Container<String, DataStyle>();
		this.masterPageStylesContainer = new Container<String, MasterPageStyle>();
		this.pageLayoutStylesContainer = new Container<String, PageLayoutStyle>();
	}

	public TableCellStyle addChildCellStyle(final TableCellStyle style, final DataStyle dataStyle) {
		this.addDataStyle(dataStyle);
		if (!style.getName().contains("@@"))
			this.addStyleToStylesCommonStyles(style); // here, the style may be a child style
		final String name = style.getRealName() + "@@" + dataStyle.getName();
		StyleTag anonymousStyle = this.styleTagsContainer.get(name,
				Dest.CONTENT_AUTOMATIC_STYLES);
		if (anonymousStyle == null) {
			anonymousStyle =
					TableCellStyle.builder(name).parentCellStyle(style)
							.dataStyle(dataStyle).build();
			this.addStyleToContentAutomaticStyles(anonymousStyle);
		}
		return (TableCellStyle) anonymousStyle;
	}

	public void addDataStyle(final DataStyle dataStyle) {
		this.dataStylesContainer.add(dataStyle.getName(), dataStyle,
				Mode.CREATE);
	}

	public boolean addDataStyle(final DataStyle dataStyle, final Mode mode) {
		return this.dataStylesContainer.add(dataStyle.getName(), dataStyle,
				mode);
	}

	public boolean addMasterPageStyle(final MasterPageStyle masterPageStyle) {
		return this.addMasterPageStyle(masterPageStyle, Mode.CREATE);
	}

	public boolean addMasterPageStyle(final MasterPageStyle ps,
									  final Mode mode) {
		if (this.masterPageStylesContainer.add(ps.getName(), ps, mode)) {
			ps.addEmbeddedStylesToStylesContainer(this, mode);
			return true;
		} else
			return false;
	}

	public void addNewDataStyleFromCellStyle(final TableCellStyle style) {
		this.addStyleToContentAutomaticStyles(style);
		this.addDataStyle(style.getDataStyle());
	}

	public boolean addPageLayoutStyle(final PageLayoutStyle pageLayoutStyle) {
		return this.addPageLayoutStyle(pageLayoutStyle, Mode.CREATE);
	}

	public boolean addPageLayoutStyle(final PageLayoutStyle pageLayoutStyle, final Mode mode) {
		return this.pageLayoutStylesContainer.add(pageLayoutStyle.getName(), pageLayoutStyle, mode);
	}

	public void addPageStyle(final PageStyle ps) {
		this.addMasterPageStyle(ps.getMasterPageStyle());
		this.addPageLayoutStyle(ps.getPageLayoutStyle());
	}

	public void addPageStyle(final PageStyle ps, final Mode mode) {
		this.addMasterPageStyle(ps.getMasterPageStyle(), mode);
		this.addPageLayoutStyle(ps.getPageLayoutStyle(), mode);
	}

	public void addStyleToContentAutomaticStyles(final StyleTag styleTag) {
		this.styleTagsContainer.add(styleTag.getKey(), styleTag,
				Dest.CONTENT_AUTOMATIC_STYLES, Mode.CREATE);
	}

	public boolean addStyleToContentAutomaticStyles(final StyleTag styleTag,
													final Mode mode) {
		return this.styleTagsContainer.add(styleTag.getKey(), styleTag,
				Dest.CONTENT_AUTOMATIC_STYLES, mode);
	}

	/*
	@Deprecated
	public Map<String, StyleTag> getStyleTagByName() {
		return this.contentAutomaticStyleTagByName;
	}
	*/

	public void addStyleToStylesAutomaticStyles(final StyleTag styleTag) {
		this.styleTagsContainer.add(styleTag.getKey(), styleTag,
				Dest.STYLES_AUTOMATIC_STYLES, Mode.CREATE);
	}

	public boolean addStyleToStylesAutomaticStyles(final StyleTag styleTag,
												   final Mode mode) {
		return this.styleTagsContainer.add(styleTag.getKey(), styleTag,
				Dest.STYLES_AUTOMATIC_STYLES, mode);
	}

	public void addStyleToStylesCommonStyles(final StyleTag styleTag) {
		this.styleTagsContainer.add(styleTag.getKey(), styleTag,
				Dest.STYLES_COMMON_STYLES, Mode.CREATE);
	}

	public boolean addStyleToStylesCommonStyles(final StyleTag styleTag,
												final Mode mode) {
		return this.styleTagsContainer.add(styleTag.getKey(), styleTag,
				Dest.STYLES_COMMON_STYLES, mode);
	}

	public void debug() {
		this.styleTagsContainer.debug();
		this.dataStylesContainer.debug();
		this.masterPageStylesContainer.debug();
		this.pageLayoutStylesContainer.debug();
	}

	public void freeze() {
		this.styleTagsContainer.freeze();
		this.dataStylesContainer.freeze();
		this.masterPageStylesContainer.freeze();
		this.pageLayoutStylesContainer.freeze();
	}

	public Map<String, DataStyle> getDataStyles() {
		return this.dataStylesContainer.getValueByKey();
	}

	public Map<String, MasterPageStyle> getMasterPageStyles() {
		return this.masterPageStylesContainer.getValueByKey();
	}

	public Map<String, PageLayoutStyle> getPageLayoutStyles() {
		return this.pageLayoutStylesContainer.getValueByKey();
	}

	public Map<String, StyleTag> getStyleTagByName(final Dest dest) {
		return this.styleTagsContainer.getValueByKey(dest);
	}

	public HasFooterHeader hasFooterHeader() {
		boolean hasHeader = false;
		boolean hasFooter = false;

		for (final MasterPageStyle ps : this.masterPageStylesContainer
				.getValues()) {
			if (hasHeader && hasFooter)
				break;
			if (!hasHeader && ps.getHeader() != null)
				hasHeader = true;
			if (!hasFooter && ps.getFooter() != null)
				hasFooter = true;
		}
		return new HasFooterHeader(hasHeader, hasFooter);
	}

	private void write(final Iterable<StyleTag> iterable, final XMLUtil util,
					   final ZipUTF8Writer writer) throws IOException {
		for (final StyleTag ts : iterable)
			ts.appendXML(util, writer);
	}

	public void writeContentAutomaticStyles(final XMLUtil util,
											final ZipUTF8Writer writer) throws IOException {
		this.write(this.styleTagsContainer
				.getValues(Dest.CONTENT_AUTOMATIC_STYLES), util, writer);
	}

	public void writeDataStyles(final XMLUtil util, final ZipUTF8Writer writer)
			throws IOException {
		for (final DataStyle dataStyle : this.dataStylesContainer.getValues())
			dataStyle.appendXML(util, writer);
	}

	public void writeMasterPageStylesToAutomaticStyles(final XMLUtil util,
													   final ZipUTF8Writer writer) throws IOException {
		for (final PageLayoutStyle ps : this.pageLayoutStylesContainer
				.getValues())
			ps.appendXMLToAutomaticStyle(util, writer);
	}

	public void writeMasterPageStylesToMasterStyles(final XMLUtil util,
													final ZipUTF8Writer writer) throws IOException {
		for (final MasterPageStyle ps : this.masterPageStylesContainer
				.getValues())
			ps.appendXMLToMasterStyle(util, writer);
	}

	public void writeStylesAutomaticStyles(final XMLUtil util,
										   final ZipUTF8Writer writer) throws IOException {
		this.write(
				this.styleTagsContainer.getValues(Dest.STYLES_AUTOMATIC_STYLES),
				util, writer);
	}

	public void writeStylesCommonStyles(final XMLUtil util,
										final ZipUTF8Writer writer) throws IOException {
		this.write(this.styleTagsContainer.getValues(Dest.STYLES_COMMON_STYLES),
				util, writer);
	}

	public enum Dest {
		CONTENT_AUTOMATIC_STYLES, STYLES_AUTOMATIC_STYLES, STYLES_COMMON_STYLES,
	}
}
