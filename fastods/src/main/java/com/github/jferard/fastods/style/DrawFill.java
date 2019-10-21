package com.github.jferard.fastods.style;

import com.github.jferard.fastods.ElementWithEmbeddedStyles;
import com.github.jferard.fastods.attribute.AttributeValue;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 20.111 draw:fill
 * The defined values for the draw:fill attribute are: bitmap, gradient, hatch, none, solid
 */
public interface DrawFill extends ElementWithEmbeddedStyles {
    void appendAttributes(XMLUtil util, Appendable appendable) throws IOException;
}
