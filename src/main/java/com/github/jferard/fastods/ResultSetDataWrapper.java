package com.github.jferard.fastods;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetDataWrapper implements DataWrapper {
	/**
	 * column count of the ResultSet.
	 */
	private int columnCount;
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
	private TableCellStyle headCellStyle;

	private ResultSetDataWrapper(final ResultSet rs, final int max) {
		this.resultSet = rs;
		this.max = max;
		try {
			this.metadata = rs.getMetaData();
			this.columnCount = this.metadata.getColumnCount();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean addToTable(Table table) {
		if (this.metadata == null)
			return false;

		int count = 0;
		try {
			TableRow row;
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
		} catch (FastOdsException e) {
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

	private void writeDateLineTo(TableRow row)
			throws SQLException, IOException {
		final List<Object> columnValues = this.getColumnValues();
		for (int j = 1; j <= this.columnCount; j++) {
			final Object object = columnValues.get(j);
			TableCell cell = row.getCell(j - 1);
			cell.setObjectValue(object);
		}
	}

	private void writeFirstLineDataTo(final TableRow row) throws SQLException {
		final List<String> columnNames = this.getColumnNames();
		for (int j = 1; j <= this.columnCount; j++) {
			final String name = columnNames.get(j);
			TableCell cell = row.getCell(j - 1);
			cell.setStringValue(name);
			cell.setStyle(this.headCellStyle);
		}
	}

	private void writeLastLineDataTo(final TableRow row, final int count) {
		if (count == 0) {// no row
			for (int j = 1; j <= this.columnCount; j++) {
				TableCell cell = row.getCell(j - 1);
				cell.setStringValue("");
			}
		} else if (count > this.max) {
			for (int j = 1; j <= this.columnCount; j++) {
				TableCell cell = row.getCell(j - 1);
				cell.setStringValue(String.format("... (%d rows remaining)",
						count - this.max));
			}
		}
	}

}
