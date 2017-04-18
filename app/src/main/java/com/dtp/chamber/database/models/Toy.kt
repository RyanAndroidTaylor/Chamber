package com.dtp.chamber.database.models

import com.dtp.DataStoreIn
import com.dtp.annotations.ChamberColumn
import com.dtp.annotations.ChamberTable
import com.dtp.chamber.ToyTable
import com.dtp.data_table.ChildDataTable

/**
 * Created by ner on 4/17/17.
 */

@ChamberTable
data class Toy(@ChamberColumn val name: String,
          @ChamberColumn val price: Int) : ChildDataTable {

    override var parentChamberId = -1L
    override val tableName = ToyTable.TABLE_NAME
    override var chamberId = -1L
    override fun getDataStoreIn(): DataStoreIn = ToyTable.getDataStoreFor(this)
}