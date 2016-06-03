package com.github.jferard.fastods;

import java.io.IOException;
import java.io.Writer;
import java.util.zip.ZipOutputStream;

import com.github.jferard.fastods.util.XMLUtil;

public interface OdsEntry {
	void write(XMLUtil util, ZipOutputStream out, Writer writer)
			throws IOException;
}
