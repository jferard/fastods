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
package com.github.jferard.fastods.testlib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public abstract class Bench {
	private final int colCount;
	private final Logger logger;
	private final Random random;
	private final int rowCount;
	private final List<Long> times;
	private final String name;

	public Bench(final Logger logger, final String name, final int rowCount, final int colCount) {
		this.logger = logger;
		this.name = name;
		this.rowCount = rowCount;
		this.colCount = colCount;
		this.times = new ArrayList<Long>();
		this.random = new Random(); // don't use a fixed seed, since the bench needs a new sequence each time
	}

	public Computations getWithWarmup() {
		return new Computations(this.name, this.times.subList(2, this.times.size()));
	}

	public Computations getWithoutWarmup() {
		return new Computations(this.name, this.times);
	}

	public void iteration() throws IOException {
		this.times.add(this.test());
	}

	public abstract long test() throws IOException;

	public int getColCount() {
		return this.colCount;
	}

	public Random getRandom() {
		return this.random;
	}

	public int getRowCount() {
		return this.rowCount;
	}
}
