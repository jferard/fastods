package com.github.jferard.fastods.style;

import com.github.jferard.fastods.attribute.Angle;
import com.github.jferard.fastods.attribute.Color;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 16.40.1 <draw:gradient>
 */
public class DrawFillGradient implements DrawFill, ObjectStyle {
    private final Angle angle;
    private final Color startColor;
    private final int startIntensity;
    private final Color endColor;
    private final int endIntensity;
    private String name;
    private String key;

    public DrawFillGradient(final String name, final Angle angle, final Color startColor,
                            final int startIntensity, final Color endColor,
                            final int endIntensity) {
        this.angle = angle;
        this.startColor = startColor;
        this.startIntensity = startIntensity;
        this.endColor = endColor;
        this.endIntensity = endIntensity;
    }

    @Override
    public ObjectStyleFamily getFamily() {
        return ObjectStyleFamily.DRAW_FILL_GRADIENT;
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
        appendable.append("<draw:gradient");
        util.appendAttribute(appendable, "draw:name", this.name);
        util.appendAttribute(appendable, "draw:angle", this.angle);
        util.appendAttribute(appendable, "draw:start-color", this.startColor);
        util.appendAttribute(appendable, "draw:start-intensity", this.startIntensity);
        util.appendAttribute(appendable, "draw:end-color", this.endColor);
        util.appendAttribute(appendable, "draw:end-intensity", this.endIntensity);
        appendable.append("/>");
    }

    @Override
    public void addToElements(final OdsElements odsElements) {
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void appendAttributes(final XMLUtil util, final Appendable appendable)
            throws IOException {
        util.appendAttribute(appendable, "draw:fill", "gradient");
        util.appendAttribute(appendable, "draw:fill-gradient-name", this.name);
    }

    @Override
    public void addEmbeddedStyles(final StylesContainer stylesContainer) {
        stylesContainer.addStylesStyle(this);
    }
}
