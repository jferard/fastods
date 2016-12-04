package com.github.jferard.fastods.util;

import java.util.HashMap;
import java.util.Map;

import com.github.jferard.fastods.util.Container.Mode;

public class MultiContainer<K, V, S extends Enum<S>> {

	private final Map<K, S> subcontainerByKey;
	private final Map<S, Map<K, V>> valueByKeyBySubcontainer;

	public MultiContainer(final Class<S> clazz) {
		this.subcontainerByKey = new HashMap<K, S>();
		this.valueByKeyBySubcontainer = new HashMap<S, Map<K, V>>();
		for (final S subcontainer : clazz.getEnumConstants()) {
			this.valueByKeyBySubcontainer.put(subcontainer,
					new HashMap<K, V>());
		}
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

			if (subcontainer != curSubcontainer)
				this.valueByKeyBySubcontainer.get(curSubcontainer).remove(key);
		}

		if (subcontainer != curSubcontainer)
			this.subcontainerByKey.put(key, subcontainer);
		this.valueByKeyBySubcontainer.get(subcontainer).put(key, value);
		return true;
	}

	public Map<K, V> getValueByKey(final S subcontainer) {
		return this.valueByKeyBySubcontainer.get(subcontainer);
	}

	public Iterable<V> getValues(final S subcontainer) {
		return this.valueByKeyBySubcontainer.get(subcontainer).values();
	}
}
