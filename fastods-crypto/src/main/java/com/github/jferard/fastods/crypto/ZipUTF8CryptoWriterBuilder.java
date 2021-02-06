/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2021 J. Férard <https://github.com/jferard>
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
import com.github.jferard.fastods.util.ZipUTF8Writer;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilder;
import com.github.jferard.fastods.util.ZipUTF8WriterBuilderImpl;

import javax.crypto.NoSuchPaddingException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

/**
 * A builder for `ZipUTF8CryptoWriter`.
 */
public class ZipUTF8CryptoWriterBuilder implements ZipUTF8WriterBuilder {
    /**
     * @param password the password to encrypt data
     * @return a builder
     */
    public static ZipUTF8WriterBuilder create(final char[] password) {
        return new ZipUTF8CryptoWriterBuilder(new ZipUTF8WriterBuilderImpl(),
                EncryptParameters.builder(), password
        );
    }


    private final ZipUTF8WriterBuilderImpl writerBuilder;
    private final char[] password;
    private final EncryptParametersBuilder parametersBuilder;

    /**
     * @param writerBuilder the writer builder. May be initialized
     * @param parametersBuilder the parameters builder. May be initialized
     * @param password the password
     */
    public ZipUTF8CryptoWriterBuilder(final ZipUTF8WriterBuilderImpl writerBuilder,
                                      final EncryptParametersBuilder parametersBuilder,
                                      final char[] password) {
        this.password = password;
        this.writerBuilder = writerBuilder;
        this.parametersBuilder = parametersBuilder;
    }

    @Override
    public ZipUTF8Writer build(final OutputStream outputStream) {
        try {
            return new ZipUTF8CryptoWriter(this.writerBuilder.build(outputStream),
                    new StandardEncrypter(this.parametersBuilder), this.password);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
