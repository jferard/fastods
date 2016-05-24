package com.github.jferard.fastods;

import java.io.IOException;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 *
 *         This file BorderAttribute.java is part of FastODS.
 */
public interface XMLAppendable {
	/**
	 * Appends XML code to the Appendable, using Util.
	 * 
	 * @param util
	 *            the Util instance
	 * @param appendable
	 *            the Appendable object
	 * @throws IOException
	 */
	void appendXML(Util util, Appendable appendable) throws IOException;
}
