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

	private Util u = Util.getInstance();
	private ObjectQueue<Table> qTables = ObjectQueue.newQueue();
	private ObjectQueue<TableStyle> qTableStyles = ObjectQueue.newQueue();
	private ObjectQueue<PageStyle> qPageStyles = ObjectQueue.newQueue();
	private ObjectQueue<TextStyle> qTextStyles = ObjectQueue.newQueue();

	public Content() {
	}

	public ObjectQueue<Table> getTableQueue() {
		return this.qTables;
	}

	public boolean addTable(String sName) throws SimpleOdsException {

		// Check if we reached the maximum number of tables
		if (this.qTables.size() >= 256) {
			throw new SimpleOdsException(
					"Maximum table number (256) reached exception");
		}

		for (int n = 0; n < this.qTables.size(); n++) {
			Table tab = this.qTables.get(n);
			if (tab.getName().equalsIgnoreCase(sName))
				return false;
		}

		this.qTables.add(new Table(sName));
		return true;
	}

	private boolean getContent(ZipOutputStream o) {
		TableStyle ts0 = null;
		TableStyle ts1 = null;
		String sDefaultCellSytle0 = "Default";
		String sDefaultCellSytle1 = "Default";

		// Loop through all tables and write the informations
		for (int n = 0; n < this.qTables.size(); n++) {
			Table tab = this.qTables.get(n);
			this.u.writeString(o, tab.toXML());
			this.u.writeString(o,
					"<office:forms form:automatic-focus=\"false\" form:apply-design-mode=\"false\"/>");

			// Loop through all table column styles and write the informations
			String sSytle0 = "co1";
			String sSytle1 = "co1";

			// If there is only one column style in column one, just write this
			// info to OutputStream o
			if (tab.getColumnStyles().size() == 1) {
				ts0 = tab.getColumnStyles().get(0);
				this.u.writeString(o, "<table:table-column table:style-name=\""
						+ ts0.getName() + "\" table:number-columns-repeated=\""
						+ 1 + "\" table:default-cell-style-name=\""
						+ ts0.getDefaultCellStyle() + "\"/>");

			}

			// If there is more than one column with a style, loop through all
			// styles and
			// write the info to OutputStream o
			if (tab.getColumnStyles().size() > 1) {
				int nCount = 1;
				for (int x = 1; x < tab.getColumnStyles().size(); x++) {

					ts0 = tab.getColumnStyles().get(x - 1);
					ts1 = tab.getColumnStyles().get(x);

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
						this.u.writeString(o,
								"<table:table-column table:style-name=\""
										+ sSytle0
										+ "\" table:number-columns-repeated=\""
										+ nCount
										+ "\" table:default-cell-style-name=\""
										+ sDefaultCellSytle1 + "\"/>");

						nCount = 1;
					}

				}
				this.u.writeString(o,
						"<table:table-column table:style-name=\"" + sSytle1
								+ "\" table:number-columns-repeated=\"" + nCount
								+ "\" table:default-cell-style-name=\""
								+ sDefaultCellSytle0 + "\"/>");

			}

			// Loop through all rows
			for (int r = 0; r < tab.getRows().size(); r++) {
				TableRow tr = tab.getRows().get(r);

				if (tr != null) {
					String[] sToPrint = tr.toXML();
					this.u.writeStringArray(o, sToPrint);
				} else {
					// Empty TableRow
					this.u.writeString(o,
							"<table:table-row table:style-name=\"ro1\">");
					this.u.writeString(o,
							"<table:table-cell table:number-columns-repeated=\""
									+ tab.getLastCol() + "\"/>");
					this.u.writeString(o, "</table:table-row>");

				}
			}
			this.u.writeString(o, "</table:table>");
		}

		return true;
	}

	public boolean createContent(ZipOutputStream o) {

		try {
			o.putNextEntry(new ZipEntry("content.xml"));
			this.u.writeString(o, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			this.u.writeString(o,
					"<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\" xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\" xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\" xmlns:presentation=\"urn:oasis:names:tc:opendocument:xmlns:presentation:1.0\" xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\" xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" xmlns:math=\"http://www.w3.org/1998/Math/MathML\" xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\" xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\" xmlns:ooow=\"http://openoffice.org/2004/writer\" xmlns:oooc=\"http://openoffice.org/2004/calc\" xmlns:dom=\"http://www.w3.org/2001/xml-events\" xmlns:xforms=\"http://www.w3.org/2002/xforms\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" office:version=\"1.1\">");
			this.u.writeString(o, "<office:scripts/>");
			this.u.writeString(o, "<office:font-face-decls>");
			this.u.writeString(o,
					"<style:font-face style:name=\"Arial\" svg:font-family=\"Arial\" style:font-family-generic=\"swiss\" style:font-pitch=\"variable\"/>");
			this.u.writeString(o,
					"<style:font-face style:name=\"Lucida Sans Unicode\" svg:font-family=\"'Lucida Sans Unicode'\" style:font-family-generic=\"system\" style:font-pitch=\"variable\"/>");
			this.u.writeString(o,
					"<style:font-face style:name=\"Tahoma\" svg:font-family=\"Tahoma\" style:font-family-generic=\"system\" style:font-pitch=\"variable\"/>");
			this.u.writeString(o, "</office:font-face-decls>");
			/* Office automatic styles */
			this.u.writeString(o, "<office:automatic-styles>");

			for (int n = 0; n < this.qTableStyles.size(); n++) {
				TableStyle ts = this.qTableStyles.get(n);
				this.u.writeString(o, ts.toXML());
			}
			this.u.writeString(o, "</office:automatic-styles>");

			this.u.writeString(o, "<office:body>");
			this.u.writeString(o, "<office:spreadsheet>");
			this.getContent(o);
			this.u.writeString(o, "</office:spreadsheet>");
			this.u.writeString(o, "</office:body>");
			this.u.writeString(o, "</office:document-content>");

			o.closeEntry();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean setCell(int nTab, int nRow, int nCol, int valuetype,
			String value) throws SimpleOdsException {

		if (nTab < 0 || nTab >= this.qTables.size()) {
			throw new SimpleOdsException("Wrong table number [" + nTab + "]");
		}

		Table tab = this.qTables.get(nTab);
		tab.setCell(nRow, nCol, valuetype, value);

		return true;
	}

	public boolean setCellStyle(int nTab, int nRow, int nCol, TableStyle ts)
			throws SimpleOdsException {

		if (nTab < 0 || nTab >= this.qTables.size()) {
			throw new SimpleOdsException("Wrong table number [" + nTab + "]");
		}

		Table tab = this.qTables.get(nTab);

		tab.setCellStyle(nRow, nCol, ts);
		this.addTableStyle(ts);

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
		if (nTab < 0 || nTab >= this.qTables.size()) {
			throw new SimpleOdsException("Wrong table number [" + nTab + "]");
		}
		Table tab = this.qTables.get(nTab);
		return tab.getCell(nRow, nCol);

	}

	public boolean setColumnStyle(int nTab, int nCol, TableStyle ts)
			throws SimpleOdsException {

		if (nTab < 0 || nTab >= this.qTables.size()) {
			return false;
		}

		Table tab = this.qTables.get(nTab);

		tab.setColumnStyle(nCol, ts);

		this.addTableStyle(ts);

		return true;
	}

	public PageStyle getDefaultPageStyle() {
		if (this.qPageStyles.size() == 0) {
			return null;
		}

		return this.qPageStyles.get(0);
	}

	public void addTableStyle(TableStyle ts) {
		int x = 0;
		for (x = 0; x < this.qTableStyles.size(); x++) {
			TableStyle tabStyle = this.qTableStyles.get(x);
			if (tabStyle.getName().equalsIgnoreCase(ts.getName())) {
				this.qTableStyles.setAt(x, ts);
				return;
			}
		}

		// Did not find it in qTableStyle, make a new entry
		this.qTableStyles.add(ts);
	}

	public void addPageStyle(final PageStyle ps) {
		int x = 0;
		for (x = 0; x < this.qPageStyles.size(); x++) {
			PageStyle pStyle = this.qPageStyles.get(x);
			if (pStyle.getName().equalsIgnoreCase(ps.getName())) {
				this.qPageStyles.setAt(x, ps);
				return;
			}
		}

		// Did not find it in qPageStyle, make a new entry
		this.qPageStyles.add(ps);
	}

	public void addTextStyle(final TextStyle ts) {
		int x = 0;
		for (x = 0; x < this.qTextStyles.size(); x++) {
			TextStyle tStyle = this.qTextStyles.get(x);
			if (tStyle.getName().equalsIgnoreCase(ts.getName())) {
				this.qTextStyles.setAt(x, ts);
				return;
			}
		}

		// Did not find it in qTextStyles, make a new entry
		this.qTextStyles.add(ts);
	}

	public ObjectQueue<TableStyle> getTableStyles() {
		return this.qTableStyles;
	}

	public void setTableStyles(final ObjectQueue<TableStyle> qTableStyles) {
		this.qTableStyles = qTableStyles;
	}

	public ObjectQueue<PageStyle> getPageStyles() {
		return this.qPageStyles;
	}

	public void setPageStyles(final ObjectQueue<PageStyle> qPageStyles) {
		this.qPageStyles = qPageStyles;
	}

	public ObjectQueue<TextStyle> getTextStyles() {
		return this.qTextStyles;
	}

	public void setTextStyles(final ObjectQueue<TextStyle> qTextStyles) {
		this.qTextStyles = qTextStyles;
	}

}
