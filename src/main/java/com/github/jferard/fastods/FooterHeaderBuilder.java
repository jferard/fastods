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

/**
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:footer
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:header
 *
 * @author Julien Férard
 */
@SuppressWarnings("unchecked")
public abstract class FooterHeaderBuilder<F extends FooterHeaderBuilder<F>> {
	/**
	 * The OdsDocument where this object belong to.
	 */
	protected final FooterHeader.Type footerHeaderType;
	protected Box<Text> curRegionBox;
	protected MarginsBuilder marginsBuilder;

	protected String minHeight;

	/**
	 * Create a new footer or header object.
	 */
	FooterHeaderBuilder(final FooterHeader.Type footerHeaderType) {
		this.footerHeaderType = footerHeaderType;
		this.minHeight = "0cm";
		this.marginsBuilder = new MarginsBuilder();
		this.marginsBuilder.all("0cm");
	}

	/**
	 * <p>Set the margin at the top, bottom, left and right. Margin is a length value
	 * expressed as a number followed by a unit of measurement e.g. 1.5cm or
	 * 12px.</p>
	 * <p>The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).</p>
	 *
	 * @param margin the margin size
	 * @return this for fluent style
	 */
	public F allMargins(final String margin) {
		this.marginsBuilder.all(margin);
		return (F) this;
	}

	public abstract FooterHeader build();

	public F content(final String string) {
		this.curRegionBox.set(Text.content(string));
		return (F) this;
	}

	/**
	 * <p>Set the bottom margin. margin is a length value
	 * expressed as a number followed by a unit of measurement e.g. 1.5cm or
	 * 12px.</p>
	 * <p>The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).</p>
	 *
	 * @param margin the margin size
	 * @return this for fluent style
	 */
	public F marginBottom(final String margin) {
		this.marginsBuilder.bottom(margin);
		return (F) this;
	}

	/**
	 * <p>Set the left margin. Margin is a length value
	 * expressed as a number followed by a unit of measurement e.g. 1.5cm or
	 * 12px.</p>
	 * <p>The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).</p>
	 *
	 * @param margin the margin size
	 * @return this for fluent style
	 */
	public F marginLeft(final String margin) {
		this.marginsBuilder.left(margin);
		return (F) this;
	}

	/**
	 * <p>Set the right margin. Margin is a length value
	 * expressed as a number followed by a unit of measurement e.g. 1.5cm or
	 * 12px.</p>
	 * <p>The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).</p>
	 *
	 * @param margin the margin size
	 * @return this for fluent style
	 */
	public F marginRight(final String margin) {
		this.marginsBuilder.right(margin);
		return (F) this;
	}

	/**
	 * <p>Set the top margin. Margin is a length value
	 * expressed as a number followed by a unit of measurement e.g. 1.5cm or
	 * 12px.</p>
	 * <p>The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).</p>
	 *
	 * @param margin the margin size
	 * @return this for fluent style
	 */
	public F marginTop(final String margin) {
		this.marginsBuilder.top(margin);
		return (F) this;
	}

	/**
	 * <p>Set the minimum height. Min height is a length value
	 * expressed as a number followed by a unit of measurement e.g. 1.5cm or
	 * 12px.</p>
	 * <p>The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).</p>
	 *
	 * @param height the height of the footer/header
	 * @return this for fluent style
	 */
	public F minHeight(final String height) {
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
}