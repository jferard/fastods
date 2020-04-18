package com.github.jferard.fastods;

import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.DataStylesBuilder;
import com.github.jferard.fastods.odselement.MetaElement;
import com.github.jferard.fastods.ref.PositionUtil;
import com.github.jferard.fastods.ref.TableNameUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

public class OdsFactoryBuilder {
    private final Logger logger;
    private final PositionUtil positionUtil;
    private final WriteUtil writeUtil;
    private final XMLUtil xmlUtil;
    private final Map<String, String> additionalNamespaceByPrefix;
    private DataStyles format;
    private boolean libreOfficeMode;
    private MetaElement metaElement;

    public OdsFactoryBuilder(final Logger logger, final Locale locale) {
        this.logger = logger;
        this.positionUtil = new PositionUtil(new TableNameUtil());
        this.writeUtil = WriteUtil.create();
        this.xmlUtil = XMLUtil.create();
        this.additionalNamespaceByPrefix = new HashMap<String, String>();

        this.format = DataStylesBuilder.create(locale).build();
        this.libreOfficeMode = true;
        this.metaElement = MetaElement.create();
    }

    public OdsFactory build() {
        return new OdsFactory(this.logger, this.positionUtil, this.writeUtil, this.xmlUtil,
                this.additionalNamespaceByPrefix, this.format, this.libreOfficeMode, this.metaElement
        );
    }

    /**
     * Set the data styles
     *
     * @param ds the data styles
     * @return this for fluent style
     */
    public OdsFactoryBuilder dataStyles(final DataStyles ds) {
        this.format = ds;
        return this;
    }

    /**
     * Disable the LibreOffice mode. The LibreOffice mode adds a style to every cell, to force
     * LibreOffice to render the cell styles correctly.
     * This mode is set by default, and might slow down the generation of the file.
     *
     * @return this for fluent style
     */
    public OdsFactoryBuilder noLibreOfficeMode() {
        this.libreOfficeMode = false;
        return this;
    }

    /**
     * Use a custom meta element
     *
     * @param metaElement the meta element.
     * @return this for fluent style
     */
    public OdsFactoryBuilder metaElement(final MetaElement metaElement) {
        this.metaElement = metaElement;
        return this;
    }

    /**
     * Use custom namespace prefixes in content element.
     *
     * @param additionalNamespaceByPrefix a map prefix -> namespace
     * @return this for fluent style
     */
    public OdsFactoryBuilder addNamespaceByPrefix(final Map<String, String> additionalNamespaceByPrefix) {
        this.additionalNamespaceByPrefix.putAll(additionalNamespaceByPrefix);
        return this;
    }

}
