/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.jferard.fastods;

import com.github.jferard.fastods.util.ColorHelper;
import org.junit.Assert;
import org.junit.Test;

public class ColorTest {
    @Test
    public final void test() {
        Assert.assertEquals("#ffffff", ColorHelper.fromRGB(255, 255, 255).getValue());
        Assert.assertEquals("#ffffff", ColorHelper.fromRGB(2550, 2550, 2550).getValue());
        Assert.assertEquals("#000000", ColorHelper.fromRGB(0, 0, 0).getValue());
        Assert.assertEquals("#000000", ColorHelper.fromRGB(-10, -10, -10).getValue());
        Assert.assertEquals("#174b81", ColorHelper.fromRGB(23, 75, 129).getValue());
    }

}
