/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.util;

import com.github.jferard.fastods.attribute.Color;
import com.github.jferard.fastods.attribute.SimpleColor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The Color class provides access to standard LibreOffice colors in hex format.
 *
 * @author Julien Férard
 */
public final class ColorHelper {
    private static ColorHelper helper;

    /**
     * Create a color from RGB values
     *
     * @param red   the red value, between 0-255
     * @param green the green value, between 0-255
     * @param blue  the blue value, between 0-255
     * @return the color
     */
    public static Color fromRGB(final int red, final int green, final int blue) {
        if (ColorHelper.helper == null) {
            ColorHelper.helper = new ColorHelper();
        }
        return ColorHelper.helper.getFromRGB(red, green, blue);
    }

    /**
     * Create a color from the hex representation
     *
     * @param hexValue e.g. "#123456"
     * @return the color
     */
    public static Color fromString(final String hexValue) {
        if (ColorHelper.helper == null) {
            ColorHelper.helper = new ColorHelper();
        }
        return ColorHelper.helper.getFromString(hexValue);
    }

    private static final int X_FF = 255;
    private static final int X_F = 15;
    private final Map<String, Color> colorByHexValue;

    /**
     * A new color helper
     */
    ColorHelper() {
        this.colorByHexValue = new HashMap<String, Color>();
        for (final Color c : SimpleColor.values()) {
            this.colorByHexValue.put(c.getValue(), c);
        }
    }

    /**
     * Helper function to create any available color string from color values.
     *
     * @param red   The red value, 0-255
     * @param green The green value, 0-255
     * @param blue  The blue value, 0-255
     * @return The hex string in the format '#rrggbb'
     */
    public Color getFromRGB(final int red, final int green, final int blue) {
        return this.getFromString(
                "#" + this.toHexString(red) + this.toHexString(green) + this.toHexString(blue));
    }

    private String toHexString(final int n) {
        if (n < 0) {
            return "00";
        }
        if (n > ColorHelper.X_FF) {
            return "ff";
        }

        final StringBuilder sbReturn = new StringBuilder();
        if (n <= ColorHelper.X_F) {
            sbReturn.append('0');
        }
        sbReturn.append(Integer.toHexString(n));
        return sbReturn.toString();
    }

    /**
     * Create a color from the hex representation
     *
     * @param hexValue e.g. "#123456"
     * @return the color
     */
    public Color getFromString(final String hexValue) {
        final String hv = hexValue.toLowerCase(Locale.US);
        Color color = this.colorByHexValue.get(hv);
        if (color == null) {
            color = new Color() {
                @Override
                public String getValue() {
                    return hv;
                }
            };
            this.colorByHexValue.put(hv, color);
        }
        return color;
    }
}
