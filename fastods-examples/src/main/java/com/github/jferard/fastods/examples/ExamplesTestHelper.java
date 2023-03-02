/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2023 J. Férard <https://github.com/jferard>
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

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class ExamplesTestHelper {
    private final static Logger logger = Logger.getLogger(ExamplesTestHelper.class.getName());

    public static final String OPEN_DOCUMENT_V_1_2_SCHEMA_RNG = "OpenDocument-v1.2-os-schema.rng";
    public static final String OPEN_DOCUMENT_V_1_3_SCHEMA_RNG = "OpenDocument-v1.3-schema.rng";

    public static final String OPEN_DOCUMENT_V_1_2_MANIFEST_SCHEMA_RNG =
            "OpenDocument-v1.2-os-manifest-schema.rng";
    public static final String OPEN_DOCUMENT_V_1_3_MANIFEST_SCHEMA_RNG =
            "OpenDocument-v1.3-manifest-schema.rng";
    public static final String OPEN_DOCUMENT_V_1_2_DSIG_SCHEMA_RNG =
            "OpenDocument-v1.2-os-dsig-schema.rng";
    public static final String OPEN_DOCUMENT_V_1_3_DSIG_SCHEMA_RNG =
            "OpenDocument-v1.3-dsig-schema.rng";

    @Deprecated
    public static void validate(final File destFile) throws IOException {
        ExamplesTestHelper.validate(destFile.toPath());
    }

    public static void validate(final Path destPath) throws IOException {
        ExamplesTestHelper.validate(destPath, "1.2");
    }

    private static void validate(final Path destPath, final String version) throws IOException {
        if (!Files.exists(destPath)) {
            ExamplesTestHelper.logger.warning("Can't validate a non existing file");
            return;
        }

        final String schemaRNGName;
        final String manifestSchemaRNGName;
        final String dsigSchemaRNGName;
        if (version.equals("1.2")) {
            schemaRNGName = OPEN_DOCUMENT_V_1_2_SCHEMA_RNG;
            manifestSchemaRNGName = OPEN_DOCUMENT_V_1_2_MANIFEST_SCHEMA_RNG;
            dsigSchemaRNGName = OPEN_DOCUMENT_V_1_2_DSIG_SCHEMA_RNG;
        } else {
            schemaRNGName = OPEN_DOCUMENT_V_1_3_SCHEMA_RNG;
            manifestSchemaRNGName = OPEN_DOCUMENT_V_1_3_MANIFEST_SCHEMA_RNG;
            dsigSchemaRNGName = OPEN_DOCUMENT_V_1_3_DSIG_SCHEMA_RNG;
        }

        final Path schemaRNG = Paths.get("target/schemas", schemaRNGName);
        if (!Files.exists(schemaRNG)) {
            ExamplesTestHelper.logger.warning("Can't validate: document schema not found");
            return;
        }

        final Path manifestSchemaRNG = Paths.get("target/schemas", manifestSchemaRNGName);
        if (!Files.exists(manifestSchemaRNG)) {
            ExamplesTestHelper.logger.warning("Can't validate: manifest schema not found");
            return;
        }

        final Path dsigSchemaRNG = Paths.get("target/schemas", dsigSchemaRNGName);
        if (!Files.exists(dsigSchemaRNG)) {
            ExamplesTestHelper.logger.warning("Can't validate: dsig schema not found");
            return;
        }

        Main.main(new String[]{
                "-O", schemaRNG.toRealPath().toString(),
                "-M", manifestSchemaRNG.toRealPath().toString(),
                "-D", dsigSchemaRNG.toRealPath().toString(),
                destPath.toRealPath().toString()});
    }
}
