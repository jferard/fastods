package com.github.jferard.fastods.style;

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

public class DrawFillImage implements ObjectStyle {
    private final String name;
    private final String href;
    private String key;

    public DrawFillImage(final String name, final String href) {
        this.name = name;
        this.href = href;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<draw:fill-image");
        util.appendAttribute(appendable, "draw:name", this.name);
        util.appendAttribute(appendable, "xlink:href", this.href);
        util.appendAttribute(appendable, "xlink:type", "simple");
        util.appendAttribute(appendable, "xlink:show", "embed");
        util.appendAttribute(appendable, "xlink:actuate", "onLoad");
        appendable.append("/>");
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ObjectStyleFamily getFamily() {
        return ObjectStyleFamily.DRAW_FILL_IMAGE;
    }

    @Override
    public String getKey() {
        if (this.key == null) {
            this.key = this.getFamily() + "@" + this.getName();
        }
        return this.key;
    }

    @Override
    public void addToElements(final OdsElements odsElements) {
    }

    @Override
    public boolean isHidden() {
        return false;
    }
}
