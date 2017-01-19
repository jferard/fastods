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

import com.github.jferard.fastods.util.Box;

/**
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:footer
 * styles.xml/office:document-styles/office:master-styles/style:master-
 * page/style:header
 *
 * @author Julien Férard
 *
 */
public class RegionFooterHeaderBuilder
		extends FooterHeaderBuilder<RegionFooterHeaderBuilder> {

	private final Box<Text> centerRegionBox;
	private final Box<Text> leftRegionBox;
	private final Box<Text> rightRegionBox;

	/**
	 * Create a new footer object.
	 */
	RegionFooterHeaderBuilder(final FooterHeader.Type footerHeaderType) {
		super(footerHeaderType);
		this.leftRegionBox = new Box<Text>();
		this.centerRegionBox = new Box<Text>();
		this.rightRegionBox = new Box<Text>();
	}

	@Override
	public FooterHeader build() {
		final FooterHeaderStyle style = new FooterHeaderStyle(this.footerHeaderType, this.marginsBuilder.build(),
				this.minHeight);
		final FooterHeaderContent header = new RegionFooterHeader(this.footerHeaderType,
				this.centerRegionBox.get(), this.leftRegionBox.get(),
				this.rightRegionBox.get());
		return new FooterHeader(this.footerHeaderType, header, style);
	}

	public RegionFooterHeaderBuilder region(final FooterHeaderContent.Region region) {
		switch (region) {
		case LEFT: // Use left region
			this.curRegionBox = this.leftRegionBox;
			break;
		case CENTER: // Use center region
			this.curRegionBox = this.centerRegionBox;
			break;
		case RIGHT: // Use right region
			this.curRegionBox = this.rightRegionBox;
			break;
		default: // Invalid footerRegionValue, use center region as default
			throw new IllegalStateException();
		}
		return this;
	}
}
