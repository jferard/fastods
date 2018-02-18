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

package com.github.jferard.fastods;

/**
 * The Color class provides access to standard LibreOffice colors in hex format.
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public enum SimpleColor implements Color {
    /**
     * No color
     **/
    NONE(""),

	/**
	 * The color aliceblue
	 **/
	ALICEBLUE("#F0F8FF"),

	/**
	 * The color antiquewhite
	 **/
	ANTIQUEWHITE("#FAEBD7"),

	/**
	 * The color aquamarine
	 **/
	AQUAMARINE("#7FFFD4"),

	/**
	 * The color azure
	 **/
	AZURE("#F0FFFF"),

	/**
	 * The color beige
	 **/
	BEIGE("#F5F5DC"),

	/**
	 * The color bisque
	 **/
	BISQUE("#FFE4C4"),

	/**
	 * The color black
	 **/
	BLACK("#000000"),

	/**
	 * The color blanchedalmond
	 **/
	BLANCHEDALMOND("#FFEBCD"),

	/**
	 * The color blue
	 **/
	BLUE("#0000FF"),

	/**
	 * The color blueviolet
	 **/
	BLUEVIOLET("#8A2BE2"),

	/**
	 * The color brown
	 **/
	BROWN("#A52A2A"),

	/**
	 * The color burlywood
	 **/
	BURLYWOOD("#DEB887"),

	/**
	 * The color cadetblue
	 **/
	CADETBLUE("#5F9EA0"),

	/**
	 * The color chartreuse
	 **/
	CHARTREUSE("#7FFF00"),

	/**
	 * The color chocolate
	 **/
	CHOCOLATE("#D2691E"),

	/**
	 * The color coral
	 **/
	CORAL("#FF7F50"),

	/**
	 * The color cornflowerblue
	 **/
	CORNFLOWERBLUE("#6495ED"),

	/**
	 * The color cornsilk
	 **/
	CORNSILK("#FFF8DC"),

	/**
	 * The color crimson
	 **/
	CRIMSON("#DC143C"),

	/**
	 * The color cyan
	 **/
	CYAN("#00FFFF"),

	/**
	 * The color darkblue
	 **/
	DARKBLUE("#00008B"),

	/**
	 * The color darkcyan
	 **/
	DARKCYAN("#008B8B"),

	/**
	 * The color darkgoldenrod
	 **/
	DARKGOLDENROD("#B8860B"),

	/**
	 * The color darkgray
	 **/
	DARKGRAY("#A9A9A9"),

	/**
	 * The color darkgreen
	 **/
	DARKGREEN("#006400"),

	/**
	 * The color darkkhaki
	 **/
	DARKKHAKI("#BDB76B"),

	/**
	 * The color darkmagenta
	 **/
	DARKMAGENTA("#8B008B"),

	/**
	 * The color darkolivegreen
	 **/
	DARKOLIVEGREEN("#556B2F"),

	/**
	 * The color darkorange
	 **/
	DARKORANGE("#FF8C00"),

	/**
	 * The color darkorchid
	 **/
	DARKORCHID("#9932CC"),

	/**
	 * The color darkred
	 **/
	DARKRED("#8B0000"),

	/**
	 * The color darksalmon
	 **/
	DARKSALMON("#E9967A"),

	/**
	 * The color darkseagreen
	 **/
	DARKSEAGREEN("#8FBC8F"),

	/**
	 * The color darkslateblue
	 **/
	DARKSLATEBLUE("#483D8B"),

	/**
	 * The color darkslategray
	 **/
	DARKSLATEGRAY("#2F4F4F"),

	/**
	 * The color darkturquoise
	 **/
	DARKTURQUOISE("#00CED1"),

	/**
	 * The color darkviolet
	 **/
	DARKVIOLET("#9400D3"),

	/**
	 * The color deeppink
	 **/
	DEEPPINK("#FF1493"),

	/**
	 * The color deepskyblue
	 **/
	DEEPSKYBLUE("#00BFFF"),

	/**
	 * The color dimgray
	 **/
	DIMGRAY("#696969"),

	/**
	 * The color dodgerblue
	 **/
	DODGERBLUE("#1E90FF"),

	/**
	 * The color firebrick
	 **/
	FIREBRICK("#B22222"),

	/**
	 * The color floralwhite
	 **/
	FLORALWHITE("#FFFAF0"),

	/**
	 * The color forestgreen
	 **/
	FORESTGREEN("#228B22"),

	/**
	 * The color gainsboro
	 **/
	GAINSBORO("#DCDCDC"),

	/**
	 * The color ghostwhite
	 **/
	GHOSTWHITE("#F8F8FF"),

	/**
	 * The color gold
	 **/
	GOLD("#FFD700"),

	/**
	 * The color goldenrod
	 **/
	GOLDENROD("#DAA520"),

	/**
	 * The color gray
	 **/
	GRAY("#808080"),

	/**
	 * The color gray16
	 **/
	GRAY16("#292929"),

	/**
	 * The color gray32
	 **/
	GRAY32("#525252"),

	/**
	 * The color gray48
	 **/
	GRAY48("#7A7A7A"),

	/**
	 * The color gray64
	 **/
	GRAY64("#A3A3A3"),

	/**
	 * The color gray80
	 **/
	GRAY80("#CCCCCC"),

	/**
	 * The color green
	 **/
	GREEN("#008000"),

	/**
	 * The color greenyellow
	 **/
	GREENYELLOW("#ADFF2F"),

	/**
	 * The color honeydew
	 **/
	HONEYDEW("#F0FFF0"),

	/**
	 * The color hotpink
	 **/
	HOTPINK("#FF69B4"),

	/**
	 * The color indianred
	 **/
	INDIANRED("#CD5C5C"),

	/**
	 * The color indigo
	 **/
	INDIGO("#4B0082"),

	/**
	 * The color ivory
	 **/
	IVORY("#FFFFF0"),

	/**
	 * The color khaki
	 **/
	KHAKI("#F0E68C"),

	/**
	 * The color lavender
	 **/
	LAVENDER("#E6E6FA"),

	/**
	 * The color lavenderblush
	 **/
	LAVENDERBLUSH("#FFF0F5"),

	/**
	 * The color lawngreen
	 **/
	LAWNGREEN("#7CFC00"),

	/**
	 * The color lemonchiffon
	 **/
	LEMONCHIFFON("#FFFACD"),

	/**
	 * The color lightblue
	 **/
	LIGHTBLUE("#ADD8E6"),

	/**
	 * The color lightcoral
	 **/
	LIGHTCORAL("#F08080"),

	/**
	 * The color lightcyan
	 **/
	LIGHTCYAN("#E0FFFF"),

	/**
	 * The color lightgoldenrodyellow
	 **/
	LIGHTGOLDENRODYELLOW("#FAFAD2"),

	/**
	 * The color lightgreen
	 **/
	LIGHTGREEN("#90EE90"),

	/**
	 * The color lightgrey
	 **/
	LIGHTGREY("#D3D3D3"),

	/**
	 * The color lightpink
	 **/
	LIGHTPINK("#FFB6C1"),

	/**
	 * The color lightsalmon
	 **/
	LIGHTSALMON("#FFA07A"),

	/**
	 * The color lightseagreen
	 **/
	LIGHTSEAGREEN("#20B2AA"),

	/**
	 * The color lightskyblue
	 **/
	LIGHTSKYBLUE("#87CEFA"),

	/**
	 * The color lightslategray
	 **/
	LIGHTSLATEGRAY("#778899"),

	/**
	 * The color lightsteelblue
	 **/
	LIGHTSTEELBLUE("#B0C4DE"),

	/**
	 * The color lightyellow
	 **/
	LIGHTYELLOW("#FFFFE0"),

	/**
	 * The color lime
	 **/
	LIME("#00FF00"),

	/**
	 * The color limegreen
	 **/
	LIMEGREEN("#32CD32"),

	/**
	 * The color linen
	 **/
	LINEN("#FAF0E6"),

	/**
	 * The color magenta
	 **/
	MAGENTA("#FF00FF"),

	/**
	 * The color maroon
	 **/
	MAROON("#800000"),

	/**
	 * The color mediumaquamarine
	 **/
	MEDIUMAQUAMARINE("#66CDAA"),

	/**
	 * The color mediumblue
	 **/
	MEDIUMBLUE("#0000CD"),

	/**
	 * The color mediumorchid
	 **/
	MEDIUMORCHID("#BA55D3"),

	/**
	 * The color mediumpurple
	 **/
	MEDIUMPURPLE("#9370DB"),

	/**
	 * The color mediumseagreen
	 **/
	MEDIUMSEAGREEN("#3CB371"),

	/**
	 * The color mediumslateblue
	 **/
	MEDIUMSLATEBLUE("#7B68EE"),

	/**
	 * The color mediumspringgreen
	 **/
	MEDIUMSPRINGGREEN("#00FA9A"),

	/**
	 * The color mediumturquoise
	 **/
	MEDIUMTURQUOISE("#48D1CC"),

	/**
	 * The color mediumvioletred
	 **/
	MEDIUMVIOLETRED("#C71585"),

	/**
	 * The color midnightblue
	 **/
	MIDNIGHTBLUE("#191970"),

	/**
	 * The color mintcream
	 **/
	MINTCREAM("#F5FFFA"),

	/**
	 * The color mistyrose
	 **/
	MISTYROSE("#FFE4E1"),

	/**
	 * The color moccasin
	 **/
	MOCCASIN("#FFE4B5"),

	/**
	 * The color navajowhite
	 **/
	NAVAJOWHITE("#FFDEAD"),

	/**
	 * The color navy
	 **/
	NAVY("#000080"),

	/**
	 * The color oldlace
	 **/
	OLDLACE("#FDF5E6"),

	/**
	 * The color olive
	 **/
	OLIVE("#808000"),

	/**
	 * The color olivedrab
	 **/
	OLIVEDRAB("#6B8E23"),

	/**
	 * The color orange
	 **/
	ORANGE("#FFA500"),

	/**
	 * The color orangered
	 **/
	ORANGERED("#FF4500"),

	/**
	 * The color orchid
	 **/
	ORCHID("#DA70D6"),

	/**
	 * The color palegoldenrod
	 **/
	PALEGOLDENROD("#EEE8AA"),

	/**
	 * The color palegreen
	 **/
	PALEGREEN("#98FB98"),

	/**
	 * The color paleturquoise
	 **/
	PALETURQUOISE("#AFEEEE"),

	/**
	 * The color palevioletred
	 **/
	PALEVIOLETRED("#DB7093"),

	/**
	 * The color papayawhip
	 **/
	PAPAYAWHIP("#FFEFD5"),

	/**
	 * The color peachpuff
	 **/
	PEACHPUFF("#FFDAB9"),

	/**
	 * The color peru
	 **/
	PERU("#CD853F"),

	/**
	 * The color pink
	 **/
	PINK("#FFC0CB"),

	/**
	 * The color plum
	 **/
	PLUM("#DDA0DD"),

	/**
	 * The color powderblue
	 **/
	POWDERBLUE("#B0E0E6"),

	/**
	 * The color purple
	 **/
	PURPLE("#800080"),

	/**
	 * The color red
	 **/
	RED("#FF0000"),

	/**
	 * The color rosybrown
	 **/
	ROSYBROWN("#BC8F8F"),

	/**
	 * The color royalblue
	 **/
	ROYALBLUE("#4169E1"),

	/**
	 * The color saddlebrown
	 **/
	SADDLEBROWN("#8B4513"),

	/**
	 * The color salmon
	 **/
	SALMON("#FA8072"),

	/**
	 * The color sandybrown
	 **/
	SANDYBROWN("#F4A460"),

	/**
	 * The color seagreen
	 **/
	SEAGREEN("#2E8B57"),

	/**
	 * The color seashell
	 **/
	SEASHELL("#FFF5EE"),

	/**
	 * The color sienna
	 **/
	SIENNA("#A0522D"),

	/**
	 * The color silver
	 **/
	SILVER("#C0C0C0"),

	/**
	 * The color skyblue
	 **/
	SKYBLUE("#87CEEB"),

	/**
	 * The color slateblue
	 **/
	SLATEBLUE("#6A5ACD"),

	/**
	 * The color slategray
	 **/
	SLATEGRAY("#708090"),

	/**
	 * The color snow
	 **/
	SNOW("#FFFAFA"),

	/**
	 * The color springgreen
	 **/
	SPRINGGREEN("#00FF7F"),

	/**
	 * The color steelblue
	 **/
	STEELBLUE("#4682B4"),

	/**
	 * The color tan
	 **/
	TAN("#D2B48C"),

	/**
	 * The color teal
	 **/
	TEAL("#008080"),

	/**
	 * The color thistle
	 **/
	THISTLE("#D8BFD8"),

	/**
	 * The color tomato
	 **/
	TOMATO("#FF6347"),

	/**
	 * The color turquoise
	 **/
	TURQUOISE("#40E0D0"),

	/**
	 * The color violet
	 **/
	VIOLET("#EE82EE"),

	/**
	 * The color wheat
	 **/
	WHEAT("#F5DEB3"),

	/**
	 * The color white
	 **/
	WHITE("#FFFFFF"),

	/**
	 * The color whitesmoke
	 **/
	WHITESMOKE("#F5F5F5"),

	/**
	 * The color yellow
	 **/
	YELLOW("#FFFF00"),

	/**
	 * The color yellowgreen
	 **/
	YELLOWGREEN("#9ACD32");

	/**
	 * The color aqua
	 **/
	public static final SimpleColor AQUA = SimpleColor.CYAN;

	/**
	 * The color fuchsia
	 **/
	public static final SimpleColor FUCHSIA = SimpleColor.MAGENTA;

    /**
     * The color gray96
     **/
    public static final SimpleColor GRAY96 = SimpleColor.WHITESMOKE;

	private final String hexValue;

	SimpleColor(final String hexValue) {
		this.hexValue = hexValue;
	}

	public String hexValue() {
		return this.hexValue;
	}
}
