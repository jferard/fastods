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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.XMLConvertible;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 4.4 manifest:encryption-data
 * <p>
 * Parameters for encryption
 */
public class EncryptParameters implements XMLConvertible {
    /**
     * @return a new builder
     */
    public static EncryptParametersBuilder builder() {
        return new EncryptParametersBuilder();
    }

    private final int uncompressedSize;
    private final int compressedSize;
    private final long crc32;
    private final String compressedCheckSumType;
    private final String compressedCheckSum;
    private final String startKeyGenerationName;
    private final int startKeySize;
    private final String keyDerivationName;
    private final int derivedKeySize;
    private final int derivationIterationCount;
    private final String derivationSalt;
    private final String algorithmName;
    private final String algorithmInitializationVector;

    public EncryptParameters(final int uncompressedSize, final int compressedSize,
                             final long crc32, final String compressedCheckSumType,
                             final String compressedCheckSum, final String startKeyGenerationName,
                             final int startKeySize, final String keyDerivationName,
                             final int derivedKeySize, final int derivationIterationCount,
                             final String derivationSalt, final String algorithmName,
                             final String algorithmInitializationVector) {
        this.uncompressedSize = uncompressedSize;
        this.compressedSize = compressedSize;
        this.crc32 = crc32;
        this.compressedCheckSumType = compressedCheckSumType;
        this.compressedCheckSum = compressedCheckSum;
        this.startKeyGenerationName = startKeyGenerationName;
        this.startKeySize = startKeySize;
        this.keyDerivationName = keyDerivationName;
        this.derivedKeySize = derivedKeySize;
        this.derivationIterationCount = derivationIterationCount;
        this.derivationSalt = derivationSalt;
        this.algorithmName = algorithmName;
        this.algorithmInitializationVector = algorithmInitializationVector;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<manifest:encryption-data");
        util.appendAttribute(appendable, "manifest:checksum-type", this.compressedCheckSumType);
        util.appendAttribute(appendable, "manifest:checksum", this.compressedCheckSum);
        appendable.append(">");
        this.appendAlgorithm(util, appendable);
        this.appendKeyGeneration(util, appendable);
        this.appendKeyDerivation(util, appendable);
        appendable.append("</manifest:encryption-data>");
    }

    private void appendAlgorithm(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<manifest:algorithm");
        util.appendAttribute(appendable, "manifest:algorithm-name", this.algorithmName);
        util.appendAttribute(appendable, "manifest:initialisation-vector",
                this.algorithmInitializationVector);
        appendable.append("/>");
    }

    private void appendKeyDerivation(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<manifest:key-derivation");
        util.appendAttribute(appendable, "manifest:key-derivation-name", this.keyDerivationName);
        util.appendAttribute(appendable, "manifest:key-size", this.derivedKeySize);
        util.appendAttribute(appendable, "manifest:iteration-count", this.derivationIterationCount);
        util.appendAttribute(appendable, "manifest:salt", this.derivationSalt);
        appendable.append("/>");
    }

    private void appendKeyGeneration(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<manifest:start-key-generation");
        util.appendAttribute(appendable, "manifest:start-key-generation-name",
                this.startKeyGenerationName);
        util.appendAttribute(appendable, "manifest:key-size", this.startKeySize);
        appendable.append("/>");
    }

    /**
     * @return the size of the uncompressed file
     */
    public int getUncompressedSize() {
        return this.uncompressedSize;
    }

    /**
     * @return the size of the compressed file
     */
    public long getCompressedSize() {
        return this.compressedSize;
    }

    /**
     * @return the CRC32
     */
    public long getCrc32() {
        return this.crc32;
    }
}
