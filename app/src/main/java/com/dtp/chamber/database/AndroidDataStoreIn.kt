package com.dtp.chamber.database

import android.content.ContentValues
import com.dtp.DataStoreIn
import com.dtp.columns.Column

/**
 * Created by ner on 4/10/17.
 */
class AndroidDataStoreIn : DataStoreIn {

    val contentValues = ContentValues()

    override fun put(key: Column, item: String) {
        contentValues.put(key.name, item)
    }

    override fun put(key: Column, item: Int) {
        contentValues.put(key.name, item)
    }

    override fun put(key: Column, item: Long) {
        contentValues.put(key.name, item)
    }

    override fun put(key: Column, item: Double) {
        contentValues.put(key.name, item)
    }

    override fun put(key: Column, item: Float) {
        contentValues.put(key.name, item)
    }

    override fun put(key: Column, item: Boolean) {
        contentValues.put(key.name, item)
    }
}