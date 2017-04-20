package com.dtp.chamber.database

import android.database.Cursor
import com.dtp.DataStoreOut
import com.dtp.columns.*
import java.security.InvalidParameterException

/**
 * Created by ner on 4/10/17.
 */
class AndroidDataStoreOut(val cursor: Cursor) : DataStoreOut {

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: Column): T {
        when (key) {
            is StringColumn, is StringListColumn, is IntListColumn, is LongListColumn,
            is FloatListColumn, is DoubleListColumn, is BooleanListColumn -> return cursor.getString(cursor.getColumnIndex(key.name)) as T
            is IntColumn -> return cursor.getInt(cursor.getColumnIndex(key.name)) as T
            is LongColumn -> return cursor.getLong(cursor.getColumnIndex(key.name)) as T
            is DoubleColumn -> return cursor.getDouble(cursor.getColumnIndex(key.name)) as T
            is FloatColumn -> return cursor.getFloat(cursor.getColumnIndex(key.name)) as T
            is BooleanColumn -> return (cursor.getInt(cursor.getColumnIndex(key.name)) == 1) as T
            else -> throw InvalidParameterException("Key was not of a supported type $key")
        }
    }
}