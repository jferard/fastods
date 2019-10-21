package com.github.jferard.fastods.style;

import com.github.jferard.fastods.attribute.Color;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

public class DrawFillSolid implements DrawFill {
    private final Color color;

    public DrawFillSolid(final Color color) {
        this.color = color;
    }

    @Override
    public void appendAttributes(final XMLUtil util, final Appendable appendable) throws IOException {
        util.appendAttribute(appendable, "draw:fill", "solid");
        util.appendAttribute(appendable, "draw:fill-color", this.color);
    }

    @Override
    public void addEmbeddedStyles(final StylesContainer stylesContainer) {
        // do nothing
    }
}
