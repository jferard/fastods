package com.github.jferard.fastods;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file BorderStyle.java is part of FastODS.
 */
public interface XMLWritable {
	void writeXML(Util util, Writer writer) throws IOException;
}
