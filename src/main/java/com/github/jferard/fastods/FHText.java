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

import java.io.IOException;

import com.github.jferard.fastods.style.FHTextStyle;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file FHText.java is part of FastODS.
 *
 *         WHERE ?
 *         styles.xml/office:document-styles/office:master-styles/style:master-
 *         page/style:footer/text:p/text:span
 *         styles.xml/office:document-styles/office:master-styles/style:master-
 *         page/style:header/text:p/text:span
 */
public class FHText {
	private final String text;
	private final /*@Nullable*/ FHTextStyle ts;

	public FHText(final String text) {
		this(text, null);
	}

	public FHText(final String s, final FHTextStyle t) {
		this.ts = t;
		this.text = s;
	}

	/**
	 * Used in file styles.xml, in <office:master-styles>,<style:master-page />
	 *
	 * @param tagName
	 *            TODO
	 *
	 * @throws IOException
	 */
	public void appendXMLOptionalSpanToParagraph(final XMLUtil util,
			final Appendable appendable) throws IOException {
		if (this.ts == null) {
			appendable.append(this.text);
		} else {
			appendable.append("<text:span");
			util.appendEAttribute(appendable, "text:style-name",
					this.ts.getName());
			appendable.append(">").append(this.text).append("</text:span>");
		}
	}

	public void appendXMLTextPToParagraph(final XMLUtil util,
			final Appendable appendable) throws IOException {
		appendable.append("<text:p");
		if (this.ts != null)
			util.appendEAttribute(appendable, "text:style-name",
					this.ts.getName());
		appendable.append(">").append(this.text).append("</text:p>");
	}

	public String getText() {
		return this.text;
	}

	public FHTextStyle getTextStyle() {
		return this.ts;
	}
}
