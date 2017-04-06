package com.dtp.chamber.database;

import android.content.ContentValues;

import com.dtp.DataTable;

/**
 * Created by ner on 4/6/17.
 */

public interface AndroidDataTable extends DataTable {
    public ContentValues getContentValues();
}
