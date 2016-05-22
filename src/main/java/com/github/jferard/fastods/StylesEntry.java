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
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 * 
 *         This file StylesEntry.java is part of Fast ODS.
 *         
 * styles.xml/office:document-styles 
 */
public class StylesEntry implements OdsEntry {
	private ObjectQueue<NumberStyle> qNumberStyles = ObjectQueue.newQueue();
	private ObjectQueue<CurrencyStyle> qCurrencyStyles = ObjectQueue.newQueue();
	private ObjectQueue<PageStyle> qPageStyles = ObjectQueue.newQueue();
	private ObjectQueue<TextStyle> qTextStyles = ObjectQueue.newQueue();
	private ObjectQueue<DateStyle> qDateStyles = ObjectQueue.newQueue();
	private FooterHeader header = null;

	private FooterHeader footer = null;

	/**
	 * The OdsFile where this object belong to.
	 */
	private OdsFile o;

	/**
	 * @param odsFile
	 *            - The OdsFile where the styles belong to
	 */
	public StylesEntry(final OdsFile odsFile) {
		this.o = odsFile;
	}

	/**
	 * Add a CurrencyStyle, if a CurrencyStyle with this name already exist, the
	 * old one is replaced.
	 * 
	 * @param cs
	 *            - The currency style to be added.
	 */
	public void addCurrencyStyle(CurrencyStyle cs) {
		ObjectQueue.addOrReplaceNamedElement(this.qCurrencyStyles, cs);
	}

	/**
	 * Add a DateStyle, if a DateStyle with this name already exist, the old one
	 * is replaced.
	 * 
	 * @param ds
	 *            - The date style to be added.
	 */
	public void addDateStyle(final DateStyle ds) {
		ObjectQueue.addOrReplaceNamedElement(this.qDateStyles, ds);
	}

	/**
	 * Add a NumberStyle, if a NumberStyle with this name already exist, the old
	 * one is replaced.
	 * 
	 * @param ns
	 *            - The number style to be added.
	 */
	public void addNumberStyle(final NumberStyle ns) {
		ObjectQueue.addOrReplaceNamedElement(this.qNumberStyles, ns);
	}

	public void addPageStyle(PageStyle ps) {
		if (ObjectQueue.addOrReplaceNamedElement(this.qPageStyles, ps))
			this.o.getContent().addPageStyle(ps);
	}

	public void addTextStyle(TextStyle ts) {
		if (ObjectQueue.addOrReplaceNamedElement(this.qTextStyles, ts))
			this.o.getContent().addTextStyle(ts);
	}

	/**
	 * Get the current footer object.
	 * 
	 * @return The current footer object.
	 */
	public FooterHeader getFooter() {
		return this.footer;
	}

	/**
	 * Get the current header object.
	 * 
	 * @return The current header object.
	 */
	public FooterHeader getHeader() {
		return this.header;
	}

	/**
	 * Reset the footer to null.
	 */
	public void resetFooter() {
		this.footer = null;
	}

	/**
	 * Reset the header to null.
	 */
	public void resetHeader() {
		this.header = null;
	}

	/**
	 * Set the footer object to f. Reset this object by setting a new
	 * FooterHeader object,<br>
	 * or use resetFooter() to remove the FooterHeader object.
	 * 
	 * @param f
	 *            - The footer object to be used
	 */
	public void setFooter(final FooterHeader f) {
		this.footer = f;
	}

	/**
	 * Set the header object to h. Reset this object by setting a new Header
	 * object,<br>
	 * or use resetHeader() to remove the Header object.
	 * 
	 * @param h
	 *            - The header object to be used
	 */
	public void setHeader(final FooterHeader h) {
		this.header = h;
	}

	@Override
	public void write(Util util, final ZipOutputStream zipOut)
			throws IOException {
		zipOut.putNextEntry(new ZipEntry("styles.xml"));
		Writer writer = util.wrapStream(zipOut);
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

		for (DateStyle ds : this.qDateStyles)
			writer.write(ds.toXML(util));

		for (NumberStyle ns : this.qNumberStyles)
			writer.write(ns.toXML(util));

		for (CurrencyStyle cs : this.qCurrencyStyles)
			writer.write(cs.toXML(util));

		if (this.footer != null) {
			StringBuilder sbTemp = new StringBuilder();
			sbTemp.append(
					"<style:style style:name=\"Footer\" style:family=\"paragraph\" style:parent-style-name=\"Standard\"  style:class=\"extra\">");
			sbTemp.append(
					"<style:paragraph-properties  text:number-lines=\"false\" text:line-number=\"0\">");
			sbTemp.append("</style:paragraph-properties>");
			sbTemp.append("</style:style>");
			final String footerXML = sbTemp.toString();
			writer.write(footerXML);
		}
		if (this.header != null) {
			StringBuilder sbTemp = new StringBuilder();
			sbTemp.append(
					"<style:style style:name=\"Header\" style:family=\"paragraph\" style:parent-style-name=\"Standard\"  style:class=\"extra\">");
			sbTemp.append(
					"<style:paragraph-properties  text:number-lines=\"false\" text:line-number=\"0\">");
			sbTemp.append("</style:paragraph-properties>");
			sbTemp.append("</style:style>");
			final String headerXML = sbTemp.toString();
			writer.write(headerXML);
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

		for (PageStyle ps : this.qPageStyles)
			writer.write(ps.toXML(util));

		for (TextStyle ts : this.qTextStyles)
			ts.appendXML(util, writer);

		writer.write("</office:automatic-styles>");
		writer.write("<office:master-styles>");

		for (PageStyle ps : this.qPageStyles)
			writer.write(ps.toMasterStyleXML(util));

		writer.write("</office:master-styles>");
		writer.write("</office:document-styles>");
		writer.flush();
		zipOut.closeEntry();
	}

}
