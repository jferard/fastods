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
import java.util.Map;
import java.util.zip.ZipOutputStream;

import com.google.common.base.Charsets;
import com.google.common.escape.Escaper;
import com.google.common.xml.XmlEscapers;

/**
 * TODO : split code, clean code
 * 
 * @author Martin Schulz<br>
 * 
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *         <br>
 * 
 *         This file Util.java is part of SimpleODS.
 *
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

	public final static String COLOR_Maroon = "#800000";
	public final static String COLOR_DarkRed = "#8B0000";
	public final static String COLOR_Firebrick = "#B22222";
	public final static String COLOR_Red = "#FF0000";
	public final static String COLOR_Salmon = "#FA8072";
	public final static String COLOR_Tomato = "#FF6347";
	public final static String COLOR_Coral = "#FF7F50";
	public final static String COLOR_OrangeRed = "#FF4500";
	public final static String COLOR_Chocolate = "#D2691E";
	public final static String COLOR_SandyBrown = "#F4A460";
	public final static String COLOR_DarkOrange = "#FF8C00";
	public final static String COLOR_Orange = "#FFA500";
	public final static String COLOR_DarkGoldenrod = "#B8860B";
	public final static String COLOR_Goldenrod = "#DAA520";
	public final static String COLOR_Gold = "#FFD700";
	public final static String COLOR_Olive = "#808000";
	public final static String COLOR_Yellow = "#FFFF00";
	public final static String COLOR_YellowGreen = "#9ACD32";
	public final static String COLOR_GreenYellow = "#ADFF2F";
	public final static String COLOR_Chartreuse = "#7FFF00";
	public final static String COLOR_LawnGreen = "#7CFC00";
	public final static String COLOR_Green = "#008000";
	public final static String COLOR_Lime = "#00FF00";
	public final static String COLOR_LimeGreen = "#32CD32";
	public final static String COLOR_SpringGreen = "#00FF7F";
	public final static String COLOR_MediumSpringGreen = "#00FA9A";
	public final static String COLOR_Turquoise = "#40E0D0";
	public final static String COLOR_LightSeaGreen = "#20B2AA";
	public final static String COLOR_MediumTurquoise = "#48D1CC";
	public final static String COLOR_Teal = "#008080";
	public final static String COLOR_DarkCyan = "#008B8B";
	public final static String COLOR_Aqua = "#00FFFF";
	public final static String COLOR_Cyan = "#00FFFF";
	public final static String COLOR_DarkTurquoise = "#00CED1";
	public final static String COLOR_DeepSkyBlue = "#00BFFF";
	public final static String COLOR_DodgerBlue = "#1E90FF";
	public final static String COLOR_RoyalBlue = "#4169E1";
	public final static String COLOR_Navy = "#000080";
	public final static String COLOR_DarkBlue = "#00008B";
	public final static String COLOR_MediumBlue = "#0000CD";
	public final static String COLOR_Blue = "#0000FF";
	public final static String COLOR_BlueViolet = "#8A2BE2";
	public final static String COLOR_DarkOrchid = "#9932CC";
	public final static String COLOR_DarkViolet = "#9400D3";
	public final static String COLOR_Purple = "#800080";
	public final static String COLOR_DarkMagenta = "#8B008B";
	public final static String COLOR_Fuchsia = "#FF00FF";
	public final static String COLOR_Magenta = "#FF00FF";
	public final static String COLOR_MediumVioletRed = "#C71585";
	public final static String COLOR_DeepPink = "#FF1493";
	public final static String COLOR_HotPink = "#FF69B4";
	public final static String COLOR_Crimson = "#DC143C";
	public final static String COLOR_Brown = "#A52A2A";
	public final static String COLOR_IndianRed = "#CD5C5C";
	public final static String COLOR_RosyBrown = "#BC8F8F";
	public final static String COLOR_LightCoral = "#F08080";
	public final static String COLOR_Snow = "#FFFAFA";
	public final static String COLOR_MistyRose = "#FFE4E1";
	public final static String COLOR_DarkSalmon = "#E9967A";
	public final static String COLOR_LightSalmon = "#FFA07A";
	public final static String COLOR_Sienna = "#A0522D";
	public final static String COLOR_SeaShell = "#FFF5EE";
	public final static String COLOR_SaddleBrown = "#8B4513";
	public final static String COLOR_Peachpuff = "#FFDAB9";
	public final static String COLOR_Peru = "#CD853F";
	public final static String COLOR_Linen = "#FAF0E6";
	public final static String COLOR_Bisque = "#FFE4C4";
	public final static String COLOR_Burlywood = "#DEB887";
	public final static String COLOR_Tan = "#D2B48C";
	public final static String COLOR_AntiqueWhite = "#FAEBD7";
	public final static String COLOR_NavajoWhite = "#FFDEAD";
	public final static String COLOR_BlanchedAlmond = "#FFEBCD";
	public final static String COLOR_PapayaWhip = "#FFEFD5";
	public final static String COLOR_Moccasin = "#FFE4B5";
	public final static String COLOR_Wheat = "#F5DEB3";
	public final static String COLOR_Oldlace = "#FDF5E6";
	public final static String COLOR_FloralWhite = "#FFFAF0";
	public final static String COLOR_Cornsilk = "#FFF8DC";
	public final static String COLOR_Khaki = "#F0E68C";
	public final static String COLOR_LemonChiffon = "#FFFACD";
	public final static String COLOR_PaleGoldenrod = "#EEE8AA";
	public final static String COLOR_DarkKhaki = "#BDB76B";
	public final static String COLOR_Beige = "#F5F5DC";
	public final static String COLOR_LightGoldenrodYellow = "#FAFAD2";
	public final static String COLOR_LightYellow = "#FFFFE0";
	public final static String COLOR_Ivory = "#FFFFF0";
	public final static String COLOR_OliveDrab = "#6B8E23";
	public final static String COLOR_DarkOliveGreen = "#556B2F";
	public final static String COLOR_DarkSeaGreen = "#8FBC8F";
	public final static String COLOR_DarkGreen = "#006400";
	public final static String COLOR_ForestGreen = "#228B22";
	public final static String COLOR_LightGreen = "#90EE90";
	public final static String COLOR_PaleGreen = "#98FB98";
	public final static String COLOR_Honeydew = "#F0FFF0";
	public final static String COLOR_SeaGreen = "#2E8B57";
	public final static String COLOR_MediumSeaGreen = "#3CB371";
	public final static String COLOR_Mintcream = "#F5FFFA";
	public final static String COLOR_MediumAquamarine = "#66CDAA";
	public final static String COLOR_Aquamarine = "#7FFFD4";
	public final static String COLOR_DarkSlateGray = "#2F4F4F";
	public final static String COLOR_PaleTurquoise = "#AFEEEE";
	public final static String COLOR_LightCyan = "#E0FFFF";
	public final static String COLOR_Azure = "#F0FFFF";
	public final static String COLOR_CadetBlue = "#5F9EA0";
	public final static String COLOR_PowderBlue = "#B0E0E6";
	public final static String COLOR_LightBlue = "#ADD8E6";
	public final static String COLOR_SkyBlue = "#87CEEB";
	public final static String COLOR_LightskyBlue = "#87CEFA";
	public final static String COLOR_SteelBlue = "#4682B4";
	public final static String COLOR_AliceBlue = "#F0F8FF";
	public final static String COLOR_SlateGray = "#708090";
	public final static String COLOR_LightSlateGray = "#778899";
	public final static String COLOR_LightSteelBlue = "#B0C4DE";
	public final static String COLOR_CornflowerBlue = "#6495ED";
	public final static String COLOR_Lavender = "#E6E6FA";
	public final static String COLOR_GhostWhite = "#F8F8FF";
	public final static String COLOR_MidnightBlue = "#191970";
	public final static String COLOR_SlateBlue = "#6A5ACD";
	public final static String COLOR_DarkSlateBlue = "#483D8B";
	public final static String COLOR_MediumSlateBlue = "#7B68EE";
	public final static String COLOR_MediumPurple = "#9370DB";
	public final static String COLOR_Indigo = "#4B0082";
	public final static String COLOR_MediumOrchid = "#BA55D3";
	public final static String COLOR_Plum = "#DDA0DD";
	public final static String COLOR_Violet = "#EE82EE";
	public final static String COLOR_Thistle = "#D8BFD8";
	public final static String COLOR_Orchid = "#DA70D6";
	public final static String COLOR_LavenderBlush = "#FFF0F5";
	public final static String COLOR_PaleVioletRed = "#DB7093";
	public final static String COLOR_Pink = "#FFC0CB";
	public final static String COLOR_LightPink = "#FFB6C1";
	public final static String COLOR_Black = "#000000";
	public final static String COLOR_DimGray = "#696969";
	public final static String COLOR_Gray = "#808080";
	public final static String COLOR_DarkGray = "#A9A9A9";
	public final static String COLOR_Silver = "#C0C0C0";
	public final static String COLOR_LightGrey = "#D3D3D3";
	public final static String COLOR_Gainsboro = "#DCDCDC";
	public final static String COLOR_WhiteSmoke = "#F5F5F5";
	public final static String COLOR_White = "#FFFFFF";

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
	public void appendAttribute(final Appendable appendable, final String sElementName,
			final int nValue) throws IOException {
		this.appendAttribute(appendable, sElementName, Integer.toString(nValue));
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
	public void appendAttribute(final Appendable appendable, final String sElementName,
			final String sValue) throws IOException {
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
	public void copyStylesFromTo(final OdsFile source, final OdsFile target) {
		target.setStyles(source.getStyles());
		target.getContent().setPageStyles(source.getContent().getPageStyles());
		target.getContent()
				.setTableStyles(source.getContent().getTableStyles());
		target.getContent().setTextStyles(source.getContent().getTextStyles());
	}

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

	public void appendAttribute(Appendable appendable, String sElementName,
			boolean b) throws IOException {
		this.appendAttribute(appendable, sElementName, Boolean.toString(b));
	}

}
