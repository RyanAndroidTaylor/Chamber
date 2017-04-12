package com.dtp.chamber.database.models

import com.dtp.DataStoreIn
import com.dtp.annotations.ChamberColumn
import com.dtp.annotations.ChamberTable
import com.dtp.chamber.ChildTable
import com.dtp.data_table.ChildDataTable

/**
 * Created by ner on 4/11/17.
 */
@ChamberTable
data class Child(@ChamberColumn val firstName: String,
                 @ChamberColumn val lastName: String,
                 @ChamberColumn val gender: Int) : ChildDataTable {

    override val tableName = ChildTable.TABLE_NAME
    override var chamberId = -1L
    override var parentChamberId = -1L
    override val dataStoreIn: DataStoreIn
        get() = ChildTable.getDataStoreFor(this)
}