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

import com.github.jferard.charbarge.AppendableConsumer;
import com.github.jferard.charbarge.CharBarge;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;

/**
 * The OdsFileWriterAdapter class represents an adapter to a writer. It stores a queue of flushers. Usage:
 * <ul>
 * <li>A producer thread that writes on a OdsFileWriterAdapter.document()</li>
 * <li>A consumer thread that uses the following stucture</li>
 * </ul>
 *
 * <pre>
 * while (this.writerAdapter.isNotStopped()) {
 *     this.writerAdapter.waitForData();
 *     this.writerAdapter.flushAdaptee();
 * }
 * this.writerAdapter.flushAdaptee();
 * </pre>
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class OdsFileWriterToBarge implements OdsFileWriter {
	private final ZipUTF8Writer zipUTF8Writer;
	private final XMLUtil xmlUtil;
	private final OdsDocument document;
	private final CharBarge barge;
	private ZipUTF8Writer bargeWrapper;

	public static OdsFileWriterToBarge create(final OdsDocument document, final ZipUTF8Writer zipUTF8Writer) {
		return new OdsFileWriterToBarge(document, zipUTF8Writer, XMLUtil.create(), CharBarge.create(256*1024));
	}


	OdsFileWriterToBarge(final OdsDocument document, final ZipUTF8Writer zipUTF8Writer, final XMLUtil xmlUtil, final CharBarge barge) {
		this.zipUTF8Writer = zipUTF8Writer;
		this.xmlUtil = xmlUtil;
		this.document = document;
		this.barge = barge;
		this.bargeWrapper = new BargeWrapper(this.barge, this.zipUTF8Writer);
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public OdsDocument document() {
		return this.document;
	}

	public AppendableConsumer consumer() { return new AppendableConsumer(this.barge, this.zipUTF8Writer); }

	@Override
	public synchronized void save() throws IOException {
	}

	@Override
	public void update(final OdsFlusher flusher) throws IOException {
		flusher.flushInto(this.xmlUtil, this.bargeWrapper);
	}
}
