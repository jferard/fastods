/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
*    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.jferard.fastods;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.github.jferard.fastods.TableCell.Type;
import com.google.common.base.Optional;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file ContentEntry.java is part of SimpleODS.<br>
 *         SimpleODS 0.5.1 Changed all 'throw Exception' to 'throw
 *         FastOdsException'
 *
 *         content.xml/office:document-content
 */
class ContentEntry implements OdsEntry {
	/*	private Map<String, PageStyle> qPageStyles;
		private Map<String, TextStyle> qTextStyles; */
	private final OdsFile odsFile;
	private final List<Table> qTables;
	private final Map<String, StyleTag> styleTagByName;

	ContentEntry(final OdsFile odsFile) {
		this.odsFile = odsFile;
		this.qTables = new LinkedList<Table>();
		this.styleTagByName = new HashMap<String, StyleTag>();
		/*		this.qPageStyles = ObjectQueue.newQueue();
				this.qTextStyles = ObjectQueue.newQueue(); */
	}

	/*
	public void addPageStyle(final PageStyle ps) {
		ObjectQueue.addOrReplaceNamedElement(this.qPageStyles, ps);
	}*/

	public void addStyleTag(final StyleTag styleTag) {
		this.styleTagByName.put(styleTag.getName(), styleTag);
	}

	public Optional<Table> addTable(final String sName) {
		Optional<Table> optTable = this.getTable(sName);
		if (optTable.isPresent())
			optTable = Optional.absent();
		else {
			final Table table = new Table(this.odsFile, sName);
			this.qTables.add(table);
			optTable = Optional.of(table);
		}
		return optTable;
	}

	/**
	 * Get the TableCell object from table nTab at position nRow,nCol.<br>
	 * If no TableCell was present at this nRow,nCol, create a new one with a
	 * default of TableCell.STYLE_STRING and a content of ""<br>
	 *
	 * @param nTab
	 * @param nRow
	 * @param nCol
	 * @return The TableCell
	 * @throws FastOdsException
	 */
	public TableCell getCell(final int nTab, final int nRow, final int nCol)
			throws FastOdsException {
		this.checkTableIndex(nTab);
		final Table tab = this.qTables.get(nTab);
		return tab.getCell(nRow, nCol);
	}

	/*
	public void addTextStyle(final TextStyle ts) {
		ObjectQueue.addOrReplaceNamedElement(this.qTextStyles, ts);
	}*/

	public Map<String, StyleTag> getStyleTagByName() {
		return this.styleTagByName;
	}

	/*
	public PageStyle getDefaultPageStyle() {
		if (this.qPageStyles.size() == 0) {
			return null;
		}

		return this.qPageStyles.get(0);
	}

	public ObjectQueue<PageStyle> getPageStyles() {
		return this.qPageStyles;
	}*/

	public Optional<Table> getTable(final String sName) {
		return Util.findElementByName(this.qTables, sName);
	}

	public List<Table> getTables() {
		return this.qTables;
	}

	/*
	public ObjectQueue<TextStyle> getTextStyles() {
		return this.qTextStyles;
	}*/

	public boolean setCell(final int nTab, final int nRow, final int nCol,
			final Type type, final String value) throws FastOdsException {
		this.checkTableIndex(nTab);
		final Table tab = this.qTables.get(nTab);
		tab.setCell(nRow, nCol, type, value);
		return true;
	}

	public boolean setCellStyle(final int nTab, final int nRow, final int nCol,
			final TableCellStyle ts) throws FastOdsException {
		this.checkTableIndex(nTab);

		final Table tab = this.qTables.get(nTab);
		tab.setCellStyle(nRow, nCol, ts);
		// this.addTableStyle(ts);
		return true;
	}

	public boolean setColumnStyle(final int nTab, final int nCol,
			final TableColumnStyle ts) throws FastOdsException {

		if (nTab < 0 || this.qTables.size() <= nTab) {
			return false;
		}

		final Table tab = this.qTables.get(nTab);
		tab.setColumnStyle(nCol, ts);
		// this.addTableStyle(ts);
		return true;
	}

	/*
	public void setPageStyles(final ObjectQueue<PageStyle> qPageStyles) {
		this.qPageStyles = qPageStyles;
	}*/

	/*
	public void setTableStyles(final ObjectQueue<NamedObject> qTableStyles) {
		this.tableStyleByName = qTableStyles;
	}

	public void setTextStyles(final ObjectQueue<TextStyle> qTextStyles) {
		this.qTextStyles = qTextStyles;
	}*/

	public void setColumnStyle(final Table table, final int nCol,
			final TableColumnStyle ts) throws FastOdsException {
		table.setColumnStyle(nCol, ts);
	}

	@Override
	public void write(final Util util, final ZipOutputStream zipOut, final Writer writer)
			throws IOException {
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
		for (final Table tab : this.qTables)
			tab.appendXMLToContentEntry(util, writer);
		writer.write("</office:spreadsheet>");
		writer.write("</office:body>");
		writer.write("</office:document-content>");
		writer.flush();
		zipOut.closeEntry();
	}

	private void checkTableIndex(final int nTab) throws FastOdsException {
		if (nTab < 0 || this.qTables.size() <= nTab) {
			throw new FastOdsException(new StringBuilder("Wrong table number [")
					.append(nTab).append("]").toString());
		}
	}
}
