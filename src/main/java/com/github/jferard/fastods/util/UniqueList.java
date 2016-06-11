package com.github.jferard.fastods.util;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard Copyright 2008-2013 Martin
 *         Schulz <mtschulz at users.sourceforge.net>
 *
 *         This file UniqueList.java is part of FastODS.
 *
 */
public class UniqueList<T extends NamedObject> extends AbstractList<T> implements List<T> {
	private List<T> list;
	private Map<String, T> elementByName;

	public UniqueList() {
		this.list = new LinkedList<T>();
		this.elementByName = new HashMap<String, T>();
	}

	@Override
	public T get(int index) {
		return this.list.get(index);
	}
	
	public T get(Object o) {
		return this.elementByName.get(o);
	}

	@Override
	public int size() {
		return this.list.size();
	}

	@Override
	public T set(int index, T element) {
		final String elementName = element.getName();
		if (this.elementByName.containsKey(elementName))
			throw new IllegalArgumentException("Element "+elementName+" already in list");
		
		this.elementByName.put(elementName, element);
		return this.list.set(index, element);
	}

	@Override
	public void add(int index, T element) {
		final String elementName = element.getName();
		if (this.elementByName.containsKey(elementName))
			throw new IllegalArgumentException("Element "+element+" already in list");
		
		this.elementByName.put(elementName, element);
		this.list.add(index, element);
	}

	@Override
	public T remove(int index) {
		T element = this.list.remove(index);
		this.elementByName.remove(element.getName());
		return element;
	}
	
	@Override
	public boolean remove(Object o) {
		if (this.elementByName.containsKey(o)) {
			T t = this.elementByName.get(o);
			this.elementByName.remove(o);
			this.list.remove(t);
			return true;
		} else
			return false;
	}

	public Set<String> keySet() {
		return this.elementByName.keySet();
	}
}
