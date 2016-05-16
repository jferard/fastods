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

import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Martin Schulz<br>
 * 
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *         <br>
 * 
 *         This file Content.java is part of SimpleODS.<br>
 *         0.5.1 Changed all 'throw Exception' to 'throw SimpleOdsException'
 *
 */
public class Content {
	private ObjectQueue<Table> qTables;
	private ObjectQueue<TableStyle> qTableStyles;
	private ObjectQueue<PageStyle> qPageStyles;
	private ObjectQueue<TextStyle> qTextStyles;

	public Content() {
		this.qTables = ObjectQueue.newQueue();
		this.qTableStyles = ObjectQueue.newQueue();
		this.qPageStyles = ObjectQueue.newQueue();
		this.qTextStyles = ObjectQueue.newQueue();
	}

	public void addPageStyle(final PageStyle ps) {
		ObjectQueue.addNamedElement(this.qPageStyles, ps);
	}

	public boolean addTable(String sName) throws SimpleOdsException {

		// Check if we reached the maximum number of tables
		if (this.qTables.size() >= 256) {
			throw new SimpleOdsException(
					"Maximum table number (256) reached exception");
		}

		if (ObjectQueue.findName(this.qTables, sName))
			return false;

		this.qTables.add(new Table(sName));
		return true;
	}

	public void addTableStyle(TableStyle ts) {
		ObjectQueue.addNamedElement(this.qTableStyles, ts);
	}

	public void addTextStyle(final TextStyle ts) {
		ObjectQueue.addNamedElement(this.qTextStyles, ts);
	}

	public boolean createContent(Util util, ZipOutputStream o) {

		try {
			o.putNextEntry(new ZipEntry("content.xml"));
			util.writeString(o, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			util.writeString(o,
					"<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\" xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\" xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\" xmlns:presentation=\"urn:oasis:names:tc:opendocument:xmlns:presentation:1.0\" xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\" xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" xmlns:math=\"http://www.w3.org/1998/Math/MathML\" xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\" xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\" xmlns:ooow=\"http://openoffice.org/2004/writer\" xmlns:oooc=\"http://openoffice.org/2004/calc\" xmlns:dom=\"http://www.w3.org/2001/xml-events\" xmlns:xforms=\"http://www.w3.org/2002/xforms\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" office:version=\"1.1\">");
			util.writeString(o, "<office:scripts/>");
			util.writeString(o, "<office:font-face-decls>");
			util.writeString(o,
					"<style:font-face style:name=\"Arial\" svg:font-family=\"Arial\" style:font-family-generic=\"swiss\" style:font-pitch=\"variable\"/>");
			util.writeString(o,
					"<style:font-face style:name=\"Lucida Sans Unicode\" svg:font-family=\"'Lucida Sans Unicode'\" style:font-family-generic=\"system\" style:font-pitch=\"variable\"/>");
			util.writeString(o,
					"<style:font-face style:name=\"Tahoma\" svg:font-family=\"Tahoma\" style:font-family-generic=\"system\" style:font-pitch=\"variable\"/>");
			util.writeString(o, "</office:font-face-decls>");
			/* Office automatic styles */
			util.writeString(o, "<office:automatic-styles>");

			for (TableStyle ts : this.qTableStyles)
				util.writeString(o, ts.toXML(util));

			util.writeString(o, "</office:automatic-styles>");

			util.writeString(o, "<office:body>");
			util.writeString(o, "<office:spreadsheet>");
			this.writeSpreadsheet(util, o);
			util.writeString(o, "</office:spreadsheet>");
			util.writeString(o, "</office:body>");
			util.writeString(o, "</office:document-content>");

			o.closeEntry();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
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
	 * @throws SimpleOdsException
	 */
	public TableCell getCell(int nTab, int nRow, int nCol)
			throws SimpleOdsException {
		checkTableIndex(nTab);
		Table tab = this.qTables.get(nTab);
		return tab.getCell(nRow, nCol);
	}

	public PageStyle getDefaultPageStyle() {
		if (this.qPageStyles.size() == 0) {
			return null;
		}

		return this.qPageStyles.get(0);
	}

	public ObjectQueue<PageStyle> getPageStyles() {
		return this.qPageStyles;
	}

	public ObjectQueue<Table> getTableQueue() {
		return this.qTables;
	}

	public ObjectQueue<TableStyle> getTableStyles() {
		return this.qTableStyles;
	}

	public ObjectQueue<TextStyle> getTextStyles() {
		return this.qTextStyles;
	}

	public boolean setCell(int nTab, int nRow, int nCol, int valuetype,
			String value) throws SimpleOdsException {
		checkTableIndex(nTab);
		Table tab = this.qTables.get(nTab);
		tab.setCell(nRow, nCol, valuetype, value);
		return true;
	}

	private void checkTableIndex(int nTab) throws SimpleOdsException {
		if (nTab < 0 || this.qTables.size() <= nTab) {
			throw new SimpleOdsException(
					new StringBuilder("Wrong table number [").append(nTab)
							.append("]").toString());
		}
	}

	public boolean setCellStyle(int nTab, int nRow, int nCol, TableStyle ts)
			throws SimpleOdsException {
		this.checkTableIndex(nTab);

		Table tab = this.qTables.get(nTab);
		tab.setCellStyle(nRow, nCol, ts);
		this.addTableStyle(ts);
		return true;
	}

	public boolean setColumnStyle(int nTab, int nCol, TableStyle ts)
			throws SimpleOdsException {

		if (nTab < 0 || this.qTables.size() <= nTab) {
			return false;
		}

		Table tab = this.qTables.get(nTab);
		tab.setColumnStyle(nCol, ts);
		this.addTableStyle(ts);
		return true;
	}

	public void setPageStyles(final ObjectQueue<PageStyle> qPageStyles) {
		this.qPageStyles = qPageStyles;
	}

	public void setTableStyles(final ObjectQueue<TableStyle> qTableStyles) {
		this.qTableStyles = qTableStyles;
	}

	public void setTextStyles(final ObjectQueue<TextStyle> qTextStyles) {
		this.qTextStyles = qTextStyles;
	}

	private boolean writeSpreadsheet(Util util, ZipOutputStream o) {
		TableStyle ts0 = null;
		TableStyle ts1 = null;
		String sDefaultCellSytle0 = "Default";
		String sDefaultCellSytle1 = "Default";

		// Loop through all tables and write the informations
		for (Table tab : this.qTables) {
			util.writeString(o, tab.toXML());
			util.writeString(o,
					"<office:forms form:automatic-focus=\"false\" form:apply-design-mode=\"false\"/>");

			// Loop through all table column styles and write the informations
			String sSytle0 = "co1";
			String sSytle1 = "co1";

			// If there is only one column style in column one, just write this
			// info to OutputStream o
			if (tab.getColumnStyles().size() == 1) {
				ts0 = tab.getColumnStyles().get(0);
				util.writeString(o,
						new StringBuilder(
								"<table:table-column table:style-name=\"")
										.append(ts0.getName())
										.append("\" table:number-columns-repeated=\"")
										.append(1)
										.append("\" table:default-cell-style-name=\"")
										.append(ts0.getDefaultCellStyle())
										.append("\"/>").toString());

			}

			// If there is more than one column with a style, loop through all
			// styles and
			// write the info to OutputStream o
			if (tab.getColumnStyles().size() > 1) {
				int nCount = 1;
				
				Iterator<TableStyle> iterator = tab.getColumnStyles().iterator();
				ts1 = iterator.next();
				while (iterator.hasNext()) {
					ts0 = ts1;
					ts1 = iterator.next();

					if (ts0 == null) {
						sSytle0 = "co1";
						sDefaultCellSytle1 = "Default";
					} else {
						sSytle0 = ts0.getName();
						sDefaultCellSytle1 = ts0.getDefaultCellStyle();
					}
					if (ts1 == null) {
						sSytle1 = "co1";
						sDefaultCellSytle0 = "Default";
					} else {
						sSytle1 = ts1.getName();
						sDefaultCellSytle0 = ts1.getDefaultCellStyle();
					}

					if (sSytle0.equalsIgnoreCase(sSytle1)) {
						nCount++;
					} else {
						util.writeString(o,
								new StringBuilder(
										"<table:table-column table:style-name=\"")
												.append(sSytle0)
												.append("\" table:number-columns-repeated=\"")
												.append(nCount)
												.append("\" table:default-cell-style-name=\"")
												.append(sDefaultCellSytle1)
												.append("\"/>").toString());

						nCount = 1;
					}

				}
				util.writeString(o,
						new StringBuilder(
								"<table:table-column table:style-name=\"")
										.append(sSytle1)
										.append("\" table:number-columns-repeated=\"")
										.append(nCount)
										.append("\" table:default-cell-style-name=\"")
										.append(sDefaultCellSytle0)
										.append("\"/>").toString());

			}

			// Loop through all rows
			for (TableRow tr : tab.getRows()) {
				if (tr != null) {
					String[] sToPrint = tr.toXML(util);
					util.writeStringArray(o, sToPrint);
				} else {
					// Empty TableRow
					util.writeString(o,
							"<table:table-row table:style-name=\"ro1\">");
					util.writeString(o,
							new StringBuilder(
									"<table:table-cell table:number-columns-repeated=\"")
											.append(tab.getLastCol())
											.append("\"/>").toString());
					util.writeString(o, "</table:table-row>");

				}
			}
			util.writeString(o, "</table:table>");
		}

		return true;
	}
}
