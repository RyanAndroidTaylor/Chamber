package com.dtp

/**
 * Created by ner on 4/6/17.
 */

interface DataTable {

    val tableName: String
    var chamberId: Long
    val dataStoreIn: DataStoreIn
}
