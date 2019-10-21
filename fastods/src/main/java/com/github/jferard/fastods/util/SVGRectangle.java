package com.github.jferard.fastods.util;

import com.github.jferard.fastods.XMLConvertible;
import com.github.jferard.fastods.attribute.Length;
import com.github.jferard.fastods.attribute.SimpleLength;

import java.io.IOException;

public class SVGRectangle implements XMLConvertible {
    /**
     * @param x      the x coord in mm
     * @param y      the y coord in mm
     * @param width  the width in mm
     * @param height the height in mm
     * @return the rectangle
     */
    public static SVGRectangle mm(final double x, final double y, final double width,
                                  final double height) {
        return new SVGRectangle(SimpleLength.mm(x), SimpleLength.mm(y), SimpleLength.mm(width),
                SimpleLength.mm(height));
    }

    /**
     * @param x      the x coord in cm
     * @param y      the y coord in cm
     * @param width  the width in cm
     * @param height the height in cm
     * @return the rectangle
     */
    public static SVGRectangle cm(final double x, final double y, final double width,
                                  final double height) {
        return new SVGRectangle(SimpleLength.cm(x), SimpleLength.cm(y), SimpleLength.cm(width),
                SimpleLength.cm(height));
    }

    /**
     * @param x      the x coord in in
     * @param y      the y coord in in
     * @param width  the width in in
     * @param height the height in in
     * @return the rectangle
     */
    public static SVGRectangle in(final double x, final double y, final double width,
                                  final double height) {
        return new SVGRectangle(SimpleLength.in(x), SimpleLength.in(y), SimpleLength.in(width),
                SimpleLength.in(height));
    }

    /**
     * @param x      the x coord in pt
     * @param y      the y coord in pt
     * @param width  the width in pt
     * @param height the height in pt
     * @return the rectangle
     */
    public static SVGRectangle pt(final double x, final double y, final double width,
                                  final double height) {
        return new SVGRectangle(SimpleLength.pt(x), SimpleLength.pt(y), SimpleLength.pt(width),
                SimpleLength.pt(height));
    }

    /**
     * @param x      the x coord in pc
     * @param y      the y coord in pc
     * @param width  the width in pc
     * @param height the height in pc
     * @return the rectangle
     */
    public static SVGRectangle pc(final double x, final double y, final double width,
                                  final double height) {
        return new SVGRectangle(SimpleLength.pc(x), SimpleLength.pc(y), SimpleLength.pc(width),
                SimpleLength.pc(height));
    }

    /**
     * @param x      the x coord in em
     * @param y      the y coord in em
     * @param width  the width in em
     * @param height the height in em
     * @return the rectangle
     */
    public static SVGRectangle em(final double x, final double y, final double width,
                                  final double height) {
        return new SVGRectangle(SimpleLength.em(x), SimpleLength.em(y), SimpleLength.em(width),
                SimpleLength.em(height));
    }

    private final Length x;
    private final Length y;
    private final Length width;
    private final Length height;

    public SVGRectangle(final Length x, final Length y, final Length width, final Length height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        if (this.x != null) {
            util.appendAttribute(appendable, "svg:x", this.x);
        }
        if (this.y != null) {
            util.appendAttribute(appendable, "svg:y", this.y);
        }
        if (this.width != null) {
            util.appendAttribute(appendable, "svg:width", this.width);
        }
        if (this.height != null) {
            util.appendAttribute(appendable, "svg:height", this.height);
        }
        // weird patch for LO bug
        if (this.x == null && this.y == null && (this.width != null || this.height != null)) {
            util.appendAttribute(appendable, "svg:x", "");
        }
    }
}
