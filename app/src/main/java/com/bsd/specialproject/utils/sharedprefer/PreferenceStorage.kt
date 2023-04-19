package com.bsd.specialproject.utils.sharedprefer

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

interface PreferenceStorage {
    fun getString(key: String, defaultValue: String = ""): String
    fun putString(key: String, value: String)
    fun getInt(key: String, defaultValue: Int = 0): Int
    fun putInt(key: String, value: Int)
    fun getLong(key: String, defaultValue: Long = 0L): Long
    fun putLong(key: String, value: Long)
    fun getFloat(key: String, defaultValue: Float = 0f): Float
    fun putFloat(key: String, value: Float)
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun putBoolean(key: String, value: Boolean)
    fun delete(key: String)
    fun deleteAll()
    fun <T> getObject(key: String, defaultValue: String?, type: Class<T>): T?
    fun <T> putObject(key: String, value: T)
    fun getStringSet(key: String): Set<String>?
    fun putStringSet(key: String, set: Set<String>)
}

class PreferenceStorageImpl constructor(
    private val prefs: SharedPreferences
) : PreferenceStorage {
    override fun <T> getObject(key: String, defaultValue: String?, type: Class<T>) = try {
        Gson().fromJson(get(key) ?: defaultValue, type)
    } catch (e: JsonSyntaxException) {
        null
    }

    override fun <T> putObject(key: String, value: T) {
        put(key, Gson().toJson(value))
    }

    override fun getString(key: String, defaultValue: String): String = get(key) ?: defaultValue

    override fun putString(key: String, value: String) {
        put(key, value)
    }

    override fun getInt(key: String, defaultValue: Int): Int =
        get(key)?.toIntOrNull() ?: defaultValue

    override fun putInt(key: String, value: Int) {
        put(key, value)
    }

    override fun getLong(key: String, defaultValue: Long): Long =
        get(key)?.toLongOrNull() ?: defaultValue

    override fun putLong(key: String, value: Long) {
        put(key, value)
    }

    override fun getFloat(key: String, defaultValue: Float): Float =
        get(key)?.toFloatOrNull() ?: defaultValue

    override fun putFloat(key: String, value: Float) {
        put(key, value)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        get(key)?.toBoolean() ?: defaultValue

    override fun putBoolean(key: String, value: Boolean) {
        put(key, value)
    }

    override fun getStringSet(key: String): Set<String>? {
        return prefs.getStringSet(key, HashSet())
    }

    override fun putStringSet(key: String, set: Set<String>) {
        prefs.edit().putStringSet(key, set).apply()
    }

    override fun delete(key: String) {
        prefs.edit().remove(key).apply()
    }

    override fun deleteAll() {
        prefs.edit().clear().apply()
    }

    private fun <T> put(key: String, value: T?) {
        try {
            prefs.edit().putString(key, value.toString()).apply()
        } catch (e: Exception) {
            Log.e("Tag", "ERROR ~> : ${e.message}")
        }
    }

    private fun get(key: String): String? {
        return try {
            prefs.getString(key, null)
        } catch (e: Exception) {
            null
        }
    }
}
