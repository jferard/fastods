/*
*	SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
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
package org.simpleods;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Martin Schulz<br>
 * 
 * Copyright 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net><br>
 * 
 * This file Manifest.java is part of SimpleODS.
 *
 */
public class Manifest {
	private Util u = Util.getInstance();

	String [] sText = { "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>", 
			  "<manifest:manifest xmlns:manifest=\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0\">",
			  "<manifest:file-entry manifest:media-type=\"application/vnd.oasis.opendocument.spreadsheet\" manifest:full-path=\"/\" />", 
			  
			  "<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/statusbar/\" />", 
			  "<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/accelerator/current.xml\" />", 
			  "<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/accelerator/\" /> " ,
			  "<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/floater/\" /> ",
			  "<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/popupmenu/\" />", 
			  "<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/progressbar/\" />", 
			  "<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/menubar/\" /> ",
			  "<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/toolbar/\" /> ",
			  "<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/images/Bitmaps/\" />", 
			  "<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Configurations2/images/\" /> ",
			  "<manifest:file-entry manifest:media-type=\"application/vnd.sun.xml.ui.configuration\" manifest:full-path=\"Configurations2/\" />", 
			  "<manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"content.xml\" /> ",
			  "<manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"styles.xml\" /> ",
			  "<manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"meta.xml\" /> ",
			  "<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Thumbnails/thumbnail.png\" />", 
			  "<manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Thumbnails/\" /> ",
			  "<manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"settings.xml\" />", 
			  "</manifest:manifest>"
			
			};
	
	
	public String [] getManifest() {
		return sText;	
	}
	
	
	public boolean createManifest(final ZipOutputStream o) {

		try {
			o.putNextEntry(new ZipEntry("META-INF/manifest.xml"));
			u.writeStringArray(o, this.getManifest());
			o.closeEntry();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
