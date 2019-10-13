package com.github.jferard.fastods.style;

import com.github.jferard.fastods.ElementWithEmbeddedStyles;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

public class GraphicStyle implements ObjectStyle, ElementWithEmbeddedStyles {
    public static GraphicStyleBuilder builder(final String name) {
        return new GraphicStyleBuilder(name);
    }

    private final String name;
    private final boolean hidden;
    private final DrawFillImage drawFillImage;
    private String key;

    public GraphicStyle(final String name, final boolean hidden,
                        final DrawFillImage drawFillImage) {
        this.name = name;
        this.hidden = hidden;
        this.drawFillImage = drawFillImage;
    }

    @Override
    public ObjectStyleFamily getFamily() {
        return ObjectStyleFamily.GRAPHIC;
    }

    @Override
    public String getKey() {
        if (this.key == null) {
            this.key = this.getFamily() + "@" + this.getName();
        }
        return this.key;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<style:style");
        util.appendAttribute(appendable, "style:name", this.name);
        util.appendAttribute(appendable, "style:family", "graphic");
        appendable.append("><style:graphic-properties");
        if (this.drawFillImage != null) {
            util.appendAttribute(appendable, "draw:fill", "bitmap");
            util.appendAttribute(appendable, "draw:fill-image-name", this.drawFillImage.getName());
        }
        appendable.append("/></style:style>");

    }

    @Override
    public void addToElements(final OdsElements odsElements) {
        odsElements.addContentStyle(this);
    }

    @Override
    public boolean isHidden() {
        return this.hidden;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void addEmbeddedStyles(final StylesContainer stylesContainer) {
        if (this.drawFillImage != null) {
            stylesContainer.addStylesStyle(this.drawFillImage);
        }
    }
}
