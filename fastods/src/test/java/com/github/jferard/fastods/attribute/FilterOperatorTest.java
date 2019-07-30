package com.github.jferard.fastods.attribute;

import org.junit.Assert;
import org.junit.Test;

public class FilterOperatorTest {
    @Test
    public void test() {
        Assert.assertEquals("bottom percent", FilterOperator.BOTTOM_PERCENT.getValue());
        Assert.assertEquals("bottom percent", FilterOperator.BOTTOM_PERCENT.toString());
    }

}