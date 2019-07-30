package com.github.jferard.fastods.attribute;

import org.junit.Assert;
import org.junit.Test;

public class PageCenteringTest {
    @Test
    public void test() {
        Assert.assertEquals("horizontal", PageCentering.HORIZONTAL.getValue());
    }
}