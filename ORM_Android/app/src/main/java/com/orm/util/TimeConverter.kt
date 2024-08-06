package com.orm.util

import java.time.LocalDateTime
import java.time.ZoneId

fun localDateTimeToLong(dateTime: LocalDateTime): Long {
    return dateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli()
}