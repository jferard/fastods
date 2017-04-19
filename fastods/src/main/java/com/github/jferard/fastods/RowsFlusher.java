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

import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * A flusher for a collection of rows
 * @author Julien Férard
 */
class RowsFlusher implements OdsFlusher {
	private final List<HeavyTableRow> rows;

	/**
	 * @param rows the rows to flush
	 */
	public RowsFlusher(final List<HeavyTableRow> rows) {
		this.rows = rows;
	}

	@Override
	public void flushInto(final XMLUtil xmlUtil, final ZipUTF8Writer writer) throws IOException {
		// flush rows
		for (final HeavyTableRow row : this.rows) {
			if (row == null) {
				System.exit(0);
			}
			row.appendXMLToTable(xmlUtil, writer);
		}
		// free rows
		Collections.fill(this.rows, null);
	}
}