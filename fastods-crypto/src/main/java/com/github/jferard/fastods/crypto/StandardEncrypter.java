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

package com.github.jferard.fastods.crypto;

import com.github.jferard.fastods.odselement.EncryptParameters;
import com.github.jferard.fastods.odselement.EncryptParametersBuilder;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * A class that can encrypt data
 */
class StandardEncrypter {
    public static final int BUFFER_SIZE = 4096;
    public static final int BITS_BY_BYTE = 8;

    public static StandardEncrypter create(final EncryptParametersBuilder parametersBuilder)
            throws NoSuchPaddingException, NoSuchAlgorithmException {
        final SecureRandom sha1PRNG = SecureRandom.getInstance("SHA1PRNG");
        final Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");  // W3C padding
        return new StandardEncrypter(sha1PRNG, cipher, 100000, 32, 32, parametersBuilder
        );
    }

    private final EncryptParametersBuilder parametersBuilder;
    private final SecureRandom randomSecureRandom;
    private final Cipher cipher;
    private final int iterationCount;
    private final int startKeySize;
    private final int keySize;

    StandardEncrypter(final SecureRandom randomSecureRandom,
                      final Cipher cipher, final int iterationCount,
                      final int startKeySize, final int keySize,
                      final EncryptParametersBuilder parametersBuilder)
            throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.parametersBuilder = parametersBuilder;
        Security.addProvider(new BouncyCastleProvider());
        this.randomSecureRandom = randomSecureRandom;
        this.cipher = cipher;
        this.iterationCount = iterationCount;
        this.startKeySize = startKeySize;
        this.keySize = keySize;
    }

    /**
     * Encrypt the compressed bytes.
     *
     * @param compressedTextBytes the compressed bytes
     * @param hashedPassword      the hashed password
     * @param salt                the salt
     * @param iv                  the initialisation vector
     * @return the encrypted bytes
     * @throws NoSuchAlgorithmException           if something fails
     * @throws NoSuchPaddingException             if something fails
     * @throws InvalidAlgorithmParameterException if something fails
     * @throws InvalidKeyException                if something fails
     * @throws BadPaddingException                if something fails
     * @throws IllegalBlockSizeException          if something fails
     */
    public byte[] encrypt(final byte[] compressedTextBytes, final byte[] hashedPassword,
                          final byte[] salt, final byte[] iv) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        final Key key = this.getKey(salt, hashedPassword);
        final Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding"); // W3C padding
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(compressedTextBytes);
    }

    /**
     * @param salt           the salt
     * @param hashedPassword the hashed password
     * @return the key
     */
    private Key getKey(final byte[] salt, final byte[] hashedPassword) {
        assert hashedPassword.length == this.startKeySize;
        final PBEParametersGenerator generator = new PKCS5S2ParametersGenerator(new SHA1Digest());
        generator.init(hashedPassword, salt, this.iterationCount);
        final KeyParameter keyParam =
                (KeyParameter) generator.generateDerivedParameters(this.keySize * BITS_BY_BYTE);
        return new SecretKeySpec(keyParam.getKey(), "AES");
    }

    /**
     * Deflate as LibreOffice.
     *
     * @param data the data to DEFLATE
     * @return the DEFLATEd data
     * @throws IOException if a I/O error occurs
     */
    public byte[] compress(final byte[] data) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, true);
        final DeflaterOutputStream dos = new DeflaterOutputStream(os, deflater);

        final InputStream is = new ByteArrayInputStream(data);
        final byte[] buffer = new byte[BUFFER_SIZE];
        int count = is.read(buffer);
        while (count != -1) {
            dos.write(buffer, 0, count);
            count = is.read(buffer);
        }
        dos.close();
        return os.toByteArray();
    }

    /**
     * @param data the data
     * @return the SHA-256 checksum of the 1024 first bytes.
     * @throws NoSuchAlgorithmException should not happen
     */
    public byte[] getDataChecksum(final byte[] data) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(data, 0, Math.min(data.length, 1024));
        return digest.digest();
    }

    /**
     * @return the initialisation vector
     */
    public byte[] generateIV() {
        final byte[] iv = new byte[this.cipher.getBlockSize()];
        this.randomSecureRandom.nextBytes(iv);
        return iv;
    }

    /**
     * @return the salt
     */
    public byte[] generateSalt() {
        final byte[] salt = new byte[16];
        this.randomSecureRandom.nextBytes(salt);
        return salt;
    }

    /**
     * Build the parameters to pass to the encrypted OdsEntry
     *
     * @param plainDataSize                   size of the plain text in bytes
     * @param compressedThenEncryptedDataSize size of the compressed then encrypted data in bytes
     * @param crc32                           check
     * @param compressedCheckSum              the checksum of the compressed data
     * @param derivationSalt                  salt
     * @param algorithmInitializationVector   iv
     * @return the parameters
     */
    public EncryptParameters buildParameters(final int plainDataSize,
                                             final int compressedThenEncryptedDataSize,
                                             final long crc32,
                                             final String compressedCheckSum,
                                             final String derivationSalt,
                                             final String algorithmInitializationVector) {
        return this.parametersBuilder
                .build(plainDataSize, compressedThenEncryptedDataSize, crc32, compressedCheckSum,
                        derivationSalt, algorithmInitializationVector);
    }
}
