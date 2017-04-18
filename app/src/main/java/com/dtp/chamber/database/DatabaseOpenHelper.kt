package com.dtp.chamber.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.dtp.TableBuilder
import com.dtp.chamber.ChildTable
import com.dtp.chamber.PersonTable
import com.dtp.chamber.ToyTable

/**
 * Created by ner on 4/6/17.
 */

class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, "com.dtp.chamber", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val tableBuilder = TableBuilder()

        db.execSQL(tableBuilder.buildTable(PersonTable.TABLE_NAME, PersonTable.COLUMNS))
        db.execSQL(tableBuilder.buildTable(ChildTable.TABLE_NAME, ChildTable.COLUMNS))
        db.execSQL(tableBuilder.buildTable(ToyTable.TABLE_NAME, ToyTable.COLUMNS))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}
