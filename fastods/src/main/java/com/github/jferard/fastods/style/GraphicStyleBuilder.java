package com.github.jferard.fastods.style;

import com.github.jferard.fastods.util.StyleBuilder;

public class GraphicStyleBuilder
        implements StyleBuilder<GraphicStyle>, ShowableBuilder<GraphicStyleBuilder> {
    private final String name;
    private boolean hidden;
    private DrawFillImage fillImage;

    public GraphicStyleBuilder(final String name) {
        this.name = name;
        this.hidden = true;
    }

    public GraphicStyleBuilder fillImage(final DrawFillImage fillImage) {
        this.fillImage = fillImage;
        return this;
    }

    @Override
    public GraphicStyleBuilder visible() {
        this.hidden = false;
        return this;
    }

    @Override
    public GraphicStyle build() {
        return new GraphicStyle(this.name, this.hidden, this.fillImage);
    }
}
