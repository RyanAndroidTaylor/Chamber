package com.dtp;

/**
 * Created by ner on 4/6/17.
 */

public interface Database {

    Long insert(DataTable dataTable);

    int update(DataTable dataTable);

    int delete(DataTable dataTable);
}
