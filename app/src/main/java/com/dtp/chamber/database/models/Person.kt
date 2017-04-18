package com.dtp.chamber.database.models

import com.dtp.*
import com.dtp.annotations.ChamberChild
import com.dtp.annotations.ChamberColumn
import com.dtp.annotations.ChamberTable
import com.dtp.chamber.ChildTable
import com.dtp.chamber.PersonTable
import com.dtp.chamber.PersonTable.*
import com.dtp.chamber.ToyTable
import com.dtp.data_table.ChildDataTable
import com.dtp.data_table.ParentDataTable
import com.dtp.query.QueryBuilder

/**
 * Created by ner on 4/5/17.
 */

@ChamberTable(overridesBuilder = true)
data class Person(@ChamberColumn(notNull = true, unique = true) val firstName: String,
                  @ChamberColumn(notNull = true, unique = true) val lastName: String,
                  @ChamberColumn(notNull = true) val age: Int,
                  @ChamberColumn val cool: Boolean,
                  @ChamberColumn(name = "NotRight") val newThing: String,
                  @ChamberChild val children: List<Child>,
                  @ChamberColumn val stringArray: List<String>) : ParentDataTable {

    override val tableName = PersonTable.TABLE_NAME

    override var chamberId = -1L

    override fun getDataStoreIn(): DataStoreIn = PersonTable.getDataStoreFor(this)

    override val chamberChildren: List<ChildDataTable> = PersonTable.getChildrenFor(this)

    class Builder : ItemBuilder<Person> {
        override val tableName: String
            get() = ToyTable.TABLE_NAME

        override fun buildItem(dataStoreOut: DataStoreOut, database: Database): Person {
            val chamberId = dataStoreOut.get<Long>(CHAMBER_ID)
            val firstName = dataStoreOut.get<String>(FIRST_NAME)
            val lastName = dataStoreOut.get<String>(LAST_NAME)
            val age = dataStoreOut.get<Int>(AGE)
            val cool = dataStoreOut.get<Boolean>(COOL)
            val newThing = dataStoreOut.get<String>(NEW_THING)
            val stringArrayString = dataStoreOut.get<String>(STRING_ARRAY)
            val stringArray = split(stringArrayString)

            val children = database.findAll(ChildTable.Builder(), QueryBuilder.with(ChildTable.TABLE_NAME).whereEquals(ChildTable.PARENT_CHAMBER_ID, chamberId).build())
            val personObject = Person(firstName, lastName, age, cool, newThing, children, stringArray)

            personObject.chamberId = chamberId

            return personObject
        }
    }
}
