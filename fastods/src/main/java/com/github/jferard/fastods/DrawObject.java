/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.ref.RangeRef;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.List;

/**
 * 10.4.6.2<draw:object>
 */
public class DrawObject implements FrameContent {
    private final String href;
    private final List<RangeRef> updateRanges;

    /**
     * @param href         the href
     * @param updateRanges the ranges
     */
    public DrawObject(final String href, final List<RangeRef> updateRanges) {
        this.href = href;
        this.updateRanges = updateRanges;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<draw:object");
        util.appendAttribute(appendable, "xlink:href", this.href);
        util.appendAttribute(appendable, "draw:notify-on-update-of-ranges", this.updateRanges, " ");
        util.appendAttribute(appendable, "xlink:type", "simple");
        util.appendAttribute(appendable, "xlink:show", "embed");
        util.appendAttribute(appendable, "xlink:actuate", "onLoad");
        appendable.append("/>");
    }
}
