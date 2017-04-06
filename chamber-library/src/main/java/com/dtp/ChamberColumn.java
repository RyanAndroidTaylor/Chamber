package com.dtp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by ner on 4/5/17.
 */

@Target(ElementType.FIELD)
public @interface ChamberColumn {
    String columnName()  default "unassigned";
}
