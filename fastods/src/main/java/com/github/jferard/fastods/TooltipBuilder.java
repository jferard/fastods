package com.github.jferard.fastods;

import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.style.GraphicStyle;
import com.github.jferard.fastods.style.ShowableBuilder;

public class TooltipBuilder implements ShowableBuilder<TooltipBuilder> {
    private final String escapedContent;
    private GraphicStyle graphicStyle;
    private Length width;
    private Length height;
    private boolean display;
    private Length x;
    private Length y;

    public TooltipBuilder(final String escapedContent) {
        this.escapedContent = escapedContent;
    }

    public TooltipBuilder x(final Length x) {
        this.x = x;
        return this;
    }

    public TooltipBuilder y(final Length y) {
        this.y = y;
        return this;
    }

    public TooltipBuilder width(final Length width) {
        this.width = width;
        return this;
    }

    public TooltipBuilder height(final Length height) {
        this.height = height;
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
        return new Tooltip(this.escapedContent, this.x, this.y, this.width, this.height, this.display, this.graphicStyle);
    }
}
