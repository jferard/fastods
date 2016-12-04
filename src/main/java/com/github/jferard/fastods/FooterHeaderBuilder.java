/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
abstract class FooterHeaderBuilder<F extends FooterHeaderBuilder<F>> {
	protected Box<Text> curRegionBox;
	/**
	 * The OdsFile where this object belong to.
	 */
	protected final FooterHeader.Type footerHeaderType;
	protected MarginsBuilder marginsBuilder;

	protected String minHeight;

	/**
	 * Create a new footer object.
	 *
	 * @param odsFile
	 *            - The OdsFile to which this footer belongs to.
	 */
	FooterHeaderBuilder(final FooterHeader.Type footerHeaderType) {
		this.footerHeaderType = footerHeaderType;
		this.minHeight = "0cm";
		this.marginsBuilder = new MarginsBuilder();
		this.marginsBuilder.all("0cm");
	}

	/**
	 * Set the margin at the top,bottom,left and right. margin is a length value
	 * expressed as a number followed by a unit of measurement e.g. 1.5cm or
	 * 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 *
	 * @param margin
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
	 * Set the margin at the bottom. margin is a length value expressed as a
	 * number followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).<br>
	 *
	 * @param margin
	 * @return this for fluent style
	 */
	public F marginBottom(final String margin) {
		this.marginsBuilder.bottom(margin);
		return (F) this;
	}

	/**
	 * Set the margin at the left. margin is a length value expressed as a
	 * number followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch), and pt (points; 72points equal one inch).<br>
	 *
	 * @param margin
	 * @return this for fluent style
	 */
	public F marginLeft(final String margin) {
		this.marginsBuilder.left(margin);
		return (F) this;
	}

	/**
	 * Set the margin at the right. margin is a length value expressed as a
	 * number followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 *
	 * @param margin
	 * @return this for fluent style
	 */
	public F marginRight(final String margin) {
		this.marginsBuilder.right(margin);
		return (F) this;
	}

	/**
	 * Set the margin at the top. margin is a length value expressed as a number
	 * followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 *
	 * @param margin
	 * @return this for fluent style
	 */
	public F marginTop(final String margin) {
		this.marginsBuilder.top(margin);
		return (F) this;
	}

	/**
	 * Set the minimum height. min height is a length value expressed as a
	 * number followed by a unit of measurement e.g. 1.5cm or 12px<br>
	 * The valid units in OpenDocument are in, cm, mm, px (pixels), pc (picas; 6
	 * picas equals one inch),<br>
	 * and pt (points; 72points equal one inch).<br>
	 *
	 * @param margin
	 * @return this for fluent style
	 */
	public F minHeight(final String height) {
		this.minHeight = height;
		return (F) this;
	}

	public F styledContent(final String text, final TextStyle ts) {
		this.curRegionBox.set(Text.styledContent(text, ts));
		return (F) this;
	}

	/**
	 * @param text
	 *            The text to write
	 * @return
	 */
	public F text(final Text text) {
		this.curRegionBox.set(text);
		return (F) this;
	}
}