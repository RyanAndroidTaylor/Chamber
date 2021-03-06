package com.dtp.columns

/**
 * Created by ner on 4/5/17.
 */

abstract class Column(val name: String, val notNull: Boolean, val unique: Boolean) {

    companion object {
        val CHAMBER_ID = LongColumn("ChamberId", true, true)
        val PARENT_CHAMBER_ID = LongColumn("ParentChamberId", true, false)
    }
}
