/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
*    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.jferard.fastods;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Martin Schulz<br>
 * 
 *         Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *         <br>
 * 
 *         This file Mimetype.java is part of SimpleODS.
 *
 */
public class Mimetype {

	public boolean createMimetype(ZipOutputStream o) {
		String sLine;

		try {
			o.putNextEntry(new ZipEntry("mimetype"));
			sLine = "application/vnd.oasis.opendocument.spreadsheet";
			byte[] bytes = sLine.getBytes("UTF-8");
			o.write(bytes, 0, bytes.length);
			o.closeEntry();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
