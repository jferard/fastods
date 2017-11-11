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

package com.github.jferard.fastods.tool;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Usage:
 * <pre><code>
 * final FastOdsBus{@literal <E>} bus = new FastOdsBus{@literal <E>}();
 *
 * Thread consumer = new Thread() {
 *     {@literal @}Override
 *     public void run() {
 *     while (!bus.isClosed()) {
 *          final E element = bus.get();
 *          // do something with E
 *     }
 * }
 *
 * consumer.start()
 *
 * // put whatever in the bus with bus put.
 *
 * try {
 *     consumer.join();
 * } catch (final InterruptedException e) {
 *     logger.log(Level.SEVERE, "", e);
 * }
 * </code></pre>
 *
 * @param <E> the elements in the bus
 * @author Julien Férard
 */
public class FastOdsBus<E> {
	private final Queue<E> elements;
	private boolean closed;

	/**
	 * Create a new bus
	 */
	public FastOdsBus() {
		this.elements = new LinkedList<E>();
		this.closed = false;
	}

	/**
	 * close the bus
	 */
	public void close() {
		this.closed = true;
	}

	/**
	 * Get an element from the bus. Blocking method.
	 * @return the next element in the bus
	 */
	synchronized public E get() {
		if (this.isClosed())
			throw new NoSuchElementException();

		while (this.elements.isEmpty()) {
			try {
				this.wait();
			} catch (final InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException(e);
			}
		}
		return this.elements.remove();
	}

	/**
	 * @return true if the bus was closed
	 */
	synchronized public boolean isClosed() {
		return this.closed && this.elements.isEmpty();
	}

	/**
	 * Add an element to the bus
	 * @param element the element
	 */
	synchronized public void put(final E element) {
		this.elements.add(element);
		this.notifyAll();
	}
}