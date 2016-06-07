package com.github.jferard.fastods.style;

import java.io.IOException;

import com.github.jferard.fastods.NamedObject;
import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.util.XMLUtil;

public abstract class DataStyle implements NamedObject {
	/**
	 * 19.517 : "The style:volatile attribute specifies whether unused style in
	 * a document are retained or discarded by consumers."
	 */
	protected final boolean volatileStyle;
	protected final String countryCode;
	protected final String languageCode;
	protected final String name;

	protected DataStyle(String name, String languageCode, String countryCode, final boolean volatileStyle) {
		this.countryCode = countryCode;
		this.languageCode = languageCode;
		this.name = name;
		this.volatileStyle = volatileStyle;
	}

	void addToFile(OdsFile odsFile) {
		odsFile.addDataStyle(this);
	}

	/**
	 * Adds this style to an OdsFile.
	 * 
	 * @param util
	 *            XML util for escaping characters and write data.
	 * @param appendable
	 *            the destination
	 * @throws IOException
	 *             if can't write data to file
	 */
	public abstract void appendXMLToStylesEntry(final XMLUtil util,
			final Appendable appendable) throws IOException;

	/**
	 * @return The two letter country code, e.g. 'US'
	 */
	String getCountryCode() {
		return this.countryCode;
	}

	/**
	 * @return The two letter language code, e.g. 'en'.
	 * @see http://www.ietf.org/rfc/rfc3066.txt
	 */
	String getLanguageCode() {
		return this.languageCode;
	}

	/* 
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	protected void appendLVAttributes(final XMLUtil util,
			final Appendable appendable) throws IOException {
		this.appendLocaleAttributes(util, appendable);
		this.appendVolatileAttribute(util, appendable);
	}

	protected void appendVolatileAttribute(final XMLUtil util,
			final Appendable appendable) throws IOException {
		if (this.volatileStyle)
			util.appendEAttribute(appendable, "style:volatile", this.volatileStyle);
	}

	protected void appendLocaleAttributes(final XMLUtil util,
			final Appendable appendable) throws IOException {
		if (this.languageCode != null)
			util.appendAttribute(appendable, "number:language",
					this.languageCode);
		if (this.countryCode != null)
			util.appendAttribute(appendable, "number:country",
					this.countryCode);
	}
}
