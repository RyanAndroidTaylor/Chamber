package com.dtp

/**
 * Created by ner on 4/6/17.
 */

interface DatabaseManager {

    /**
     * This can potentially be call from different threads so you need to make sure it is thread safe
     * @return
     */
    fun beginTransaction(): Database

    fun endTransaction(): Boolean

    fun createInDataStore(): DataStoreIn
}
