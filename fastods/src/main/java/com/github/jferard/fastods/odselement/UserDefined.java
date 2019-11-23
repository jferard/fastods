package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.XMLConvertible;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.Date;

/**
 * 4.3.3<meta:user-defined>
 */
public class UserDefined implements XMLConvertible {
    /**
     * @param name  the name of the metadata
     * @param value the boolean value
     * @return the user defined metadata
     */
    public static UserDefined fromBoolean(final String name, final boolean value) {
        return new UserDefined(name, MetaValueType.BOOLEAN, String.valueOf(value));
    }

    /**
     * @param name  the name of the metadata
     * @param value the date value
     * @return the user defined metadata
     */
    public static UserDefined fromDate(final String name, final Date value) {
        return new UserDefined(name, MetaValueType.DATE, MetaElement.DF_DATE.format(value));
    }

    /**
     * @param name  the name of the metadata
     * @param value the numeric value
     * @return the user defined metadata
     */
    public static UserDefined fromFloat(final String name, final Number value) {
        return new UserDefined(name, MetaValueType.FLOAT, value.toString());
    }

    /**
     * @param name  the name of the metadata
     * @param value the date value
     * @return the user defined metadata
     */
    public static UserDefined fromTime(final String name, final Date value) {
        return new UserDefined(name, MetaValueType.TIME, MetaElement.DF_TIME.format(value));
    }

    /**
     * @param name  the name of the metadata
     * @param value the string value
     * @return the user defined metadata
     */
    public static UserDefined fromString(final String name, final String value) {
        return new UserDefined(name, MetaValueType.STRING, value);
    }

    private final String name;
    private final MetaValueType valueType;
    private final String value;

    /**
     * @param name      the name of the metadata
     * @param valueType the value type
     * @param value     the value as a string
     */
    UserDefined(final String name, final MetaValueType valueType, final String value) {
        this.name = name;
        this.valueType = valueType;
        this.value = value;
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<meta:user-defined");
        util.appendAttribute(appendable, "meta:name", this.name);
        util.appendAttribute(appendable, "meta:type", this.valueType.getValue());
        appendable.append(">").append(this.value).append("</meta:user-defined>");
    }
}
