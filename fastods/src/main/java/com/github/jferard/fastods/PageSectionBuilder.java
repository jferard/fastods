/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.SimpleLength;

/**
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:footer
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:header
 *
 * @author Julien Férard
 */
@SuppressWarnings("unchecked")
public abstract class PageSectionBuilder<F extends PageSectionBuilder<F>> {
	protected Box<Text> curRegionBox;
	protected final MarginsBuilder marginsBuilder;

	protected Length minHeight;

	/**
	 * Create a new footer or header object.
	 */
	PageSectionBuilder() {
		this.minHeight = SimpleLength.cm(0.0);
		this.marginsBuilder = new MarginsBuilder();
		this.marginsBuilder.all(SimpleLength.cm(0.0));
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

	public abstract PageSection build();

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

	public Header buildHeader() {
		return new Header(this.build());
	}

	public Footer buildFooter() {
		return new Footer(this.build());
	}
}