package com.dtp;

/**
 * Created by ner on 4/5/17.
 */

public class ColumnData {

    public String variableName;
    public ColumnType type;
    public String columnName;

    public ColumnData(String variableName, ColumnType type, String columnName) {
        this.variableName = variableName;
        this.type = type;
        this.columnName = columnName;
    }
}
