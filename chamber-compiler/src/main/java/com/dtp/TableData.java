package com.dtp;

import java.util.List;

/**
 * Created by ner on 4/5/17.
 */

public class TableData {

    public String tableName;
    public List<ColumnData> columns;

    public TableData(String tableName, List<ColumnData> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }
}
