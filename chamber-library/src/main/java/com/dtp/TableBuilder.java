package com.dtp;

import com.dtp.columns.BooleanColumn;
import com.dtp.columns.Column;
import com.dtp.columns.DoubleColumn;
import com.dtp.columns.FloatColumn;
import com.dtp.columns.IntColumn;
import com.dtp.columns.LongColumn;
import com.dtp.columns.StringColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ner on 4/6/17.
 */

public class TableBuilder {

    private static final String TEXT = "TEXT";
    private static final String INTEGER = "INTEGER";
    private static final String REAL = "REAL";
    private static final String BOOLEAN = "BOOLEAN";
    private static final String BLOB = "BLOB";

    private static final String SPACE = " ";
    private static final String PERIOD = ".";
    private static final String COMMA = ",";
    private static final String DEFAULT = "DEFAULT";

    private static final String NOT_NULL = "NOT NULL";
    private static final String UNIQUE = "UNIQUE";
    private static final String REFERENCES = "REFERENCES";

    private StringBuilder createStringBuilder = new StringBuilder();
    private List<String> foreignKeys = new ArrayList<String>();
    private String currentTable;
    private List<String> columnNames = new ArrayList<>();

    public String generateCreateString(String tableName, Column[] columns) {
        open(tableName);

        for (Column column : columns) {
            if (column.name.contains("ChamberId"))
                continue;

            ColumnBuilder columnBuilder = getColumnBuilderForColumn(column);

            if (column.notNull)
                columnBuilder.notNull();

            if (column.unique)
                columnBuilder.unique();

            //TODO default value
            //TODO foreignKey

            columnBuilder.build();
        }

        return generateCreateString();
    }

    private void open(String tableName) {
        prepareForNewTable(tableName);

        createStringBuilder.append("CREATE TABLE ");
        createStringBuilder.append(currentTable);
        createStringBuilder.append(" ( ");
        createStringBuilder.append(tableName);
        createStringBuilder.append("ChamberId");
        createStringBuilder.append(" INTEGER PRIMARY KEY AUTOINCREMENT");

        columnNames.add(currentTable + PERIOD + "CHAMBER_ID");
    }

    private void prepareForNewTable(String tableName) {
        createStringBuilder = new StringBuilder();
        columnNames = new ArrayList<>();
        foreignKeys = new ArrayList<>();
        currentTable = tableName;
    }

    private String generateCreateString() {
        createStringBuilder.append(" ");

        for (String foreignKey : foreignKeys) {
            createStringBuilder.append(COMMA);
            createStringBuilder.append(SPACE);
            createStringBuilder.append(foreignKey);
        }

        createStringBuilder.append(")");

        return createStringBuilder.toString();
    }

    private ColumnBuilder getColumnBuilderForColumn(Column column) {
        if (column instanceof StringColumn) {
            return new ColumnBuilder().appendText(column.name);
        } else if (column instanceof IntColumn || column instanceof LongColumn) {
            return new ColumnBuilder().appendInt(column.name);
        } else if (column instanceof FloatColumn || column instanceof DoubleColumn) {
            return new ColumnBuilder().appendReal(column.name);
        } else if (column instanceof BooleanColumn) {
            return new ColumnBuilder().appendBoolean(column.name);
        } else {
            throw new IllegalArgumentException("ColumnBuilder does not support " + column.getClass().getSimpleName());
        }
    }

    class ColumnBuilder {
        private String columnName;
        private String type;
        private List<String> constraints = new ArrayList<>();
        private Object defaultValue;

        ColumnBuilder appendText(String columnName) {
            return append(TEXT, columnName);
        }

        ColumnBuilder appendInt(String columnName) {
            return append(INTEGER, columnName);
        }

        ColumnBuilder appendReal(String columnName) {
            return append(REAL, columnName);
        }

        ColumnBuilder appendBoolean(String columnName) {
            return append(BOOLEAN, columnName);
        }

        public ColumnBuilder appendBlob(String columnName) {
            return append(BLOB, columnName);
        }

        public ColumnBuilder notNull() {
            constraints.add(NOT_NULL);

            return this;
        }

        public ColumnBuilder unique() {
            constraints.add(UNIQUE);

            return this;
        }

        public ColumnBuilder foreignKey(String foreignTableName, String foreignColumnName) {
            foreignKeys.add("FOREIGN KEY (" + columnName + ")" + REFERENCES + foreignTableName + "(" + foreignColumnName + ")");

            return this;
        }

        private ColumnBuilder append(String type, String columnName) {
            this.type = type;
            this.columnName = columnName;

            return this;
        }

        void build() {
            createStringBuilder.append(COMMA);
            createStringBuilder.append(columnName);
            createStringBuilder.append(SPACE);
            createStringBuilder.append(type);

            for (String constraint : constraints) {
                createStringBuilder.append(SPACE);
                createStringBuilder.append(constraint);
            }

            if (defaultValue != null) {
                createStringBuilder.append(DEFAULT);

                if (defaultValue instanceof String || defaultValue instanceof Integer ||
                        defaultValue instanceof Long || defaultValue instanceof Float || defaultValue instanceof Double) {
                    createStringBuilder.append(defaultValue);
                } else if (defaultValue instanceof Boolean) {
                    createStringBuilder.append((Boolean) defaultValue ? 1 : 0);
                }
            }

            columnNames.add(currentTable + PERIOD + columnName);
        }
    }
}
