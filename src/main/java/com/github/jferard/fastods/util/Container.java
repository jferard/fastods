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

package com.github.jferard.fastods.util;

import java.util.HashMap;
import java.util.Map;

public class Container<K, V> {
	public enum Mode {
		CREATE, CREATE_OR_UPDATE, UPDATE
	}

	private final Map<K, V> valueByKey;

	public Container() {
		this.valueByKey = new HashMap<K, V>();
	}

	public boolean add(final K key, final V value, final Mode mode) {
		final V curValue = this.valueByKey.get(key);
		if (curValue == null) { // key does not exist
			if (mode == Mode.UPDATE)
				return false;

		} else { // key exists
			if (mode == Mode.CREATE)
				return false;
		}

		this.valueByKey.put(key, value);
		return true;
	}

	public Map<K, V> getValueByKey() {
		return this.valueByKey;
	}

	public Iterable<V> getValues() {
		return this.valueByKey.values();
	}
}
