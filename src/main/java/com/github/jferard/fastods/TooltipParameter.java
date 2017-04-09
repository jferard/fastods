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

import com.github.jferard.fastods.util.Length;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * Created by jferard on 08/04/17.
 */
public class TooltipParameter {
    final Length width;
    final Length height;
    final boolean visible;

    TooltipParameter(final Length width, final Length height, final boolean visible) {
        this.width = width;
        this.height = height;
        this.visible = visible;
    }

    public void appendXMLToTable(final XMLUtil util, final Appendable appendable) throws IOException {
        util.appendAttribute(appendable,"office:display", this.visible ? "true" : "false");
        util.appendAttribute(appendable,"svg:width", this.width.toString());
        util.appendAttribute(appendable,"svg:height", this.height.toString());
    }

    public static TooltipParameter create(final Length width, final Length height, final boolean visible) {
        return new TooltipParameter(width, height, visible);
    }
}
