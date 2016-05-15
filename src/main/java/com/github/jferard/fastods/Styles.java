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
 *         This file Styles.java is part of SimpleODS.<br>
 *         0.5.1 Added support for DateStyle
 *
 */
public class Styles {
	private ObjectQueue<NumberStyle> qNumberStyles = ObjectQueue.newQueue();
	private ObjectQueue<CurrencyStyle> qCurrencyStyles = ObjectQueue.newQueue();
	private ObjectQueue<PageStyle> qPageStyles = ObjectQueue.newQueue();
	private ObjectQueue<TextStyle> qTextStyles = ObjectQueue.newQueue();
	private ObjectQueue<DateStyle> qDateStyles = ObjectQueue.newQueue();
	private Header header = null;
	private Footer footer = null;

	/**
	 * The OdsFile where this object belong to.
	 */
	private OdsFile o;

	/**
	 * @param odsFile
	 *            - The OdsFile where the styles belong to
	 */
	public Styles(final OdsFile odsFile) {
		this.o = odsFile;
	}

	/**
	 * Add a DateStyle, if a DateStyle with this name already exist, the old one
	 * is replaced.
	 * 
	 * @param ds
	 *            - The date style to be added.
	 */
	public void addDateStyle(final DateStyle ds) {

		// --------------------------------------------------------------
		// Check is a style with this name exists and replace if yes
		// --------------------------------------------------------------
		int x = 0;
		for (x = 0; x < this.qDateStyles.size(); x++) {
			DateStyle dateStyle = this.qDateStyles.get(x);
			if (dateStyle.getName().equalsIgnoreCase(ds.getName())) {
				this.qDateStyles.setAt(x, ds);
				return;
			}
		}

		// --------------------------------------------------------------
		// We did not find it in qDateStyles, make a new entry
		// --------------------------------------------------------------
		this.qDateStyles.add(ds);

	}

	/**
	 * Add a NumberStyle, if a NumberStyle with this name already exist, the old
	 * one is replaced.
	 * 
	 * @param ns
	 *            - The number style to be added.
	 */
	public void addNumberStyle(final NumberStyle ns) {

		// --------------------------------------------------------------
		// Check is a style with this name exists and replace if yes
		// --------------------------------------------------------------
		int x = 0;
		for (x = 0; x < this.qNumberStyles.size(); x++) {
			NumberStyle numStyle = this.qNumberStyles.get(x);
			if (numStyle.getName().equalsIgnoreCase(ns.getName())) {
				this.qNumberStyles.setAt(x, ns);
				return;
			}
		}

		// --------------------------------------------------------------
		// We did not find it in qNumberStyles, make a new entry
		// --------------------------------------------------------------
		this.qNumberStyles.add(ns);

	}

	/**
	 * Add a CurrencyStyle, if a CurrencyStyle with this name already exist, the
	 * old one is replaced.
	 * 
	 * @param cs
	 *            - The currency style to be added.
	 */
	public void addCurrencyStyle(CurrencyStyle cs) {

		// --------------------------------------------------------------
		// Check is a style with this name exists and replace if yes
		// --------------------------------------------------------------
		int x = 0;
		for (x = 0; x < this.qCurrencyStyles.size(); x++) {
			CurrencyStyle cStyle = this.qCurrencyStyles.get(x);
			if (cStyle.getName().equalsIgnoreCase(cs.getName())) {
				this.qCurrencyStyles.setAt(x, cs);
				return;
			}
		}

		// --------------------------------------------------------------
		// We did not find it in qCurrencyStyles, make a new entry
		// --------------------------------------------------------------
		this.qCurrencyStyles.add(cs);
	}

	public void addPageStyle(PageStyle ps) {

		// --------------------------------------------------------------
		// Check is a style with this name exists and replace if yes
		// --------------------------------------------------------------
		int x = 0;
		for (x = 0; x < this.qPageStyles.size(); x++) {
			PageStyle pStyle = this.qPageStyles.get(x);
			if (pStyle.getName().equalsIgnoreCase(ps.getName())) {
				this.qPageStyles.setAt(x, ps);
				return;
			}
		}

		// --------------------------------------------------------------
		// We did not find it in qPageStyles, make a new entry
		// --------------------------------------------------------------
		this.qPageStyles.add(ps);
		this.o.getContent().addPageStyle(ps);

	}

	public void addTextStyle(TextStyle ts) {

		// --------------------------------------------------------------
		// Check is a style with this name exists and replace if yes
		// --------------------------------------------------------------
		int x = 0;
		for (x = 0; x < this.qTextStyles.size(); x++) {
			TextStyle tStyle = this.qTextStyles.get(x);
			if (tStyle.getName().equalsIgnoreCase(ts.getName())) {
				this.qTextStyles.setAt(x, ts);
				return;
			}
		}

		// --------------------------------------------------------------
		// We did not find it in qTextStyles, make a new entry
		// --------------------------------------------------------------
		this.qTextStyles.add(ts);
		this.o.getContent().addTextStyle(ts);

	}

	public boolean createStyles(Util util, final ZipOutputStream out) {
		try {
			out.putNextEntry(new ZipEntry("styles.xml"));
			util.writeString(out,
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			util.writeString(out,
					"<office:document-styles xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\" xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\" xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\" xmlns:presentation=\"urn:oasis:names:tc:opendocument:xmlns:presentation:1.0\" xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\" xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" xmlns:math=\"http://www.w3.org/1998/Math/MathML\" xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\" xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\" xmlns:ooow=\"http://openoffice.org/2004/writer\" xmlns:oooc=\"http://openoffice.org/2004/calc\" xmlns:dom=\"http://www.w3.org/2001/xml-events\" office:version=\"1.1\">");
			util.writeString(out, "<office:font-face-decls>");
			util.writeString(out,
					"<style:font-face style:name=\"Arial\" svg:font-family=\"Arial\" style:font-family-generic=\"swiss\" style:font-pitch=\"variable\"/>");
			util.writeString(out,
					"<style:font-face style:name=\"Lucida Sans Unicode\" svg:font-family=\"&apos;Lucida Sans Unicode&apos;\" style:font-family-generic=\"system\" style:font-pitch=\"variable\"/>");
			util.writeString(out,
					"<style:font-face style:name=\"Tahoma\" svg:font-family=\"Tahoma\" style:font-family-generic=\"system\" style:font-pitch=\"variable\"/>");
			util.writeString(out, "</office:font-face-decls>");
			util.writeString(out, "<office:styles>");

			for (int n = 0; n < this.qDateStyles.size(); n++) {
				DateStyle ds = this.qDateStyles.get(n);
				util.writeString(out, ds.toXML());
			}
			for (int n = 0; n < this.qNumberStyles.size(); n++) {
				NumberStyle ns = this.qNumberStyles.get(n);
				util.writeString(out, ns.toXML());
			}
			for (int n = 0; n < this.qCurrencyStyles.size(); n++) {
				CurrencyStyle cs = this.qCurrencyStyles.get(n);
				util.writeString(out, cs.toXML());
			}

			if (this.footer != null) {
				util.writeString(out, this.footer.toXML());
			}
			if (this.header != null) {
				util.writeString(out, this.header.toXML());
			}

			util.writeString(out, "</office:styles>");
			util.writeString(out, "<office:automatic-styles>");

			/*
			u.writeString(out, "<number:date-style style:name=\"N01\" number:automatic-order=\"true\">");
			u.writeString(out, "<number:day number:style=\"long\"/>");
			u.writeString(out, "<number:text>.</number:text>");
			u.writeString(out, "<number:month number:style=\"long\"/>");
			u.writeString(out, "<number:text>.</number:text>");
			u.writeString(out, "<number:year/>");
			u.writeString(out, "</number:date-style>");
			*/

			for (int n = 0; n < this.qPageStyles.size(); n++) {
				PageStyle ps = this.qPageStyles.get(n);
				util.writeString(out, ps.toXML());
			}
			for (int n = 0; n < this.qTextStyles.size(); n++) {
				TextStyle ts = this.qTextStyles.get(n);
				util.writeString(out, ts.toXML(util));
			}

			util.writeString(out, "</office:automatic-styles>");
			util.writeString(out, "<office:master-styles>");

			for (int n = 0; n < this.qPageStyles.size(); n++) {
				PageStyle ps = this.qPageStyles.get(n);
				util.writeString(out, ps.toMasterStyleXML());
			}

			util.writeString(out, "</office:master-styles>");
			util.writeString(out, "</office:document-styles>");
			out.closeEntry();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Get the current footer object.
	 * 
	 * @return The current footer object.
	 */
	public Footer getFooter() {
		return this.footer;
	}

	/**
	 * Set the footer object to f. Reset this object by setting a new Footer
	 * object,<br>
	 * or use resetFooter() to remove the Footer object.
	 * 
	 * @param f
	 *            - The footer object to be used
	 */
	public void setFooter(final Footer f) {
		this.footer = f;
	}

	/**
	 * Reset the footer to null.
	 */
	public void resetFooter() {
		this.footer = null;
	}

	/**
	 * Get the current header object.
	 * 
	 * @return The current header object.
	 */
	public Header getHeader() {
		return this.header;
	}

	/**
	 * Set the header object to h. Reset this object by setting a new Header
	 * object,<br>
	 * or use resetHeader() to remove the Header object.
	 * 
	 * @param h
	 *            - The header object to be used
	 */
	public void setHeader(final Header h) {
		this.header = h;
	}

	/**
	 * Reset the header to null.
	 */
	public void resetHeader() {
		this.header = null;
	}

}
