package com.github.jferard.fastods;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.github.jferard.fastods.util.XMLEscaper;

public class FrenchTableCellFormat implements TableCellFormat {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private XMLEscaper escaper;
	private NumberFormat numberFormat;
	private NumberFormat currencyFormat;
	private NumberFormat percentageFormat;

	public FrenchTableCellFormat(XMLEscaper escaper) {
		this.escaper = escaper;
		this.numberFormat = NumberFormat.getInstance(Locale.FRANCE);
		this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE);
		this.percentageFormat = NumberFormat.getPercentInstance(Locale.FRANCE);
	}

	@Override
	public String formatBoolean(Boolean value) {
		return value ? " VRAI" : "FAUX";
	}

	@Override
	public String formatCurrency(Number value, String currency) {
		return this.formatFloat(value)+" "+currency;
	}

	@Override
	public String formatDate(Date value) {
		return DATE_FORMAT.format(value);
	}

	@Override
	public String formatFloat(Number value) {
		return this.numberFormat.format(value);
	}

	@Override
	public String formatPercentage(Number value) {
		return this.percentageFormat.format(value);
	}

	@Override
	public String formatString(String value) {
		return this.escaper.escapeXMLContent(value);
	}

	@Override
	public String formatTime(long milliseconds) {
		StringBuilder sb = new StringBuilder();
		long curMilliseconds = milliseconds;

		final long days = TimeUnit.MILLISECONDS.toDays(curMilliseconds);
		sb.append(days).append("j.");
		curMilliseconds -= TimeUnit.DAYS.toMillis(days);

		final long hours = TimeUnit.MILLISECONDS.toHours(curMilliseconds);
		sb.append(hours).append(":");
		curMilliseconds -= TimeUnit.HOURS.toMillis(hours);

		final long minutes = TimeUnit.MILLISECONDS.toMinutes(curMilliseconds);
		sb.append(minutes).append(":");
		curMilliseconds -= TimeUnit.MINUTES.toMillis(minutes);
		
		final long seconds = TimeUnit.MILLISECONDS.toSeconds(curMilliseconds);
		sb.append(seconds);
		return sb.toString();
	}

}
