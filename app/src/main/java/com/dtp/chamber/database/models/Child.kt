package com.dtp.chamber.database.models

import com.dtp.DataStoreIn
import com.dtp.annotations.ChamberChild
import com.dtp.annotations.ChamberColumn
import com.dtp.annotations.ChamberTable
import com.dtp.chamber.ChildTable
import com.dtp.data_table.ChildDataTable
import com.dtp.data_table.ParentDataTable

/**
 * Created by ner on 4/11/17.
 */
@ChamberTable
data class Child(@ChamberColumn val firstName: String,
                 @ChamberColumn val lastName: String,
                 @ChamberColumn val gender: Int,
                 @ChamberColumn val nullableItem: String?,
                 @ChamberChild val toy: Toy) : ParentDataTable, ChildDataTable {

    override val tableName = ChildTable.TABLE_NAME
    override var chamberId = -1L
    override var parentChamberId = -1L
    override fun getDataStoreIn(): DataStoreIn = ChildTable.getDataStoreFor(this)
    override val chamberChildren: List<ChildDataTable> = ChildTable.getChildrenFor(this)
}