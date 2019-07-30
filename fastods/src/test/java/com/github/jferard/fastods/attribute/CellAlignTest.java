package com.github.jferard.fastods.attribute;

import org.junit.Assert;
import org.junit.Test;

public class CellAlignTest {
    @Test
    public void test() {
        Assert.assertEquals("center", CellAlign.CENTER.getValue());
    }
}