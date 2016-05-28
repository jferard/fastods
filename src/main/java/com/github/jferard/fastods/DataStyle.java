package com.github.jferard.fastods;

import java.io.IOException;

public interface DataStyle extends NamedObject {
	void addToFile(OdsFile file);
	
	void appendXMLToStylesEntry(final Util util,
			final Appendable appendable) throws IOException;
}
