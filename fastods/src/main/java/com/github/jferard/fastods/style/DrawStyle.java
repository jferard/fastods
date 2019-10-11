package com.github.jferard.fastods.style;

import com.github.jferard.fastods.odselement.OdsElements;
import com.github.jferard.fastods.util.Style;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

public class DrawStyle implements ObjectStyle  {
    @Override
    public void appendXMLContent(XMLUtil util, Appendable appendable) throws IOException {

    }

    @Override
    public void addToElements(OdsElements odsElements) {

    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public ObjectStyleFamily getFamily() {
        return null;
    }

    @Override
    public String getKey() {
        return null;
    }
}
