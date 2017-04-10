package com.dtp.chamber.database.models

import android.content.ContentValues

import com.dtp.annotations.ChamberColumn
import com.dtp.annotations.ChamberId
import com.dtp.annotations.ChamberTable
import com.dtp.annotations.ChamberType
import com.dtp.chamber.PersonTable
import com.dtp.chamber.database.AndroidDataTable

/**
 * Created by ner on 4/5/17.
 */

@ChamberTable(type = ChamberType.ANDROID)
class Person(@ChamberColumn(notNull = true, unique = true) var firstName: String,
             @ChamberColumn(notNull = true, unique = true) var lastName: String,
             @ChamberColumn(notNull = true) var age: Int,
             @ChamberColumn var cool: Boolean) : AndroidDataTable {

    @ChamberId
    var chamberIdHolder: Long? = null

    override fun getTableName(): String {
        return PersonTable.NAME
    }

    override fun getChamberId(): Long? {
        return chamberIdHolder
    }

    override fun getContentValues(): ContentValues {
        return PersonTable.getContentValuesFor(this)
    }
}
