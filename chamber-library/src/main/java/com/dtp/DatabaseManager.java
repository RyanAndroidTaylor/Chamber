package com.dtp;

/**
 * Created by ner on 4/6/17.
 */

public interface DatabaseManager {

    /**
     * This can potentially be call from different threads so you need to hand
     * @return
     */
    Database beginTransaction();

    boolean endTransaction();
}
