package com.github.jferard.fastods;

import com.github.jferard.fastods.style.GraphicStyle;
import com.github.jferard.fastods.style.TextStyle;
import com.github.jferard.fastods.util.SVGRectangle;

/**
 * A builder for DrawFrame class
 */
public class DrawFrameBuilder {
    private final String name;
    private final FrameContent content;
    private final SVGRectangle rectangle;
    private TextStyle textStyle;
    private GraphicStyle drawStyle;
    private int zIndex;

    /**
     * @param name      the name of the frame
     * @param content   the content of the frame
     * @param rectangle the frame coordinates
     */
    DrawFrameBuilder(final String name, final FrameContent content, final SVGRectangle rectangle) {
        this.name = name;
        this.content = content;
        this.rectangle = rectangle;
        this.zIndex = 0;
        this.drawStyle = null;
        this.textStyle = null;
    }

    /**
     * @param zIndex the new z index
     * @return this for fluent style
     */
    public DrawFrameBuilder zIndex(final int zIndex) {
        this.zIndex = zIndex;
        return this;
    }

    /**
     * @param drawStyle the style of the frame
     * @return this for fluent style
     */
    public DrawFrameBuilder style(final GraphicStyle drawStyle) {
        this.drawStyle = drawStyle;
        return this;
    }

    /**
     * @param textStyle the style of the text
     * @return this for fluent style
     */
    public DrawFrameBuilder textStyle(final TextStyle textStyle) {
        this.textStyle = textStyle;
        return this;
    }

    /**
     * @return the frame
     */
    public DrawFrame build() {
        return new DrawFrame(this.name, this.content, this.rectangle, this.zIndex, this.drawStyle,
                this.textStyle);
    }
}
