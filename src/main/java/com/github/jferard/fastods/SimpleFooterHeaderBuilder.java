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
class SimpleFooterHeaderBuilder extends FooterHeaderBuilder {
	/**
	 * Create a new simple footer object.
	 *
	 * @param footerHeaderType
	 *            footer or header ?
	 */
	SimpleFooterHeaderBuilder(final FooterHeader.Type footerHeaderType) {
		super(footerHeaderType);
		this.curRegion = FullList.<FHParagraph> builder().capacity(16).build();
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	public FooterHeader build() {
		return new SimpleFooterHeader(this.footerHeaderType, this.curRegion,
				this.marginLeft, this.marginRight, this.marginTop,
				this.minHeight);
	}
}
