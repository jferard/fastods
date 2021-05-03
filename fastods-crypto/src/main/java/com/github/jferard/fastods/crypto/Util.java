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

import com.github.jferard.fastods.util.CharsetUtil;
import org.bouncycastle.util.encoders.Base64;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Util {
    /**
     * Convert a char array to a byte array containing the SHA-256 digest. **Beware: for security
     * reasons, this fills the password array with 0's**.
     * <p>
     * (char[] -> byte[]) See https://stackoverflow.com/a/9670279/6914441
     *
     * @param password  the data
     * @param algorithm the algorithm (see java.security.MessageDigest)
     * @return the byte array
     * @throws NoSuchAlgorithmException should not happen
     */
    public static byte[] getPasswordChecksum(final char[] password, final String algorithm)
            throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance(algorithm);
        final CharBuffer charBuffer = CharBuffer.wrap(password);
        final ByteBuffer byteBuffer = CharsetUtil.UTF_8.encode(charBuffer);
        final byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        digest.update(bytes);
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        Arrays.fill(password, '\0'); // clear sensitive data
        return digest.digest();
    }

    public static String toBase64String(final byte[] arr) {
        return Base64.toBase64String(arr);
    }
}
