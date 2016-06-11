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
import java.util.List;

import com.github.jferard.fastods.util.FullList;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 *
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file TextStyleBuilder.java is part of FastODS. SimpleOds Version
 *         0.5.0 Added support for Font underline style
 */
public class FHParagraph {
	private final List<FHText> texts;

	FHParagraph() {
		this.texts = FullList.<FHText> builder().capacity(16).build();
	}

	public void add(final FHText fHText) {
		this.texts.add(fHText);
	}

	public void add(final String content) {
		this.texts.add(new FHText(content));
	}

	public void appendXMLToRegionBody(final XMLUtil util,
			final Appendable appendable) throws IOException {
		switch (this.texts.size()) {
		case 0:
			appendable.append("<text:p/>");
			break;
		case 1:
			final FHText text = this.texts.get(0);
			text.appendXMLTextPToParagraph(util, appendable);
			break;
		default:
			appendable.append("<text:p>");
			for (final FHText textChunk : this.texts)
				textChunk.appendXMLOptionalSpanToParagraph(util, appendable);
			appendable.append("</text:p>");
			break;
		}
	}

	public List<FHText> getTexts() {
		return this.texts;
	}

}
