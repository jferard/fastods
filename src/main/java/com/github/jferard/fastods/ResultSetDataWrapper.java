package com.github.jferard.fastods;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.jferard.fastods.style.TableCellStyle;

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
		for (int j = 1; j <= this.columnCount; j++) {
			final Object object = columnValues.get(j);
			walker.nextCell();
			walker.setObjectValue(object);
		}
	}

	private void writeFirstLineDataTo(final HeavyTableRow row)
			throws SQLException {
		final List<String> columnNames = this.getColumnNames();
		final TableCellWalker walker = row.getWalker();
		for (int j = 1; j <= this.columnCount; j++) {
			walker.nextCell();
			final String name = columnNames.get(j);
			walker.setStringValue(name);
			walker.setStyle(this.headCellStyle);
		}
	}

	private void writeLastLineDataTo(final HeavyTableRow row, final int count) {
		final TableCellWalker walker = row.getWalker();
		if (count == 0) {// no row
			for (int j = 1; j <= this.columnCount; j++) {
				walker.nextCell();
				walker.setStringValue("");
			}
		} else if (count > this.max) {
			for (int j = 1; j <= this.columnCount; j++) {
				walker.nextCell();
				walker.setStringValue(String.format("... (%d rows remaining)",
						count - this.max));
			}
		}
	}

}
