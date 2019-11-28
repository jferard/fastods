/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.attribute.Color;
import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.attribute.SimpleColor;
import com.github.jferard.fastods.style.TextProperties.Underline;

/**
 * @author Julien Férard
 */
public class TextPropertiesBuilder {
    private Color fontColor;
    private String fontName;
    private String fontStyle;
    private Color fontUnderlineColor;
    private Underline fontUnderlineStyle;
    private String fontWeight;
    private Length fontSizeLength;
    private double fontSizePercentage;

    /**
     * A new builder
     */
    TextPropertiesBuilder() {
        this.fontSizePercentage = -1;
        this.fontColor = SimpleColor.NONE;
        this.fontUnderlineColor = SimpleColor.NONE;
    }

    public TextPropertiesBuilder(final Color fontColor, final String fontName,
                                 final String fontWeight, final String fontStyle,
                                 final double fontSizePercentage, final Length fontSizeLength,
                                 final Color fontUnderlineColor,
                                 final Underline fontUnderlineStyle) {
        this.fontColor = fontColor;
        this.fontName = fontName;
        this.fontWeight = fontWeight;
        this.fontStyle = fontStyle;
        this.fontSizePercentage = fontSizePercentage;
        this.fontSizeLength = fontSizeLength;
        this.fontUnderlineColor = fontUnderlineColor;
        this.fontUnderlineStyle = fontUnderlineStyle;
    }

    /**
     * @return the TextProperties
     */
    public TextProperties build() {
        return new TextProperties(this.fontColor, this.fontName, this.fontWeight, this.fontStyle,
                this.fontSizePercentage, this.fontSizeLength, this.fontUnderlineColor,
                this.fontUnderlineStyle);
    }

    /**
     * @param name the name of the style
     * @return the TextStyle of that name
     */
    public TextStyle buildStyle(final String name) {
        return new TextStyle(name, false, this.build());
    }

    /**
     * @param name the name of the style
     * @return the TextStyle of that name
     */
    public TextStyle buildHiddenStyle(final String name) {
        if (this.fontSizePercentage > 0) {
            throw new IllegalArgumentException(
                    "20.183 fo:font-size: fontSizePercentage values can be used within common " +
                            "styles only");
        }
        return new TextStyle(name, true, this.build());
    }

    /**
     * Set the font color to color.
     *
     * @param color The color to be used in format #rrggbb e.g. #ff0000 for a red
     *              cell background
     * @return this for fluent style
     */
    public TextPropertiesBuilder fontColor(final Color color) {
        this.fontColor = color;
        return this;
    }

    /**
     * Set the font name to be used for this style.
     *
     * @param fontName The font name for this TextStyle
     * @return this for fluent style
     */
    public TextPropertiesBuilder fontName(final String fontName) {
        this.fontName = fontName;
        return this;
    }

    /**
     * Set the font size to the given value.
     *
     * @param fontSize - The font size
     * @return this for fluent style
     */
    public TextPropertiesBuilder fontSize(final Length fontSize) {
        this.fontSizePercentage = -1;
        this.fontSizeLength = fontSize;
        return this;
    }

    /**
     * Set the font size to the given fontSizePercentage.
     * See 20.183 fo:font-size
     * "fontSizePercentage values can be used within common styles only and are based on
     * the font height of the parent style rather than to the font height
     * of the attributes neighborhood"
     *
     * @param percentage the font size as a fontSizePercentage.
     * @return this for fluent style
     */
    public TextPropertiesBuilder fontSizePercentage(final double percentage) {
        this.fontSizeLength = null;
        this.fontSizePercentage = percentage;
        return this;
    }


    /**
     * Set the font weight to italic.
     *
     * @return true
     */
    public TextPropertiesBuilder fontStyleItalic() {
        this.fontStyle = "italic";
        return this;
    }

    /**
     * Set the font weight to italic.
     *
     * @return true
     */
    public TextPropertiesBuilder fontStyleNormal() {
        this.fontStyle = "normal";
        return this;
    }

    /**
     * Set the font underline color to color. Use an empty string to reset it to
     * 'auto'.
     *
     * @param color The color to be used in format #rrggbb e.g. #ff0000 for a red
     *              cell background.
     * @return this for fluent style
     */
    public TextPropertiesBuilder fontUnderlineColor(final Color color) {
        this.fontUnderlineColor = color;
        return this;
    }

    /**
     * Set the style that should be used for the underline.
     *
     * @param style One of the TextStyle.STYLE_UNDERLINE
     * @return this for fluent style
     */
    public TextPropertiesBuilder fontUnderlineStyle(final Underline style) {
        this.fontUnderlineStyle = style;
        return this;
    }

    /**
     * Set the font weight to bold.
     *
     * @return this for fluent style
     */
    public TextPropertiesBuilder fontWeightBold() {
        this.fontWeight = "bold";
        return this;
    }

    /**
     * Set the font weight to normal.
     *
     * @return this for fluent style
     */
    public TextPropertiesBuilder fontWeightNormal() {
        this.fontWeight = "normal";
        return this;
    }
}
