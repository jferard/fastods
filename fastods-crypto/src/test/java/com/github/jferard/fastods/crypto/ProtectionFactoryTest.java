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
import com.github.jferard.fastods.util.XMLUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ProtectionFactoryTest {
    @Test
    public void test() throws NoSuchAlgorithmException, IOException {
        final Protection p = ProtectionFactory.createSha256("passwd".toCharArray());
        final StringBuilder sb = new StringBuilder();
        p.appendAttributes(XMLUtil.create(), sb);

        Assert.assertEquals(
                " table:protected=\"true\" table:protection-key=\"DWvmmyZHF/LdM2UuISsXMQS0pke3wRrnLpiF8RzTEvs=\" table:protection-key-digest-algorithm=\"http://www.w3.org/2000/09/xmldsig#sha256\"",
                sb.toString());
    }

}