package com.dtp.chamber;

import com.dtp.ChamberColumn;
import com.dtp.ChamberTable;

/**
 * Created by ner on 4/5/17.
 */

@ChamberTable
public class Person {

    @ChamberColumn
    public String firstName;

    @ChamberColumn
    public String lastName;

    @ChamberColumn()
    public int age;

    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}
