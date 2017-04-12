package com.dtp.data_table

/**
 * Created by ner on 4/11/17.
 */
interface ParentDataTable : DataTable {
    val chamberChildren: List<ChildDataTable>
}