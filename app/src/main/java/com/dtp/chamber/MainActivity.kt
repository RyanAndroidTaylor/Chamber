package com.dtp.chamber

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dtp.DataConnection
import com.dtp.chamber.database.models.Person

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val person = Person("Ryan", "Taylor", 102, false)

        DataConnection.doAndClose { it.insert(person) }
    }
}
