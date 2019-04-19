/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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
import java.util.logging.Logger;

/**
 * A Container is a Map like object, but with a mode parameter: one may create, update, or create or update a key-value pair.
 * The container may be frozen: no new key-value pair is accepted.
 *
 * @param <K> key class
 * @param <V> value class
 *
 * @author Julien Férard
 */
public class Container<K, V> {
	private final Map<K, V> valueByKey;
	private boolean closed;
	private boolean debug;
    private Mode mode;

    /**
	 * Builds a default container
	 */
	public Container() {
		this.valueByKey = new HashMap<K, V>();
		this.closed = false;
		this.debug = false;
        this.mode = Mode.CREATE;
	}

    /**
     * Set the new mode to use
     * @param mode the mode (CREATE, UPDATE, CREATE_OR_UPDATE)
     */
	public void setMode(final Mode mode) {
	    this.mode = mode;
    }

	/**
	 * If mode is update, then the key must exist. If the mode is create, then the key must not exist.
	 * Otherwise, the key may exist. If the container is frozen, no new key-value pair is accepted.
	 *
	 * @param key the key
	 * @param value the value
	 * @return true if the value was updated
	 */
	public boolean add(final K key, final V value) {
		final V curValue = this.valueByKey.get(key);
		if (curValue == null) { // key does not exist
			if (this.mode == Mode.UPDATE)
				return false;

		} else { // key exists
			if (this.mode == Mode.CREATE)
				return false;
		}

		if (this.closed && !this.valueByKey.containsKey(key)) {
			throw new IllegalStateException(
					"Container put(" + key + ", " + value + ")");
		} else if (this.debug && !this.valueByKey.containsKey(key)) {
			Logger.getLogger("debug").severe(
					"Container put(" + key + ", " + value + ")");
		}

		this.valueByKey.put(key, value);
		return true;
	}

	/**
	 * Set the debug mode: every add will be stored
	 */
	public void debug() {
		this.debug = true;
	}

	/**
	 * Freeze the container: no new key-value pair is accepted.
	 */
	public void freeze() {
		this.closed = true;
	}

	/**
	 * @param key the key to look for
	 * @return the value mapped to the key
	 */
	public V get(final K key) {
		return this.valueByKey.get(key);
	}

	/**
	 * @return the container as a Map
	 */
	public Map<K, V> getValueByKey() {
		return this.valueByKey;
	}

	/**
	 * @return the values
	 */
	public Iterable<V> getValues() {
		return this.valueByKey.values();
	}

	/**
	 * the mode
	 */
	public enum Mode {
		/**
		 * to create a new key-value pair.
		 */
		CREATE,
		/**
		 * to create or update a new key-value pair: works like a standard Map.
		 */
		CREATE_OR_UPDATE,
		/**
		 * to update only a key-value pair.
		 */
		UPDATE
	}
}
