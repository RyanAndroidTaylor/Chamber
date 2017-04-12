package com.dtp

import com.dtp.data_table.DataTable
import com.dtp.data_table.ParentDataTable
import com.dtp.query.Query

/**
 * Created by ner on 4/6/17.
 */

class DataConnection private constructor() {

    companion object {
        //TODO Implement upsert
        //TODO Implement Async getAndClose and doAndClose
        //TODO Implement Async with RxJava
        //TODO Implement table watchers with RxJava

        private lateinit var databaseManager: DatabaseManager

        fun init(databaseManager: DatabaseManager) {
            DataConnection.databaseManager = databaseManager
        }

        fun createDataStore(): DataStoreIn {
            return databaseManager.createInDataStore()
        }

        fun doAndClose(block: (DataConnection) -> Unit) {
            block(openConnection())
        }

        fun <T> getAndClose(block: (DataConnection) -> T): T {
            return block(openConnection())
        }

        private fun openConnection(): DataConnection {
            return DataConnection()
        }
    }

    fun insert(dataTable: DataTable) {
        val database = databaseManager.beginTransaction()

        insert(dataTable, database)

        databaseManager.endTransaction()
    }

    fun insertAll(dataTables: List<DataTable>) {
        val database = databaseManager.beginTransaction()

        dataTables.forEach { insert(it, database) }

        databaseManager.endTransaction()
    }

    private fun insert(dataTable: DataTable, database: Database) {
        val chamberId = database.insert(dataTable)

        dataTable.chamberId = chamberId

        if (dataTable is ParentDataTable) {
            dataTable.chamberChildren.forEach {
                it.parentChamberId = dataTable.chamberId

                insert(it, database)
            }
        }
    }

    fun update(dataTable: DataTable) {
        val database = databaseManager.beginTransaction()

        update(dataTable, database)

        databaseManager.endTransaction()
    }

    fun updateAll(dataTables: List<DataTable>) {
        val database = databaseManager.beginTransaction()

        dataTables.forEach { update(it, database) }

        databaseManager.endTransaction()
    }

    private fun update(dataTable: DataTable, database: Database) {
        database.update(dataTable)

        if (dataTable is ParentDataTable)
            dataTable.chamberChildren.forEach { update(it, database) }
    }

    fun delete(dataTable: DataTable) {
        val database = databaseManager.beginTransaction()

        delete(dataTable, database)

        databaseManager.endTransaction()
    }

    fun delete(dataTables: List<DataTable>) {
        val database = databaseManager.beginTransaction()

        dataTables.forEach { delete(it, database) }

        databaseManager.endTransaction()
    }

    private fun delete(dataTable: DataTable, database: Database) {
        database.delete(dataTable)

        if (dataTable is ParentDataTable)
            dataTable.chamberChildren.forEach { delete(it, database) }
    }

    fun <T : DataTable> findFirst(itemBuilder: ItemBuilder<T>, query: Query): T? {
        val database = databaseManager.beginTransaction()

        val item = database.findFirst(itemBuilder, query)

        databaseManager.endTransaction()

        return item
    }

    fun <T : DataTable> findAll(itemBuilder: ItemBuilder<T>, query: Query): List<T> {
        val database = databaseManager.beginTransaction()

        val items = database.findAll(itemBuilder, query)

        databaseManager.endTransaction()

        return items
    }
}
