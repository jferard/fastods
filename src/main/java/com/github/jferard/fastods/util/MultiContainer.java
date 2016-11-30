package com.github.jferard.fastods.util;

import java.util.HashMap;
import java.util.Map;

import com.github.jferard.fastods.style.StyleTag;
import com.github.jferard.fastods.util.Container.Mode;

public class MultiContainer<K, V, S extends Enum<S>> {

	private Map<K, S> subcontainerByKey;
	private Map<S, Map<K, V>> valueByKeyBySubcontainer;

	public MultiContainer(Class<S> clazz) {
		this.subcontainerByKey = new HashMap<K, S>();
		this.valueByKeyBySubcontainer = new HashMap<S, Map<K, V>>();
		for (S subcontainer : clazz.getEnumConstants()) {
			this.valueByKeyBySubcontainer.put(subcontainer, new HashMap<K, V>());
		}
	}

	public boolean add(K key, V value, S subcontainer, Mode mode) {
		S curSubcontainer = this.subcontainerByKey.get(key);
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

	public Iterable<V> getValues(S subcontainer) {
		return this.valueByKeyBySubcontainer.get(subcontainer).values();
	}

	public Map<K, V> getValueByKey(S subcontainer) {
		return this.valueByKeyBySubcontainer.get(subcontainer);
	}
}
