package com.dtp.chamber.database.models

import com.dtp.DataStoreIn
import com.dtp.annotations.ChamberChild
import com.dtp.annotations.ChamberColumn
import com.dtp.annotations.ChamberTable
import com.dtp.chamber.PersonTable
import com.dtp.data_table.ChildDataTable
import com.dtp.data_table.ParentDataTable

/**
 * Created by ner on 4/5/17.
 */

@ChamberTable()
data class Person(@ChamberColumn(notNull = true, unique = true) val firstName: String,
                  @ChamberColumn(notNull = true, unique = true) val lastName: String,
                  @ChamberColumn(notNull = true) val age: Int,
                  @ChamberColumn val cool: Boolean,
                  @ChamberColumn(name = "NotRight") val newThing: String,
                  @ChamberChild val children: List<Child>,
                  @ChamberColumn val stringArray: List<String>) : ParentDataTable {

    override val tableName = PersonTable.TABLE_NAME

    override var chamberId = -1L

    override fun getDataStoreIn(): DataStoreIn  = PersonTable.getDataStoreFor(this)

    override val chamberChildren: List<ChildDataTable> = PersonTable.getChildrenFor(this)
}
