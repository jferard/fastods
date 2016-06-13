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

import java.util.List;

import com.github.jferard.fastods.util.FullList;

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

	private final List<FHParagraph> centerRegion;
	private final List<FHParagraph> leftRegion;
	private final List<FHParagraph> rightRegion;

	/**
	 * Create a new footer object.
	 *
	 * @param odsFile
	 *            - The OdsFile to which this footer belongs to.
	 */
	RegionFooterHeaderBuilder(final FooterHeader.Type footerHeaderType) {
		super(footerHeaderType);
		this.leftRegion = FullList.<FHParagraph> builder().capacity(16)
				.build();
		this.centerRegion = FullList.<FHParagraph> builder().capacity(16)
				.build();
		this.rightRegion = FullList.<FHParagraph> builder().capacity(16)
				.build();
	}

	@Override
	public FooterHeader build() {
		return new RegionFooterHeader(this.footerHeaderType, this.centerRegion,
				this.leftRegion, this.rightRegion, this.marginLeft,
				this.marginRight, this.marginTop, this.minHeight);
	}

	public RegionFooterHeaderBuilder region(final FooterHeader.Region region) {
		switch (region) {
		case LEFT: // Use left region
			this.curRegion = this.leftRegion;
			break;
		case CENTER: // Use center region
			this.curRegion = this.centerRegion;
			break;
		case RIGHT: // Use right region
			this.curRegion = this.rightRegion;
			break;
		default: // Invalid footerRegionValue, use center region as default
			throw new IllegalStateException();
		}
		return this;
	}
}
