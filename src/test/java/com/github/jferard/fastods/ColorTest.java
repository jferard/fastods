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

import org.junit.Assert;
import org.junit.Test;

public class ColorTest {

	@Test
	public final void test() {
		Assert.assertEquals("#ffffff", Color.createHexColor(255, 255, 255));
		Assert.assertEquals("#ffffff", Color.createHexColor(2550, 2550, 2550));
		Assert.assertEquals("#000000", Color.createHexColor(0, 0, 0));
		Assert.assertEquals("#000000", Color.createHexColor(-10, -10, -10));
		Assert.assertEquals("#174b81", Color.createHexColor(23, 75, 129));
	}

}
