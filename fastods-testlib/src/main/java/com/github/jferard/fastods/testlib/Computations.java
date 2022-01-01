/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2022 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods.testlib;

import java.util.Collections;
import java.util.List;

/**
 * A helper class for stats.
 *
 * @author Julien Férard
 */
public class Computations {
    private final String name;
    private final List<Long> times;

    /**
     * @param name  the name
     * @param times the list of times
     */
    public Computations(final String name, final List<Long> times) {
        this.name = name;
        this.times = times;
    }

    /**
     * @return the mean of the times
     */
    public long getAvgTime() {
        if (this.times.isEmpty()) {
            return -1L;
        }

        long l = 0L;
        for (final long time : this.times) {
            l += time;
        }

        return l / this.times.size();
    }

    /**
     * @return the best time
     */
    public long getBestTime() {
        if (this.times.isEmpty()) {
            return -1L;
        }
        return Collections.min(this.times);
    }

    /**
     * @return the worst time
     */
    public long getWorstTime() {
        if (this.times.isEmpty()) {
            return -1L;
        }
        return Collections.max(this.times);
    }

    @Override
    public String toString() {
        return "Computations[name = " + this.name + ", avg = " + this.getAvgTime() + ", best = " +
                this.getBestTime() + ", worst = " + this.getWorstTime() + "]";
    }
}