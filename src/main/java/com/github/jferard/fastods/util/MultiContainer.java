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

import com.github.jferard.fastods.util.Container.Mode;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MultiContainer<K, V, S extends Enum<S>> {
	private final Map<K, S> subcontainerByKey;
	private final Map<S, Map<K, V>> valueByKeyBySubcontainer;
	private boolean closed;
	private boolean debug;

	public MultiContainer(final Class<S> clazz) {
		this.subcontainerByKey = new HashMap<K, S>();
		this.valueByKeyBySubcontainer = new HashMap<S, Map<K, V>>();
		for (final S subcontainer : clazz.getEnumConstants()) {
			this.valueByKeyBySubcontainer.put(subcontainer,
					new HashMap<K, V>());
		}
		this.closed = false;
		this.debug = false;
	}

	public boolean add(final K key, final V value, final S subcontainer,
					   final Mode mode) {
		final S curSubcontainer = this.subcontainerByKey.get(key);
		if (curSubcontainer == null) { // key does not exist
			if (mode == Mode.UPDATE)
				return false;

		} else { // key exists
			if (mode == Mode.CREATE)
				return false;

			if (subcontainer != curSubcontainer) {
				if (this.closed)
					throw new IllegalStateException(
							"MultiContainer put(" + key + ", " + value + ") in " + subcontainer);
				this.valueByKeyBySubcontainer.get(curSubcontainer).remove(key);
			}
		}


		if (subcontainer != curSubcontainer)
			this.subcontainerByKey.put(key, subcontainer);

		final Map<K, V> valueByKey = this.valueByKeyBySubcontainer.get(subcontainer);
		if (this.closed && !valueByKey.containsKey(key))
			throw new IllegalStateException(
					"MultiContainer put(" + key + ", " + value + ") in " + subcontainer);
		else if (this.debug && !valueByKey.containsKey(key))
			Logger.getLogger("debug").severe(
					"MultiContainer put(" + key + ", " + value + ") in " + subcontainer);

		valueByKey.put(key, value);
		return true;
	}

	public V get(final K key, final S subcontainer) {
		final Map<K, V> valueByKey = this.valueByKeyBySubcontainer.get(subcontainer);
		return valueByKey == null ? null : valueByKey.get(key);
	}

	public void debug() {
		this.debug = true;
	}

	public void freeze() {
		this.closed = true;
	}

	public Map<K, V> getValueByKey(final S subcontainer) {
		return this.valueByKeyBySubcontainer.get(subcontainer);
	}

	public Iterable<V> getValues(final S subcontainer) {
		return this.valueByKeyBySubcontainer.get(subcontainer).values();
	}
}
