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
import java.util.zip.ZipEntry;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

/**
 * styles.xml/office:document-styles
 *
 * @author Julien Férard
 * @author Martin Schulz
 *
 */
public class StylesEntry implements OdsEntry {
	private static void appendDefaultFooterHeaderStyle(final XMLUtil util,
			final Appendable appendable, final String name) throws IOException {
		appendable.append("<style:style");
		util.appendEAttribute(appendable, "style:name", name);
		util.appendEAttribute(appendable, "style:family", "paragraph");
		util.appendEAttribute(appendable, "style:parent-style-name",
				"Standard");
		util.appendEAttribute(appendable, "style:class", "extra");
		appendable.append("><style:paragraph-properties");
		util.appendEAttribute(appendable, "text:number-lines", false);
		util.appendEAttribute(appendable, "text:line-number", 0);
		appendable.append("/></style:style>");
	}

	private final Map<String, DataStyle> dataStyles;
	private final Map<String, PageStyle> pageStyles;
	private final Map<String, TextStyle> textStyles;
	private final Map<String, StyleTag> styleTagByName;

	/**
	 */
	public StylesEntry() {
		this.dataStyles = new HashMap<String, DataStyle>();
		this.pageStyles = new HashMap<String, PageStyle>();
		this.textStyles = new HashMap<String, TextStyle>();
		this.styleTagByName = new HashMap<String, StyleTag>();
	}

	public enum Mode {
		CREATE, UPDATE, UPDATE_IF_EXISTS;
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

	/**
	 * Add a DataStyle, if a DataStyle with this name already exist, the old one
	 * is replaced.
	 *
	 * @param dataStyle
	 *            - The data style to be added.
	 */
	public void addDataStyle(final DataStyle dataStyle) {
		final String name = dataStyle.getName();
		this.dataStyles.put(name, dataStyle);
	}

	/**
	 * Add a DataStyle, if a DataStyle with this name already exist, the old one
	 * is replaced.
	 *
	 * @param dataStyle
	 *            - The data style to be added.
	 */
	public boolean addDataStyle(final DataStyle dataStyle, Mode mode) {
		final String key = dataStyle.getName();
		switch (mode) {
		case CREATE:
			if (this.dataStyles.containsKey(key))
				return false;
			break;
		case UPDATE:
			if (!this.dataStyles.containsKey(key))
				return false;
			break;
		default:
			break;
		}
		this.dataStyles.put(key, dataStyle);
		return true;
	}

	/**
	 * Add a DataStyle, if a DataStyle with this name already exist, the old one
	 * is replaced.
	 *
	 * @param dataStyle
	 *            - The data style to be added.
	 */
	public void addPageStyle(final PageStyle ps) {
		final String key = ps.getName();
		this.pageStyles.put(key, ps);
		ps.addEmbeddedStylesToStylesEntry(this);
	}

	/**
	 * Add a DataStyle, if a DataStyle with this name already exist, the old one
	 * is replaced.
	 *
	 * @param dataStyle
	 *            - The data style to be added.
	 * @return
	 */
	public boolean addPageStyle(final PageStyle ps, Mode mode) {
		final String key = ps.getName();
		switch (mode) {
		case CREATE:
			if (this.pageStyles.containsKey(key))
				return false;
			break;
		case UPDATE:
			if (!this.pageStyles.containsKey(key))
				return false;
			break;
		default:
			break;
		}
		this.pageStyles.put(key, ps);
		ps.addEmbeddedStylesToStylesEntry(this, mode);
		return true;
	}

	@Override
	public void write(final XMLUtil util, final ZipUTF8Writer writer)
			throws IOException {
		writer.putNextEntry(new ZipEntry("styles.xml"));
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.write(
				"<office:document-styles xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\" xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\" xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\" xmlns:presentation=\"urn:oasis:names:tc:opendocument:xmlns:presentation:1.0\" xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\" xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" xmlns:math=\"http://www.w3.org/1998/Math/MathML\" xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\" xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\" xmlns:ooow=\"http://openoffice.org/2004/writer\" xmlns:oooc=\"http://openoffice.org/2004/calc\" xmlns:dom=\"http://www.w3.org/2001/xml-events\" office:version=\"1.1\">");
		writer.write("<office:font-face-decls>");
		writer.write(
				"<style:font-face style:name=\"Arial\" svg:font-family=\"Arial\" style:font-family-generic=\"swiss\" style:font-pitch=\"variable\"/>");
		writer.write(
				"<style:font-face style:name=\"Lucida Sans Unicode\" svg:font-family=\"&apos;Lucida Sans Unicode&apos;\" style:font-family-generic=\"system\" style:font-pitch=\"variable\"/>");
		writer.write(
				"<style:font-face style:name=\"Tahoma\" svg:font-family=\"Tahoma\" style:font-family-generic=\"system\" style:font-pitch=\"variable\"/>");
		writer.write("</office:font-face-decls>");
		writer.write("<office:styles>");

		for (final DataStyle bs : this.dataStyles.values())
			bs.appendXMLToStylesEntry(util, writer);

		boolean hasHeader = false;
		boolean hasFooter = false;
		for (final PageStyle ps : this.pageStyles.values()) {
			if (hasHeader && hasFooter)
				break;
			if (!hasHeader && ps.getHeader() != null)
				hasHeader = true;
			if (!hasFooter && ps.getFooter() != null)
				hasFooter = true;
		}

		if (hasHeader) {
			StylesEntry.appendDefaultFooterHeaderStyle(util, writer, "Header");
		}
		if (hasFooter) {
			StylesEntry.appendDefaultFooterHeaderStyle(util, writer, "Footer");
		}

		for (final StyleTag ts : this.styleTagByName.values())
			ts.appendXMLToStylesEntry(util, writer);

		for (final TextStyle ts : this.textStyles.values())
			if (ts.isNotEmpty())
				ts.appendXMLToStylesEntry(util, writer);

		writer.write("</office:styles>");
		writer.write("<office:automatic-styles>");

		for (final PageStyle ps : this.pageStyles.values())
			ps.appendXMLToAutomaticStyle(util, writer);

		writer.write("</office:automatic-styles>");
		writer.write("<office:master-styles>");

		for (final PageStyle ps : this.pageStyles.values())
			ps.appendXMLToMasterStyle(util, writer);

		writer.write("</office:master-styles>");
		writer.write("</office:document-styles>");
		writer.flush();
		writer.closeEntry();
	}

}
