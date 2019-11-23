package com.github.jferard.fastods.style;

import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

public class DrawFillNone implements DrawFill {
    @Override
    public void appendAttributes(final XMLUtil util, final Appendable appendable)
            throws IOException {
        util.appendAttribute(appendable, "draw:fill", "none");
    }

    @Override
    public void addEmbeddedStyles(final StylesContainer stylesContainer) {
        // do nothing
    }
}
