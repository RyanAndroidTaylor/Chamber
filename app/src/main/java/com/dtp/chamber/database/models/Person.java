package com.dtp.chamber.database.models;

import com.dtp.annotations.ChamberColumn;
import com.dtp.annotations.ChamberId;
import com.dtp.annotations.ChamberTable;

/**
 * Created by ner on 4/5/17.
 */

@ChamberTable
public class Person {

    @ChamberId
    public Long chamberId;

    @ChamberColumn(notNull = true, unique = true)
    public String firstName;

    @ChamberColumn(notNull = true, unique = true)
    public String lastName;

    @ChamberColumn(notNull = true)
    public int age;

    @ChamberColumn
    public boolean cool;

    public Person(String firstName, String lastName, int age, boolean cool) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.cool = cool;
    }
}
