package com.dtp

/**
 * Created by ner on 4/10/17.
 */

interface ItemBuilder<out T : DataTable> {
    fun buildItem(dataStoreOut: DataStoreOut, database: Database): T
}
