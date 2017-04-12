package com.dtp;

import com.dtp.columns.Column;

/**
 * Created by ner on 4/5/17.
 */

class Const {
    static final String PACKAGE_NAME = "com.dtp.chamber";

    static final String TABLE_CLASS_SUFFIX = "Table";
    static final String MODEL_CLASS_SUFFIX = "Model";

    static final String TABLE_NAME = "TABLE_NAME";
    static final String COLUMNS = "COLUMNS";

    static final String CHAMBER_ID_VARIABLE_NAME = Util.toLowerFistLetter(Column.Companion.getCHAMBER_ID().getName());
    static final String CHAMBER_ID_COLUMN_NAME= Column.Companion.getCHAMBER_ID().getName();
    static final String CHAMBER_ID_COLUMN_VARIABLE_NAME = Util.formatAsStaticFinalName(Column.Companion.getCHAMBER_ID().getName());

    static final String PARENT_CHAMBER_ID_VARIABLE_NAME = Util.toLowerFistLetter(Column.Companion.getPARENT_CHAMBER_ID().getName());
    static final String PARENT_CHAMBER_ID_COLUMN_NAME = Column.Companion.getPARENT_CHAMBER_ID().getName();
    static final String PARENT_CHAMBER_ID_COLUMN_VARIABLE_NAME = Util.formatAsStaticFinalName(Column.Companion.getPARENT_CHAMBER_ID().getName());

    static final String DATA_STORE_OUT_VARIABLE_NAME = "dataStoreOut";
    static final String DATA_STORE_IN_VARIABLE_NAME = "dataStoreIn";

    static final String DATABASE_VARIABLE_NAME = "database";
}
