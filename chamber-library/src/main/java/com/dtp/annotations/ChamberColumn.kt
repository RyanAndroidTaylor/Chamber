package com.dtp.annotations

/**
 * Created by ner on 4/5/17.
 */

@Target(AnnotationTarget.FIELD)
annotation class ChamberColumn(val name: String = "undefined", val unique: Boolean = false, val notNull: Boolean = false)
