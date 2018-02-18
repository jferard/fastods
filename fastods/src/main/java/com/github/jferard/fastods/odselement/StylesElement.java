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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.zip.ZipEntry;

/**
 * styles.xml/office:document-styles
 *
 * @author Julien Férard
 * @author Martin Schulz
 *
 */
public class StylesElement implements OdsElement {
	private static void appendDefaultFooterHeaderStyle(final XMLUtil util,
			final Appendable appendable, final String name) throws IOException {
		appendable.append("<style:style");
		util.appendEAttribute(appendable, "style:name", name);
		util.appendAttribute(appendable, "style:family", "paragraph");
		util.appendAttribute(appendable, "style:parent-style-name",
				"Standard");
		util.appendAttribute(appendable, "style:class", "extra");
		appendable.append("><style:paragraph-properties");
		util.appendAttribute(appendable, "text:number-lines", false);
		util.appendAttribute(appendable, "text:line-number", 0);
		appendable.append("/></style:style>");
	}

	private final StylesContainer stylesContainer;

	/**
	 * @param stylesContainer the container for all styles
	 */
	public StylesElement(final StylesContainer stylesContainer) {
		this.stylesContainer = stylesContainer;
	}

	/**
	 * @return the container of the styles
	 */
	public StylesContainer getStyleTagsContainer() {
		return this.stylesContainer;
	}

	@Override
	public void write(final XMLUtil util, final ZipUTF8Writer writer)
			throws IOException {
		final HasFooterHeader hasFooterHeader = this.stylesContainer
				.hasFooterHeader();

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

		this.stylesContainer.writeStylesCommonStyles(util, writer); // table-cell

		if (hasFooterHeader.hasHeader()) {
			StylesElement.appendDefaultFooterHeaderStyle(util, writer, "Header");
		}
		if (hasFooterHeader.hasFooter()) {
			StylesElement.appendDefaultFooterHeaderStyle(util, writer, "Footer");
		}

		writer.write("</office:styles>");
		writer.write("<office:automatic-styles>");

		this.stylesContainer.writeStylesAutomaticStyles(util, writer);
		this.stylesContainer.writePageLayoutStylesToAutomaticStyles(util,
				writer);

		writer.write("</office:automatic-styles>");
		writer.write("<office:master-styles>");

		this.stylesContainer.writeMasterPageStylesToMasterStyles(util, writer);

		writer.write("</office:master-styles>");
		writer.write("</office:document-styles>");
		writer.flush();
		writer.closeEntry();
	}
}
