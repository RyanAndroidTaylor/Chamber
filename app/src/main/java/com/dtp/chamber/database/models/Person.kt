package com.dtp.chamber.database.models

import com.dtp.DataStoreIn
import com.dtp.data_table.DataTable
import com.dtp.data_table.ParentDataTable
import com.dtp.annotations.ChamberChild
import com.dtp.annotations.ChamberColumn
import com.dtp.annotations.ChamberTable
import com.dtp.chamber.PersonTable
import com.dtp.data_table.ChildDataTable

/**
 * Created by ner on 4/5/17.
 */

@ChamberTable()
data class Person(@ChamberColumn(notNull = true, unique = true) var firstName: String,
                  @ChamberColumn(notNull = true, unique = true) var lastName: String,
                  @ChamberColumn(notNull = true) var age: Int,
                  @ChamberColumn var cool: Boolean,
                  @ChamberChild var children: List<Child>) : ParentDataTable {

    override val tableName = PersonTable.TABLE_NAME

    override var chamberId = -1L

    override val dataStoreIn: DataStoreIn = PersonTable.getDataStoreFor(this)

    override val chamberChildren: List<ChildDataTable> = PersonTable.getChildrenFor(this)
}
