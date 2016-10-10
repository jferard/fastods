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
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.util.UniqueList;
import com.github.jferard.fastods.util.Util;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * content.xml/office:document-content
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
class ContentEntry implements OdsEntry {
	private final DataStyles format;
	private final OdsFile odsFile;
	private final Map<String, StyleTag> styleTagByName;
	private final UniqueList<Table> tables;
	private final Util util;
	private final XMLUtil xmlUtil;

	ContentEntry(final OdsFile odsFile, final XMLUtil xmlUtil, final Util util,
			final DataStyles format) {
		this.odsFile = odsFile;
		this.xmlUtil = xmlUtil;
		this.util = util;
		this.format = format;
		this.tables = new UniqueList<Table>();
		this.styleTagByName = new HashMap<String, StyleTag>();
	}

	/**
	 * @param name
	 *            the name of the table to create
	 * @param columnCapacity
	 * @param rowCapacity
	 * @return the table (whether it existed before call or not). Never null
	 */
	public Table addTable(final String name, final int rowCapacity,
			final int columnCapacity) {
		Table table = this.tables.getByName(name);
		if (table == null) {
			table = new Table(this.odsFile, this.xmlUtil, this.util,
					this.format, name, rowCapacity, columnCapacity);
			this.tables.add(table);
		}
		return table;
	}

	// /**
	// * Get the OldHeavyTableCell object from table tab at position
	// row,col.<br>
	// * If no OldHeavyTableCell was present at this row,col, create a new one
	// with a
	// * default of OldHeavyTableCell.STYLE_STRING and a content of ""<br>
	// *
	// * @param tab
	// * @param row
	// * @param col
	// * @return The OldHeavyTableCell
	// * @throws FastOdsException
	// */
	// @Deprecated
	// public OldHeavyTableCell getCell(final int tab, final int row, final int
	// col)
	// throws FastOdsException {
	// this.checkTableIndex(tab);
	// final Table tab = this.tables.get(tab);
	// return tab.getCell(row, col);
	// }

	public Table getTable(final int tableIndex) {
		return this.tables.get(tableIndex);
	}

	/**
	 * @param name
	 *            the name of the table to find
	 * @return the table, or null if none present
	 */
	public Table getTable(final String name) {
		return this.tables.getByName(name);
	}

	public int getTableCount() {
		return this.tables.size();
	}

	/**
	 * @return the list of tables
	 */
	public List<Table> getTables() {
		return this.tables;
	}

	@Override
	public void write(final XMLUtil util, final ZipOutputStream zipOut,
			final Writer writer) throws IOException {
		zipOut.putNextEntry(new ZipEntry("content.xml"));
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.write(
				"<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\" xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\" xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\" xmlns:presentation=\"urn:oasis:names:tc:opendocument:xmlns:presentation:1.0\" xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\" xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" xmlns:math=\"http://www.w3.org/1998/Math/MathML\" xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\" xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\" xmlns:ooow=\"http://openoffice.org/2004/writer\" xmlns:oooc=\"http://openoffice.org/2004/calc\" xmlns:dom=\"http://www.w3.org/2001/xml-events\" xmlns:xforms=\"http://www.w3.org/2002/xforms\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" office:version=\"1.1\">");
		writer.write("<office:scripts/>");
		writer.write("<office:font-face-decls>");
		writer.write(
				"<style:font-face style:name=\"Arial\" svg:font-family=\"Arial\" style:font-family-generic=\"swiss\" style:font-pitch=\"variable\"/>");
		writer.write(
				"<style:font-face style:name=\"Lucida Sans Unicode\" svg:font-family=\"'Lucida Sans Unicode'\" style:font-family-generic=\"system\" style:font-pitch=\"variable\"/>");
		writer.write(
				"<style:font-face style:name=\"Tahoma\" svg:font-family=\"Tahoma\" style:font-family-generic=\"system\" style:font-pitch=\"variable\"/>");
		writer.write("</office:font-face-decls>");
		/* Office automatic styles */
		writer.write("<office:automatic-styles>");

		for (final StyleTag ts : this.styleTagByName.values())
			ts.appendXMLToContentEntry(util, writer);

		writer.write("</office:automatic-styles>");

		writer.write("<office:body>");
		writer.write("<office:spreadsheet>");
		for (final Table table : this.tables)
			table.appendXMLToContentEntry(util, writer);
		writer.write("</office:spreadsheet>");
		writer.write("</office:body>");
		writer.write("</office:document-content>");
		writer.flush();
		zipOut.closeEntry();
	}

	private void checkTableIndex(final int tab) throws FastOdsException {
		if (tab < 0 || this.tables.size() <= tab) {
			throw new FastOdsException(new StringBuilder("Wrong table number [")
					.append(tab).append("]").toString());
		}
	}

	void addStyleTag(final StyleTag styleTag) {
		final String name = styleTag.getName();
		if (this.styleTagByName.containsKey(name))
			return;

		this.styleTagByName.put(name, styleTag);
	}
}
