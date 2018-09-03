package com.dtp.chamber.database

import android.database.sqlite.SQLiteOpenHelper
import com.dtp.DataStoreIn

import com.dtp.Database
import com.dtp.DatabaseManager

/**
 * Created by ner on 4/6/17.
 */

class AndroidDatabaseManager(private val openHelper: SQLiteOpenHelper) : DatabaseManager {

    override fun beginTransaction(): Database = AndroidDatabase(openHelper.writableDatabase)

    override fun endTransaction(): Boolean {
        openHelper.close()

        return true
    }

    override fun createInDataStore(): DataStoreIn = AndroidDataStoreIn()
}
