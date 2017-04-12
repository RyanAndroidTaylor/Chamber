package com.dtp.data_table

import com.dtp.DataStoreIn

/**
 * Created by ner on 4/6/17.
 */

interface DataTable {
    val tableName: String
    var chamberId: Long

    fun getDataStoreIn(): DataStoreIn
}
