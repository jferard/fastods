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

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file FooterHeader.java is part of FastODS.
 *
 *         styles.xml/office:document-styles/office:master-styles/style:master-
 *         page/style:footer
 *         styles.xml/office:document-styles/office:master-styles/style:master-
 *         page/style:header
 */
class RegionFooterHeader extends FooterHeader {
	/**
	 * The OdsFile where this object belong to.
	 */
	private final List<List<StyledText>> qCenterRegion;
	private final List<List<StyledText>> qLeftRegion;
	private final List<List<StyledText>> qRightRegion;

	/**
	 * Create a new footer object.
	 *
	 * @param odsFile
	 *            - The OdsFile to which this footer belongs to.
	 */
	RegionFooterHeader(final RegionFooterHeader.Type footerHeaderType,
			final List<List<StyledText>> qCenterRegion,
			final List<List<StyledText>> qLeftRegion,
			final List<List<StyledText>> qRightRegion, String sMarginLeft,
			String sMarginRight, String sMarginTop, String sMinHeight) {
		super(footerHeaderType, sMarginLeft, sMarginRight, sMarginTop, sMinHeight);
		this.qCenterRegion = qCenterRegion;
		this.qLeftRegion = qLeftRegion;
		this.qRightRegion = qRightRegion;
	}

	/**
	 * Used in file styles.xml, in <office:master-styles>,<style:master-page />.
	 *
	 * @throws IOException
	 */
	@Override
	public void appendXMLToMasterStyle(final Util util,
			final Appendable appendable) throws IOException {
		RegionFooterHeader.appendRegion(util, appendable, this.qLeftRegion, "region-left");
		RegionFooterHeader.appendRegion(util, appendable, this.qCenterRegion,
				"region-center");
		RegionFooterHeader.appendRegion(util, appendable, this.qRightRegion, "region-right");
	}

	private static void appendRegion(final Util util, final Appendable appendable,
			final List<List<StyledText>> qRegion, final String sRegionName)
			throws IOException {
		if (qRegion.size() == 0)
			return;

		appendable.append("<style:").append(sRegionName).append(">");
		FooterHeader.appendRegionBody(util, appendable, qRegion);
		appendable.append("</style:").append(sRegionName).append(">");
	}
}
