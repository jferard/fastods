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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.escape.Escaper;
import com.google.common.xml.XmlEscapers;

import sun.nio.cs.StreamEncoder;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file Util.java is part of FastODS.
 */
@SuppressWarnings("PMD.UnusedLocalVariable")
public class Util {
	/**
	 * A space char for append
	 */
	public static final char SPACE_CHAR = ' ';

	private static Util instance;

	public static <T extends NamedObject> Optional<T> findElementByName(
			final List<T> list, final String name) {
		// Check is a style with this name exists and replace if yes
		final ListIterator<T> listIterator = list.listIterator();
		while (listIterator.hasNext()) {
			final T curElement = listIterator.next();
			if (curElement.getName().equals(name)) {
				return Optional.of(curElement);
			}
		}
		return Optional.absent();
	}

	public static Util getInstance() {
		if (Util.instance == null) {
			Util.instance = new Util();
		}
		return Util.instance;
	}

	private final Map<String, String> attrMap;

	private final Escaper xmlAttributeEscaper;

	private final Escaper xmlContentEscaper;

	public Util() {
		this.xmlContentEscaper = XmlEscapers.xmlContentEscaper();
		this.xmlAttributeEscaper = XmlEscapers.xmlAttributeEscaper();
		this.attrMap = new HashMap<String, String>();
	}

	public void appendAttribute(final Appendable appendable,
			final String sElementName, final boolean b) throws IOException {
		this.appendEAttribute(appendable, sElementName, Boolean.toString(b));
	}

	/**
	 * Append a new element to StringBuilder sb, the name of the element is
	 * sElementName<br>
	 * and the value is nValue.
	 *
	 * @param appendable
	 *            The StringBuilder to which the new element should be added.
	 * @param sElementName
	 *            The new element name
	 * @param nValue
	 *            The value of the element
	 * @throws IOException
	 */
	public void appendAttribute(final Appendable appendable,
			final String sElementName, final int nValue) throws IOException {
		this.appendEAttribute(appendable, sElementName,
				Integer.toString(nValue));
	}

	/**
	 * Append a new element to StringBuilder sb, the name of the element is
	 * sElementName<br>
	 * and the value is sValue.
	 *
	 * @param appendable
	 *            The StringBuilder to which the new element should be added.
	 * @param sElementName
	 *            The new element name
	 * @param sValue
	 *            The value of the element
	 * @throws IOException
	 */
	public void appendAttribute(final Appendable appendable,
			final String sElementName, final String sValue) throws IOException {
		appendable.append(' ').append(sElementName).append("=\"")
				.append(this.escapeXMLAttribute(sValue)).append('"');
	}

	/**
	 * Copy all styles from one OdsFile to another OdsFile.<br>
	 * The target file will then contain all styles from the source file.
	 *
	 * @param source
	 *            The source file
	 * @param target
	 *            The target file.
	 */
	/*
	public void copyStylesFromTo(final OdsFile source, final OdsFile target) {
		target.setStyles(source.getStyles());
		target.getContent().setPageStyles(source.getContent().getPageStyles());
		target.getContent()
				.setTableStyles(source.getContent().getTableStyles());
		target.getContent().setTextStyles(source.getContent().getTextStyles());
	}*/

	/**
	 * @param sValue
	 *            escaped attribute
	 */
	public void appendEAttribute(final Appendable appendable,
			final String sElementName, final String sValue) throws IOException {
		appendable.append(' ').append(sElementName).append("=\"").append(sValue)
				.append('"');
	}

	public void appendTag(final Appendable appendable, final String tagName,
			final String content) throws IOException {
		appendable.append('<').append(tagName).append('>')
				.append(this.escapeXMLContent(content)).append("</")
				.append(tagName).append('>');
	}

	/**
	 * Get the todays date in format sDateFormat. See Javas 'SimpleDateFormat'
	 * for details.<br>
	 * The following pattern letters are defined (all other characters from 'A'
	 * to 'Z' and from 'a' to 'z' are reserved):<br>
	 * Letter Date or Time Component Presentation Examples<br>
	 * G Era designator FHText AD<br>
	 * y Year Year 1996; 96<br>
	 * M Month in year Month July; Jul; 07<br>
	 * w Week in year Number 27<br>
	 * W Week in month Number 2<br>
	 * D Day in year Number 189<br>
	 * d Day in month Number 10<br>
	 * F Day of week in month Number 2<br>
	 * E Day in week FHText Tuesday; Tue<br>
	 * a Am/pm marker FHText PM<br>
	 * H Hour in day (0-23) Number 0<br>
	 * k Hour in day (1-24) Number 24<br>
	 * K Hour in am/pm (0-11) Number 0<br>
	 * h Hour in am/pm (1-12) Number 12<br>
	 * m Minute in hour Number 30<br>
	 * s Second in minute Number 55<br>
	 * S Millisecond Number 978<br>
	 * z Time zone General time zone Pacific Standard Time; PST; GMT-08:00<br>
	 * Z Time zone RFC 822 time zone -0800<br>
	 *
	 * @param sDateFormat
	 *            The date format for todays date that should be returned.
	 * @return A string with the current date in sDateFormat
	 */
	public String dateToday(final String sDateFormat) {
		final DateFormat formatter = new SimpleDateFormat(sDateFormat,
				Locale.US);

		return formatter.format(new Date());
	}

	public String escapeXMLAttribute(final String s) {
		String s2 = this.attrMap.get(s);
		if (s2 == null) {
			s2 = this.xmlAttributeEscaper.escape(s);
			this.attrMap.put(s, s2);
		}
		return s2;
	}

	public String escapeXMLContent(final String s) {
		return this.xmlContentEscaper.escape(s);
	}

	/**
	 * Convert a cell position string like B3 to the column number.
	 *
	 * @param sPos
	 *            The cell position in the range 'A1' to 'IV65536'
	 * @return The row, e.g. A1 will return 0, B1 will return 1, E1 will return
	 *         4
	 */
	public int positionToColumn(final String sPos) {

		final String s = sPos.toUpperCase(Locale.US);
		int nCount = 0;
		int nValue = 0;

		// ------------------------------------------------
		// Loop through the string, starting at the end
		// ------------------------------------------------
		for (int n = s.length() - 1; n >= 0; n--) {
			final char c = s.charAt(n);
			// ------------------------------------------------
			// Only letters A-Z are valid
			// ------------------------------------------------
			if (c >= 'A' && c <= 'Z') {
				nValue += (c - 64) * (int) Math.pow(26, nCount);
				nCount++;
				if (nCount >= 2) {
					break; // 'IV' is the last column name, so break out after
							// the second letter
				}
			}
		}

		return nValue - 1;
	}

	/**
	 * Convert a string like B3 to the row number.
	 *
	 * @param sPos
	 *            The cell position in the range 'A1' to 'IV65536'
	 * @return The column, e.g. A1 will return 0, B3 will return 2, X65000 will
	 *         return 64999
	 */
	public int positionToRow(final String sPos) {

		int nCount = 0;
		int nValue = 0;

		// -------------------------------------------------
		// Loop through the string, starting at the end
		// -------------------------------------------------
		for (int n = sPos.length() - 1; n >= 0; n--) {
			final char c = sPos.charAt(n);
			// -------------------------------------------------
			// Only numbers are valid
			// -------------------------------------------------
			if (c >= '0' && c <= '9') {
				nValue += (c - 48) * (int) Math.pow(10, nCount);
				nCount++;
				if (nCount >= 5) {
					break; // 65536 is the highest row, so break out after the
							// fifth number
				}

			}
		}

		return nValue - 1;
	}

	/**
	 * Replace all occurrences of a sub-string within a string.
	 *
	 * @param sHaystack
	 *            The source string to be checked
	 * @param sNeedle
	 *            The sub-string to be replaced
	 * @param sReplacement
	 *            The replacement string
	 * @return The new string
	 */
	public String replace(final String sHaystack, final String sNeedle,
			final String sReplacement) {
		int nStart = 0;
		int nEnd = 0;
		final StringBuilder sbResult = new StringBuilder();

		// ----------------------------------------
		// Loop until each sNeedle is replaced
		// ----------------------------------------
		while ((nEnd = sHaystack.indexOf(sNeedle, nStart)) >= 0) {
			sbResult.append(sHaystack.substring(nStart, nEnd));
			sbResult.append(sReplacement);
			nStart = nEnd + sNeedle.length();
		}
		sbResult.append(sHaystack.substring(nStart));

		return sbResult.toString();
	}

	// JF
	public byte[] toBytes(final String s) throws UnsupportedEncodingException {
		return s.getBytes("UTF-8");
	}

	/**
	 * Wraps an OutputStream in a BufferedWriter
	 *
	 * @param out
	 *            the stream
	 * @return the writer
	 */
	public Writer wrapStream(final OutputStream out) {
		return new OutputStreamWriter(out, Charsets.UTF_8);
	}

	public boolean writeString(final ZipOutputStream o, final String sText) {

		try {
			final byte[] bytes = this.toBytes(sText);
			o.write(bytes, 0, bytes.length);
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean writeStringArray(final ZipOutputStream o,
			final String... sTexts) {

		try {
			for (final String sText : sTexts) {
				final byte[] bytes = this.toBytes(sText);
				o.write(bytes, 0, bytes.length);
			}
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean equal(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		} else {
			return o1.equals(o2);
		}
	}
}
