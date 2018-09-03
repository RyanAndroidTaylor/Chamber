package com.dtp

/**
 * Created by ner on 4/17/17.
 */

fun listToString(items: List<Any>?): String? = items?.joinToString()

fun splitAsString(stringList: String): List<String> = stringList.split("|;|")

fun splitAsInt(intList: String): List<Int> = intList.split("|;|").map { it.trim().toInt() }

fun splitAsLong(intList: String): List<Long> = intList.split("|;|").map { it.trim().toLong() }

fun splitAsFloat(intList: String): List<Float> = intList.split("|;|").map { it.trim().toFloat() }

fun splitAsDouble(intList: String): List<Double> = intList.split("|;|").map { it.trim().toDouble() }

fun splitAsBoolean(intList: String): List<Boolean> =
        intList.split("|;|").map { it.trim().toBoolean() }