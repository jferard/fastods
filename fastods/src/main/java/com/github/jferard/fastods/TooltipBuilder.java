package com.github.jferard.fastods;

import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.style.GraphicStyle;
import com.github.jferard.fastods.style.ShowableBuilder;
import com.github.jferard.fastods.util.SVGRectangle;

public class TooltipBuilder implements ShowableBuilder<TooltipBuilder> {
    private final String escapedContent;
    private GraphicStyle graphicStyle;
    private Length width;
    private Length height;
    private boolean display;
    private Length x;
    private Length y;
    private SVGRectangle rectangle;

    public TooltipBuilder(final String escapedContent) {
        this.escapedContent = escapedContent;
    }

    public TooltipBuilder rectangle(final SVGRectangle rectangle) {
        this.rectangle = rectangle;
        return this;
    }

    public TooltipBuilder graphicStyle(final GraphicStyle graphicStyle) {
        this.graphicStyle = graphicStyle;
        return this;
    }

    @Override
    public TooltipBuilder visible() {
        this.display = true;
        return this;
    }

    public Tooltip build() {
        return new Tooltip(this.escapedContent, this.rectangle, this.display, this.graphicStyle);
    }
}
