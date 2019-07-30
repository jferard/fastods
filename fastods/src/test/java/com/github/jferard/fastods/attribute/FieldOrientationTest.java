package com.github.jferard.fastods.attribute;

import org.junit.Assert;
import org.junit.Test;

public class FieldOrientationTest {
    @Test
    public void standardTest() {
        Assert.assertEquals("hidden", FieldOrientation.HIDDEN.getValue());
        Assert.assertEquals("hidden", FieldOrientation.HIDDEN.toString());
    }
}