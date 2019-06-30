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

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.Container.Mode;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A multi container contains values indexed by sub container. The sub container is a value inside an enum.
 * Basically, we have a map (K,S) -> V.
 * @see StylesContainer for an example.
 * @param <K> the key type
 * @param <V> the value type
 * @param <S> an enum of sub containers
 * @author Julien Férard
 */
public class MultiContainer<K, S extends Enum<S>, V> {
	private final Map<K, S> subcontainerByKey;
	private final Map<S, Map<K, V>> valueByKeyBySubcontainer;
	private final Logger logger;
	private boolean closed;
	private boolean debug;
    private Mode mode;

    /**
     * Create a new multi container
	 * @param logger
	 * @param clazz the enum
	 */
    public MultiContainer(final Logger logger, final Class<S> clazz) {
		this.logger = logger;
		this.subcontainerByKey = new HashMap<K, S>();
		this.valueByKeyBySubcontainer = new HashMap<S, Map<K, V>>();
		for (final S subcontainer : clazz.getEnumConstants()) {
			this.valueByKeyBySubcontainer.put(subcontainer,
					new HashMap<K, V>());
		}
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
     * Add a value to this multi container
     * @param key the key
     * @param subcontainer the sub container
     * @param value the value
     * @return true
     */
    public boolean add(final K key, final S subcontainer, final V value) {
		final S curSubcontainer = this.subcontainerByKey.get(key);
		if (curSubcontainer == null) { // key does not exist
			if (this.mode == Mode.UPDATE)
				return false;

		} else { // key exists
			if (this.mode == Mode.CREATE)
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
		if (this.closed && !valueByKey.containsKey(key)) {
			throw new IllegalStateException(
					"MultiContainer put(" + key + ", " + value + ") in " + subcontainer);
		} else if (this.debug && !valueByKey.containsKey(key)) {
			this.logger.severe("MultiContainer put(" + key + ", " + value + ") in " + subcontainer);
		}
		valueByKey.put(key, value);
		return true;
	}

    /**
     * @param key the key
     * @param subcontainer the sub container
     * @return the value, null if none
     */
    public V get(final K key, final S subcontainer) {
		final Map<K, V> valueByKey = this.valueByKeyBySubcontainer.get(subcontainer);
		return valueByKey == null ? null : valueByKey.get(key);
	}

    /**
     * Enable debug mode
     */
    public void debug() {
		this.debug = true;
	}

    /**
     * Freeze the container: no more add is allowed
     */
	public void freeze() {
		this.closed = true;
	}

    /**
     * @param subcontainer the sub container
     * @return a map of K->V for the given container
     */
    public Map<K, V> getValueByKey(final S subcontainer) {
		return this.valueByKeyBySubcontainer.get(subcontainer);
	}

    /**
     * @param subcontainer the sub container
     * @return all the values
     */
    public Iterable<V> getValues(final S subcontainer) {
		return this.valueByKeyBySubcontainer.get(subcontainer).values();
	}

	@Override
	public String toString() {
		return this.valueByKeyBySubcontainer.toString();
	}
}
