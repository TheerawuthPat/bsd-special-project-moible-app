package com.bsd.specialproject.utils

fun String?.toDefaultValue(defaultValue: String = DEFAULT_STRING): String {
    return this ?: defaultValue
}

fun Int?.toDefaultValue(defaultValue: Int = DEFAULT_INT): Int {
    return this ?: defaultValue
}

fun Long?.toDefaultValue(): Long {
    return this ?: DEFAULT_LONG
}

fun Double?.toDefaultValue(): Double {
    return this ?: DEFAULT_DOUBLE
}

fun Float?.toDefaultValue(defaultValue: Float = DEFAULT_FLOAT): Float {
    return this ?: defaultValue
}

fun Boolean?.toDefaultValue(defaultValue: Boolean = DEFAULT_BOOLEAN): Boolean {
    return this ?: defaultValue
}

fun <T> List<T>?.toDefaultValue(): List<T> {
    return this ?: emptyList()
}

fun <T> MutableList<T>?.toDefaultMutableList(): MutableList<T> {
    return this ?: mutableListOf()
}

const val DEFAULT_STRING: String = ""

const val DEFAULT_INT: Int = 0

const val DEFAULT_LONG: Long = 0L

const val DEFAULT_DOUBLE: Double = 0.0

const val DEFAULT_FLOAT: Float = 0F

const val DEFAULT_BOOLEAN: Boolean = false
