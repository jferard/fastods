package com.github.jferard.fastods;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class CellValueTest {
	private HeavyTableRow htr;

	@Before
	public void setUp() {
		this.htr = PowerMock.createMock(HeavyTableRow.class);
	}

	@Test
	public final void testBooleanFromTypeAndObject() {
		final CellValue v = CellValue.fromTypeAndObject(TableCell.Type.BOOLEAN, true);
		
		// PLAY
		this.htr.setBooleanValue(0, true);
		PowerMock.replayAll();
		v.setToRow(this.htr, 0);
		PowerMock.verifyAll();
	}

	@Test
	public final void testVoidFromObject() {
		final CellValue v = CellValue.fromObject(null);
		
		// PLAY
		this.htr.setVoidValue(0);
		PowerMock.replayAll();
		v.setToRow(this.htr, 0);
		PowerMock.verifyAll();
	}

	@Test
	public final void testStringFromObject() {
		final CellValue v = CellValue.fromObject("str");
		
		// PLAY
		this.htr.setStringValue(0, "str");
		PowerMock.replayAll();
		v.setToRow(this.htr, 0);
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testNumberFromObject() {
		final CellValue v = CellValue.fromObject(10);
		
		// PLAY
		this.htr.setFloatValue(0, (Number) 10);
		PowerMock.replayAll();
		v.setToRow(this.htr, 0);
		PowerMock.verifyAll();
	}

	@Test
	public final void testBooleanFromObject() {
		final CellValue v = CellValue.fromObject(true);
		
		// PLAY
		this.htr.setBooleanValue(0, true);
		PowerMock.replayAll();
		v.setToRow(this.htr, 0);
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testDateFromObject() {
		final Date d = new Date(0);
		final CellValue v = CellValue.fromObject(d);
		
		// PLAY
		this.htr.setDateValue(0, d);
		PowerMock.replayAll();
		v.setToRow(this.htr, 0);
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testCalendarFromObject() {
		final Calendar c = Calendar.getInstance();
		final CellValue v = CellValue.fromObject(c);
		
		// PLAY
		this.htr.setDateValue(0, c.getTime());
		PowerMock.replayAll();
		v.setToRow(this.htr, 0);
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testOtherFromObject() {
		final Character j = Character.valueOf('j');
		final CellValue v = CellValue.fromObject(j);
		
		// PLAY
		this.htr.setStringValue(0, "j");
		PowerMock.replayAll();
		v.setToRow(this.htr, 0);
		PowerMock.verifyAll();
	}
}
