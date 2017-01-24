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

package com.github.jferard.fastods.odselement;

/**
 * The FlushPosition class represents a position in the flush process of the content.xml file.
 */
public class FlushPosition {
	private int lastTableIndex;
	private int lastRowIndex;

	FlushPosition() {
		this.lastTableIndex = -1;
		this.lastRowIndex = -1;
	}

	public int getLastRowIndex() {
		return this.lastRowIndex;
	}

	public int getTableIndex() {
		return this.lastTableIndex;
	}

	public boolean isUndefined() {
		return this.lastTableIndex == -1 && this.lastRowIndex == -1;
	}

	public void set(final int lastTableIndex, final int lastRowIndex) {
		this.lastTableIndex = lastTableIndex;
		this.lastRowIndex = lastRowIndex;
	}
}
