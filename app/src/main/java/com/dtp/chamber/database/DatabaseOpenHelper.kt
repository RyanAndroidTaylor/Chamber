package com.dtp.chamber.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.dtp.TableBuilder
import com.dtp.chamber.PersonTable

/**
 * Created by ner on 4/6/17.
 */

class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, "com.dtp.chamber", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val tableBuilder = TableBuilder()

        db.execSQL(tableBuilder.buildTable(PersonTable.NAME, PersonTable.COLUMNS))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}
