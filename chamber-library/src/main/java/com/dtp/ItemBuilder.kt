package com.dtp

import com.dtp.data_table.DataTable

/**
 * Created by ner on 4/10/17.
 */

interface ItemBuilder<out T : DataTable> {
    fun buildItem(dataStoreOut: DataStoreOut, database: Database): T
}
