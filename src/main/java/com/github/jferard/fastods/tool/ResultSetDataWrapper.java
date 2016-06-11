package com.github.jferard.fastods.tool;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.jferard.fastods.DataWrapper;
import com.github.jferard.fastods.HeavyTableRow;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCellWalker;
import com.github.jferard.fastods.style.TableCellStyle;

/**
 *
 * This file LightTableRow.java is part of FastODS.
 *
 * WHERE ? content.xml/office:document-content/office:body/office:spreadsheet/
 * table:table/table:table-row
 * 
 * Usage :
 * 
 * <pre>
 * {@code
 * 		OdsFile file = OdsFile.create("7columns.ods");
 *		final Table table = file.addTable("test", 50, 5);
 *		XMLUtil xmlUtil = FastOds.getXMLUtil();
 *		TableCellStyle tcls = TableCellStyle.builder(xmlUtil, "rs-head")
 *				.backgroundColor("#dddddd").fontWeightBold().build();
 *		DataWrapper data = new ResultSetDataWrapper(rs, tcls, 100);
 *		DataWrapper data2 = new ResultSetDataWrapper(rs2, tcls, 100);
 *		
 *		table.addData(data);
 *		table.nextRow();
 *		table.addData(data2);
 *		TableCellStyle tcls = TableCellStyle.builder(xmlUtil, "rs-head")
 *				.backgroundColor("#dddddd").fontWeightBold().build();
 *		DataWrapper data = new ResultSetDataWrapper(rs, tcls, 100);
 *		DataWrapper data2 = new ResultSetDataWrapper(rs2, tcls, 100);
 *		
 *		table.addData(data);
 *		table.nextRow();
 *		table.addData(data2);
 *
 * @author Julien Férard Copyright (C) 2016 J. Férard.
 * @author Martin Schulz Copyright 2008-2013 Martin Schulz <mtschulz at
 *         users.sourceforge.net>
 * 
 */
public final class ResultSetDataWrapper implements DataWrapper {
	/**
	 * column count of the ResultSet.
	 */
	private int columnCount;
	private TableCellStyle headCellStyle;
	/**
	 * maximum number of lines to be written
	 */
	private final int max;
	/**
	 * metadata of the ResultSet.
	 */
	private ResultSetMetaData metadata;
	/**
	 * the ResultSet.
	 */
	private final ResultSet resultSet;

	public ResultSetDataWrapper(final ResultSet rs,
			TableCellStyle headCellStyle, final int max) {
		this.resultSet = rs;
		this.headCellStyle = headCellStyle;
		this.max = max;
		try {
			this.metadata = rs.getMetaData();
			this.columnCount = this.metadata.getColumnCount();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean addToTable(final Table table) {
		if (this.metadata == null)
			return false;

		final int count = 0;
		try {
			HeavyTableRow row;
			if (this.resultSet.next()) {
				row = table.nextRow();
				this.writeFirstLineDataTo(row);
				do {
					row = table.nextRow();
					if (count <= this.max)
						this.writeDateLineTo(row);
				} while (this.resultSet.next());

				this.writeLastLineDataTo(row, count);
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return count > 0;
	}

	/**
	 * @return the name of the columns
	 * @throws SQLException
	 */
	private List<String> getColumnNames() throws SQLException {
		final List<String> names = new ArrayList<String>(this.columnCount);
		for (int i = 0; i < this.columnCount; i++)
			names.add(this.metadata.getColumnName(i + 1));

		return names;
	}

	/**
	 * @return the values of the current column
	 * @throws SQLException
	 * @throws IOException
	 */
	private List<Object> getColumnValues() throws SQLException, IOException {
		final List<Object> values = new ArrayList<Object>(this.columnCount);
		for (int i = 0; i < this.metadata.getColumnCount(); i++) {
			values.add(this.resultSet.getObject(i + 1));
		}
		return values;
	}

	private void writeDateLineTo(final HeavyTableRow row)
			throws SQLException, IOException {
		final List<Object> columnValues = this.getColumnValues();
		final TableCellWalker walker = row.getWalker();
		for (int j = 0; j <= this.columnCount - 1; j++) {
			final Object object = columnValues.get(j);
			walker.nextCell();
			walker.setObjectValue(object);
		}
	}

	private void writeFirstLineDataTo(final HeavyTableRow row)
			throws SQLException {
		final List<String> columnNames = this.getColumnNames();
		final TableCellWalker walker = row.getWalker();
		for (int j = 0; j <= this.columnCount - 1; j++) {
			walker.nextCell();
			final String name = columnNames.get(j);
			walker.setStringValue(name);
			walker.setStyle(this.headCellStyle);
		}
	}

	private void writeLastLineDataTo(final HeavyTableRow row, final int count) {
		final TableCellWalker walker = row.getWalker();
		if (count == 0) {// no row
			for (int j = 0; j <= this.columnCount - 1; j++) {
				walker.nextCell();
				walker.setStringValue("");
			}
		} else if (count > this.max) {
			for (int j = 0; j <= this.columnCount - 1; j++) {
				walker.nextCell();
				walker.setStringValue(String.format("... (%d rows remaining)",
						count - this.max));
			}
		}
	}

}
