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

package com.github.jferard.fastods.odselement;

/**
 * A builder for `EncryptParameters`.
 */
public class EncryptParametersBuilder {
    private String compressedCheckSumType;
    private String startKeyGenerationName;
    private int startKeySize;
    private String keyDerivationName;
    private int derivedKeySize;
    private int derivationIterationCount;
    private String algorithmName;

    /**
     *
     */
    public EncryptParametersBuilder() {
        this.compressedCheckSumType =
                "urn:oasis:names:tc:opendocument:xmlns:manifest:1.0#sha256-1k";
        this.startKeyGenerationName = "http://www.w3.org/2000/09/xmldsig#sha256";
        this.startKeySize = 32;
        this.keyDerivationName = "PBKDF2";
        this.derivedKeySize = 32;
        this.derivationIterationCount = 100000;
        this.algorithmName = "http://www.w3.org/2001/04/xmlenc#aes256-cbc";
    }

    /**
     * @param plainDataSize                   4.8.13, manifest:size
     * @param compressedThenEncryptedDataSize the size of the data after : 1. compression ; 2. encryption
     * @param crc32                           the CRC-32 checksum of the uncompressed data
     * @param compressedCheckSum              4.8.2, manifest:checksum
     * @param derivationSalt                  4.8.12, manifest:salt
     * @param algorithmInitializationVector   4.8.5, manifest:initialisation-vector
     * @return the parameters
     */
    public EncryptParameters build(final int plainDataSize,
                                   final int compressedThenEncryptedDataSize,
                                   final long crc32, final String compressedCheckSum,
                                   final String derivationSalt,
                                   final String algorithmInitializationVector) {
        return new EncryptParameters(plainDataSize, compressedThenEncryptedDataSize, crc32,
                this.compressedCheckSumType, compressedCheckSum, this.startKeyGenerationName,
                this.startKeySize, this.keyDerivationName, this.derivedKeySize,
                this.derivationIterationCount, derivationSalt, this.algorithmName,
                algorithmInitializationVector);
    }

    /**
     * @param compressedCheckSumType 4.8.3, manifest:checksum-type
     * @return this for fluent style
     */
    public EncryptParametersBuilder compressedCheckSumType(final String compressedCheckSumType) {
        this.compressedCheckSumType = compressedCheckSumType;
        return this;
    }

    /**
     * @param startKeyGenerationName 4.8.6, manifest:start-key-generation-name
     * @return this for fluent style
     */
    public EncryptParametersBuilder startKeyGenerationName(final String startKeyGenerationName) {
        this.startKeyGenerationName = startKeyGenerationName;
        return this;
    }

    /**
     * @param startKeySize 4.8.7, manifest:key-size
     * @return this for fluent style
     */
    public EncryptParametersBuilder startKeySize(final int startKeySize) {
        this.startKeySize = startKeySize;
        return this;
    }

    /**
     * @param keyDerivationName 4.8.9, manifest:key-derivation-name
     * @return this for fluent style
     */
    public EncryptParametersBuilder keyDerivationName(final String keyDerivationName) {
        this.keyDerivationName = keyDerivationName;
        return this;
    }

    /**
     * @param derivedKeySize 4.8.7, manifest:key-size
     * @return this for fluent style
     */
    public EncryptParametersBuilder derivedKeySize(final int derivedKeySize) {
        this.derivedKeySize = derivedKeySize;
        return this;
    }

    /**
     * @param iterationCount 4.8.8, manifest:iteration-count
     * @return this for fluent style
     */
    public EncryptParametersBuilder derivationIterationCount(final int iterationCount) {
        this.derivationIterationCount = iterationCount;
        return this;
    }

    /**
     * @param algorithmName 4.8.1, manifest:algorithm-name
     * @return this for fluent style
     */
    public EncryptParametersBuilder algorithmName(final String algorithmName) {
        this.algorithmName = algorithmName;
        return this;
    }
}
