package com.dtp

import com.dtp.data_table.DataTable
import com.dtp.query.Query

/**
 * Created by ner on 4/6/17.
 */

interface Database {

    fun insert(dataTable: DataTable): Long

    fun update(dataTable: DataTable): Int

    fun delete(dataTable: DataTable): Int

    fun <T : DataTable> findFirst(itemBuilder: ItemBuilder<T>, query: Query): T?

    fun <T : DataTable> findAll(itemBuilder: ItemBuilder<T>, query: Query): List<T>

    fun count(tableName: String): Int
}
