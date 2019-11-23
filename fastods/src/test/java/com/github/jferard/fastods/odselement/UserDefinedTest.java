package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.TestHelper;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

public class UserDefinedTest {
    @Test
    public void fromBoolean() throws IOException {
        TestHelper.assertXMLEquals(
                "<meta:user-defined meta:name=\"b\" meta:type=\"boolean\">true</meta:user-defined>",
                UserDefined.fromBoolean("b", true));
    }

    @Test
    public void fromDate() throws IOException {
        TestHelper.assertXMLEquals(
                "<meta:user-defined meta:name=\"d\" meta:type=\"date\">1970-01-01</meta:user-defined>",
                UserDefined.fromDate("d", new Date(0)));
    }

    @Test
    public void fromFloat() throws IOException {
        TestHelper.assertXMLEquals(
                "<meta:user-defined meta:name=\"f\" meta:type=\"float\">123.45</meta:user-defined>",
                UserDefined.fromFloat("f", 123.45));
    }

    @Test
    public void fromTime() throws IOException {
        TestHelper.assertXMLEquals(
                "<meta:user-defined meta:name=\"t\" meta:type=\"time\">00:20:34</meta:user-defined>",
                UserDefined.fromTime("t", new Date(1234567)));
    }

    @Test
    public void fromString() throws IOException {
        TestHelper.assertXMLEquals(
                "<meta:user-defined meta:name=\"s\" meta:type=\"string\">a string</meta:user-defined>",
                UserDefined.fromString("s", "a string"));
    }

    @Test
    public void appendXMLContent() {
    }
}