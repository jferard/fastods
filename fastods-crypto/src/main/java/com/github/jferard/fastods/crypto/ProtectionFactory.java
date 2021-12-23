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

package com.github.jferard.fastods.crypto;

import com.github.jferard.fastods.util.Protection;
import org.bouncycastle.util.encoders.Base64;

import java.security.NoSuchAlgorithmException;

/**
 * The ProtectionFactory class is temporary in this submodule because of the need of a base64
 * encoder (absent of JRE 6).
 *
 * 19.698: "Producers should use http://www.w3.org/2000/09/xmldsig#sha256."
 */
public class ProtectionFactory {
    /**
     * Create a new protection. **Beware: for security reasons, this fills the password array
     * with 0's**
     *
     * @param password the password.
     * @return the protection.
     * @throws NoSuchAlgorithmException shoud not happen since SHA-256 is pretty common
     */
    public static Protection createSha256(final char[] password) throws NoSuchAlgorithmException {
        final String algorithm = "http://www.w3.org/2000/09/xmldsig#sha256";
        final String base64digest = Base64.toBase64String(Util.getPasswordChecksum(password, "SHA-256"));

        return new Protection(base64digest, algorithm);
    }
}
