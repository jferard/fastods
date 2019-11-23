package com.github.jferard.fastods.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SVGRectangleTest {
    private XMLUtil util;

    @Before
    public void setUp() {
        this.util = XMLUtil.create();
    }

    @Test
    public void testMm() throws IOException {
        final SVGRectangle rectangle = SVGRectangle.mm(0, 1, 2, 3);

        this.testAux(rectangle, "mm");
    }

    @Test
    public void testCm() throws IOException {
        final SVGRectangle rectangle = SVGRectangle.cm(0, 1, 2, 3);

        this.testAux(rectangle, "cm");
    }

    @Test
    public void testIn() throws IOException {
        final SVGRectangle rectangle = SVGRectangle.in(0, 1, 2, 3);

        this.testAux(rectangle, "in");
    }

    @Test
    public void testPt() throws IOException {
        final SVGRectangle rectangle = SVGRectangle.pt(0, 1, 2, 3);

        this.testAux(rectangle, "pt");
    }

    @Test
    public void testPc() throws IOException {
        final SVGRectangle rectangle = SVGRectangle.pc(0, 1, 2, 3);

        this.testAux(rectangle, "pc");
    }

    @Test
    public void testEm() throws IOException {
        final SVGRectangle rectangle = SVGRectangle.em(0, 1, 2, 3);

        this.testAux(rectangle, "em");
    }

    private void testAux(final SVGRectangle rectangle, final String unit) throws IOException {
        final StringBuilder sb = new StringBuilder();
        rectangle.appendXMLContent(this.util, sb);
        Assert.assertEquals(" svg:x=\"0@\" svg:y=\"1@\" svg:width=\"2@\" svg:height=\"3@\""
                .replaceAll("@", unit), sb.toString());
    }

}