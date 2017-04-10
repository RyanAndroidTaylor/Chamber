package com.dtp.chamber

import android.app.Application

import com.dtp.DataConnection
import com.dtp.chamber.database.AndroidDatabaseManager
import com.dtp.chamber.database.DatabaseOpenHelper
import com.facebook.stetho.Stetho

/**
 * Created by ner on 4/6/17.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        DataConnection.init(AndroidDatabaseManager(DatabaseOpenHelper(this)))
    }
}
