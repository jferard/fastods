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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.escape.Escaper;
import com.google.common.xml.XmlEscapers;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file Util.java is part of FastODS.
 */
public class Util {
	private static Util instance;

	public static final char SPACE_CHAR = ' ';

	// Gray
	public final static String COLOR_GRAY16 = "#292929";
	public final static String COLOR_GRAY32 = "#525252";
	public final static String COLOR_GRAY48 = "#7A7A7A";
	public final static String COLOR_GRAY64 = "#A3A3A3";
	public final static String COLOR_GRAY80 = "#CCCCCC";
	public final static String COLOR_GRAY96 = "#F5F5F5";

	public final static String COLOR_MAROON = "#800000";
	public final static String COLOR_DARKRED = "#8B0000";
	public final static String COLOR_FIREBRICK = "#B22222";
	public final static String COLOR_RED = "#FF0000";
	public final static String COLOR_SALMON = "#FA8072";
	public final static String COLOR_TOMATO = "#FF6347";
	public final static String COLOR_CORAL = "#FF7F50";
	public final static String COLOR_ORANGERED = "#FF4500";
	public final static String COLOR_CHOCOLATE = "#D2691E";
	public final static String COLOR_SANDYBROWN = "#F4A460";
	public final static String COLOR_DARKORANGE = "#FF8C00";
	public final static String COLOR_ORANGE = "#FFA500";
	public final static String COLOR_DARKGOLDENROD = "#B8860B";
	public final static String COLOR_GOLDENROD = "#DAA520";
	public final static String COLOR_GOLD = "#FFD700";
	public final static String COLOR_OLIVE = "#808000";
	public final static String COLOR_YELLOW = "#FFFF00";
	public final static String COLOR_YELLOWGREEN = "#9ACD32";
	public final static String COLOR_GREENYELLOW = "#ADFF2F";
	public final static String COLOR_CHARTREUSE = "#7FFF00";
	public final static String COLOR_LAWNGREEN = "#7CFC00";
	public final static String COLOR_GREEN = "#008000";
	public final static String COLOR_LIME = "#00FF00";
	public final static String COLOR_LIMEGREEN = "#32CD32";
	public final static String COLOR_SPRINGGREEN = "#00FF7F";
	public final static String COLOR_MEDIUMSPRINGGREEN = "#00FA9A";
	public final static String COLOR_TURQUOISE = "#40E0D0";
	public final static String COLOR_LIGHTSEAGREEN = "#20B2AA";
	public final static String COLOR_MEDIUMTURQUOISE = "#48D1CC";
	public final static String COLOR_TEAL = "#008080";
	public final static String COLOR_DARKCYAN = "#008B8B";
	public final static String COLOR_AQUA = "#00FFFF";
	public final static String COLOR_CYAN = "#00FFFF";
	public final static String COLOR_DARKTURQUOISE = "#00CED1";
	public final static String COLOR_DEEPSKYBLUE = "#00BFFF";
	public final static String COLOR_DODGERBLUE = "#1E90FF";
	public final static String COLOR_ROYALBLUE = "#4169E1";
	public final static String COLOR_NAVY = "#000080";
	public final static String COLOR_DARKBLUE = "#00008B";
	public final static String COLOR_MEDIUMBLUE = "#0000CD";
	public final static String COLOR_BLUE = "#0000FF";
	public final static String COLOR_BLUEVIOLET = "#8A2BE2";
	public final static String COLOR_DARKORCHID = "#9932CC";
	public final static String COLOR_DARKVIOLET = "#9400D3";
	public final static String COLOR_PURPLE = "#800080";
	public final static String COLOR_DARKMAGENTA = "#8B008B";
	public final static String COLOR_FUCHSIA = "#FF00FF";
	public final static String COLOR_MAGENTA = "#FF00FF";
	public final static String COLOR_MEDIUMVIOLETRED = "#C71585";
	public final static String COLOR_DEEPPINK = "#FF1493";
	public final static String COLOR_HOTPINK = "#FF69B4";
	public final static String COLOR_CRIMSON = "#DC143C";
	public final static String COLOR_BROWN = "#A52A2A";
	public final static String COLOR_INDIANRED = "#CD5C5C";
	public final static String COLOR_ROSYBROWN = "#BC8F8F";
	public final static String COLOR_LIGHTCORAL = "#F08080";
	public final static String COLOR_SNOW = "#FFFAFA";
	public final static String COLOR_MISTYROSE = "#FFE4E1";
	public final static String COLOR_DARKSALMON = "#E9967A";
	public final static String COLOR_LIGHTSALMON = "#FFA07A";
	public final static String COLOR_SIENNA = "#A0522D";
	public final static String COLOR_SEASHELL = "#FFF5EE";
	public final static String COLOR_SADDLEBROWN = "#8B4513";
	public final static String COLOR_PEACHPUFF = "#FFDAB9";
	public final static String COLOR_PERU = "#CD853F";
	public final static String COLOR_LINEN = "#FAF0E6";
	public final static String COLOR_BISQUE = "#FFE4C4";
	public final static String COLOR_BURLYWOOD = "#DEB887";
	public final static String COLOR_TAN = "#D2B48C";
	public final static String COLOR_ANTIQUEWHITE = "#FAEBD7";
	public final static String COLOR_NAVAJOWHITE = "#FFDEAD";
	public final static String COLOR_BLANCHEDALMOND = "#FFEBCD";
	public final static String COLOR_PAPAYAWHIP = "#FFEFD5";
	public final static String COLOR_MOCCASIN = "#FFE4B5";
	public final static String COLOR_WHEAT = "#F5DEB3";
	public final static String COLOR_OLDLACE = "#FDF5E6";
	public final static String COLOR_FLORALWHITE = "#FFFAF0";
	public final static String COLOR_CORNSILK = "#FFF8DC";
	public final static String COLOR_KHAKI = "#F0E68C";
	public final static String COLOR_LEMONCHIFFON = "#FFFACD";
	public final static String COLOR_PALEGOLDENROD = "#EEE8AA";
	public final static String COLOR_DARKKHAKI = "#BDB76B";
	public final static String COLOR_BEIGE = "#F5F5DC";
	public final static String COLOR_LIGHTGOLDENRODYELLOW = "#FAFAD2";
	public final static String COLOR_LIGHTYELLOW = "#FFFFE0";
	public final static String COLOR_IVORY = "#FFFFF0";
	public final static String COLOR_OLIVEDRAB = "#6B8E23";
	public final static String COLOR_DARKOLIVEGREEN = "#556B2F";
	public final static String COLOR_DARKSEAGREEN = "#8FBC8F";
	public final static String COLOR_DARKGREEN = "#006400";
	public final static String COLOR_FORESTGREEN = "#228B22";
	public final static String COLOR_LIGHTGREEN = "#90EE90";
	public final static String COLOR_PALEGREEN = "#98FB98";
	public final static String COLOR_HONEYDEW = "#F0FFF0";
	public final static String COLOR_SEAGREEN = "#2E8B57";
	public final static String COLOR_MEDIUMSEAGREEN = "#3CB371";
	public final static String COLOR_MINTCREAM = "#F5FFFA";
	public final static String COLOR_MEDIUMAQUAMARINE = "#66CDAA";
	public final static String COLOR_AQUAMARINE = "#7FFFD4";
	public final static String COLOR_DARKSLATEGRAY = "#2F4F4F";
	public final static String COLOR_PALETURQUOISE = "#AFEEEE";
	public final static String COLOR_LIGHTCYAN = "#E0FFFF";
	public final static String COLOR_AZURE = "#F0FFFF";
	public final static String COLOR_CADETBLUE = "#5F9EA0";
	public final static String COLOR_POWDERBLUE = "#B0E0E6";
	public final static String COLOR_LIGHTBLUE = "#ADD8E6";
	public final static String COLOR_SKYBLUE = "#87CEEB";
	public final static String COLOR_LIGHTSKYBLUE = "#87CEFA";
	public final static String COLOR_STEELBLUE = "#4682B4";
	public final static String COLOR_ALICEBLUE = "#F0F8FF";
	public final static String COLOR_SLATEGRAY = "#708090";
	public final static String COLOR_LIGHTSLATEGRAY = "#778899";
	public final static String COLOR_LIGHTSTEELBLUE = "#B0C4DE";
	public final static String COLOR_CORNFLOWERBLUE = "#6495ED";
	public final static String COLOR_LAVENDER = "#E6E6FA";
	public final static String COLOR_GHOSTWHITE = "#F8F8FF";
	public final static String COLOR_MIDNIGHTBLUE = "#191970";
	public final static String COLOR_SLATEBLUE = "#6A5ACD";
	public final static String COLOR_DARKSLATEBLUE = "#483D8B";
	public final static String COLOR_MEDIUMSLATEBLUE = "#7B68EE";
	public final static String COLOR_MEDIUMPURPLE = "#9370DB";
	public final static String COLOR_INDIGO = "#4B0082";
	public final static String COLOR_MEDIUMORCHID = "#BA55D3";
	public final static String COLOR_PLUM = "#DDA0DD";
	public final static String COLOR_VIOLET = "#EE82EE";
	public final static String COLOR_THISTLE = "#D8BFD8";
	public final static String COLOR_ORCHID = "#DA70D6";
	public final static String COLOR_LAVENDERBLUSH = "#FFF0F5";
	public final static String COLOR_PALEVIOLETRED = "#DB7093";
	public final static String COLOR_PINK = "#FFC0CB";
	public final static String COLOR_LIGHTPINK = "#FFB6C1";
	public final static String COLOR_BLACK = "#000000";
	public final static String COLOR_DIMGRAY = "#696969";
	public final static String COLOR_GRAY = "#808080";
	public final static String COLOR_DARKGRAY = "#A9A9A9";
	public final static String COLOR_SILVER = "#C0C0C0";
	public final static String COLOR_LIGHTGREY = "#D3D3D3";
	public final static String COLOR_GAINSBORO = "#DCDCDC";
	public final static String COLOR_WHITESMOKE = "#F5F5F5";
	public final static String COLOR_WHITE = "#FFFFFF";

	public static Util getInstance() {
		if (Util.instance == null) {
			Util.instance = new Util();
		}
		return Util.instance;
	}

	private Escaper xmlContentEscaper;

	private Escaper xmlAttributeEscaper;

	private Map<String, String> attrMap;

	public Util() {
		this.xmlContentEscaper = XmlEscapers.xmlContentEscaper();
		this.xmlAttributeEscaper = XmlEscapers.xmlAttributeEscaper();
		this.attrMap = new HashMap<String, String>();
	}

	public void appendAttribute(Appendable appendable, String sElementName,
			boolean b) throws IOException {
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
	 * @param sValue
	 *            escaped attribute
	 */
	public void appendEAttribute(Appendable appendable, String sElementName,
			String sValue) throws IOException {
		appendable.append(' ').append(sElementName).append("=\"").append(sValue)
				.append('"');
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
	 * Helper function to create any available color string from color values.
	 * 
	 * @param nRed
	 *            The red value, 0-255
	 * @param nGreen
	 *            The green value, 0-255
	 * @param nBlue
	 *            The blue value, 0-255
	 * @return The hex string in the format '#rrggbb'
	 */
	public String createHexColor(int nRed, int nGreen, int nBlue) {
		if (nRed > 255) {
			nRed = 255;
		}
		if (nGreen > 255) {
			nGreen = 255;
		}
		if (nBlue > 255) {
			nBlue = 255;
		}
		if (nRed < 0) {
			nRed = 0;
		}
		if (nGreen < 0) {
			nGreen = 0;
		}
		if (nBlue < 0) {
			nBlue = 0;
		}

		return new StringBuilder("#").append(toHexString(nRed))
				.append(toHexString(nGreen)).append(toHexString(nBlue))
				.toString();

	}

	/**
	 * Get the todays date in format sDateFormat. See Javas 'SimpleDateFormat'
	 * for details.<br>
	 * The following pattern letters are defined (all other characters from 'A'
	 * to 'Z' and from 'a' to 'z' are reserved):<br>
	 * Letter Date or Time Component Presentation Examples<br>
	 * G Era designator Text AD<br>
	 * y Year Year 1996; 96<br>
	 * M Month in year Month July; Jul; 07<br>
	 * w Week in year Number 27<br>
	 * W Week in month Number 2<br>
	 * D Day in year Number 189<br>
	 * d Day in month Number 10<br>
	 * F Day of week in month Number 2<br>
	 * E Day in week Text Tuesday; Tue<br>
	 * a Am/pm marker Text PM<br>
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
		DateFormat formatter = new SimpleDateFormat(sDateFormat);

		return formatter.format(new Date());
	}

	public String escapeXMLAttribute(String s) {
		String s2 = this.attrMap.get(s);
		if (s2 == null) {
			s2 = this.xmlAttributeEscaper.escape(s);
			this.attrMap.put(s, s2);
		}
		return s2;
	}

	public String escapeXMLContent(String s) {
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

		String s = sPos.toUpperCase();
		int nCount = 0;
		int nValue = 0;

		// ------------------------------------------------
		// Loop through the string, starting at the end
		// ------------------------------------------------
		for (int n = s.length() - 1; n >= 0; n--) {
			char c = s.charAt(n);
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
			char c = sPos.charAt(n);
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
		StringBuilder sbResult = new StringBuilder();

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
	public byte[] toBytes(String s) throws UnsupportedEncodingException {
		return s.getBytes("UTF-8");
	}

	/**
	 * Helper function to convert non-ASCII character to XML encoding.
	 * 
	 * @param sString
	 *            The string to be converted
	 * @return The converted string
	 */
	@Deprecated
	public String toXmlString(String sString) {

		for (int n = 0; n < sString.length(); n++) {
			int c = sString.charAt(n);

			if (c > 128) {
				sString = replace(sString, sString.substring(n, n + 1),
						new StringBuilder("&#").append(c).append(";")
								.toString());
			}
		}

		return sString;
	}

	public Writer wrapStream(OutputStream out) {
		return new BufferedWriter(new OutputStreamWriter(out, Charsets.UTF_8));
	}

	public boolean writeString(final ZipOutputStream o, final String sText) {

		try {
			byte[] bytes = toBytes(sText);
			o.write(bytes, 0, bytes.length);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean writeStringArray(final ZipOutputStream o,
			final String... sTexts) {

		try {
			for (final String sText : sTexts) {
				byte[] bytes = toBytes(sText);
				o.write(bytes, 0, bytes.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private String toHexString(final int n) {
		StringBuilder sbReturn = new StringBuilder();
		if (n < 16) {
			sbReturn.append("0");
		}
		sbReturn.append(Integer.toHexString(n));
		return sbReturn.toString();
	}

	public static <T extends NamedObject> Optional<T> findElementByName(
			List<T> list, String name) {
		// Check is a style with this name exists and replace if yes
		ListIterator<T> listIterator = list.listIterator();
		while (listIterator.hasNext()) {
			T curElement = listIterator.next();
			if (curElement.getName().equals(name)) {
				return Optional.of(curElement);
			}
		}
		return Optional.absent();
	}

	public void appendTag(Appendable appendable, String tagName, String content)
			throws IOException {
		appendable.append('<').append(tagName).append('>')
				.append(this.escapeXMLContent(content)).append("</")
				.append(tagName).append('>');
	}

}
