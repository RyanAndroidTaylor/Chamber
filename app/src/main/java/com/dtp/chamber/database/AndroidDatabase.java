package com.dtp.chamber.database;

import android.database.sqlite.SQLiteDatabase;

import com.dtp.DataTable;
import com.dtp.Database;
import com.dtp.columns.Column;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_FAIL;

/**
 * Created by ner on 4/6/17.
 */

class AndroidDatabase implements Database {

    private SQLiteDatabase database;
    private int conflictAlgorithm;

    AndroidDatabase(SQLiteDatabase database) {
        this(database, CONFLICT_FAIL);
    }

    AndroidDatabase(SQLiteDatabase database, int conflictAlgorithm) {
        this.database = database;
        this.conflictAlgorithm = conflictAlgorithm;
    }

    @Override
    public Long insert(DataTable dataTable) {
        AndroidDataTable androidDataTable = safeCastToAndroidDataTable(dataTable);

        return database.insertWithOnConflict(androidDataTable.getTableName(), null, androidDataTable.getContentValues(), conflictAlgorithm);
    }

    @Override
    public int update(DataTable dataTable) {
        AndroidDataTable androidDataTable = safeCastToAndroidDataTable(dataTable);

        return database.update(androidDataTable.getTableName(), androidDataTable.getContentValues(), androidDataTable.getTableName() + Column.CHAMBER_ID + "=?", new String[] { androidDataTable.getChamberId().toString() });
    }

    @Override
    public int delete(DataTable dataTable) {
        AndroidDataTable androidDataTable = safeCastToAndroidDataTable(dataTable);

        return database.delete(androidDataTable.getTableName(), androidDataTable.getTableName() + Column.CHAMBER_ID + "=?", new String[] { androidDataTable.getChamberId().toString() });
    }

    private AndroidDataTable safeCastToAndroidDataTable(DataTable dataTable) {
        if (dataTable instanceof AndroidDataTable)
            return (AndroidDataTable) dataTable;
        else
            throw new IllegalArgumentException("DataTable was not an instance of AndroidDataTable. DataTable: " + dataTable.getClass().getSimpleName());
    }
}
