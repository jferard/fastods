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

import java.util.List;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 *
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file FooterHeaderBuilder.java is part of FastODS.
 *
 *         styles.xml/office:document-styles/office:master-styles/style:master-
 *         page/style:footer
 *         styles.xml/office:document-styles/office:master-styles/style:master-
 *         page/style:header
 */
class RegionFooterHeaderBuilder extends FooterHeaderBuilder {

	private final List<FHParagraph> qCenterRegion;
	private final List<FHParagraph> qLeftRegion;
	private final List<FHParagraph> qRightRegion;

	/**
	 * Create a new footer object.
	 *
	 * @param odsFile
	 *            - The OdsFile to which this footer belongs to.
	 */
	RegionFooterHeaderBuilder(final FooterHeader.Type footerHeaderType) {
		super(footerHeaderType);
		this.qLeftRegion = FullList.<FHParagraph> builder().capacity(16)
				.build();
		this.qCenterRegion = FullList.<FHParagraph> builder().capacity(16)
				.build();
		this.qRightRegion = FullList.<FHParagraph> builder().capacity(16)
				.build();
	}

	@Override
	public FooterHeader build() {
		return new RegionFooterHeader(this.footerHeaderType, this.qCenterRegion,
				this.qLeftRegion, this.qRightRegion, this.sMarginLeft,
				this.sMarginRight, this.sMarginTop, this.sMinHeight);
	}

	public RegionFooterHeaderBuilder region(final FooterHeader.Region region) {
		switch (region) {
		case LEFT: // Use left region
			this.curRegion = this.qLeftRegion;
			break;
		case CENTER: // Use center region
			this.curRegion = this.qCenterRegion;
			break;
		case RIGHT: // Use right region
			this.curRegion = this.qRightRegion;
			break;
		default: // Invalid nFooterRegionValue, use center region as default
			throw new IllegalStateException();
		}
		return this;
	}
}
