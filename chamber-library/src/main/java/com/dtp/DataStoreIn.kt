package com.dtp

import com.dtp.columns.Column

/**
 * Created by ner on 4/10/17.
 */
open interface DataStoreIn {
    fun put(key: Column, item: String?)
    fun put(key: Column, item: Int?)
    fun put(key: Column, item: Long?)
    fun put(key: Column, item: Double?)
    fun put(key: Column, item: Float?)
    fun put(key: Column, item: Boolean?)
}