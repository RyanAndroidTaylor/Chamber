package com.dtp.chamber.database;

import android.database.sqlite.SQLiteOpenHelper;

import com.dtp.Database;
import com.dtp.DatabaseManager;

/**
 * Created by ner on 4/6/17.
 */

public class AndroidDatabaseManager implements DatabaseManager {

    private SQLiteOpenHelper openHelper;

    public AndroidDatabaseManager(SQLiteOpenHelper openHelper) {
        this.openHelper = openHelper;
    }

    @Override
    public Database beginTransaction() {
        return new AndroidDatabase(openHelper.getWritableDatabase());
    }

    @Override
    public boolean endTransaction() {
        openHelper.close();

        return true;
    }
}
