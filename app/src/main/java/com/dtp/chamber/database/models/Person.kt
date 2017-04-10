package com.dtp.chamber.database.models

import com.dtp.DataStoreIn
import com.dtp.DataTable
import com.dtp.annotations.ChamberColumn
import com.dtp.annotations.ChamberTable
import com.dtp.chamber.PersonTable

/**
 * Created by ner on 4/5/17.
 */

@ChamberTable()
class Person(@ChamberColumn(notNull = true, unique = true) var firstName: String,
             @ChamberColumn(notNull = true, unique = true) var lastName: String,
             @ChamberColumn(notNull = true) var age: Int,
             @ChamberColumn var cool: Boolean) : DataTable {

    override val tableName = PersonTable.NAME

    override var chamberId = -1L

    override val dataStoreIn: DataStoreIn = PersonTable.getDataStoreFor(this)
}
