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

package com.github.jferard.fastods.style;

import com.github.jferard.fastods.attribute.Color;
import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.style.TextProperties.Underline;

/**
 * A builder for text styles
 *
 * @author Julien Férard
 */
public class TextStyleBuilder {
    private final TextPropertiesBuilder propertiesBuilder;
    private final String name;
    private boolean visible;

    /**
     * A new builder
     */
    TextStyleBuilder(final String name) {
        this.name = name;
        this.propertiesBuilder = new TextPropertiesBuilder();
    }

    /**
     * @return the TextProperties
     */
    public TextStyle build() {
        if (this.visible) {
            return this.buildStyle(this.name);
        } else {
            return this.buildHiddenStyle(this.name);
        }
    }

    /**
     * @param name the name of the style
     * @return the TextStyle of that name
     */
    private TextStyle buildStyle(final String name) {
        return new TextStyle(name, false, this.propertiesBuilder.build());
    }

    /**
     * @param name the name of the style
     * @return the TextStyle of that name
     */
    private TextStyle buildHiddenStyle(final String name) {
        if (this.propertiesBuilder.hasFontSizePercentage()) {
            throw new IllegalArgumentException(
                    "20.183 fo:font-size: fontSizePercentage values can be used within visible " +
                            "styles only");
        }
        return new TextStyle(name, true, this.propertiesBuilder.build());
    }

    /**
     * Set the font color to color.
     *
     * @param color The color to be used in format #rrggbb e.g. #ff0000 for a red
     *              cell background
     * @return this for fluent style
     */
    public TextStyleBuilder fontColor(final Color color) {
        this.propertiesBuilder.fontColor(color);
        return this;
    }

    /**
     * Set the font name to be used for this style.
     *
     * @param fontName The font name for this TextStyle
     * @return this for fluent style
     */
    public TextStyleBuilder fontName(final String fontName) {
        this.propertiesBuilder.fontName(fontName);
        return this;
    }

    /**
     * Set the font size to the given value.
     *
     * @param fontSize - The font size
     * @return this for fluent style
     */
    public TextStyleBuilder fontSize(final Length fontSize) {
        this.propertiesBuilder.fontSize(fontSize);
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
    public TextStyleBuilder fontSizePercentage(final double percentage) {
        this.propertiesBuilder.fontSizePercentage(percentage);
        return this;
    }


    /**
     * Set the font weight to italic.
     *
     * @return true
     */
    public TextStyleBuilder fontStyleItalic() {
        this.propertiesBuilder.fontStyleItalic();
        return this;
    }

    /**
     * Set the font weight to italic.
     *
     * @return true
     */
    public TextStyleBuilder fontStyleNormal() {
        this.propertiesBuilder.fontStyleNormal();
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
    public TextStyleBuilder fontUnderlineColor(final Color color) {
        this.propertiesBuilder.fontUnderlineColor(color);
        return this;
    }

    /**
     * Set the style that should be used for the underline.
     *
     * @param style One of the TextStyle.STYLE_UNDERLINE
     * @return this for fluent style
     */
    public TextStyleBuilder fontUnderlineStyle(final Underline style) {
        this.propertiesBuilder.fontUnderlineStyle(style);
        return this;
    }

    /**
     * Set the font weight to bold.
     *
     * @return this for fluent style
     */
    public TextStyleBuilder fontWeightBold() {
        this.propertiesBuilder.fontWeightBold();
        return this;
    }

    /**
     * Set the font weight to normal.
     *
     * @return this for fluent style
     */
    public TextStyleBuilder fontWeightNormal() {
        this.propertiesBuilder.fontWeightNormal();
        return this;
    }

    /**
     * Set the style visible.
     *
     * @return this for fluent style
     */
    public TextStyleBuilder visible() {
        this.visible = true;
        return this;
    }
}
