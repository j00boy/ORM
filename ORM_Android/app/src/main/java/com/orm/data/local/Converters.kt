package com.orm.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orm.data.model.Point
import com.orm.data.model.Trace
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val gson = Gson()

    // com.orm.data.local.Converters for List<Int>
    @TypeConverter
    fun fromStringToIntList(value: String?): List<Int>? {
        val listType = object : TypeToken<List<Int>>() {}.type
        return value?.let {
            gson.fromJson(it, listType)
        }
    }

    @TypeConverter
    fun fromIntListToString(list: List<Int>?): String? {
        return list?.let {
            gson.toJson(it)
        }
    }

    // com.orm.data.local.Converters for LocalDateTime
    @TypeConverter
    fun fromStringToLocalDateTime(value: String?): LocalDateTime? {
        return value?.let {
            LocalDateTime.parse(it, formatter)
        }
    }

    @TypeConverter
    fun fromLocalDateTimeToString(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }

    // com.orm.data.local.Converters for List<String>
    @TypeConverter
    fun fromStringToStringList(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return value?.let {
            gson.fromJson(it, listType)
        }
    }

    @TypeConverter
    fun fromStringListToString(list: List<String>?): String? {
        return list?.let {
            gson.toJson(it)
        }
    }

    @TypeConverter
    fun fromTraceListToString(list: List<Trace>?): String? {
        return list?.let {
            gson.toJson(it)
        }
    }

    @TypeConverter
    fun fromStringToTraceList(value: String?): List<Trace>? {
        val listType = object : TypeToken<List<Trace>>() {}.type
        return value?.let {
            gson.fromJson(it, listType)
        }
    }

    @TypeConverter
    fun fromPointListToString(list: List<Point>?): String? {
        return list?.let {
            gson.toJson(it)
        }
    }

    @TypeConverter
    fun fromStringToPointList(value: String?): List<Point>? {
        val listType = object : TypeToken<List<Point>>() {}.type
        return value?.let {
            gson.fromJson(it, listType)
        }
    }
}
