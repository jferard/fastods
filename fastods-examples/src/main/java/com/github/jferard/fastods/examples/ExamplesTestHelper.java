/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2020 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.examples;

import org.odftoolkit.odfvalidator.Main;

import java.io.File;

public class ExamplesTestHelper {

    public static void validate(final File destFile) {
//        final File schemaRNG = new File("target/schemas", "OpenDocument-v1.3-schema.rng");
//        final File manifestSchemaRNG =
//                new File("target/schemas", "OpenDocument-v1.3-manifest-schema.rng");
//        final File dsigSchemaRNG = new File("target/schemas", "OpenDocument-v1.3-dsig-schema.rng");
        final File schemaRNG = new File("target/schemas", "OpenDocument-v1.2-os-schema.rng");
        final File manifestSchemaRNG =
                new File("target/schemas", "OpenDocument-v1.2-os-manifest-schema.rng");
        final File dsigSchemaRNG = new File("target/schemas", "OpenDocument-v1.2-os-dsig-schema.rng");
        if (schemaRNG.exists() && manifestSchemaRNG.exists() && dsigSchemaRNG.exists()) {
            Main.main(new String[]{
                    "-O", schemaRNG.getAbsolutePath(),
                    "-M", manifestSchemaRNG.getAbsolutePath(),
                    "-D", dsigSchemaRNG.getAbsolutePath(),
                    destFile.getAbsolutePath()});
        }
    }
}
