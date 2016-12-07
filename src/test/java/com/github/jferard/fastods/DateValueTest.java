/**
 * 
 */
package com.github.jferard.fastods;

import java.util.Date;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

/**
 * @author Julien
 *
 */
public class DateValueTest {
	@Test
	public final void test() {
		HeavyTableRow htr = PowerMock.createMock(HeavyTableRow.class);
		Capture<Date> captured = Capture.newInstance();
		
		// PLAY
		htr.setDateValue(EasyMock.eq(1), EasyMock.capture(captured));
		PowerMock.replayAll();
		
		final Date date = new Date(10);
		CellValue dv= new DateValue(date);
		date.setTime(0);
		dv.setToRow(htr, 1);
		Assert.assertEquals(10, captured.getValue().getTime());

		PowerMock.verifyAll();
	}

}
