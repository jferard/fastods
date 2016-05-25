package com.github.jferard.fastods;

import java.io.IOException;

public interface StyleTag {
	String getName();
	
	void appendXML(Util util, Appendable appendable, ContentEntry content) throws IOException;
}
