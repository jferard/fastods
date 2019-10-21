package com.github.jferard.fastods.style;

import com.github.jferard.fastods.attribute.Angle;
import com.github.jferard.fastods.attribute.Color;
import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.odselement.StylesContainer;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * 16.40.5 <draw:hatch>
 */
public class DrawFillHatch implements DrawFill, ObjectStyle {
    private final String name;
    private final Angle angle;
    private final Color color;
    private final Length distance;
    private String key;

    public DrawFillHatch(final String name, final Angle angle, final Color color,
                         final Length distance) {
        this.name = name;
        this.angle = angle;
        this.color = color;
        this.distance = distance;
    }

    @Override
    public ObjectStyleFamily getFamily() {
        return ObjectStyleFamily.DRAW_FILL_HATCH;
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
        appendable.append("<draw:hatch");
        util.appendAttribute(appendable, "draw:name", this.name);
        util.appendAttribute(appendable, "draw:angle", this.angle);
        util.appendAttribute(appendable, "draw:color", this.color);
        util.appendAttribute(appendable, "draw:distance", this.distance);
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
        util.appendAttribute(appendable, "draw:fill", "hatch");
        util.appendAttribute(appendable, "draw:fill-hatch-name", this.name);
    }

    @Override
    public void addEmbeddedStyles(final StylesContainer stylesContainer) {
        stylesContainer.addStylesStyle(this);
    }
}
