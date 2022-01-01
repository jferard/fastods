/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods.attribute;

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
    ALICEBLUE("#f0f8ff"),

    /**
     * The color antiquewhite
     **/
    ANTIQUEWHITE("#faebd7"),

    /**
     * The color aquamarine
     **/
    AQUAMARINE("#7fffd4"),

    /**
     * The color azure
     **/
    AZURE("#f0ffff"),

    /**
     * The color beige
     **/
    BEIGE("#f5f5dc"),

    /**
     * The color bisque
     **/
    BISQUE("#ffe4c4"),

    /**
     * The color black
     **/
    BLACK("#000000"),

    /**
     * The color blanchedalmond
     **/
    BLANCHEDALMOND("#ffebcd"),

    /**
     * The color blue
     **/
    BLUE("#0000ff"),

    /**
     * The color blueviolet
     **/
    BLUEVIOLET("#8a2be2"),

    /**
     * The color brown
     **/
    BROWN("#a52a2a"),

    /**
     * The color burlywood
     **/
    BURLYWOOD("#deb887"),

    /**
     * The color cadetblue
     **/
    CADETBLUE("#5f9ea0"),

    /**
     * The color chartreuse
     **/
    CHARTREUSE("#7fff00"),

    /**
     * The color chocolate
     **/
    CHOCOLATE("#d2691e"),

    /**
     * The color coral
     **/
    CORAL("#ff7f50"),

    /**
     * The color cornflowerblue
     **/
    CORNFLOWERBLUE("#6495ed"),

    /**
     * The color cornsilk
     **/
    CORNSILK("#fff8dc"),

    /**
     * The color crimson
     **/
    CRIMSON("#dc143c"),

    /**
     * The color cyan
     **/
    CYAN("#00ffff"),

    /**
     * The color darkblue
     **/
    DARKBLUE("#00008b"),

    /**
     * The color darkcyan
     **/
    DARKCYAN("#008b8b"),

    /**
     * The color darkgoldenrod
     **/
    DARKGOLDENROD("#b8860b"),

    /**
     * The color darkgray
     **/
    DARKGRAY("#a9a9a9"),

    /**
     * The color darkgreen
     **/
    DARKGREEN("#006400"),

    /**
     * The color darkkhaki
     **/
    DARKKHAKI("#bdb76b"),

    /**
     * The color darkmagenta
     **/
    DARKMAGENTA("#8b008b"),

    /**
     * The color darkolivegreen
     **/
    DARKOLIVEGREEN("#556b2f"),

    /**
     * The color darkorange
     **/
    DARKORANGE("#ff8c00"),

    /**
     * The color darkorchid
     **/
    DARKORCHID("#9932cc"),

    /**
     * The color darkred
     **/
    DARKRED("#8b0000"),

    /**
     * The color darksalmon
     **/
    DARKSALMON("#e9967a"),

    /**
     * The color darkseagreen
     **/
    DARKSEAGREEN("#8fbc8f"),

    /**
     * The color darkslateblue
     **/
    DARKSLATEBLUE("#483d8b"),

    /**
     * The color darkslategray
     **/
    DARKSLATEGRAY("#2f4f4f"),

    /**
     * The color darkturquoise
     **/
    DARKTURQUOISE("#00ced1"),

    /**
     * The color darkviolet
     **/
    DARKVIOLET("#9400d3"),

    /**
     * The color deeppink
     **/
    DEEPPINK("#ff1493"),

    /**
     * The color deepskyblue
     **/
    DEEPSKYBLUE("#00bfff"),

    /**
     * The color dimgray
     **/
    DIMGRAY("#696969"),

    /**
     * The color dodgerblue
     **/
    DODGERBLUE("#1e90ff"),

    /**
     * The color firebrick
     **/
    FIREBRICK("#b22222"),

    /**
     * The color floralwhite
     **/
    FLORALWHITE("#fffaf0"),

    /**
     * The color forestgreen
     **/
    FORESTGREEN("#228b22"),

    /**
     * The color gainsboro
     **/
    GAINSBORO("#dcdcdc"),

    /**
     * The color ghostwhite
     **/
    GHOSTWHITE("#f8f8ff"),

    /**
     * The color gold
     **/
    GOLD("#ffd700"),

    /**
     * The color goldenrod
     **/
    GOLDENROD("#daa520"),

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
    GRAY48("#7a7a7a"),

    /**
     * The color gray64
     **/
    GRAY64("#a3a3a3"),

    /**
     * The color gray80
     **/
    GRAY80("#cccccc"),

    /**
     * The color green
     **/
    GREEN("#008000"),

    /**
     * The color greenyellow
     **/
    GREENYELLOW("#adff2f"),

    /**
     * The color honeydew
     **/
    HONEYDEW("#f0fff0"),

    /**
     * The color hotpink
     **/
    HOTPINK("#ff69b4"),

    /**
     * The color indianred
     **/
    INDIANRED("#cd5c5c"),

    /**
     * The color indigo
     **/
    INDIGO("#4b0082"),

    /**
     * The color ivory
     **/
    IVORY("#fffff0"),

    /**
     * The color khaki
     **/
    KHAKI("#f0e68c"),

    /**
     * The color lavender
     **/
    LAVENDER("#e6e6fa"),

    /**
     * The color lavenderblush
     **/
    LAVENDERBLUSH("#fff0f5"),

    /**
     * The color lawngreen
     **/
    LAWNGREEN("#7cfc00"),

    /**
     * The color lemonchiffon
     **/
    LEMONCHIFFON("#fffacd"),

    /**
     * The color lightblue
     **/
    LIGHTBLUE("#add8e6"),

    /**
     * The color lightcoral
     **/
    LIGHTCORAL("#f08080"),

    /**
     * The color lightcyan
     **/
    LIGHTCYAN("#e0ffff"),

    /**
     * The color lightgoldenrodyellow
     **/
    LIGHTGOLDENRODYELLOW("#fafad2"),

    /**
     * The color lightgreen
     **/
    LIGHTGREEN("#90ee90"),

    /**
     * The color lightgrey
     **/
    LIGHTGREY("#d3d3d3"),

    /**
     * The color lightpink
     **/
    LIGHTPINK("#ffb6c1"),

    /**
     * The color lightsalmon
     **/
    LIGHTSALMON("#ffa07a"),

    /**
     * The color lightseagreen
     **/
    LIGHTSEAGREEN("#20b2aa"),

    /**
     * The color lightskyblue
     **/
    LIGHTSKYBLUE("#87cefa"),

    /**
     * The color lightslategray
     **/
    LIGHTSLATEGRAY("#778899"),

    /**
     * The color lightsteelblue
     **/
    LIGHTSTEELBLUE("#b0c4de"),

    /**
     * The color lightyellow
     **/
    LIGHTYELLOW("#ffffe0"),

    /**
     * The color lime
     **/
    LIME("#00ff00"),

    /**
     * The color limegreen
     **/
    LIMEGREEN("#32cd32"),

    /**
     * The color linen
     **/
    LINEN("#faf0e6"),

    /**
     * The color magenta
     **/
    MAGENTA("#ff00ff"),

    /**
     * The color maroon
     **/
    MAROON("#800000"),

    /**
     * The color mediumaquamarine
     **/
    MEDIUMAQUAMARINE("#66cdaa"),

    /**
     * The color mediumblue
     **/
    MEDIUMBLUE("#0000cd"),

    /**
     * The color mediumorchid
     **/
    MEDIUMORCHID("#ba55d3"),

    /**
     * The color mediumpurple
     **/
    MEDIUMPURPLE("#9370db"),

    /**
     * The color mediumseagreen
     **/
    MEDIUMSEAGREEN("#3cb371"),

    /**
     * The color mediumslateblue
     **/
    MEDIUMSLATEBLUE("#7b68ee"),

    /**
     * The color mediumspringgreen
     **/
    MEDIUMSPRINGGREEN("#00fa9a"),

    /**
     * The color mediumturquoise
     **/
    MEDIUMTURQUOISE("#48d1cc"),

    /**
     * The color mediumvioletred
     **/
    MEDIUMVIOLETRED("#c71585"),

    /**
     * The color midnightblue
     **/
    MIDNIGHTBLUE("#191970"),

    /**
     * The color mintcream
     **/
    MINTCREAM("#f5fffa"),

    /**
     * The color mistyrose
     **/
    MISTYROSE("#ffe4e1"),

    /**
     * The color moccasin
     **/
    MOCCASIN("#ffe4b5"),

    /**
     * The color navajowhite
     **/
    NAVAJOWHITE("#ffdead"),

    /**
     * The color navy
     **/
    NAVY("#000080"),

    /**
     * The color oldlace
     **/
    OLDLACE("#fdf5e6"),

    /**
     * The color olive
     **/
    OLIVE("#808000"),

    /**
     * The color olivedrab
     **/
    OLIVEDRAB("#6b8e23"),

    /**
     * The color orange
     **/
    ORANGE("#ffa500"),

    /**
     * The color orangered
     **/
    ORANGERED("#ff4500"),

    /**
     * The color orchid
     **/
    ORCHID("#da70d6"),

    /**
     * The color palegoldenrod
     **/
    PALEGOLDENROD("#eee8aa"),

    /**
     * The color palegreen
     **/
    PALEGREEN("#98fb98"),

    /**
     * The color paleturquoise
     **/
    PALETURQUOISE("#afeeee"),

    /**
     * The color palevioletred
     **/
    PALEVIOLETRED("#db7093"),

    /**
     * The color papayawhip
     **/
    PAPAYAWHIP("#ffefd5"),

    /**
     * The color peachpuff
     **/
    PEACHPUFF("#ffdab9"),

    /**
     * The color peru
     **/
    PERU("#cd853f"),

    /**
     * The color pink
     **/
    PINK("#ffc0cb"),

    /**
     * The color plum
     **/
    PLUM("#dda0dd"),

    /**
     * The color powderblue
     **/
    POWDERBLUE("#b0e0e6"),

    /**
     * The color purple
     **/
    PURPLE("#800080"),

    /**
     * The color red
     **/
    RED("#ff0000"),

    /**
     * The color rosybrown
     **/
    ROSYBROWN("#bc8f8f"),

    /**
     * The color royalblue
     **/
    ROYALBLUE("#4169e1"),

    /**
     * The color saddlebrown
     **/
    SADDLEBROWN("#8b4513"),

    /**
     * The color salmon
     **/
    SALMON("#fa8072"),

    /**
     * The color sandybrown
     **/
    SANDYBROWN("#f4a460"),

    /**
     * The color seagreen
     **/
    SEAGREEN("#2e8b57"),

    /**
     * The color seashell
     **/
    SEASHELL("#fff5ee"),

    /**
     * The color sienna
     **/
    SIENNA("#a0522d"),

    /**
     * The color silver
     **/
    SILVER("#c0c0c0"),

    /**
     * The color skyblue
     **/
    SKYBLUE("#87ceeb"),

    /**
     * The color slateblue
     **/
    SLATEBLUE("#6a5acd"),

    /**
     * The color slategray
     **/
    SLATEGRAY("#708090"),

    /**
     * The color snow
     **/
    SNOW("#fffafa"),

    /**
     * The color springgreen
     **/
    SPRINGGREEN("#00ff7f"),

    /**
     * The color steelblue
     **/
    STEELBLUE("#4682b4"),

    /**
     * The color tan
     **/
    TAN("#d2b48c"),

    /**
     * The color teal
     **/
    TEAL("#008080"),

    /**
     * The color thistle
     **/
    THISTLE("#d8bfd8"),

    /**
     * The color tomato
     **/
    TOMATO("#ff6347"),

    /**
     * The color turquoise
     **/
    TURQUOISE("#40e0d0"),

    /**
     * The color violet
     **/
    VIOLET("#ee82ee"),

    /**
     * The color wheat
     **/
    WHEAT("#f5deb3"),

    /**
     * The color white
     **/
    WHITE("#ffffff"),

    /**
     * The color whitesmoke
     **/
    WHITESMOKE("#f5f5f5"),

    /**
     * The color yellow
     **/
    YELLOW("#ffff00"),

    /**
     * The color yellowgreen
     **/
    YELLOWGREEN("#9acd32");

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

    @Override
    public String getValue() {
        return this.hexValue;
    }
}
