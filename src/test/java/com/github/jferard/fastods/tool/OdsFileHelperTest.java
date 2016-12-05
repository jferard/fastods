package com.github.jferard.fastods.tool;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.BooleanValue;
import com.github.jferard.fastods.FastOdsException;
import com.github.jferard.fastods.HeavyTableRow;
import com.github.jferard.fastods.OdsFile;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.PositionUtil;

public class OdsFileHelperTest {
	private OdsFileHelper helper;
	private List<Table> l;
	private OdsFile odsFile;
	private PositionUtil positionUtil;
	private Table t1;
	private Table t2;
	private Table t3;
	private TableHelper tableHelper;

	@Before
	public void setUp() {
		this.positionUtil = new PositionUtil(new EqualityUtil());
		this.odsFile = PowerMock.createMock(OdsFile.class);
		this.tableHelper = PowerMock.createMock(TableHelper.class);
		this.t1 = PowerMock.createMock(Table.class);
		this.t2 = PowerMock.createMock(Table.class);
		this.t3 = PowerMock.createMock(Table.class);
		this.l = Arrays.asList(this.t1, this.t2, this.t3);
		this.helper = new OdsFileHelper(this.odsFile, this.tableHelper,
				this.positionUtil);
	}

	@Test
	public final void testCellMergeInAllTables() throws FastOdsException {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(1234567891011l);

		EasyMock.expect(this.odsFile.getTables()).andReturn(this.l);
		this.tableHelper.setCellMerge(this.t1, 10, 5, 3, 2);
		this.tableHelper.setCellMerge(this.t2, 10, 5, 3, 2);
		this.tableHelper.setCellMerge(this.t3, 10, 5, 3, 2);
		PowerMock.replayAll();
		this.helper.setCellMergeInAllTables("F11", 3, 2);
		PowerMock.verifyAll();
	}

	@Test
	public final void testCellValueInAllTables() throws FastOdsException {
		final TableCellStyle ts = TableCellStyle.builder("a").build();
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(1234567891011l);
		final BooleanValue v = new BooleanValue(true);

		EasyMock.expect(this.odsFile.getTables()).andReturn(this.l);
		this.tableHelper.setCellValue(this.t1, 6, 2, v, ts);
		this.tableHelper.setCellValue(this.t2, 6, 2, v, ts);
		this.tableHelper.setCellValue(this.t3, 6, 2, v, ts);
		PowerMock.replayAll();
		this.helper.setCellValueInAllTables("C7", v, ts);
		PowerMock.verifyAll();
	}
}
