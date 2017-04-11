package com.dtp.chamber.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CONFLICT_FAIL
import com.dtp.*
import com.dtp.columns.Column
import com.dtp.query.Query
import com.dtp.query.RawQuery

/**
 * Created by ner on 4/6/17.
 */

internal class AndroidDatabase @JvmOverloads constructor(private val database: SQLiteDatabase, private val conflictAlgorithm: Int = CONFLICT_FAIL) : Database {

    override fun insert(dataTable: DataTable): Long? {
        return database.insertWithOnConflict(dataTable.tableName, null, getContentValues(dataTable.dataStoreIn), conflictAlgorithm)
    }

    override fun update(dataTable: DataTable): Int {
        return database.update(dataTable.tableName, getContentValues(dataTable.dataStoreIn), dataTable.tableName + Column.CHAMBER_ID + "=?", arrayOf(dataTable.chamberId.toString()))
    }

    override fun delete(dataTable: DataTable): Int {
        return database.delete(dataTable.tableName, dataTable.tableName + Column.CHAMBER_ID + "=?", arrayOf(dataTable.chamberId.toString()))
    }

    override fun <T : DataTable> findFirst(itemBuilder: ItemBuilder<T>, query: Query): T? {
        var item: T? = null

        getCursor(query).also {
            if (it.moveToFirst())
                item = itemBuilder.buildItem(AndroidDataStoreOut(it), this)

            it.close()
        }

        return item
    }

    override fun <T : DataTable> findAll(itemBuilder: ItemBuilder<T>, query: Query): List<T> {
        val items = mutableListOf<T>()

        getCursor(query).also {
            while (it.moveToNext())
                items.add(itemBuilder.buildItem(AndroidDataStoreOut(it), this))

            it.close()
        }

        return items
    }

    private fun getCursor(query: Query): Cursor {
        if (query is RawQuery) {
            return database.rawQuery(query.query, query.selectionArgs)
        } else {
            return database.query(query.tableName, query.columns, query.selection, query.selectionArgs, query.groupBy, null, query.order, query.limit)
        }
    }

    private fun getContentValues(dataStoreIn: DataStoreIn): ContentValues {
        //TODO Should fail the insert and log an error but not crash the app.
        if (dataStoreIn is AndroidDataStoreIn)
            return dataStoreIn.contentValues
        else
            throw IllegalArgumentException("DataTable was not an instance of AndroidDataTable. DataTable: " + dataStoreIn.javaClass.simpleName)
    }
}
