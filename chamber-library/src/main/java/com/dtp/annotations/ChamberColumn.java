package com.dtp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by ner on 4/5/17.
 */

@Target(ElementType.FIELD)
public @interface ChamberColumn {
    String name() default "undefined";
    boolean unique() default  false;
    boolean notNull() default false;
}
