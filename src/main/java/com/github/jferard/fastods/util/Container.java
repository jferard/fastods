package com.github.jferard.fastods.util;

import java.util.HashMap;
import java.util.Map;

public class Container<K, V> {
	public enum Mode {
		CREATE, UPDATE, CREATE_OR_UPDATE
	}

	private Map<K, V> valueByKey;

	public Container() {
		this.valueByKey = new HashMap<K, V>();
	}

	public boolean add(K key, V value, Mode mode) {
		V curValue = this.valueByKey.get(key);
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

	public Iterable<V> getValues() {
		return this.valueByKey.values();
	}
}
