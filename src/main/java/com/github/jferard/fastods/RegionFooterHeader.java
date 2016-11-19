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

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.github.jferard.fastods.style.FHTextStyle;
import com.github.jferard.fastods.style.Margins;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:footer
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:header
 *
 * @author Julien Férard
 * @author Martin Schulz
 *
 */
class RegionFooterHeader extends FooterHeader {
	private static void appendRegion(final XMLUtil util,
			final Appendable appendable, final List<Paragraph> region,
			final String regionName) throws IOException {
		if (region.size() == 0)
			return;

		appendable.append("<style:").append(regionName).append(">");
		FooterHeader.appendXMLRegionBodyToMasterStyle(util, appendable, region);
		appendable.append("</style:").append(regionName).append(">");
	}

	/**
	 * The OdsFile where this object belong to.
	 */
	private final List<Paragraph> centerRegion;
	private final List<Paragraph> leftRegion;

	private final List<Paragraph> rightRegion;

	/**
	 * Create a new footer object.
	 * 
	 * @param textStyles
	 *
	 * @param minHeight2
	 *
	 * @param odsFile
	 *            - The OdsFile to which this footer belongs to.
	 */
	RegionFooterHeader(final RegionFooterHeader.Type footerHeaderType,
			final List<Paragraph> centerRegion,
			final List<Paragraph> leftRegion,
			final List<Paragraph> rightRegion, final Margins margins,
			final String minHeight, final Set<FHTextStyle> textStyles) {
		super(footerHeaderType, margins, minHeight, textStyles);
		this.centerRegion = centerRegion;
		this.leftRegion = leftRegion;
		this.rightRegion = rightRegion;
	}

	/**
	 * Used in file styles.xml, in <office:master-styles>,<style:master-page />.
	 *
	 * @throws IOException
	 */
	@Override
	public void appendXMLToMasterStyle(final XMLUtil util,
			final Appendable appendable) throws IOException {
		RegionFooterHeader.appendRegion(util, appendable, this.leftRegion,
				"region-left");
		RegionFooterHeader.appendRegion(util, appendable, this.centerRegion,
				"region-center");
		RegionFooterHeader.appendRegion(util, appendable, this.rightRegion,
				"region-right");
	}
}
