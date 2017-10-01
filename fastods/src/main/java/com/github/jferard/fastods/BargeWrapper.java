/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
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
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods;

import com.github.jferard.charbarge.CharBarge;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.zip.ZipEntry;

/**
 * Created by jferard on 01/10/17.
 */
public class BargeWrapper implements ZipUTF8Writer {
    private final CharBarge barge;
    private final ZipUTF8Writer zipUTF8Writer;

    public BargeWrapper(final CharBarge barge, final ZipUTF8Writer zipUTF8Writer) {
        this.barge = barge;
        this.zipUTF8Writer = zipUTF8Writer;
    }

    @Override
    public void closeEntry() throws IOException {
        this.zipUTF8Writer.closeEntry();
    }

    @Override
    public void finish() throws IOException {
        this.barge.forceFlushTo(this.zipUTF8Writer);
        this.barge.close();
        this.zipUTF8Writer.finish();
    }

    @Override
    public void putNextEntry(final ZipEntry entry) throws IOException {
        this.zipUTF8Writer.putNextEntry(entry);
    }

    @Override
    public void setComment(final String comment) {
        this.zipUTF8Writer.setComment(comment);
    }

    @Override
    public void write(final String str) throws IOException {
        this.barge.append(str);
    }

    @Override
    public void close() throws IOException {
        this.barge.close();
        this.zipUTF8Writer.close();
    }

    @Override
    public void flush() throws IOException {
        this.barge.forceFlushTo(this.zipUTF8Writer);
    }

    @Override
    public Appendable append(final CharSequence charSequence) throws IOException {
        return this.barge.append(charSequence);
    }

    @Override
    public Appendable append(final CharSequence charSequence, final int start, final int end) throws IOException {
        return this.barge.append(charSequence, start, end);
    }

    @Override
    public Appendable append(final char c) throws IOException {
        return this.barge.append(c);
    }
}
