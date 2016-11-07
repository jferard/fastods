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
package com.github.jferard.fastods;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.github.jferard.fastods.datastyle.DataStyle;
import com.github.jferard.fastods.style.FHTextStyle;
import com.github.jferard.fastods.style.PageStyle;
import com.github.jferard.fastods.util.XMLUtil;

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
	private final Map<String, FHTextStyle> textStyles;

	/**
	 */
	public StylesEntry() {
		this.dataStyles = new HashMap<String, DataStyle>();
		this.pageStyles = new HashMap<String, PageStyle>();
		this.textStyles = new HashMap<String, FHTextStyle>();
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
		if (this.dataStyles.containsKey(name))
			return;

		this.dataStyles.put(name, dataStyle);
	}

	public void addPageStyle(final PageStyle ps) {
		final String name = ps.getName();
		if (this.pageStyles.containsKey(name))
			return;

		this.pageStyles.put(name, ps);
	}

	public void addTextStyle(final FHTextStyle ts) {
		final String name = ts.getName();
		if (this.textStyles.containsKey(name))
			return;

		this.textStyles.put(name, ts);
	}

	@Override
	public void write(final XMLUtil util, final ZipOutputStream zipOut,
			final Writer writer) throws IOException {
		zipOut.putNextEntry(new ZipEntry("styles.xml"));
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

		writer.write("</office:styles>");
		writer.write("<office:automatic-styles>");

		/*
		u.writeString(out, "<number:date-style style:name=\"N01\" number:automatic-order=\"true\">");
		u.writeString(out, "<number:day number:style=\"long\"/>");
		u.writeString(out, "<number:text>.</number:text>");
		u.writeString(out, "<number:month number:style=\"long\"/>");
		u.writeString(out, "<number:text>.</number:text>");
		u.writeString(out, "<number:year/>");
		u.writeString(out, "</number:date-style>");
		*/

		for (final PageStyle ps : this.pageStyles.values())
			ps.appendXMLToAutomaticStyle(util, writer);

		for (final FHTextStyle ts : this.textStyles.values())
			if (ts.isNotEmpty())
				ts.appendXMLToStylesEntry(util, writer);

		writer.write("</office:automatic-styles>");
		writer.write("<office:master-styles>");

		for (final PageStyle ps : this.pageStyles.values())
			ps.appendXMLToMasterStyle(util, writer);

		writer.write("</office:master-styles>");
		writer.write("</office:document-styles>");
		writer.flush();
		zipOut.closeEntry();
	}

}
