package com.dtp.columns;

/**
 * Created by ner on 4/5/17.
 */

abstract public class Column {

    public static final String CHAMBER_ID = "ChamberId";

    public final String name;
    public final boolean notNull;
    public final boolean unique;

    public Column(String name, boolean notNull, boolean unique) {
        this.name = name;
        this.notNull = notNull;
        this.unique = unique;
    }
}
