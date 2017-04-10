package com.dtp

import com.dtp.columns.Column

/**
 * Created by ner on 4/10/17.
 */

interface DataStoreOut {
    fun <T> get(key: Column): T
}