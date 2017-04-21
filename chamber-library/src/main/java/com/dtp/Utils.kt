package com.dtp

/**
 * Created by ner on 4/17/17.
 */

fun listToString(items: List<Any>?): String? {
    return items?.joinToString()
}

fun splitAsString(stringList: String): List<String> {
    return stringList.split(',')
}

fun splitAsInt(intList: String): List<Int> {
    return intList.split(',').map { it.trim().toInt() }
}

fun splitAsLong(intList: String): List<Long> {
    return intList.split(',').map { it.trim().toLong() }
}

fun splitAsFloat(intList: String): List<Float> {
    return intList.split(',').map { it.trim().toFloat() }
}

fun splitAsDouble(intList: String): List<Double> {
    return intList.split(',').map { it.trim().toDouble() }
}

fun splitAsBoolean(intList: String): List<Boolean> {
    return intList.split(',').map { it.trim().toBoolean() }
}