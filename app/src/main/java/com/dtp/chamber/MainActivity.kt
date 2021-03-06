package com.dtp.chamber

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.dtp.DataConnection
import com.dtp.chamber.database.models.Child
import com.dtp.chamber.database.models.Person
import com.dtp.chamber.database.models.Toy
import com.dtp.query.QueryBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val people = mutableListOf<Person>()

        for (i in 0..26) {
            val children = listOf(Child("First$i", "1", 1, null, Toy("Ball", 199)),
                                  Child("Second$i", "2", 2, "NOT NULL", Toy("Gun", 250)),
                                  Child("Third$i", "3", 3, null, Toy("Axe", 2399)))

            val person = Person("The$i",
                                "Man$i",
                                124,
                                true,
                                "New Field",
                                children,
                                listOf("some", "thing", "is", "going", "on"),
                                listOf(101, 505, 909),
                                listOf(111111L, 222222L, 333333L),
                                listOf(1.0f, 2.1f, 3.2f),
                                listOf(11.22, 22.33, 44.55),
                                listOf(true, false, true),
                                null,
                                null)

            people.add(person)
        }

        DataConnection.doAndClose {
            it.insertAll(people)

            val loadedPerson = it.findFirst(PersonTable.Builder(), QueryBuilder.any(PersonTable.TABLE_NAME))

            Log.i("MainActivity", "Loaded Person $loadedPerson")
        }
    }
}
