package com.github.jferard.fastods.util;

public class Box<T> {
	private T t;

	public T get() {
		return this.t;
	}
	
	public void set(T t) {
		this.t = t;
	}
}
