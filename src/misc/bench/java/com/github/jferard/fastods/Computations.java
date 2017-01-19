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

package com.github.jferard.fastods;

import java.util.Collections;
import java.util.List;

/**
 */
public class Computations {
	private String name;
	private List<Long> times;

	public Computations(String name, List<Long> times) {
		this.name = name;
		this.times = times;
	}

	public Long getAvgTime() {
		if (this.times.size() == 0)
			return -1L;

		long l = 0L;
		for (long time: times)
			l += time;

		return l / this.times.size();
	}

	public long getBestTime() {
		return Collections.min(this.times);
	}

	public long getWorstTime() {
		return Collections.max(this.times);
	}

	public String toString() {
		return "Computations[name = "+name+", avg = "+this.getAvgTime()+", best = "+this.getBestTime()+", worst = "+this.getWorstTime()+"]";
	}
}
