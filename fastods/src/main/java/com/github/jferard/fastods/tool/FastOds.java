/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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

import com.github.jferard.fastods.util.XMLUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple utility class for : default dependencies creation, open file.
 *
 * @author Julien Férard
 */
public final class FastOds {
	private static final XMLUtil xmlUtil = XMLUtil.create();
	/**
	 * The desktop
	 */
	static Desktop desktop;

	static {
		try {
			desktop = Desktop.getDesktop();
		} catch (final Exception e) {
			Logger.getAnonymousLogger().warning("Cant' find desktop");
		}
	}

	/**
	 * @return the default XMLUtil.
	 */
	public static XMLUtil getXMLUtil() {
		return FastOds.xmlUtil;
	}

	/**
	 * Opens a file with the default application.
	 *
	 * @param f
	 *            the file to open
	 * @return true if succeeded, false otherwise.
	 */
	public static boolean openFile(final File f) {
		if (desktop != null && f.exists() && f.isFile()) {
			try {
				desktop.open(f);
				return true;
			} catch (final IOException e) {
				Logger.getLogger(FastOds.class.getName()).log(Level.SEVERE,
						"Can't open file " + f + " in appropriate application",
						e);
			}
		}
		return false;
	}

	private FastOds() {
	}
}
