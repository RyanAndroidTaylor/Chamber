package com.dtp.chamber.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CONFLICT_FAIL
import com.dtp.*
import com.dtp.columns.Column

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
        return null
    }

    override fun <T : DataTable> findAll(itemBuilder: ItemBuilder<T>, query: Query): List<T> {
        return listOf()
    }

    private fun getContentValues(dataStoreIn: DataStoreIn): ContentValues {
        //TODO Should fail the insert and log an error but not crash the app.
        if (dataStoreIn is AndroidDataStoreIn)
            return dataStoreIn.contentValues
        else
            throw IllegalArgumentException("DataTable was not an instance of AndroidDataTable. DataTable: " + dataStoreIn.javaClass.simpleName)
    }
}
