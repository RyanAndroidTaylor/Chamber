package com.dtp.chamber

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.dtp.DataConnection
import com.dtp.chamber.database.models.Child
import com.dtp.chamber.database.models.Person
import com.dtp.query.QueryBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val children = listOf(Child("First", "1", 1, null),
                              Child("Second", "2", 2, "NOT NULL"),
                              Child("Third", "3", 3, null))

        val person = Person("The", "Man", 124, true, "New Field", children, listOf("some", "thing", "is", "going", "on"))

        DataConnection.doAndClose {
            it.insert(person)

            val loadedPerson = it.findFirst(Person.Builder(), QueryBuilder.with(PersonTable.TABLE_NAME).build())

            Log.i("MainActivity", "Loaded Person $loadedPerson")
        }
    }
}
