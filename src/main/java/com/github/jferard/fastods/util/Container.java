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
