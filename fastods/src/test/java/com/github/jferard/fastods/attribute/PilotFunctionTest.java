package com.github.jferard.fastods.attribute;

import org.junit.Assert;
import org.junit.Test;

public class PilotFunctionTest {
    @Test
    public void standardTest() {
        Assert.assertEquals("sum", PilotStandardFunction.SUM.getValue());
        Assert.assertEquals("sum", PilotStandardFunction.SUM.toString());
    }

    @Test
    public void customTest() {
        final PilotFunction pilotFunction = new PilotFunction() {
            @Override
            public CharSequence getValue() {
                return "myfunc";
            }
        };
        Assert.assertEquals("myfunc", pilotFunction.getValue());
    }
}