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

package com.github.jferard.fastods.crypto;

import com.github.jferard.fastods.annotation.Beta;
import com.github.jferard.fastods.odselement.OdsEntry;
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilder;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

/**
 * A writer that encypts data on the fly.
 */
@Beta
public class ZipUTF8CryptoWriter implements ZipUTF8Writer {
    /**
     * **Beware: for security reasons, this fills the password array with 0's**
     *
     * @param password the password to encrypt data
     * @return a builder
     * @throws NoSuchAlgorithmException won't happen since SHA-256 is pretty common
     */
    public static ZipUTF8WriterBuilder builder(final char[] password)
            throws NoSuchAlgorithmException {
        return ZipUTF8CryptoWriterBuilder.create(password);
    }

    private final ZipUTF8Writer zipUTF8Writer;
    private final StandardEncrypter encrypter;
    private final byte[] hashedPassword;
    private ByteArrayOutputStream out;
    private Writer writer;
    private OdsEntry curEntry;
    private boolean toRegister;

    public ZipUTF8CryptoWriter(final ZipUTF8Writer zipUTF8Writer,
                               final StandardEncrypter encrypter, final byte[] hashedPassword) {
        this.zipUTF8Writer = zipUTF8Writer;
        this.encrypter = encrypter;
        this.hashedPassword = hashedPassword;
    }

    @Override
    public void setComment(final String comment) {
        this.zipUTF8Writer.setComment(comment);
    }

    @Override
    public void putAndRegisterNextEntry(final OdsEntry entry) {
        this.toRegister = true;
        this.putNextEntry(entry);
    }

    @Override
    public void registerEntry(final OdsEntry entry) {
        this.toRegister = false;
        this.zipUTF8Writer.registerEntry(entry);
    }

    @Override
    public void putNextEntry(final OdsEntry entry) {
        this.curEntry = entry;
        this.out = new ByteArrayOutputStream();
        this.writer = new OutputStreamWriter(this.out, StandardCharsets.UTF_8);
    }

    @Override
    public void closeEntry() throws IOException {
        this.writer.flush();
        final byte[] plainTextBytes = this.out.toByteArray();
        final EntryAndData entryAndData;
        if (this.curEntry.neverEncrypt()) {
            entryAndData = new EntryAndData(this.curEntry, plainTextBytes);
        } else {
            entryAndData = this.getEncryptedEntryAndData(plainTextBytes);
            if (this.toRegister) {
                this.zipUTF8Writer.registerEntry(entryAndData.getEntry());
                this.toRegister = false;
            }
        }
        this.zipUTF8Writer.putNextEntry(entryAndData.getEntry());
        this.zipUTF8Writer.write(entryAndData.getData());
        this.zipUTF8Writer.flush();
        this.zipUTF8Writer.closeEntry();
        this.curEntry = null;
    }

    private EntryAndData getEncryptedEntryAndData(final byte[] plainTextBytes) throws IOException {
        final EntryAndData entryAndData;
        try {
            entryAndData = this.getEncryptedEntryAndDataUnchecked(plainTextBytes);
        } catch (final NoSuchAlgorithmException | InvalidKeyException
                | InvalidAlgorithmParameterException | NoSuchPaddingException
                | BadPaddingException | IllegalBlockSizeException e) {
            throw new IOException("Can't encrypt file", e);
        }
        return entryAndData;
    }

    private EntryAndData getEncryptedEntryAndDataUnchecked(final byte[] plainTextBytes)
            throws IOException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        final byte[] salt = this.encrypter.generateSalt();
        final byte[] iv = this.encrypter.generateIV();
        final byte[] compressedData = this.encrypter.compress(plainTextBytes);
        final byte[] compressedThenEncryptedData = this.encrypter.encrypt(
                compressedData, this.hashedPassword, salt, iv);
        final String compressedCheckSum = Base64.toBase64String(
                this.encrypter.getDataChecksum(compressedData));
        final long crc32 = this.getCrc32(compressedThenEncryptedData);
        final OdsEntry entry = this.curEntry.encryptParameters(
                this.encrypter.buildParameters(
                        plainTextBytes.length, compressedThenEncryptedData.length, crc32,
                        compressedCheckSum,
                        Base64.toBase64String(salt), Base64.toBase64String(iv)));
        return new EntryAndData(entry, compressedThenEncryptedData);
    }

    private long getCrc32(final byte[] encryptedCompressedTextBytes) {
        final CRC32 crc32 = new CRC32();
        crc32.update(encryptedCompressedTextBytes);
        return crc32.getValue();
    }

    @Override
    public void finish() throws IOException {
        this.zipUTF8Writer.finish();
    }

    @Override
    public void write(final byte[] bytes) throws IOException {
        this.out.write(bytes);
    }

    @Override
    public void close() throws IOException {
        this.zipUTF8Writer.close();
    }

    @Override
    public void flush() throws IOException {
        this.writer.flush();
        this.out.flush();
    }

    @Override
    public Appendable append(final CharSequence csq) throws IOException {
        this.writer.append(csq);
        return this;
    }

    @Override
    public Appendable append(final CharSequence csq, final int start, final int end)
            throws IOException {
        this.writer.append(csq, start, end);
        return this;
    }

    @Override
    public Appendable append(final char c) throws IOException {
        this.writer.append(c);
        return this;
    }
}
