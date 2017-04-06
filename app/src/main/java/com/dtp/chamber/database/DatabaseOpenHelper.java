package com.dtp.chamber.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dtp.TableBuilder;
import com.dtp.chamber.PersonManager;

/**
 * Created by ner on 4/6/17.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public DatabaseOpenHelper(Context context) {
        super(context, "com.dtp.chamber", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableBuilder tableBuilder = new TableBuilder();

        db.execSQL(tableBuilder.generateCreateString(PersonManager.TABLE_NAME, PersonManager.COLUMNS));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
