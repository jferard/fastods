package com.github.jferard.fastods.style;

import com.github.jferard.fastods.attribute.Color;
import com.github.jferard.fastods.util.StyleBuilder;

/**
 * 16.37 Graphic Styles
 */
public class GraphicStyleBuilder
        implements StyleBuilder<GraphicStyle>, ShowableBuilder<GraphicStyleBuilder> {
    private final String name;
    private boolean hidden;
    private DrawFillBitmap fillImage;
    private DrawFillGradient gradient;
    private Color color;
    private DrawFillHatch hatch;
    private DrawFill drawFill;

    /**
     * @param name the name of the style
     */
    GraphicStyleBuilder(final String name) {
        this.name = name;
        this.hidden = true;
    }

    /**
     * @param drawFill the fill style
     * @return this for fluent style
     */
    public GraphicStyleBuilder drawFill(final DrawFill drawFill) {
        this.drawFill = drawFill;
        return this;
    }

    @Override
    public GraphicStyleBuilder visible() {
        this.hidden = false;
        return this;
    }

    @Override
    public GraphicStyle build() {
        return new GraphicStyle(this.name, this.hidden, this.drawFill);
    }
}
