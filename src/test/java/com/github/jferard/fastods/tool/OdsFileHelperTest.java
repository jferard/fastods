package com.github.jferard.fastods.tool;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.easymock.EasyMock;
import static org.easymock.EasyMock.*;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.HeavyTableRow;
import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell.Type;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.Util;

public class OdsFileHelperTest {
	private OdsFile odsFile;
	private Util util;
	private Table t1;
	private Table t2;
	private Table t3;
	private OdsFileHelper helper;
	private List<Table> l;
	private HeavyTableRow r1;
	private HeavyTableRow r2;
	private HeavyTableRow r3;
	private TableHelper tableHelper;

	@Before
	public void setUp() {
		this.util = Util.create();
		this.odsFile = PowerMock.createMock(OdsFile.class);
		this.tableHelper = PowerMock.createMock(TableHelper.class);
		this.t1 = PowerMock.createMock(Table.class);
		this.t2 = PowerMock.createMock(Table.class);
		this.t3 = PowerMock.createMock(Table.class);
		this.l = Arrays.asList(this.t1, this.t2, this.t3);
		this.helper = new OdsFileHelper(this.odsFile, this.tableHelper, this.util);
	}

	@Test
	public final void testCalValueInAllTables() throws FastOdsException {
		TableCellStyle ts = TableCellStyle.builder("a").build();
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(1234567891011l);
		
		expect(this.odsFile.getTables()).andReturn(this.l);
		this.tableHelper.setCell(this.t1, 6, 2, cal, ts);
		this.tableHelper.setCell(this.t2, 6, 2, cal, ts);
		this.tableHelper.setCell(this.t3, 6, 2, cal, ts);
		PowerMock.replayAll();
		this.helper.setCellInAllTables("C7", cal, ts);
		PowerMock.verifyAll();
	}

	@Test
	public final void testCellValueInAllTables() throws FastOdsException {
		TableCellStyle ts = TableCellStyle.builder("a").build();
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(1234567891011l);
		
		expect(this.odsFile.getTables()).andReturn(this.l);
		this.tableHelper.setCell(this.t1, 6, 2, Type.BOOLEAN, "true", ts);
		this.tableHelper.setCell(this.t2, 6, 2, Type.BOOLEAN, "true", ts);
		this.tableHelper.setCell(this.t3, 6, 2, Type.BOOLEAN, "true", ts);
		PowerMock.replayAll();
		this.helper.setCellInAllTables("C7", Type.BOOLEAN, "true", ts);
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testCellMergeInAllTables() throws FastOdsException {
		TableCellStyle ts = TableCellStyle.builder("a").build();
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(1234567891011l);
		
		expect(this.odsFile.getTables()).andReturn(this.l);
		this.tableHelper.setCellMerge(this.t1, 10, 5, 3, 2);
		this.tableHelper.setCellMerge(this.t2, 10, 5, 3, 2);
		this.tableHelper.setCellMerge(this.t3, 10, 5, 3, 2);
		PowerMock.replayAll();
		this.helper.setCellMergeInAllTables("F11", 3, 2);
		PowerMock.verifyAll();
	}
}
