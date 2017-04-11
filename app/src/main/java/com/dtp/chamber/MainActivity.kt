package com.dtp.chamber

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.dtp.DataConnection
import com.dtp.query.QueryBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DataConnection.doAndClose {
            val people = it.findAll(PersonTable.Builder(), QueryBuilder.all(PersonTable.NAME))

            people.forEach {
                Log.i("MainActivity", "Loaded person $it")
            }
        }
    }
}
