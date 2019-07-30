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

package com.github.jferard.fastods;

import com.github.jferard.fastods.style.MarginsBuilder;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.Box;
import com.github.jferard.fastods.attribute.Length;

/**
 * A builder for a page section. It will build a header or a footer
 *
 * @param <F> L-bound for the page section builder itself
 * @author Julien Férard
 */
@SuppressWarnings("unchecked")
public abstract class PageSectionBuilder<F extends PageSectionBuilder<F>> {
    /**
     * The region box that is currently written
     */
    protected Box<Text> curRegionBox;
    /**
     * a margins builder
     */
    protected final MarginsBuilder marginsBuilder;

    /**
     * the minimum height of the page section
     */
    protected Length minHeight;

    /**
     * Create a new footer or header object.
     */
    PageSectionBuilder() {
        this.minHeight = Length.NULL_LENGTH;
        this.marginsBuilder = new MarginsBuilder();
        this.marginsBuilder.all(Length.NULL_LENGTH);
    }

    /**
     * Set the margin at the top, bottom, left and right.
     *
     * @param margin the margin size
     * @return this for fluent style
     */
    public F allMargins(final Length margin) {
        this.marginsBuilder.all(margin);
        return (F) this;
    }

    /**
     * For public calls, see buildFooter or buildHeader
     *
     * @return the page section
     */
    protected abstract PageSection build();

    /**
     * Set the text content of the section
     *
     * @param string the text
     * @return this for fluent style
     */
    public F content(final String string) {
        this.curRegionBox.set(Text.content(string));
        return (F) this;
    }

    /**
     * Set the bottom margin. margin is a length value
     *
     * @param margin the margin size
     * @return this for fluent style
     */
    public F marginBottom(final Length margin) {
        this.marginsBuilder.bottom(margin);
        return (F) this;
    }

    /**
     * Set the left margin. Margin is a length value
     *
     * @param margin the margin size
     * @return this for fluent style
     */
    public F marginLeft(final Length margin) {
        this.marginsBuilder.left(margin);
        return (F) this;
    }

    /**
     * Set the right margin. Margin is a length value
     *
     * @param margin the margin size
     * @return this for fluent style
     */
    public F marginRight(final Length margin) {
        this.marginsBuilder.right(margin);
        return (F) this;
    }

    /**
     * Set the top margin. Margin is a length value
     *
     * @param margin the margin size
     * @return this for fluent style
     */
    public F marginTop(final Length margin) {
        this.marginsBuilder.top(margin);
        return (F) this;
    }

    /**
     * Set the minimum height. Min height is a length value
     *
     * @param height the height of the footer/header
     * @return this for fluent style
     */
    public F minHeight(final Length height) {
        this.minHeight = height;
        return (F) this;
    }

    /**
     * @param text The text to write
     * @param ts   the text style
     * @return this for fluent style
     */
    public F styledContent(final String text, final TextStyle ts) {
        this.curRegionBox.set(Text.styledContent(text, ts));
        return (F) this;
    }

    /**
     * @param text The text to write
     * @return this for fluent style
     */
    public F text(final Text text) {
        this.curRegionBox.set(text);
        return (F) this;
    }

    /**
     * @return the header
     */
    public Header buildHeader() {
        return new Header(this.build());
    }

    /**
     * @return the footer
     */
    public Footer buildFooter() {
        return new Footer(this.build());
    }
}